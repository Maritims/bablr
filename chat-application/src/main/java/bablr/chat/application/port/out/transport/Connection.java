package bablr.chat.application.port.out.transport;

import bablr.chat.model.chat.ChatId;

import java.util.Set;

public record Connection<TId>(ConnectionId<TId> id, Set<ChatId> chatIds) {
}
