package bablr.chat.application.port.in.chat;

import bablr.chat.application.command.SendMessageCommand;

public interface SendMessage {
    void sendMessage(SendMessageCommand command);
}
