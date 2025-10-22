package bablr.chat.infrastructure.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;

public class ChatEventEncoder implements Encoder.Text<ChatEvent> {
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String encode(ChatEvent chatEvent) throws EncodeException {
        try {
            return objectMapper.writeValueAsString(chatEvent);
        } catch (JsonProcessingException e) {
            throw new EncodeException(chatEvent, "Failed to encode chat event", e);
        }
    }

    @Override
    public void destroy() {
        Text.super.destroy();
        objectMapper.clearCaches();
        objectMapper = null;
    }
}
