# ...

```
# ecr login 
REPO=979050235289.dkr.ecr.ap-northeast-2.amazonaws.com/uengine
REGION=ap-northeast-2
source <(aws ecr get-login --region $REGION --no-include-email)
# aws ecr get-login return 12hour valid token then login ecr
# 아래 정보는 미리 ecr 에 레파지토리로 등록되어있어야한다.
# uengine/serviceapi
# uengine/service-kube-controller
# uengine/pod-monitor
# uengine/service-monitor
# uengine/deploy-monitor
# uengine/serviceui

# service build and push
cd service-api
mvn clean package -B
docker build -t $REPO/serviceapi .
docker push $REPO/serviceapi
cd ..

cd service-kube-controller
mvn clean package -B
docker build -t $REPO/service-kube-controller .
docker push $REPO/service-kube-controller
cd ..


cd pod-monitor
mvn clean package -B
docker build -t $REPO/pod-monitor .
docker push $REPO/pod-monitor
cd ..

cd service-monitor
mvn clean package -B
docker build -t $REPO/service-monitor .
docker push $REPO/service-monitor
cd ..

cd deploy-monitor
mvn clean package -B
docker build -t $REPO/deploy-monitor .
docker push $REPO/deploy-monitor
cd ..


cd service-UI
npm run build
docker build -t $REPO/serviceui .
docker push $REPO/serviceui
cd ..

```

```
cd helm/deployprod
helm dependency update
helm init

-- install  
helm install --dry-run --debug --name kube-uengine .
helm install --name kube-uengine .

-- 삭제  
helm delete kube-uengine --purge

-- 값을 업데이트  
helm install --name kube-uengine --set repo=aaa .
```