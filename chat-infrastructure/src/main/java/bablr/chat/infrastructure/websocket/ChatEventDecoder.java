package bablr.chat.infrastructure.websocket;

import bablr.chat.infrastructure.EventDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;

import java.util.Map;

public class ChatEventDecoder implements Decoder.Text<ChatEvent> {
    private static EventDeserializer<ChatEvent> deserializer = new EventDeserializer<>(Map.of(
            JoinChatEvent.class.getSimpleName(), JoinChatEvent.class,
            SendMessageEvent.class.getSimpleName(), SendMessageEvent.class,
            StartChatEvent.class.getSimpleName(), StartChatEvent.class
    ));

    @Override
    public ChatEvent decode(String s) throws DecodeException {
        try {
            return deserializer.deserialize(s);
        } catch (JsonProcessingException e) {
            throw new DecodeException(s, "Failed to decode chat event", e);
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
