package bablr.chat.domain.aggregate;

import bablr.chat.domain.event.DomainEvent;
import bablr.chat.domain.entity.Participant;
import bablr.chat.domain.event.MessageSentEvent;
import bablr.chat.domain.value.ChatId;
import bablr.chat.domain.entity.Message;
import bablr.chat.domain.value.MessageId;
import bablr.chat.domain.value.ParticipantId;

import java.time.Instant;
import java.util.*;

public class Chat {
    private final ChatId                    id;
    private final Set<Participant>          participants            = new HashSet<>();
    private final Map<Participant, Instant> lastReadAtByParticipant = new HashMap<>();
    private final List<DomainEvent>         events                  = new ArrayList<>();

    private Chat(ChatId id, Set<Participant> participants) {
        this.id    = id;
        this.participants.addAll(participants);
    }

    public static Chat newChat(Set<Participant> participants) {
        if (participants == null) {
            throw new IllegalArgumentException("participants must not be null");
        }
        if (participants.isEmpty()) {
            throw new IllegalArgumentException("participants must not be empty");
        }

        return new Chat(null, participants);
    }

    public static Chat existingChat(ChatId id, Set<Participant> participants) {
        if(id == null) {
            throw new IllegalArgumentException("value must not be null");
        }
        if(participants == null) {
            throw new IllegalArgumentException("participants must not be null");
        }
        if(participants.isEmpty()) {
            throw new IllegalArgumentException("participants must not be empty");
        }

        return new Chat(id, participants);
    }

    public ChatId getId() {
        return id;
    }

    public Set<Participant> getParticipants() {
        return Collections.unmodifiableSet(participants);
    }

    public boolean isNew() {
        return id == null;
    }

    public Message sendMessage(ParticipantId senderId, String content) {
        if(participants.stream().noneMatch(p -> p.id().equals(senderId))) {
            throw new IllegalArgumentException("Sender is not part of this chat");
        }

        var message = new Message(new MessageId(UUID.randomUUID()), id, senderId, content, Instant.now());
        events.add(new MessageSentEvent(id, message, Instant.now()));
        return message;
    }

    public List<DomainEvent> getEvents() {
        return events;
    }

    public void clearEvents() {
        events.clear();
    }

    public void markAsRead(Participant participant, Instant upToTime) {
        if(!participants.contains(participant)) {
            throw new IllegalArgumentException("Participant is not part of this chat");
        }
        if (upToTime == null) {
            throw new IllegalArgumentException("messageId must not be null");
        }

        var current = lastReadAtByParticipant.get(participant);
        if(current == null || current.isBefore(upToTime)) {
            lastReadAtByParticipant.put(participant, upToTime);
        }
    }
}
