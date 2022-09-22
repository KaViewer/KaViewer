<img width="296" alt="image" src="https://user-images.githubusercontent.com/33706142/185340722-6e070255-3a82-45e7-9e80-a7704dc30764.png">

> <img src="https://github.githubassets.com/images/modules/profile/achievements/quickdraw-default.png" width="35" alt="Achievement: Quickdraw"> An Viewer and Operator for Kafka.
---
[![KaViewer CI](https://github.com/KaViewer/KaViewer/actions/workflows/build.yml/badge.svg)](https://github.com/KaViewer/KaViewer/actions/workflows/build.yml)
[![CodeQL](https://github.com/KaViewer/KaViewer/actions/workflows/codescan.yml/badge.svg)](https://github.com/KaViewer/KaViewer/actions/workflows/codescan.yml)
[![GitHub license](https://img.shields.io/github/license/KaViewer/KaViewer)](https://github.com/KaViewer/KaViewer/blob/main/LICENSE)
---

<img width="1360" alt="image" src="https://user-images.githubusercontent.com/33706142/188314516-c63cb925-c044-456e-a6eb-cfc129bdd88e.png">

## Description

Today, we have lots of Kafka tools to use, but we have barely neat web Kafka UI to use and bind to our business
services. Hence, KaViewer is here, which is made for both Local, On-Premise and Cloud Native service. KaViewer web ui,
let you can view and operate kafka with permission control, more details see [Architecture](##Architecture).

## Feature

### Permission Control

KaViewer natural support those operations:

- Add/Delete New Kafka Cluster/Broker (Not delete the real instance).
- Add/Delete Topic.
- Publish/Consumer Messages.

Based on those function, there have two mode to suit different scenario.

- FULL Support all the operations above.
- LITE (default)
  Only Support view kafka message.

And support the custom toggle config to enable or disable those operations as well.

| Group            | Config Name                    | Config Value | Description                         |
|------------------|--------------------------------|--------------|-------------------------------------|
| Cluster          | kaviewer.toggle.cluster.create | Boolean      | Allow to add new cluster (true)     |
| Cluster          | kaviewer.toggle.cluster.delete | Boolean      | Allow to delete new cluster (true)  |
| Topic            | kaviewer.toggle.topic.create   | Boolean      | Allow to create new topic (true)    |
| Topic            | kaviewer.toggle.topic.delete   | Boolean      | Allow to delete new topic (true)    |
| Consumer         | kaviewer.toggle.consumer.write | Boolean      | Allow to publish new message (true) |
| Consumer         | kaviewer.toggle.consumer.read  | Boolean      | Allow to consume new message (true) |

### Persist API

The exposed API interface `Persistent` let you do the enhancement to persist KaViewer data. It is similar to SpringBoot
autoconfiguration mechanism. You can import `kaviewer-runner.jar` as dependency and implement the `Persistent`
interface, then add the `META-INF/kaviewer.factories` to do the trick.

## Usage

> Require `Java >=11` and `Kafka >=0.11.0`.

![kaviewer-auto](https://user-images.githubusercontent.com/33706142/188315278-89ee6d19-02a0-4333-92ca-56e5b02fbb30.gif)


#### Build Run
Use `git clone` to clone the repository.  
Go into the directory and run `mvn clean package -Dmaven.test.skip=true --file pom.xml` to build project.   
Run `java -jar app/target/app-0.0.1.jar`.

#### Docker Run

> Make sure you already have the docker env.  
 
There have many [`docker-compose file`](/docker) for different scenarios.

- Run with DockerHub public image  
  `docker-compose -f docker-compose.yaml up -d`  

- Checkout use this simple command to run the application from project:  
  `docker-compose -f docker-compose-local-app.yaml up -d`  

#### Kubernetes Run
KaViewer uses the `Helm`to install and deploy to kubernetes resources.  
Please check the [KaViewer Helm Chart Usage](/k8s/README.md) doc for more details.  

## Architecture

<img width="808" alt="Architecture" src="https://user-images.githubusercontent.com/33706142/185749496-0edb4e0f-f559-4d76-87f1-e441e64f77fd.png">

## Contributing

Folk this project and build locally `mvn clean build`. Make your best changes and send a PR. :rocket:  
Using the `Issue` trace is a plus.

## License

KaViewer is licensed under the Apache License 2.0. @[Koy](https://github.com/Koooooo-7)

## Star History

<img height="280px" src="https://api.star-history.com/svg?repos=KaViewer/KaViewer&amp;type=Date" title="Star History" width="80%"/>
