package bablr.chat.application.port.out.persistence;

import bablr.chat.model.chat.Message;

public interface MessageRepository {
    void save(Message message);
}
