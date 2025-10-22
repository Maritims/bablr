package bablr.chat.infrastructure.websocket;

import bablr.chat.application.SendMessageUseCase;
import bablr.chat.application.StartChatUseCase;
import bablr.chat.application.event.DomainEventPublisher;
import bablr.chat.application.impl.SendMessageService;
import bablr.chat.application.impl.StartChatService;
import bablr.chat.domain.entity.Participant;
import bablr.chat.domain.event.DomainEvent;
import bablr.chat.domain.value.ChatId;
import bablr.chat.domain.value.ParticipantId;
import bablr.chat.infrastructure.persistence.sqlite.ChatRepositorySqliteImpl;
import bablr.chat.infrastructure.persistence.sqlite.MessageRepositorySqliteImpl;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/chat", encoders = ChatEventEncoder.class, decoders = ChatEventDecoder.class)
public class ChatEndpoint {
    private static final Set<ChatEndpoint>   chatEndpoints = new CopyOnWriteArraySet<>();
    private static final Map<String, ChatId> chatIds       = new HashMap<>();
    private final        StartChatUseCase    startChatUseCase;
    private final        SendMessageUseCase  sendMessageUseCase;
    private              Session             session;

    public ChatEndpoint(StartChatUseCase startChatUseCase, SendMessageUseCase sendMessageUseCase) {
        if (startChatUseCase == null) {
            throw new IllegalArgumentException("startChatUseCase must not be null");
        }
        if (sendMessageUseCase == null) {
            throw new IllegalArgumentException("sendMessageUseCase must not be null");
        }

        this.startChatUseCase   = startChatUseCase;
        this.sendMessageUseCase = sendMessageUseCase;
    }

    public ChatEndpoint() {
        this(new StartChatService(new ChatRepositorySqliteImpl()), new SendMessageService(new ChatRepositorySqliteImpl(), new MessageRepositorySqliteImpl(), new DomainEventPublisher() {
            @Override
            public void publish(DomainEvent event) {

            }
        }));
    }

    @OnOpen
    public void onOpen(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("session must not be null");
        }

        this.session = session;
        chatEndpoints.add(this);
    }

    @OnMessage
    public void onMessage(Session session, ChatEvent event) {
        if (event instanceof StartChatEvent startChatEvent) {
            var senderId   = ParticipantId.parseParticipantId(startChatEvent.sender());
            var receiverId = ParticipantId.parseParticipantId(startChatEvent.receiver());
            var sender     = new Participant(senderId);
            var receiver   = new Participant(receiverId);
            var chat       = startChatUseCase.startChat(Set.of(sender, receiver));
            chatIds.put(session.getId(), chat.getId());
        } else if (event instanceof JoinChatEvent joinChatEvent) {
            var chatId = ChatId.parseChatId(joinChatEvent.chatId());
            chatIds.put(session.getId(), chatId);
        } else if (event instanceof SendMessageEvent sendMessageEvent) {
            var chatId = chatIds.get(session.getId());
            if (chatId == null) {
                throw new IllegalArgumentException("chatId not found for session: " + session.getId());
            }

            sendMessageUseCase.sendMessage(chatId, null, sendMessageEvent.message());
        }
    }

    @OnClose
    public void onClose(Session session) {
        var chatId = chatIds.get(session.getId());
        if (chatId == null) {
            throw new IllegalArgumentException("chatId not found for session: " + session.getId());
        }

        chatIds.remove(session.getId());
        chatEndpoints.remove(this);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }

    private static void broadcast() {
        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().sendObject(null);
                } catch (IOException | EncodeException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
