package com.koy.kaviewer.app.core;

import com.koy.kaviewer.kafka.entity.properties.PropertiesResources;
import com.koy.kaviewer.kafka.entity.KafkaPropertiesVO;
import com.koy.kaviewer.kafka.entity.properties.KafkaProperties;
import com.koy.kaviewer.kafka.entity.properties.KafkaPropertiesConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityRequestSourceLoader implements ConfigSourceLoader<KafkaPropertiesVO, PropertiesResources<KafkaPropertiesVO>> {


    @Override
    public KafkaPropertiesVO load(PropertiesResources<KafkaPropertiesVO> source) {
        return source.getResource();
    }


    @Override
    public boolean support(PropertiesResources.ResourcesType type) {
        return PropertiesResources.ResourcesType.ENTITY == type;
    }

    @Override
    public <V> Converter<KafkaPropertiesVO, V> getConvertor() {
        return (Converter<KafkaPropertiesVO, V>) entityConvertor;
    }

    private static final KafkaPropertiesConverter<KafkaPropertiesVO> entityConvertor = configs -> {
        final KafkaProperties kafkaProperties = new KafkaProperties();
        kafkaProperties.setBootstrapServers(configs.getBootstrapServers());
        kafkaProperties.setClusterName(configs.getClusterName());
        return kafkaProperties.buildProperties();
    };
}
