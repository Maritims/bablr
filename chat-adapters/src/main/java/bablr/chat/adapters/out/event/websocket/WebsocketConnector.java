package bablr.chat.adapters.out.event.websocket;

import bablr.chat.application.command.DomainCommand;
import bablr.chat.application.command.DomainCommandHandler;
import bablr.chat.application.port.out.event.DomainEventHandler;
import bablr.chat.application.port.out.transport.ConnectionId;
import bablr.chat.application.port.out.transport.Connector;
import bablr.chat.model.chat.ChatId;
import bablr.chat.model.event.DomainEvent;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.eclipse.jetty.ee11.servlet.ServletContextHandler;
import org.eclipse.jetty.ee11.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ServerEndpoint(value = "/chat", encoders = Encoder.class, decoders = Decoder.class)
public class WebsocketConnector implements Connector<Session, String>,
                                           DomainEventHandler {
    private static final Logger                                        log         = LoggerFactory.getLogger(WebsocketConnector.class);
    private static final Map<ConnectionId<String>, WebsocketConnector> connections = new HashMap<>();

    private final short                port;
    private final DomainCommandHandler domainCommandHandler;

    private Server      server;
    private Session     session;
    private Set<ChatId> chats;

    public WebsocketConnector(short port, DomainCommandHandler domainCommandHandler) {
        if (port < 0) {
            throw new IllegalArgumentException("port must not be negative");
        }
        if (domainCommandHandler == null) {
            throw new IllegalArgumentException("commandHandler must not be null");
        }

        this.port = port;
        this.domainCommandHandler = domainCommandHandler;
    }

    @Override
    public void listen() {
        server = new Server(port);
        var context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        JakartaWebSocketServletContainerInitializer.configure(context, (ctx, container) -> container.addEndpoint(WebsocketConnector.class));

        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException("Failed to start server", e);
        }

        try {
            server.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to join server", e);
        }

        log.info("Server started on port {}", port);
    }

    @Override
    public void shutdown() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException("Failed to stop server", e);
        }
    }

    @Override
    public ConnectionId<String> parseConnectionId(Session id) {
        return new ConnectionId<>(id.getId());
    }

    @OnOpen
    @Override
    public void onClientConnected(Session session) {
        this.session = session;
        connections.put(parseConnectionId(session), this);
    }

    @OnClose
    @Override
    public void onClientDisconnected(Session session) {
        connections.remove(parseConnectionId(session));
        chats.clear();
    }

    @OnMessage
    @Override
    public void onCommandReceived(DomainCommand domainCommand) {
        if (domainCommand == null) {
            throw new IllegalArgumentException("message must not be null");
        }

        domainCommandHandler.handle(domainCommand);
    }

    @Override
    public void send(Session connectionIdHolder, DomainEvent domainEvent) {
        if (connectionIdHolder == null) {
            throw new IllegalArgumentException("connectionIdHolder must not be null");
        }
        if (domainEvent == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        var connector = connections.get(parseConnectionId(connectionIdHolder));
        if (connector == null) {
            throw new IllegalArgumentException("connectionIdHolder must be a valid connection");
        }

        connector.chats
                .stream()
                .filter(chatId -> chatId.equals(domainEvent.chatId()))
                .findFirst()
                .ifPresent(chatId -> connector.session.getAsyncRemote().sendObject(domainEvent, result -> {
                }));
    }

    @Override
    public void broadcast(DomainEvent domainEvent) {
        if (domainEvent == null) {
            throw new IllegalArgumentException("domainEvent must not be null");
        }

        connections.forEach((participantId, connection) -> send(connection.session, domainEvent));
    }

    @Override
    public void handle(DomainEvent domainEvent) {
        if (domainEvent == null) {
            throw new IllegalArgumentException("domainEvent must not be null");
        }

        broadcast(domainEvent);
    }
}
