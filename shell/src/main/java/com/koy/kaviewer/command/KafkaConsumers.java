package com.koy.kaviewer.command;

import com.koy.kaviewer.command.client.KafkaAdminClientWrapper;
import com.koy.kaviewer.command.entity.Consumer;
import com.koy.kaviewer.common.DefaultValues;
import com.koy.kaviewer.core.annotation.KaViewerShellComponent;
import com.koy.kaviewer.core.annotation.KaViewerShellGroupCommand;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.BeanListTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@KaViewerShellComponent
@ShellCommandGroup("Consumer Commands")
@KaViewerShellGroupCommand("consumer")
public class KafkaConsumers {
    private final KafkaAdminClientWrapper kafkaAdminClient;

    @Autowired
    public KafkaConsumers(KafkaAdminClientWrapper kafkaAdminClient) {
        this.kafkaAdminClient = kafkaAdminClient;
    }

    @ShellMethod(value = "consumer <-n size>, list <n> consumer, default all.", key = {"consumer"}, prefix = "-")
    public Table consumer(@ShellOption(defaultValue = DefaultValues.DEFAULT_ALL, arity = 1) String n) throws ExecutionException, InterruptedException {
        Collection<ConsumerGroupListing> consumerGroupListings = kafkaAdminClient.listConsumerGroups().all().get();
        List<Consumer> consumers = consumerGroupListings.stream().map(cg -> new Consumer(cg.groupId())).collect(Collectors.toList());
        if (!DefaultValues.DEFAULT_ALL.equals(n) && Integer.parseInt(n) < consumers.size()) {
            consumers = consumers.subList(0, Integer.parseInt(n));
        }

        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("groupId", "ConsumerGroup");
        TableModel model = new BeanListTableModel<>(consumers, headers);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addInnerBorder(BorderStyle.oldschool);
        tableBuilder.addHeaderBorder(BorderStyle.oldschool);
        return tableBuilder.build();
    }

    @ShellMethod(value = "consumer describe/desc -g groupName1 -t topicName, describe consumer group details for a topic", key = {"describe", "D", "desc"}, prefix = "-")
    public Table describes(@ShellOption String t, @ShellOption String g) throws ExecutionException, InterruptedException {
        Map<String, ConsumerGroupDescription> groups = kafkaAdminClient.
                describeConsumerGroups(Collections.singleton(g)).all().get();
        ConsumerGroupDescription consumerGroupDescription = groups.get(g);
        List<Consumer> consumers = new LinkedList<>();
        consumerGroupDescription.members().stream()
                .filter(md -> md.assignment().topicPartitions().stream().anyMatch(tp -> t.equalsIgnoreCase(tp.topic()))
                ).forEach(memberDescription -> {
            Consumer consumer = new Consumer();
            consumer.setGroupId(g);
            consumer.setTopic(t);
            consumer.setConsumerId(memberDescription.consumerId());
            consumer.setTopicPartitions(memberDescription.assignment().topicPartitions());
            consumer.setClientId(memberDescription.clientId());
            consumer.setHost(memberDescription.host());
            consumers.add(consumer);
        });

        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("groupId", "ConsumerGroup");
        headers.put("topic", "Topic");
        headers.put("consumerId", "ConsumerId");
        headers.put("clientId", "clientId");
        headers.put("host", "Host");
        headers.put("topicPartitions", "Partitions");
        TableModel model = new BeanListTableModel<>(consumers, headers);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addInnerBorder(BorderStyle.oldschool);
        tableBuilder.addHeaderBorder(BorderStyle.oldschool);
        return tableBuilder.build();
    }
}

