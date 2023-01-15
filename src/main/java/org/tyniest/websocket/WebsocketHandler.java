package org.tyniest.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.tyniest.notification.dto.NotificationDto;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.MessageConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@ServerEndpoint(
        value = "/ws/{token}",
        encoders = {MessageEncoder.class})
@ApplicationScoped
public class WebsocketHandler {
    public static final String CHANNEL_PREFIX = "WS";
    private final ConnectionHolder connectionHolder;
    private final EventBus bus;
    private final Map<String, MessageConsumer<?>> consumers = new HashMap<>();

    protected void registerNotifications(final UUID userId, final Session session) {
        final var topic = userId.toString();
        final var remote = session.getAsyncRemote();
        final var consumer = bus.<NotificationDto>consumer(topic);
        consumer.bodyStream()
            .toMulti()
            .subscribe()
            .with(e -> {
                remote.sendObject(e);
            });
        log.info("subscribed to {}", topic);
        this.consumers.put(makeKey(session), consumer);
    }

    @ConsumeEvent
    public void registerCodec(DisconnectUserNotification body) {

    }

    protected String makeKey(final Session session) {
        return session.getId();
    }

    protected Uni<Void> unregisterNotifications(final Session session) {
        return this.consumers.get(makeKey(session)).unregister();
    }

    @OnOpen
    public void onOpen(@PathParam("token") final String token, final Session session) {
        // log.info("handshake headers: {}", req.getHeaders());
        // final var ip = Optional.ofNullable(req.getHeaders().get("key")).filter(e -> e.size() >
        // 0).map(e -> e.get(0));
        final var ip = Optional.<String>empty();
        connectionHolder
                .getUserByToken(token)
                .invoke(u -> this.registerNotifications(u.getUserId(), session))
                .chain(
                        u -> {
                            if (u == null) {
                                log.warn("{} failed to connect", token);
                                try {
                                    session.close();
                                } catch (IOException e) {
                                    log.error("failed to close");
                                }
                                return Uni.createFrom()
                                        .failure(
                                                new Exception(
                                                        "Failed to bind connection, token is"
                                                                + " null"));
                            }
                            // if we can get a user we bind the connection to the given user
                            return connectionHolder
                                    .connectUser(token, session, u, ip)
                                    .replaceWith(u);
                        })
                .subscribe()
                .with(
                        u -> {
                            log.info("Connect {}", u);
                        },
                        e -> {
                            log.warn("Failed to connect {}", e);
                        });
    }

    /**
     * Receives message of the form {method name as string},{json body}
     *
     * @param message
     * @param session
     * @return
     */
    @OnMessage
    public void onMessage(final String message, final Session session) {
        // handle message
        // split {method},{body} -> [{method}, {body}] // body may contain ',' that's
        // why we use the
        // 2
        final var splited = message.split(",", 2);
        if (splited.length != 2) {
            return;
        }
        final var method = splited[0];
        final var body = splited[1];
        final var currentSession = this.connectionHolder.getSession(session).orElseThrow();

        final var wsMessage = WsMessage.of(body, currentSession.getId());
        // here we dispatch the query to the app
        // this is a bit like a router
        this.bus
                .request(CHANNEL_PREFIX + method, wsMessage)
                // with success
                .onItem()
                .transform(d -> new ResponseDto(method, d.body(), true, null))
                // with error -> we inject the error
                .onFailure()
                .recoverWithItem(e -> new ResponseDto(method, null, false, e.toString()))
                .subscribe()
                .with(
                        r ->
                                session.getAsyncRemote()
                                        .sendObject(
                                                r, // render done by the MessageEncoder specified
                                                // earlier
                                                handler -> {
                                                    if (handler.isOK()) {
                                                        return;
                                                    }
                                                    log.error(
                                                            "error: {} with data {}",
                                                            handler.getException(),
                                                            r.toString());
                                                }));
    }

    @OnClose
    public void onClose(Session session) {
        final var user = this.connectionHolder.disconnectSession(session, false);
        this.unregisterNotifications(session);
        log.debug("{} disconnected", user);
    }

    @OnError
    public void onError(Session session, Throwable t) {
        this.unregisterNotifications(session);
        log.error("{} error: {}", session, t);
    }
}
