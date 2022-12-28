package org.tyniest.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Used to send the response to the front-end users */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class ResponseDto {
    /**
     * The name of the method used, it basically tells the frontend which action it should do next
     */
    protected String type;
    /** Any POJO a endpoint needs to send */
    protected Object body;
    /** If the query was successful or not */
    protected boolean ok = false;
    /** The error serialyzed to string, most of the time, it's simply e.getMessage() */
    protected String error;

    /** Default constructor */
    public ResponseDto(final String type) {
        this.type = type;
    }

    /** Sets the body return status to ok (by default its false) */
    public ResponseDto setOk() {
        this.ok = true;
        return this;
    }
}
