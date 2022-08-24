package com.koy.kaviewer.common.entity.properties;

import org.springframework.core.convert.converter.Converter;

public interface KafkaPropertiesConverter<S> extends Converter<S, KafkaProperties> {


}
