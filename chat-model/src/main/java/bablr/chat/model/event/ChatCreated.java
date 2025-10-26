package bablr.chat.model.event;

import bablr.chat.model.chat.ChatId;
import bablr.chat.model.chat.ParticipantId;

import java.time.Instant;

public record ChatCreated(ChatId chatId, ParticipantId creatorId, Instant occurredAt) implements DomainEvent {
}
