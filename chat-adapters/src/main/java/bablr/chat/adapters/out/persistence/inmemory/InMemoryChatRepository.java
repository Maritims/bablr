package bablr.chat.adapters.out.persistence.inmemory;

import bablr.chat.application.port.out.persistence.ChatRepository;
import bablr.chat.model.chat.Chat;
import bablr.chat.model.chat.ChatId;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryChatRepository implements ChatRepository {
    private final Map<ChatId, Chat> chats = new HashMap<>();

    @Override
    public Optional<Chat> findById(ChatId id) {
        return chats.containsKey(id) ? Optional.of(chats.get(id)) : Optional.empty();
    }

    @Override
    public void save(Chat chat) {
        if (chat.getId() == null) {
            chat.setId(ChatId.newChatId());
            chat.setCreated(Instant.now());
            chats.put(chat.getId(), chat);
        } else if(chats.containsKey(chat.getId())) {
            var existing = chats.get(chat.getId());
            existing.clearParticipants();
            existing.getParticipants().addAll(chat.getParticipants());
        } else {
            throw new IllegalArgumentException("Chat already exists: " + chat);
        }
    }
}
