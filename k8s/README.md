# Usage for local development with Minikube

## Create Test Namespace

> Test namespace is created for local development named `kaviewer``.

<!--kubectl create namespace kaviewer --dry-run -o yaml  -->

```shell
kubectl create namespace kaviewer
```

or use this `yaml` file localed in `k8s/local` folder.

```shell
kubectl apply -f test-namespace.yaml
```

## Install Test Kafka

> Install Kafka Deployment and Export Kafka Service
> Kafka Deployment is exported to `k8s/local/test-kafka-deployment.yaml`
> Kafka Service is exported to `k8s/local/test-kafka-service.yaml`

```shell
kubectl apply -f test-kafka-deployment.yaml
```

```shell
kubectl apply -f test-kafka-service.yaml
```

## Local Check Test Kafka

Using `port-forwarding` to access Kafka and test with local Kafka manager or `KaViewer` app locally.

```shell
kubectl port-forward svc/test-kafka-svc 9092:9092
```

## Install KaViewer Helm Chart

> [Minikube](https://minikube.sigs.k8s.io/) as example.

Before install, you need to install the local `KaViewer image` into [Minikube](https://minikube.sigs.k8s.io/).

```shell
 eval $(minikube docker-env)
```

```shell
 docker build -f docker/Dockerfile -t kooooooy/kaviewer:latest .  
```

Check Images in Minikube

```shell
 minikube image ls
```

> Tips: Delete images  `minikube image rm docker.io/kooooooy/kaviewer:latest`

#### Install KaViewer

```shell
helm install kaviewer-latest-release kaviewer --namespace kaviewer 
```

```shell
> Output

Get the application URL by running these commands:
  export NODE_PORT=$(kubectl get --namespace kaviewer -o jsonpath="{.spec.ports[0].nodePort}" services kaviewer-latest-release)
  export NODE_IP=$(kubectl get nodes --namespace kaviewer -o jsonpath="{.items[0].status.addresses[0].address}")
  echo http://$NODE_IP:$NODE_PORT


```

Check `Helm release` to see if KaViewer is installed.

```shell
 helm list
```

Check `Minikube dashboard` to see if KaViewer is running.

> Minikube dashboard UI .
```shell
 minikube dashboard
```
or 

```shell
kubectl get pods --namespace kaviewer
kubectl get svc --namespace kaviewer
```

Check `KaViewer` UI

```shell
 minikube service kaviewer <--url>
```

or

```shell
 kubectl port-forward svc/kaviewer-latest-release 8080:9394 (app)
 kubectl port-forward svc/kaviewer-latest-release 10086:10086 (actuator endpoint)
```

#### Uninstall KaViewer

```shell
helm uninstall kaviewer-latest-release
```