package com.koy.kaviewer.shell.core.converter;


import com.google.common.base.Splitter;
import com.koy.kaviewer.shell.common.DefaultValues;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Component
public class KaViewerConverter implements Converter<String, List<String>> {

    @Override
    public List<String> convert(String source) {
        if (!StringUtils.hasLength(source) || DefaultValues.isDefaultValue(source)){
            return Collections.singletonList(DefaultValues.DEFAULT_ALL);
        }
        return Splitter.on(",")
                .trimResults()
                .splitToList(source);
    }



}
