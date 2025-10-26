package bablr.chat.adapters.out.event.websocket;

import bablr.chat.application.port.in.chat.Command;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.websocket.DecodeException;

import java.util.Map;

public class Decoder implements jakarta.websocket.Decoder.Text<Command> {
    private static Deserializer<Command> deserializer = new Deserializer<>(Map.of(
    ));

    @Override
    public Command decode(String s) throws DecodeException {
        try {
            return deserializer.deserialize(s);
        } catch (JsonProcessingException e) {
            throw new DecodeException(s, "Failed to decode command", e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return s != null && !s.isBlank();
    }

    @Override
    public void destroy() {
        Text.super.destroy();
        deserializer = null;
    }
}
