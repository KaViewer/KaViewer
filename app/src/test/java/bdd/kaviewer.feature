Feature: Cluster Operations

  Background: Common Constant
    * url baseUrl
    * def bootstrapDummyServers = bootstrapServer
    * def pb =  payloadBuilder
    * def clusterApi = '/api/v1/cluster'
    * def clusterDummyName = 'dummyCluster'

  Scenario: Create a cluster
    Given path clusterApi
    And request { clusterName: '#(clusterDummyName)', bootstrapServers: '#(bootstrapDummyServers)'}
    And header Accept = 'application/json'
    When method post
    Then status 200

  Scenario: Get the created created
    Given path clusterApi
    When method GET
    Then status 200
    And match response[*] contains clusterDummyName

#  Scenario: Create a topic

