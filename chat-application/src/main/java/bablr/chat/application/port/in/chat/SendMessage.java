package bablr.chat.application.port.in.chat;

import bablr.chat.application.command.SendMessageDomainCommand;

public interface SendMessage {
    void sendMessage(SendMessageDomainCommand command);
}
