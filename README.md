# ...

```
cd service-api
mvn clean package -B
docker build -t sanaloveyou/serviceapi .
docker push sanaloveyou/serviceapi
cd ..

cd service-snapshot
mvn clean package -B
docker build -t sanaloveyou/servicesnap .
docker push sanaloveyou/servicesnap
cd ..


cd kube-monitor
mvn clean package -B
docker build -t sanaloveyou/servicemonitor .
docker push sanaloveyou/servicemonitor
cd ..


cd service-UI
npm run build
docker build -t sanaloveyou/serviceui .
docker push sanaloveyou/serviceui
cd ..

```


ingress-nginx 설치  

kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/mandatory.yaml  
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/provider/baremetal/service-nodeport.yaml  

LoadBalancer로 서비스 타입 변경
