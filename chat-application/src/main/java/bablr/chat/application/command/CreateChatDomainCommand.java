package bablr.chat.application.command;

import bablr.chat.model.chat.ParticipantId;

import java.time.Instant;

public record CreateChatDomainCommand(String name, ParticipantId ownerId, Instant occurredAt, ParticipantId... participantIds) implements DomainCommand {
    @Override
    public String type() {
        return "CreateChat";
    }
}
