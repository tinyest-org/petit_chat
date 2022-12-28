package org.tyniest.websocket;

import java.io.IOException;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.tyniest.notification.dto.NotificationDto;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
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

    protected Uni<Void> registerNotifications(final String topic, final Session session) {
        final var remote = session.getAsyncRemote();
        return bus.<NotificationDto>consumer(topic)
            .bodyStream()
            .toMulti()
            .onItem()
            .invoke(notif -> {
                remote.sendObject(notif);
            })
            .toUni()
            .replaceWithVoid();
    }

    @OnOpen
    public void onOpen(@PathParam("token") final String token, final Session session) {
        // log.info("handshake headers: {}", req.getHeaders());
        // final var ip = Optional.ofNullable(req.getHeaders().get("key")).filter(e -> e.size() >
        // 0).map(e -> e.get(0));
        final var ip = Optional.<String>empty();
        connectionHolder
                .getUserByToken(token)
                .invoke(u -> {
                    this.registerNotifications(u.getId().toString(), session);
                })
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
                            log.debug("Connect {}", u);
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
        log.debug("{} disconnected", user);
    }

    @OnError
    public void onError(Session session, Throwable t) {
        log.error("{} error: {}", session, t);
    }
}
