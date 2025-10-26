package bablr.chat.adapters.out.event.websocket;

import bablr.chat.application.port.in.chat.Command;
import bablr.chat.application.port.in.chat.CommandHandler;
import bablr.chat.application.port.out.event.DomainEventListener;
import bablr.chat.model.chat.ChatId;
import bablr.chat.model.chat.ParticipantId;
import bablr.chat.model.event.DomainEvent;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.eclipse.jetty.ee11.servlet.ServletContextHandler;
import org.eclipse.jetty.ee11.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer;
import org.eclipse.jetty.server.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ServerEndpoint(value = "/chat", encoders = Encoder.class, decoders = Decoder.class)
public class WebsocketServer implements DomainEventListener {
    private static final Map<ParticipantId, WebsocketServer> connections = new HashMap<>();

    private CommandHandler commandHandler;

    private Server      server;
    private Session     session;
    private Set<ChatId> chats;

    public DomainEventListener commandHandler(CommandHandler commandHandler) {
        if (commandHandler == null) {
            throw new IllegalArgumentException("commandHandler must not be null");
        }
        this.commandHandler = commandHandler;
        return this;
    }

    @Override
    public void start() {
        server = new Server(8080);
        var context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        JakartaWebSocketServletContainerInitializer.configure(context, (ctx, container) -> container.addEndpoint(WebsocketServer.class));

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
    }

    @Override
    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException("Failed to stop server", e);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        connections.put(ParticipantId.parseParticipantId(session.getId()), this);
    }

    @OnClose
    public void onClose(Session session) {
        connections.remove(ParticipantId.parseParticipantId(session.getId()));
        chats.clear();
    }

    @OnMessage
    public void onMessage(Command command) {
        if (command == null) {
            throw new IllegalArgumentException("message must not be null");
        }

        commandHandler.handle(command);
    }

    @Override
    public void handle(DomainEvent domainEvent) {
        if (domainEvent == null) {
            throw new IllegalArgumentException("domainEvent must not be null");
        }

        connections.forEach((participantId, connection) -> connection.chats
                .stream()
                .filter(chatId -> chatId.equals(domainEvent.chatId()))
                .findFirst()
                .ifPresent(chatId -> connection.session.getAsyncRemote().sendObject(domainEvent, result -> {
                })));
    }
}
