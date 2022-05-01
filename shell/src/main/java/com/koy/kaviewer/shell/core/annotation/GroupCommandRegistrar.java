package com.koy.kaviewer.shell.core.annotation;

import com.google.common.collect.Lists;
import com.koy.kaviewer.shell.core.KaViewerParsedLine;
import org.jline.reader.ParsedLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GroupCommandRegistrar {

    private static final ConcurrentHashMap<String, Function<String, String>> groupCommands = new ConcurrentHashMap<>();
    private final ApplicationContext applicationContext;

    @Autowired
    public GroupCommandRegistrar(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void register() {
        Map<String, Object> groups = applicationContext.getBeansWithAnnotation(KaViewerShellGroupCommand.class);
        groups.entrySet().stream().parallel().forEach(e -> {
            Object beanInstance = e.getValue();
            KaViewerShellGroupCommand kaViewerShellGroupCommand = beanInstance.getClass().getAnnotation(KaViewerShellGroupCommand.class);
            String groupCommand = kaViewerShellGroupCommand.value();

            Function<String, String> trim = cmd -> {
                final String origin = cmd;
                String regex = "(" + groupCommand + "\\s+).*";
                Matcher matcher = Pattern.compile(regex).matcher(cmd);
                if (matcher.find()) {
                    String group = matcher.group(1);
                    cmd = cmd.replace(matcher.group(1), "");
                    // no main command
                    if (cmd.startsWith("-")) {
                        cmd = group.trim() + "#" + origin;
                    } else {
                        cmd = group.trim() + "#" + cmd;
                    }
                    return cmd;
                }
                // topic describe -t xxx   ->   topic#describe -t xxx
                // topic -n -> topic#topic -n
                // topic -> topic#topic
                // help -> help
                return groupCommand.contains(cmd) ? cmd + "#" + cmd : cmd;
            };
            groupCommands.put(groupCommand, trim);
        });
    }

    public ParsedLine trimGroupCommand(ParsedLine parsedLine) {
        String regex = "(\\w+\\s*).*";
        Matcher matcher = Pattern.compile(regex).matcher(parsedLine.line());
        String group = parsedLine.line();
        if (matcher.find()) {
            group = matcher.group(1).trim();
        }

        Function<String, String> trimFunc = groupCommands.getOrDefault(group, (cmd) -> cmd);
        String trimCommand = trimFunc.apply(parsedLine.line());

        String[] tokens = trimCommand.split(" ");
        List<String> newWords = Lists.newArrayListWithCapacity(tokens.length);
        Arrays.stream(tokens).forEach(it -> newWords.add(it.trim()));

        return new KaViewerParsedLine(trimCommand,
                newWords,
                newWords.size() - 1,
                parsedLine.wordCursor(),
                trimCommand.length());
    }
}
