package org.tyniest.websocket;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** Class to notify other instances to disconnect their session if a user opens a second session */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DisconnectUserNotification {
    public String openedSessionId;
    public Long userId;
    /** If true, then we should disconnect all users */
    public boolean all;
}
