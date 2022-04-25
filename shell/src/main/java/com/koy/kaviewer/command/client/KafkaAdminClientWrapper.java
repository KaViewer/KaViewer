package com.koy.kaviewer.command.client;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AlterClientQuotasOptions;
import org.apache.kafka.clients.admin.AlterClientQuotasResult;
import org.apache.kafka.clients.admin.AlterConfigOp;
import org.apache.kafka.clients.admin.AlterConfigsOptions;
import org.apache.kafka.clients.admin.AlterConfigsResult;
import org.apache.kafka.clients.admin.AlterConsumerGroupOffsetsOptions;
import org.apache.kafka.clients.admin.AlterConsumerGroupOffsetsResult;
import org.apache.kafka.clients.admin.AlterPartitionReassignmentsOptions;
import org.apache.kafka.clients.admin.AlterPartitionReassignmentsResult;
import org.apache.kafka.clients.admin.AlterReplicaLogDirsOptions;
import org.apache.kafka.clients.admin.AlterReplicaLogDirsResult;
import org.apache.kafka.clients.admin.AlterUserScramCredentialsOptions;
import org.apache.kafka.clients.admin.AlterUserScramCredentialsResult;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.CreateAclsOptions;
import org.apache.kafka.clients.admin.CreateAclsResult;
import org.apache.kafka.clients.admin.CreateDelegationTokenOptions;
import org.apache.kafka.clients.admin.CreateDelegationTokenResult;
import org.apache.kafka.clients.admin.CreatePartitionsOptions;
import org.apache.kafka.clients.admin.CreatePartitionsResult;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteAclsOptions;
import org.apache.kafka.clients.admin.DeleteAclsResult;
import org.apache.kafka.clients.admin.DeleteConsumerGroupOffsetsOptions;
import org.apache.kafka.clients.admin.DeleteConsumerGroupOffsetsResult;
import org.apache.kafka.clients.admin.DeleteConsumerGroupsOptions;
import org.apache.kafka.clients.admin.DeleteConsumerGroupsResult;
import org.apache.kafka.clients.admin.DeleteRecordsOptions;
import org.apache.kafka.clients.admin.DeleteRecordsResult;
import org.apache.kafka.clients.admin.DeleteTopicsOptions;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.DescribeAclsOptions;
import org.apache.kafka.clients.admin.DescribeAclsResult;
import org.apache.kafka.clients.admin.DescribeClientQuotasOptions;
import org.apache.kafka.clients.admin.DescribeClientQuotasResult;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.DescribeConfigsOptions;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsOptions;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsResult;
import org.apache.kafka.clients.admin.DescribeDelegationTokenOptions;
import org.apache.kafka.clients.admin.DescribeDelegationTokenResult;
import org.apache.kafka.clients.admin.DescribeFeaturesOptions;
import org.apache.kafka.clients.admin.DescribeFeaturesResult;
import org.apache.kafka.clients.admin.DescribeLogDirsOptions;
import org.apache.kafka.clients.admin.DescribeLogDirsResult;
import org.apache.kafka.clients.admin.DescribeReplicaLogDirsOptions;
import org.apache.kafka.clients.admin.DescribeReplicaLogDirsResult;
import org.apache.kafka.clients.admin.DescribeTopicsOptions;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.DescribeUserScramCredentialsOptions;
import org.apache.kafka.clients.admin.DescribeUserScramCredentialsResult;
import org.apache.kafka.clients.admin.ElectLeadersOptions;
import org.apache.kafka.clients.admin.ElectLeadersResult;
import org.apache.kafka.clients.admin.ExpireDelegationTokenOptions;
import org.apache.kafka.clients.admin.ExpireDelegationTokenResult;
import org.apache.kafka.clients.admin.FeatureUpdate;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsOptions;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupsOptions;
import org.apache.kafka.clients.admin.ListConsumerGroupsResult;
import org.apache.kafka.clients.admin.ListOffsetsOptions;
import org.apache.kafka.clients.admin.ListOffsetsResult;
import org.apache.kafka.clients.admin.ListPartitionReassignmentsOptions;
import org.apache.kafka.clients.admin.ListPartitionReassignmentsResult;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewPartitionReassignment;
import org.apache.kafka.clients.admin.NewPartitions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.OffsetSpec;
import org.apache.kafka.clients.admin.RecordsToDelete;
import org.apache.kafka.clients.admin.RemoveMembersFromConsumerGroupOptions;
import org.apache.kafka.clients.admin.RemoveMembersFromConsumerGroupResult;
import org.apache.kafka.clients.admin.RenewDelegationTokenOptions;
import org.apache.kafka.clients.admin.RenewDelegationTokenResult;
import org.apache.kafka.clients.admin.UnregisterBrokerOptions;
import org.apache.kafka.clients.admin.UnregisterBrokerResult;
import org.apache.kafka.clients.admin.UpdateFeaturesOptions;
import org.apache.kafka.clients.admin.UpdateFeaturesResult;
import org.apache.kafka.clients.admin.UserScramCredentialAlteration;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.ElectionType;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionReplica;
import org.apache.kafka.common.acl.AclBinding;
import org.apache.kafka.common.acl.AclBindingFilter;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.quota.ClientQuotaAlteration;
import org.apache.kafka.common.quota.ClientQuotaFilter;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class KafkaAdminClientWrapper extends AdminClient {

    private AdminClientCfg config;
    private AdminClient delegate;

    public void create() {
        delegate = create(config);
    }

    public void config(AdminClientCfg config) {
        this.config = config;
    }

    public AdminClient getDelegate() {
        return delegate;
    }

    public AdminClientCfg getConfig() {
        return config;
    }

    @Override
    public void close(Duration duration) {
        delegate.close();
    }

    @Override
    public CreateTopicsResult createTopics(Collection<NewTopic> collection, CreateTopicsOptions createTopicsOptions) {
        return delegate.createTopics(collection, createTopicsOptions);
    }

    // TODO
    @Override
    public DeleteTopicsResult deleteTopics(Collection<String> collection, DeleteTopicsOptions deleteTopicsOptions) {
        throw new UnsupportedOperationException();
    }


    @Override
    public ListTopicsResult listTopics() {
        return delegate.listTopics();
    }

    @Override
    public ListTopicsResult listTopics(ListTopicsOptions listTopicsOptions) {
        return delegate.listTopics(listTopicsOptions);
    }

    @Override
    public DescribeClusterResult describeCluster() {
        return delegate.describeCluster();
    }

    @Override
    public DescribeClusterResult describeCluster(DescribeClusterOptions describeClusterOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeAclsResult describeAcls(AclBindingFilter aclBindingFilter, DescribeAclsOptions describeAclsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateAclsResult createAcls(Collection<AclBinding> collection, CreateAclsOptions createAclsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DeleteAclsResult deleteAcls(Collection<AclBindingFilter> collection, DeleteAclsOptions deleteAclsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeConfigsResult describeConfigs(Collection<ConfigResource> collection, DescribeConfigsOptions describeConfigsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AlterConfigsResult alterConfigs(Map<ConfigResource, Config> map, AlterConfigsOptions alterConfigsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AlterConfigsResult incrementalAlterConfigs(Map<ConfigResource, Collection<AlterConfigOp>> map, AlterConfigsOptions alterConfigsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AlterReplicaLogDirsResult alterReplicaLogDirs(Map<TopicPartitionReplica, String> map, AlterReplicaLogDirsOptions alterReplicaLogDirsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeLogDirsResult describeLogDirs(Collection<Integer> collection, DescribeLogDirsOptions describeLogDirsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeReplicaLogDirsResult describeReplicaLogDirs(Collection<TopicPartitionReplica> collection, DescribeReplicaLogDirsOptions describeReplicaLogDirsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeTopicsResult describeTopics(Collection<String> collection, DescribeTopicsOptions describeTopicsOptions) {
        return delegate.describeTopics(collection, describeTopicsOptions);
    }

    @Override
    public CreatePartitionsResult createPartitions(Map<String, NewPartitions> map, CreatePartitionsOptions createPartitionsOptions) {
        return delegate.createPartitions(map, createPartitionsOptions);
    }

    @Override
    public DeleteRecordsResult deleteRecords(Map<TopicPartition, RecordsToDelete> map, DeleteRecordsOptions deleteRecordsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateDelegationTokenResult createDelegationToken(CreateDelegationTokenOptions createDelegationTokenOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RenewDelegationTokenResult renewDelegationToken(byte[] bytes, RenewDelegationTokenOptions renewDelegationTokenOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExpireDelegationTokenResult expireDelegationToken(byte[] bytes, ExpireDelegationTokenOptions expireDelegationTokenOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeDelegationTokenResult describeDelegationToken(DescribeDelegationTokenOptions describeDelegationTokenOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeConsumerGroupsResult describeConsumerGroups(Collection<String> collection, DescribeConsumerGroupsOptions describeConsumerGroupsOptions) {
        return delegate.describeConsumerGroups(collection,describeConsumerGroupsOptions);
    }

    @Override
    public ListConsumerGroupsResult listConsumerGroups() {
        return delegate.listConsumerGroups();
    }

    @Override
    public ListConsumerGroupsResult listConsumerGroups(ListConsumerGroupsOptions listConsumerGroupsOptions) {
        return delegate.listConsumerGroups(listConsumerGroupsOptions);
    }

    @Override
    public ListConsumerGroupOffsetsResult listConsumerGroupOffsets(String s, ListConsumerGroupOffsetsOptions listConsumerGroupOffsetsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DeleteConsumerGroupsResult deleteConsumerGroups(Collection<String> collection, DeleteConsumerGroupsOptions deleteConsumerGroupsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DeleteConsumerGroupOffsetsResult deleteConsumerGroupOffsets(String s, Set<TopicPartition> set, DeleteConsumerGroupOffsetsOptions deleteConsumerGroupOffsetsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ElectLeadersResult electLeaders(ElectionType electionType, Set<TopicPartition> set, ElectLeadersOptions electLeadersOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AlterPartitionReassignmentsResult alterPartitionReassignments(Map<TopicPartition, Optional<NewPartitionReassignment>> map, AlterPartitionReassignmentsOptions alterPartitionReassignmentsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListPartitionReassignmentsResult listPartitionReassignments(Optional<Set<TopicPartition>> optional, ListPartitionReassignmentsOptions listPartitionReassignmentsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RemoveMembersFromConsumerGroupResult removeMembersFromConsumerGroup(String s, RemoveMembersFromConsumerGroupOptions removeMembersFromConsumerGroupOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AlterConsumerGroupOffsetsResult alterConsumerGroupOffsets(String s, Map<TopicPartition, OffsetAndMetadata> map, AlterConsumerGroupOffsetsOptions alterConsumerGroupOffsetsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListOffsetsResult listOffsets(Map<TopicPartition, OffsetSpec> map, ListOffsetsOptions listOffsetsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeClientQuotasResult describeClientQuotas(ClientQuotaFilter clientQuotaFilter, DescribeClientQuotasOptions describeClientQuotasOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AlterClientQuotasResult alterClientQuotas(Collection<ClientQuotaAlteration> collection, AlterClientQuotasOptions alterClientQuotasOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeUserScramCredentialsResult describeUserScramCredentials(List<String> list, DescribeUserScramCredentialsOptions describeUserScramCredentialsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AlterUserScramCredentialsResult alterUserScramCredentials(List<UserScramCredentialAlteration> list, AlterUserScramCredentialsOptions alterUserScramCredentialsOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeFeaturesResult describeFeatures(DescribeFeaturesOptions describeFeaturesOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UpdateFeaturesResult updateFeatures(Map<String, FeatureUpdate> map, UpdateFeaturesOptions updateFeaturesOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UnregisterBrokerResult unregisterBroker(int i, UnregisterBrokerOptions unregisterBrokerOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<MetricName, ? extends Metric> metrics() {
        throw new UnsupportedOperationException();
    }


}