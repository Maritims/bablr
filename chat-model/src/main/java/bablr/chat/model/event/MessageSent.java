package bablr.chat.model.event;

import bablr.chat.model.chat.ChatId;
import bablr.chat.model.chat.ParticipantId;

import java.time.Instant;

public record MessageSent(ChatId chatId, ParticipantId senderId, String content, Instant occurredAt) implements DomainEvent {
}
