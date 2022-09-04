package com.koy.kaviewer.app.log;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.koy.kaviewer.web.core.RequestContextManagement;
import org.apache.logging.log4j.util.Strings;

import java.util.Optional;
import java.util.StringJoiner;

public class LoggerMaskingLayout extends PatternLayout {
    static {
        DEFAULT_CONVERTER_MAP.put("kaviewerLOG", LoggerMaskConvert.class.getName());
    }

    public static class LoggerMaskConvert extends MessageConverter {

        @Override
        public String convert(ILoggingEvent event) {
            final String msg = super.convert(event);
            String cluster = "[" + Optional.ofNullable(RequestContextManagement.getCluster()).orElseGet(() -> Strings.EMPTY) + "]";
            final String masked = msg.replaceAll("(?<=username=|password=).*?(?=,|\\s)", "****");
            final StringJoiner joiner = new StringJoiner("-", "", "");
            joiner.add(cluster);
            joiner.add(masked);
            return joiner.toString();
        }
    }
}
