package org.tyniest.websocket;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;

import org.tyniest.security.service.IdentityService;
import org.tyniest.websocket.WebsocketTokenController.AuthDto;
import org.tyniest.websocket.state.SessionState;

import io.smallrye.mutiny.Uni;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


/** Controller to handle Websocket Authentication and token generation */
@RolesAllowed("**")
@RequiredArgsConstructor
@Path("/ws/token-provider")
public class WebsocketTokenController {

    private final IdentityService identityService;
    private final ConnectionHolder connectionHolder;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthDto {
        private String token;
    }

    /** Returns a token for a given user to open a webscoket connection */
    @GET
    @Path("/")
    public Uni<AuthDto> getToken(@HeaderParam("X-Real-Ip") final Optional<String> ip) {
        final var roles = identityService.getCurrentRoles();
        return identityService
                .getCurrentUserIdAsync()
                .chain(
                        userId ->
                                connectionHolder
                                        .prepareConnectionFor(userId, ip, roles)
                                        .onItem()
                                        .ifNull()
                                        .failWith(BadRequestException::new)
                                        .onItem()
                                        .ifNotNull()
                                        .transform(AuthDto::new));
    }

    @GET
    @Path("/connected-users")
    @RolesAllowed({"admin"})
    public Uni<List<SessionState>> getConnectedUsers() {
        // will complete the rest of the infos using a multi-get on user endpoint
        return this.connectionHolder.getConnectedUsers();
    }

    @DELETE
    @Path("/connected-users")
    @RolesAllowed({"admin"})
    public Uni<Void> removeAllSessionStates() {
        return this.connectionHolder.removeAllSessionState();
    }
}
