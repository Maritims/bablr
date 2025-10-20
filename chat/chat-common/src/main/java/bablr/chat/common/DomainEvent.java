package bablr.chat.common;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();

    String type();
}
