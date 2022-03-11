package aytackydln.duyuru.adapter;

import aytackydln.duyuru.configuration.port.ConfigurationPort;
import aytackydln.duyuru.jpa.entity.ConfigurationEntity;
import aytackydln.duyuru.jpa.repository.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ConfigurationsAdapter implements ConfigurationPort {
    private final ConfigurationRepository configurationRepository;

    @Override
    public String get(String key) {
        return configurationRepository.findById(key).orElseThrow().getValue();
    }

    @Override
    public Optional<String> find(String key) {
        Optional<ConfigurationEntity> configuration = configurationRepository.findById(key);
        return configuration.map(ConfigurationEntity::getValue);
    }

    @Override
    public void set(String key, String value) {
        ConfigurationEntity configurationEntity = new ConfigurationEntity(key, value);
        configurationRepository.save(configurationEntity);
    }
}
