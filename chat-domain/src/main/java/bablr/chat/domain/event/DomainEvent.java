package bablr.chat.domain.event;

import bablr.chat.common.HasTypeForDeserialization;

import java.time.Instant;

public interface DomainEvent extends HasTypeForDeserialization {
    Instant occurredAt();
}
