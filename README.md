<img width="296" alt="image" src="https://user-images.githubusercontent.com/33706142/185340722-6e070255-3a82-45e7-9e80-a7704dc30764.png">

> :incoming_envelope: An Viewer and Operator for Kafka.

## Description
Today, we have lots of Kafka tools to use, but we have barely neat web Kafka UI to use and bind to our business services.
Hence, KaViewer is here, which is made for both Local, On-Premise and Cloud Native service.

## Architecture
<img width="808" alt="Architecture" src="https://user-images.githubusercontent.com/33706142/185749496-0edb4e0f-f559-4d76-87f1-e441e64f77fd.png">

## Usage
#### Local Run
Just `git clone` the repository and `cd` into the directory.
Then, run `mvn clean package` and `java -jar app/target/app-0.0.1.jar`.

#### Docker Run
Just `git clone` the repository and `cd` into the directory.
And use this simple command to run the application:
`docker build -f docker/Dockerfile.local -t kooooooy/kaviewer:0.0.1 .`

#### Helm Run
Just check the [KaViewer Helm Chart Usage](/k8s/README.md) doc and `helm install` the chart.

## Contributing
PR is welcome.

## License
KaViewer is licensed under the Apache License 2.0. @[Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)

