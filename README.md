# ...

```
# ecr login 
REPO=979050235289.dkr.ecr.ap-northeast-2.amazonaws.com/uengine
REGION=ap-northeast-2
aws ecr get-login --region $REGION --no-include-email
# aws ecr get-login return 12hour valid token then login ecr
# 아래 정보는 미리 ecr 에 레파지토리로 등록되어있어야한다.
# uengine/serviceapi
# uengine/service-kube-controller
# uengine/pod-monitor
# uengine/service-monitor
# uengine/deploy-monitor

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




cd service-UI
npm run build
docker build -t sanaloveyou/serviceui .
docker push sanaloveyou/serviceui
cd ..

```

