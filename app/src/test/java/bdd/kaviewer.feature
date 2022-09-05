Feature: Cluster Operations

  Background: Common Constant

  Background: User is Logged In
    * url baseUrl
    * def pb =  payloadBuilder
    * def clusterApi = '/api/v1/cluster'
    * def clusterDummyName = 'dummyCluster222'

  Scenario: Create a cluster
    Given path clusterApi
    And request
    And request { clusterName: '#(clusterDummyName)', bootstrapServers: 'localhost:9092'}
    And header Accept = 'application/json'
    When method post
    Then status 200

  Scenario: Get the created created
    Given path clusterApi + '/meta'
    And header k-cluster = clusterDummyName
    When method GET
    Then status 200
    And match response[*].cluster contains clusterDummyName

#  Scenario: Create a topic

