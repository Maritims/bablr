package bablr.chat.infrastructure.config;

import java.io.IOException;
import java.util.Properties;

public class BablrConfig {
    private static final Properties props = new Properties();

    static {
        try {
            props.load(ClassLoader.getSystemResourceAsStream("bablr.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public static String get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        if (key.isBlank()) {
            throw new IllegalArgumentException("key must not be blank");
        }
        if (!props.containsKey(key)) {
            throw new IllegalArgumentException("Configuration key not found: " + key);
        }

        return props.getProperty(key);
    }

    public static Integer getInt(String key) {
        var value = get(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Failed to parse configuration value for key: " + key, e);
        }
    }
}
