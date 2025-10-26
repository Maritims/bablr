package bablr.chat.application.port.in.chat;

import bablr.chat.application.command.Command;

public interface CanProcessCommand<TCommand extends Command, TResult> {
    TResult process(TCommand command);
}
