package bablr.chat.application.direct;

import bablr.chat.domain.entity.ChatParticipant;
import bablr.chat.domain.value.DirectChatId;
import bablr.chat.domain.value.MessageId;

public interface MarkMessageAsReadUseCase {
    void markMessageAsRead(DirectChatId directChatId, MessageId messageId, ChatParticipant participant);
}
