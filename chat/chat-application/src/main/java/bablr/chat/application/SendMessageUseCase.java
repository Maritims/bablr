package bablr.chat.application;

import bablr.chat.domain.entity.Participant;
import bablr.chat.domain.value.ChatId;
import bablr.chat.domain.entity.Message;

public interface SendMessageUseCase {
    Message sendMessage(ChatId chatId, Participant participant, String content);
}
