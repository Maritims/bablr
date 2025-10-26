package bablr.chat.adapters.out.persistence.inmemory;

import bablr.chat.application.port.out.persistence.MessageRepository;
import bablr.chat.model.chat.Message;
import bablr.chat.model.chat.MessageId;

import java.util.HashMap;
import java.util.Map;

public class InMemoryMessageRepository implements MessageRepository {
    private final Map<MessageId, Message> messages = new HashMap<>();

    @Override
    public void save(Message message) {
        if(messages.containsKey(message.getId())) {
            var existing = messages.get(message.getId());
            existing.setContent(message.getContent());
            existing.setCreated(message.getCreated());
            existing.setSenderId(message.getSenderId());
            existing.setChatId(message.getChatId());
        } else {
            messages.put(message.getId(), message);
        }
    }
}
