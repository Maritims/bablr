package bablr.chat.application;

import bablr.chat.domain.aggregate.Chat;
import bablr.chat.domain.entity.Participant;

import java.util.Set;

public interface StartChatUseCase {
    Chat startChat(Set<Participant> participants);
}
