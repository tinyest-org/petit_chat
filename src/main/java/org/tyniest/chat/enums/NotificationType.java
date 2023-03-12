package org.tyniest.chat.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NotificationType {
    MESSAGE_ACTIVITY("message"), REACTION_ACTIVITY("reaction");

    private String name = "";

    NotificationType(String name) {
        this.name = name;
    }

    @JsonValue
    public String toString() {
        return name;
    }
}
