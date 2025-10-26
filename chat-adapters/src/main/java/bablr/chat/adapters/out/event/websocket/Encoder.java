package bablr.chat.adapters.out.event.websocket;

import bablr.chat.application.port.in.chat.Command;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.EncodeException;

public class Encoder implements jakarta.websocket.Encoder.Text<Command> {
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String encode(Command command) throws EncodeException {
        try {
            return objectMapper.writeValueAsString(command);
        } catch (JsonProcessingException e) {
            throw new EncodeException(command, "Failed to encode command", e);
        }
    }

    @Override
    public void destroy() {
        Text.super.destroy();
        objectMapper.clearCaches();
        objectMapper = null;
    }
}
