package bablr.chat.application;

import bablr.chat.domain.entity.Participant;
import bablr.chat.domain.value.ChatId;
import bablr.chat.domain.value.MessageId;

public interface MarkAsReadUseCase {
    void markAsRead(ChatId chatId, MessageId messageId, Participant participant);
}
