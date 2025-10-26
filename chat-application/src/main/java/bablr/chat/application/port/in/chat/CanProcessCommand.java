package bablr.chat.application.port.in.chat;

import bablr.chat.application.command.DomainCommand;

public interface CanProcessCommand<TCommand extends DomainCommand, TResult> {
    TResult createChat(TCommand command);
}
