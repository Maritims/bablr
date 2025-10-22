package bablr.chat.application;

import bablr.chat.domain.value.ChatId;
import bablr.chat.domain.entity.Message;

public interface GetMessagesUseCase {
    Iterable<Message> getMessages(ChatId chatId, int pageNumber, int pageSize);
}
