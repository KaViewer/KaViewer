package com.koy.kaviewer.core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.koy.kaviewer.common.DefaultValues;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class KaViewerCommandHistory {

    @Autowired
    @Lazy
    private Terminal terminal;

    private static final Cache<String, Long> commandHis = CacheBuilder.newBuilder()
            .initialCapacity(500)
            .maximumSize(1000)
            .build();

    public void addCommandHis(String cmd, Long line) {
        commandHis.put(cmd, line);
    }

    public void showHis(String token) {
        List<String> his = commandHis.asMap().entrySet().stream()
                .filter(it -> DefaultValues.isDefaultValue(token) || it.getKey().contains(token))
                .sorted((e1, e2) -> (int) (e1.getValue() - e2.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        AttributedStringBuilder attributedStringBuilder = new AttributedStringBuilder();
        his.forEach(cmd -> {
            attributedStringBuilder.append(cmd);
            attributedStringBuilder.append("\n");
        });
        if (StringUtils.hasLength(token)) {
            terminal.writer().println(
                    attributedStringBuilder.toAnsi()
            );
            terminal.writer().flush();
        }

    }
}
