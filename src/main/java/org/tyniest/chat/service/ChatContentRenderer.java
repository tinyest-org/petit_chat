package org.tyniest.chat.service;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.entity.Signal;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@ApplicationScoped
@RequiredArgsConstructor
public class ChatContentRenderer {
    private final ObjectMapper mapper;

    public Signal ofText(
        final UUID chatId, final UUID createdAt, 
        final UUID userId, final String content
    ) {
        return Signal.builder()
            .chatId(chatId)
            .createdAt(createdAt)
            .userId(userId)
            .content(content)
            .type(Signal.TEXT_TYPE)
            .build();
    }

    @SneakyThrows
    protected String toJsonString(final Object item) {
        return mapper.writeValueAsString(item);
    }

    protected boolean isImage() {
        return false;
    }

    public Signal ofFile(
        final UUID chatId, final UUID createdAt, 
        final UUID userId, final String content
    ) {
        if (isImage()) {
            return ofImage(chatId, createdAt, userId, content);
        }
        return Signal.builder()
            .chatId(chatId)
            .createdAt(createdAt)
            .userId(userId)
            .content(content)
            .type(Signal.FILE_TYPE)
            .build();
    }

    protected Signal ofImage(
        final UUID chatId, final UUID createdAt, 
        final UUID userId, final String content
    ) {
        return Signal.builder()
            .chatId(chatId)
            .createdAt(createdAt)
            .userId(userId)
            .content(content)
            .type(Signal.IMAGE_TYPE)
            .build();
    }
}
