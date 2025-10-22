package bablr.chat.infrastructure.websocket;

public record SendMessageEvent(String message, String type) implements ChatEvent {
    public SendMessageEvent {
        if (message == null) {
            throw new IllegalArgumentException("message must not be null");
        }
        if (message.isBlank()) {
            throw new IllegalArgumentException("message must not be blank");
        }
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }
        if (type.isBlank()) {
            throw new IllegalArgumentException("type must not be blank");
        }
    }

    public SendMessageEvent(String message) {
        this(message, "SendMessageEvent");
    }
}
