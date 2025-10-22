package bablr.chat.infrastructure.websocket;

public record JoinChatEvent(String chatId, String type) implements ChatEvent {
    public JoinChatEvent {
        if (chatId == null) {
            throw new IllegalArgumentException("chatId must not be null");
        }
        if (chatId.isBlank()) {
            throw new IllegalArgumentException("chatId must not be blank");
        }
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }
        if (type.isBlank()) {
            throw new IllegalArgumentException("type must not be blank");
        }
    }

    public JoinChatEvent(String chatId) {
        this(chatId, "JoinChatEvent");
    }
}
