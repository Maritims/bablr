package bablr.chat.domain.repository;

import bablr.chat.domain.aggregate.DirectChat;
import bablr.chat.domain.value.DirectChatId;

import java.util.Optional;

public interface DirectChatRepository {
    Optional<DirectChat> findById(DirectChatId id);

    void save(DirectChat chat);
}
