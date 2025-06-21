# Sistema de Generación de Cotizaciones
Este repositorio contiene el último código fuente del Sistema de Generación de Cotizaciones

## Descripción general de los servicios

- Client Service
- Product Service
- Quotation Service
- Notification Service
- API Gateway 
- Frontend using Angular 18

## Stack tecnológico

Las tecnologías utilizadas en este proyecto son:

- Spring Boot
- Angular
- MySQL
- Kafka
- Keycloak
- Grafana Stack (Prometheus, Grafana, Loki and Tempo)
- Docker
- Kubernetes

##  Arquitectura del Cluster de Kubernetes
![image](https://github.com/FrankElias27/Proyecto-Cotizacion-Microservicios/blob/main/ClusterKubernetes.png)

##  Arquitectura de la aplicación
![image](https://github.com/FrankElias27/Proyecto-Cotizacion-Microservicios/blob/main/SistemaCotizacion.png)

## Cómo Implementar la infraestructura

### Implementar la infraestructura

Una vez copiados los archivos que se encuentran en Proyecto-Cotizacion-Microservicios/k8s/manifest/ a nuestro servidor de nombre Nodo Maestro.
Ejecute los archivos .yml para desplegar la infraestructura. Por ejemplo:

```shell
kubectl apply -f kafka.yml
```
### Implementar los servicios

Acceda a la carpeta applications y ejecute los archivos .yml para desplegar los servicios. Por ejemplo:

```shell
cd applications
```

```shell
kubectl apply -f frontend.yml
```
Debera obtener el siguiente resultado.

```shell
kubectl get pods -o wide
```

| NAME                                  | READY | STATUS  | RESTARTS      | AGE   | IP            | NODE           | 
| ------------------------------------- | ----- | ------- | ------------- | ----- | ------------- | -------------- | 
| api-gateway-6b84d9ccf9-cbdtl          | 1/1   | Running | 1 (105s ago)  | 156m  | 192.168.3.127 | worker3-server |
| broker-7897476547-plznt               | 1/1   | Running | 3 (106s ago)  | 20h   | 192.168.4.212 | worker4-server | 
| client-service-76f9d59c4c-6dcm6       | 1/1   | Running | 1 (105s ago)  | 156m  | 192.168.3.50  | worker3-server |
| frontend-76dd44f7c9-xnmt8             | 1/1   | Running |  2 (2m42s ago)| 15h   | 192.168.2.144 | worker2-server |
| grafana-5875fbf85-9lv92               | 1/1   | Running | 14 (119s ago) | 5d23h | 192.168.1.153 | worker1-server | 
| kafka-ui-59dd4954b5-pz22m             | 1/1   | Running | 3 (106s ago)  | 20h   | 192.168.4.8   | worker4-server | 
| keycloak-957c65686-x5tgf              | 1/1   | Running | 9 (116s ago)  | 4d21h | 192.168.2.17  | worker2-server | 
| loki-bc54ffd8-tqnzd                   | 1/1   | Running | 14 (119s ago) | 5d23h | 192.168.1.171 | worker1-server | 
| mysql-7b888cdc48-mxcgb                | 1/1   | Running | 11 (119s ago) | 4d22h | 192.168.1.113 | worker1-server | 
| nginx-proxy-675cfcf87b-7ztl8          | 1/1   | Running | 3 (20s ago)   | 15h   | 192.168.4.72  | worker4-server |
| notification-service-7d8d76b475-jwf5b | 1/1   | Running | 1 (105s ago)  | 139m  | 192.168.5.59  | worker5-server | 
| product-service-7c8c84cf89-k59m2      | 1/1   | Running | 1 (105s ago)  | 156m  | 192.168.3.172 | worker3-server |
| prometheus-6b4cd5c7fc-95qzk           | 1/1   | Running | 11 (119s ago) | 4d22h | 192.168.1.211 | worker1-server | 
| quotation-service-7744f9cc8d-hlml9    | 1/1   | Running | 1 (105s ago)  | 156m  | 192.168.5.11  | worker5-server | 
| schema-registry-6bc997f695-zgqnt      | 1/1   | Running | 2 (105s ago)  | 15h   | 192.168.5.196 | worker5-server |
| tempo-7c8966c5d6-s84mb                | 1/1   | Running | 11 (119s ago) | 4d22h | 192.168.1.56  | worker1-server |
| zookeeper-7b644bdfcd-fknsr            | 1/1   | Running | 3 (106s ago)  | 20h   | 192.168.4.179 | worker4-server | 


