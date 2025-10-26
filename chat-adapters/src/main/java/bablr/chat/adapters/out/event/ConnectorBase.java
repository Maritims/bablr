package bablr.chat.adapters.out.event;

import bablr.chat.application.command.CreateChatDomainCommand;
import bablr.chat.application.command.DomainCommand;
import bablr.chat.application.command.SendMessageDomainCommand;
import bablr.chat.application.port.in.chat.CreateChat;
import bablr.chat.application.port.in.chat.SendMessage;
import bablr.chat.application.port.out.transport.ConnectionId;
import bablr.chat.application.port.out.transport.Connector;
import bablr.chat.model.chat.ChatId;
import bablr.chat.model.event.ChatCreatedEvent;
import bablr.chat.model.event.DomainEvent;
import bablr.chat.model.event.MessageSentEvent;

import java.util.*;

public abstract class ConnectorBase<TConnectionIdHolder, TConnectionId> implements Connector<TConnectionIdHolder, TConnectionId> {
    protected static final Map<ConnectionId<?>, Connector<?, ?>> connectors = new HashMap<>();

    protected final Set<ChatId> chats = new HashSet<>();
    private final   CreateChat  createChat;
    private final   SendMessage sendMessage;

    protected TConnectionIdHolder connectionIdHolder;

    protected ConnectorBase(CreateChat createChat, SendMessage sendMessage) {
        this.createChat  = Objects.requireNonNull(createChat);
        this.sendMessage = Objects.requireNonNull(sendMessage);
    }

    @Override
    public void onClientConnected(TConnectionIdHolder connectionIdHolder) {
        this.connectionIdHolder = connectionIdHolder;

        var connectionId = parseConnectionId(connectionIdHolder);
        connectors.put(connectionId, this);
    }

    @Override
    public void onClientDisconnected(TConnectionIdHolder connectionIdHolder) {
        chats.clear();
        connectors.remove(parseConnectionId(connectionIdHolder));
    }

    @Override
    public void onCommandReceived(DomainCommand domainCommand) {
        if (domainCommand == null) {
            throw new IllegalArgumentException("message must not be null");
        }

        DomainEvent domainEvent;

        if (domainCommand instanceof CreateChatDomainCommand createChatDomainCommand) {
            var chat = createChat.createChat(createChatDomainCommand);
            chats.add(chat.getId());
            domainEvent = new ChatCreatedEvent(chat.getId(), chat.getOwnerId(), createChatDomainCommand.occurredAt());
        } else if (domainCommand instanceof SendMessageDomainCommand sendMessageDomainCommand) {
            sendMessage.sendMessage(sendMessageDomainCommand);
            domainEvent = new MessageSentEvent(sendMessageDomainCommand.chatId(), sendMessageDomainCommand.senderId(), sendMessageDomainCommand.content(), sendMessageDomainCommand.occurredAt());
        } else {
            throw new IllegalArgumentException("Unsupported command: " + domainCommand);
        }

        broadcast(domainEvent);
    }

    @Override
    public void send(TConnectionIdHolder connectionIdHolder, DomainEvent domainEvent) {
        if (connectionIdHolder == null) {
            throw new IllegalArgumentException("connectionIdHolder must not be null");
        }
        if (domainEvent == null) {
            throw new IllegalArgumentException("domainEvent must not be null");
        }

        var connectionId = parseConnectionId(connectionIdHolder);
        var connector    = connectors.get(connectionId);
        if (connector == null) {
            throw new IllegalArgumentException("connectionIdHolder must be a valid connection");
        }

        connector.getChatIds()
                .stream()
                .filter(c -> c.equals(domainEvent.chatId()))
                .findFirst()
                .ifPresent(chatId -> connector.);
    }

    @Override
    public void broadcast(DomainEvent domainEvent) {
        if (domainEvent == null) {
            throw new IllegalArgumentException("domainEvent must not be null");
        }

        connectors.forEach((connectionId, connector) -> connector.send(connectionIdHolder, domainEvent));
    }
}
