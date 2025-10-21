package bablr.chat.domain.repository;

import bablr.chat.domain.value.ChatId;
import bablr.chat.domain.entity.Message;

import java.util.List;

public interface MessageRepository {
    List<Message> findByChatId(ChatId chatId, int pageNumber, int pageSize);

    void save(Message message);
}
