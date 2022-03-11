package aytackydln.duyuru.configuration.port;

import java.util.Optional;

public interface ConfigurationPort {
    String get(String key);
    Optional<String> find(String key);
    void set(String key, String value);
}
