package bablr.chat.application.port.out.transport;

import bablr.chat.application.command.Command;

public interface ReceiveCommand {
    Command receive(String payload);
}
