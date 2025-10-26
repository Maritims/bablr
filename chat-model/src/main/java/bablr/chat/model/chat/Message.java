package bablr.chat.model.chat;

import java.time.Instant;
import java.util.Objects;

public class Message {
    private MessageId     id;
    private ChatId        chatId;
    private ParticipantId senderId;
    private String        content;
    private Instant       created;

    public static Message newMessage(ChatId chatId, ParticipantId senderId, String content) {
        var message = new Message();
        message.chatId   = chatId;
        message.senderId = senderId;
        message.content  = content;
        message.created  = Instant.now();
        return message;
    }

    public static Message existingMessage(MessageId id, ChatId chatId, ParticipantId senderId, String content, Instant created) {
        var message = new Message();
        message.id       = id;
        message.chatId   = chatId;
        message.senderId = senderId;
        message.content  = content;
        message.created  = created;
        return message;
    }

    public MessageId getId() {
        return id;
    }

    public void setId(MessageId id) {
        this.id = id;
    }

    public ChatId getChatId() {
        return chatId;
    }

    public void setChatId(ChatId chatId) {
        this.chatId = chatId;
    }

    public ParticipantId getSenderId() {
        return senderId;
    }

    public void setSenderId(ParticipantId senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Message) obj;
        return Objects.equals(this.id, that.id) &&
               Objects.equals(this.chatId, that.chatId) &&
               Objects.equals(this.senderId, that.senderId) &&
               Objects.equals(this.content, that.content) &&
               Objects.equals(this.created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, senderId, content, created);
    }

    @Override
    public String toString() {
        return "Message[" +
               "id=" + id + ", " +
               "chatId=" + chatId + ", " +
               "senderId=" + senderId + ", " +
               "content=" + content + ", " +
               "created=" + created + ']';
    }

}
