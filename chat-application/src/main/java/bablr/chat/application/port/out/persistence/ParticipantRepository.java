package bablr.chat.application.port.out.persistence;

import bablr.chat.model.chat.Participant;
import bablr.chat.model.chat.ParticipantId;

import java.util.Optional;

public interface ParticipantRepository {
    Optional<Participant> findById(ParticipantId id);

    void save(Participant participant);
}
