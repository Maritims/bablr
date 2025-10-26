package bablr.chat.model.chat;

import java.util.UUID;

public record ChatId(UUID value) {
    public static ChatId newChatId() {
        return new ChatId(UUID.randomUUID());
    }

    public static ChatId parseChatId(String chatId) {
        if (chatId == null) {
            throw new IllegalArgumentException("chatId must not be null");
        }
        if (chatId.isBlank()) {
            throw new IllegalArgumentException("chatId must not be blank");
        }
        return new ChatId(UUID.fromString(chatId));
    }
}
