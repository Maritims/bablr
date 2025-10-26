package bablr.chat.application.port.in.chat;

import bablr.chat.model.chat.ParticipantId;

import java.time.Instant;

public record CreateChatCommand(String name, ParticipantId ownerId, Instant occurredAt, ParticipantId... participantIds) implements Command {
    @Override
    public String type() {
        return "CreateChat";
    }
}
