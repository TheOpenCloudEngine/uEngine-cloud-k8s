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
*** back-end 설치 ***

1. 설치할 helm folder로 이동
 - cd helm/uengine-kube

2. values.xml 값 수정
 - kubehost: k8s가 설치되어 있는 도메인 주소
 - kubetoken: k8s 접근하기 위한 토큰 정보

3. helm install
 - helm repo add googleapis https://kubernetes-charts-incubator.storage.googleapis.com/
 - helm repo add incubator http://storage.googleapis.com/kubernetes-charts-incubator
 - helm dependency update
 - helm init
 - helm install --name uengine-kube  --set mysql.mysqlRootPassword=test,mysql.mysqlUser=test,mysql.mysqlPassword=test,mysql.mysqlDatabase=uengine .
  -- DB 유저 및 tablespace 명을 바꾸면 실행되지 않는다.

4. nginx-ingress 의 External-IP 취득
 - kubectl get svc 
  -- EXTERNAL-IP 값 복사 저장해 둔다.
```

```
*** front-end 설치 ***
1. 설치할 helm folder로 이동
 - cd helm/uengine-kube-ui

2. values.xml 값 수정
 - repo: 이미지 저장경로(여기선 default 것 사용 - docker hub)
 - apihost: back-end의 4번 externalip 세팅
  -- 예) apihost: http://xxx.test.com

3. helm install
 - helm install --name uengine-kube-ui .

4. 접속할 external ip 취득
 - kubectl get svc 
  -- serviceui의  EXTERNAL-IP, PORTS 정보 확인

5. 브라우저 접속
 - 4번의 EXTERNAL-IP, PORTS로 접속
  -- http://[EXTERNAL-IP]:[PORTS]
```

```
-- 삭제
helm delete uengine-kube-ui --purge
helm delete uengine-kube --purge
```
