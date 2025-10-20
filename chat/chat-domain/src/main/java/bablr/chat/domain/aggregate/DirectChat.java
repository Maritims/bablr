package bablr.chat.domain.aggregate;

import bablr.chat.common.DomainEvent;
import bablr.chat.domain.entity.ChatParticipant;
import bablr.chat.domain.event.MessageSentEvent;
import bablr.chat.domain.value.DirectChatId;
import bablr.chat.domain.value.Message;
import bablr.chat.domain.value.MessageId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DirectChat {
    private final DirectChatId      id;
    private final ChatParticipant   participantA;
    private final ChatParticipant   participantB;
    private final List<Message>     messages     = new ArrayList<>();
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public DirectChat(DirectChatId id, ChatParticipant participantA, ChatParticipant participantB) {
        this.id           = Objects.requireNonNull(id);
        this.participantA = Objects.requireNonNull(participantA);
        this.participantB = Objects.requireNonNull(participantB);

        if (participantA.equals(participantB)) {
            throw new IllegalArgumentException("Participants must not be the same");
        }
    }

    public Message sendMessage(ChatParticipant sender, String content) {
        if (!sender.equals(participantA) && !sender.equals(participantB)) {
            throw new IllegalArgumentException("Sender is not part of this chat");
        }

        var message = new Message(new MessageId(UUID.randomUUID()), sender, content, Instant.now());
        messages.add(message);
        domainEvents.add(new MessageSentEvent(id, message, Instant.now()));
        return message;
    }

    public DirectChatId getId() {
        return id;
    }

    public ChatParticipant getParticipantA() {
        return participantA;
    }

    public ChatParticipant getParticipantB() {
        return participantB;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<DomainEvent> getDomainEvents() {
        return domainEvents;
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    public void markMessageAsRead(ChatParticipant participant, MessageId messageId) {
        if (!participant.equals(participantA) && !participant.equals(participantB)) {
            throw new IllegalArgumentException("Participant is not part of this chat");
        }
        if (messageId == null) {
            throw new IllegalArgumentException("messageId must not be null");
        }


    }
}
