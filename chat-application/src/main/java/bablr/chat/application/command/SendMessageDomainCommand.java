package bablr.chat.application.command;

import bablr.chat.model.chat.ChatId;
import bablr.chat.model.chat.ParticipantId;

import java.time.Instant;

public record SendMessageDomainCommand(ChatId chatId, ParticipantId senderId, String content, Instant occurredAt) implements DomainCommand {
    @Override
    public String type() {
        return "SendMessage";
    }
}
