Feature: Cluster Operations

  Background: Common Constant
    * url baseUrl
    * def bootstrapMockServers = bootstrapServer
    * def pb =  payloadBuilder

    * def clusterApi = '/api/v1/cluster'
    * def clusterMockName = 'dummyMockCluster'

    * def topicApi = '/api/v1/topic'
    * def topicMockName = 'dummyTopic'

    * def publishApi = '/api/v1/producer'

  Scenario: Create a cluster
    Given path clusterApi
    And request { clusterName: '#(clusterMockName)', bootstrapServers: '#(bootstrapMockServers)'}
    And header Accept = 'application/json'
    When method post
    Then status 200

  Scenario: Get the created created
    Given path clusterApi, 'meta'
    When method GET
    Then status 200
    And match response[*].cluster contains clusterMockName

  Scenario: Create a topic
    Given path topicApi
    And request { topicName:'#(topicMockName)', 'partitionSize':1, 'replicationFactor':1 }
    And header k-cluster = clusterMockName
    When method post
    Then status 200

  Scenario: Publish a message
    Given path publishApi
    And request { topic:'#(topicMockName)', 'partition': 0, 'key':'mockKey', 'value':'mockValue', 'headers':[ {'key':'headerKey','value': 'headerVal'}] }
    And header k-cluster = clusterMockName
    When method post
    Then status 200

  Scenario: Consume a message
    Given path topicApi, topicMockName , 'p/-1'
    And param limit = 5
    And header k-cluster = clusterMockName
    When method get
    Then status 200
    And match response[*].value contains 'mockValue'
    And match response[*].topic contains topicMockName

  Scenario: Delete a topic
    Given path topicApi
    And param topic = topicMockName
    And header k-cluster = clusterMockName
    When method delete
    Then status 200

  Scenario: Can not found the topic
    Given path topicApi, 'meta'
    And header k-cluster = clusterMockName
    When method get
    Then status 200
    And match response[*].topicName !contains topicMockName
