package bablr.chat.runner;

import bablr.chat.infrastructure.websocket.ChatEndpoint;
import org.eclipse.jetty.ee11.servlet.ServletContextHandler;
import org.eclipse.jetty.ee11.webapp.WebAppContext;
import org.eclipse.jetty.ee11.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer;
import org.eclipse.jetty.server.Server;

public class Application {
    public static void main(String[] args) throws Exception {
        var server = new Server(8080);
        var context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        JakartaWebSocketServletContainerInitializer.configure(context, (ctx, container) -> {
            container.addEndpoint(ChatEndpoint.class);
        });

        server.start();
        server.join();
    }
}
