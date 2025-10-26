package bablr.chat.application.port.in.chat;

import bablr.chat.model.chat.Chat;

public interface CreateChat {
    Chat createChat(CreateChatCommand command);
}
