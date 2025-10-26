package bablr.chat.model.chat;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Chat {
    private ChatId        id;
    private ParticipantId ownerId;
    private Instant       created;

    private Set<Participant> participants = new HashSet<>();

    public static Chat newChat(ParticipantId ownerId) {
        var chat = new Chat();
        chat.ownerId = ownerId;
        return chat;
    }

    public static Chat existingChat(ChatId id, ParticipantId ownerId, Set<Participant> participants, Instant created) {
        var chat = new Chat();
        chat.id           = id;
        chat.ownerId      = ownerId;
        chat.participants = participants;
        chat.created      = created;
        return chat;
    }

    public void addParticipant(Participant participant) {
        if (participant == null) {
            throw new IllegalArgumentException("participant must not be null");
        }

        participants.add(participant);
    }

    public void removeParticipant(Participant participant) {
        if (participant == null) {
            throw new IllegalArgumentException("participant must not be null");
        }
        participants.remove(participant);
    }

    public ChatId getId() {
        return id;
    }

    public void setId(ChatId id) {
        this.id = id;
    }

    public ParticipantId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(ParticipantId ownerId) {
        this.ownerId = ownerId;
    }

    public Set<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }

    public void clearParticipants() {
        participants.clear();
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return Objects.equals(id, chat.id) && Objects.equals(ownerId, chat.ownerId) && Objects.equals(created, chat.created) && Objects.equals(participants, chat.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId, created, participants);
    }

    @Override
    public String toString() {
        return "Chat{" +
               "id=" + id +
               ", ownerId=" + ownerId +
               ", created=" + created +
               ", participants=" + participants +
               '}';
    }
}
