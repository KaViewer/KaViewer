package com.koy.kaviewer.kafka.entity.properties;

import org.springframework.core.convert.converter.Converter;

public interface KafkaPropertiesConverter<S> extends Converter<S, KafkaProperties> {


}
