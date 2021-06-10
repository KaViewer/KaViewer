package com.koy.kaviewer.command;

import com.koy.kaviewer.command.client.KafkaAdminClientWrapper;
import com.koy.kaviewer.common.DefaultValues;
import com.koy.kaviewer.core.annotation.KaViewerShellComponent;
import com.koy.kaviewer.core.annotation.KaViewerShellGroupCommand;
import com.koy.kaviewer.command.entity.Topic;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.BeanListTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@ShellCommandGroup("Topic Commands")
@KaViewerShellGroupCommand("topic")
@KaViewerShellComponent
public class KafkaTopics {
    private final KafkaAdminClientWrapper kafkaAdminClient;

    @Autowired
    public KafkaTopics(KafkaAdminClientWrapper kafkaAdminClient) {
        this.kafkaAdminClient = kafkaAdminClient;
    }

    @ShellMethod(value = "topic <-n size>, list <n> topics, default all.", key = {"topic"}, prefix = "-")
    public Table topics(@ShellOption(defaultValue = DefaultValues.DEFAULT_ALL, arity = 1) String n) throws ExecutionException, InterruptedException {
        ListTopicsResult listTopicsResult = kafkaAdminClient.listTopics();
        KafkaFuture<Set<String>> names = listTopicsResult.names();
        List<Topic> topics = names.get().stream().map(Topic::new).collect(Collectors.toList());
        if (!DefaultValues.DEFAULT_ALL.equals(n) && topics.size() > Integer.parseInt(n)) {
            topics = topics.subList(0, Integer.parseInt(n));
        }
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("name", "Topic");
        TableModel model = new BeanListTableModel<>(topics, headers);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addInnerBorder(BorderStyle.oldschool);
        tableBuilder.addHeaderBorder(BorderStyle.oldschool);
        return tableBuilder.build();
    }

    @ShellMethod(value = "topic describe/desc <-t topic1 topic2 ...>, list describe topics by name, default all.", key = {"describe", "D", "desc"}, prefix = "-t")
    public Table describe(@ShellOption(optOut = true) List<String> topics) throws ExecutionException, InterruptedException {
        ListTopicsResult listTopicsResult = kafkaAdminClient.listTopics();
        KafkaFuture<Set<String>> names = listTopicsResult.names();
        DescribeTopicsResult describeTopicsResult = kafkaAdminClient.describeTopics(names.get());
        Map<String, KafkaFuture<TopicDescription>> topicFutureMap = describeTopicsResult.values();

        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("name", "Topic");
        headers.put("topicId", "TopicId");
        headers.put("internal", "Internal");
        headers.put("partitions", "Partitions");

        Predicate<String> topicFilter = topic -> topics.contains(DefaultValues.DEFAULT_ALL) || topics.contains(topic);
        List<KafkaFuture<TopicDescription>> kafkaFutures = topicFutureMap.entrySet().stream().filter(it -> topicFilter.test(it.getKey()))
                .map(Map.Entry::getValue).collect(Collectors.toList());
        List<TopicDescription> descriptions = kafkaFutures.stream().map(it -> {
            try {
                return it.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        TableModel model = new BeanListTableModel<>(descriptions, headers);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addInnerBorder(BorderStyle.oldschool);
        tableBuilder.addHeaderBorder(BorderStyle.oldschool);
        return tableBuilder.build();
    }

}

