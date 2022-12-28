package org.tyniest.websocket;

import java.util.UUID;
import lombok.Value;

/** Message broadcasted to the internal bus It is used to call the Websocket endpoints internally */
@Value(staticConstructor = "of")
public class WsMessage {
    private String body;
    private UUID sessionIdentifier;
}
