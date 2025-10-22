package bablr.chat.infrastructure.websocket;

public record StartChatEvent(String sender, String receiver, String type) implements ChatEvent {
    public StartChatEvent {
        if(sender == null) {
            throw new IllegalArgumentException("sender must not be null");
        }
        if(sender.isBlank()) {
            throw new IllegalArgumentException("sender must not be blank");
        }
        if(receiver == null) {
            throw new IllegalArgumentException("receiver must not be null");
        }
        if(receiver.isBlank()) {
            throw new IllegalArgumentException("receiver must not be blank");
        }
        if(sender.equals(receiver)) {
            throw new IllegalArgumentException("sender and receiver must not be the same");
        }
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }
        if (type.isBlank()) {
            throw new IllegalArgumentException("type must not be blank");
        }
    }

    public StartChatEvent(String sender, String receiver) {
        this(sender, receiver, "StartChatEvent");
    }
}
