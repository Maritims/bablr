package bablr.chat.domain.event;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();

    String type();
}
