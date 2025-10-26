package bablr.chat.application.port.out.transport;

import bablr.chat.application.command.DomainCommand;

public interface ReceiveCommand {
    DomainCommand receive(String payload);
}
