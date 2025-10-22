package bablr.chat.application.impl;

import bablr.chat.application.GetMessagesUseCase;
import bablr.chat.domain.repository.ChatRepository;
import bablr.chat.domain.value.ChatId;
import bablr.chat.domain.entity.Message;

import java.util.Objects;

public class GetMessagesService implements GetMessagesUseCase {
    private final ChatRepository chatRepository;

    public GetMessagesService(ChatRepository chatRepository) {
        this.chatRepository = Objects.requireNonNull(chatRepository);
    }

    @Override
    public Iterable<Message> getMessages(ChatId chatId, int pageNumber, int pageSize) {
        var chat = chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException("Chat not found"));

        return null;
    }
}
