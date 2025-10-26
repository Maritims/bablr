package bablr.chat.application.port.in.chat;

import bablr.chat.application.command.CreateChatDomainCommand;
import bablr.chat.model.chat.Chat;

public interface CreateChat extends CanProcessCommand<CreateChatDomainCommand, Chat> {
    Chat createChat(CreateChatDomainCommand command);
}
