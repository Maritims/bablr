package bablr.chat.domain.repository;

import bablr.chat.domain.aggregate.Chat;
import bablr.chat.domain.value.ChatId;

import java.util.Optional;

public interface ChatRepository {
    Optional<Chat> findById(ChatId id);

    Chat save(Chat chat);
}
