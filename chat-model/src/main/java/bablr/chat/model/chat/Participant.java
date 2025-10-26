package bablr.chat.model.chat;

import java.time.Instant;
import java.util.Objects;

public class Participant {
    private ParticipantId id;
    private Instant       createdAt;

    public static Participant newParticipant(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }

        return new Participant();
    }

    public static Participant existingParticipant(ParticipantId id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }

        var participant = new Participant();
        participant.id   = id;
        return participant;
    }

    public ParticipantId getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(id, that.id) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt);
    }

    @Override
    public String toString() {
        return "Participant{" +
               "id=" + id +
               ", created=" + createdAt +
               '}';
    }
}
