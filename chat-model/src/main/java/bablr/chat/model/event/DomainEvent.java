package bablr.chat.model.event;

import bablr.chat.model.chat.ChatId;

public interface DomainEvent {
    ChatId chatId();
}
