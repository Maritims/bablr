package bablr.chat.application.direct;

import bablr.chat.domain.entity.ChatParticipant;
import bablr.chat.domain.value.DirectChatId;
import bablr.chat.domain.value.Message;

public interface SendDirectMessageUseCase {
    Message sendMessage(DirectChatId directChatId, ChatParticipant participant, String content);
}
