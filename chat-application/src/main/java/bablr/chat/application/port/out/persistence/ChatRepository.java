package bablr.chat.application.port.out.persistence;

import bablr.chat.model.chat.Chat;
import bablr.chat.model.chat.ChatId;

import java.util.Optional;

public interface ChatRepository {
    Optional<Chat> findById(ChatId id);

    void save(Chat chat);
}
