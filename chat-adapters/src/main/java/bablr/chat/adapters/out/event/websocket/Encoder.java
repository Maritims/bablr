package bablr.chat.adapters.out.event.websocket;

import bablr.chat.application.command.DomainCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.EncodeException;

public class Encoder implements jakarta.websocket.Encoder.Text<DomainCommand> {
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String encode(DomainCommand domainCommand) throws EncodeException {
        try {
            return objectMapper.writeValueAsString(domainCommand);
        } catch (JsonProcessingException e) {
            throw new EncodeException(domainCommand, "Failed to encode command", e);
        }
    }

    @Override
    public void destroy() {
        Text.super.destroy();
        objectMapper.clearCaches();
        objectMapper = null;
    }
}
