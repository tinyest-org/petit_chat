package org.tyniest.websocket.state;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Represents a sessionState */
@ToString
@Getter
@EqualsAndHashCode(of = "id")
public class SessionState {
    /** Will be id as String in our case */
    protected Long userId;

    protected UUID id;
    protected Set<String> roles;
    /** To know when the session was opened */
    protected LocalDateTime openedAt;

    @Setter protected Float latitude;
    @Setter protected Float longitude;

    @Setter protected String ip;

    private SessionState(
            final Long userId, final UUID id, final String ip, final Set<String> roles) {
        this.userId = userId;
        this.id = id;
        this.roles = roles;
        this.ip = ip;
    }

    public void setOpen() {
        this.openedAt = LocalDateTime.now();
    }

    public static SessionState of(
            final Long userId, final UUID uid, final String ip, final Set<String> roles) {
        return new SessionState(userId, uid, ip, roles);
    }
}
