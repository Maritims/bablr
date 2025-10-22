package bablr.chat.application.impl;

import bablr.chat.application.StartChatUseCase;
import bablr.chat.domain.aggregate.Chat;
import bablr.chat.domain.entity.Participant;
import bablr.chat.domain.repository.ChatRepository;

import java.util.Set;

public class StartChatService implements StartChatUseCase {
    private final ChatRepository chatRepository;

    public StartChatService(ChatRepository chatRepository) {
        if (chatRepository == null) {
            throw new IllegalArgumentException("chatRepository must not be null");
        }

        this.chatRepository = chatRepository;
    }

    @Override
    public Chat startChat(Set<Participant> participants) {
        if (participants == null) {
            throw new IllegalArgumentException("participants must not be null");
        }
        if (participants.size() < 2) {
            throw new IllegalArgumentException("participants must contain at least 2 participants");
        }

        var chat = Chat.newChat(participants);
        return chatRepository.save(chat);
    }
}
