package bablr.chat.demo;

import bablr.chat.application.event.DomainEventPublisher;
import bablr.chat.application.impl.SendMessageService;
import bablr.chat.application.impl.StartChatService;
import bablr.chat.domain.entity.Participant;
import bablr.chat.domain.event.MessageSentEvent;
import bablr.chat.domain.value.ParticipantId;
import bablr.chat.infrastructure.persistence.sqlite.ChatRepositorySqliteImpl;
import bablr.chat.infrastructure.persistence.sqlite.MessageRepositorySqliteImpl;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AppTest {
    @Test
    public void testApp() {
        var chatRepository    = new ChatRepositorySqliteImpl("jdbc:sqlite:foo.db", 10);
        var messageRepository = new MessageRepositorySqliteImpl("jdbc:sqlite:foo.db", 10);
        var startChatService  = new StartChatService(chatRepository);
        var publisher         = mock(DomainEventPublisher.class);
        doAnswer(invocationOnMock -> {
            var domainEvent = (MessageSentEvent) invocationOnMock.getArgument(0);
            System.out.printf(
                    "[%s] %s: %s%n",
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            .withZone(ZoneId.systemDefault())
                            .format(domainEvent.message().timestamp()),
                    domainEvent.message().senderId().value(),
                    domainEvent.message().content()
            );
            return invocationOnMock;
        }).when(publisher).publish(any(MessageSentEvent.class));
        var sendMessageService = new SendMessageService(chatRepository, messageRepository, publisher);

        chatRepository.initialize();
        messageRepository.initialize();

        var participantA = new Participant(ParticipantId.newParticipantId());
        var participantB = new Participant(ParticipantId.newParticipantId());
        var participants = new HashSet<>(Set.of(participantA, participantB));
        var chat         = startChatService.startChat(participants);

        sendMessageService.sendMessage(chat.getId(), participantA, "Hello World!");
    }
}
