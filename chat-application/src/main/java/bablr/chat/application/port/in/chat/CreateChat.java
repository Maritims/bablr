package bablr.chat.application.port.in.chat;

import bablr.chat.application.command.CommandProcessor;
import bablr.chat.application.command.CreateChatCommand;
import bablr.chat.model.chat.Chat;

public interface CreateChat extends CanProcessCommand<CreateChatCommand, Chat> {
    Chat process(CreateChatCommand command);
}
