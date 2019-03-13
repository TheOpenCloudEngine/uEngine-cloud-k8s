# uEnginecloud for Kubernetes

- 리소스 조회 모니터링 (Pod, Service, Deployment)
- 리소스 생성 Yaml 템플리에서 리소스 생성
- 리소스 삭제, 수정
- 커스텀 리소스 유형 생성 관리
- 토폴로지 모델링 및 디플로이 

![image](https://user-images.githubusercontent.com/487999/54256571-cfcdd300-459f-11e9-89d3-c457a50676c0.png)

![image](https://user-images.githubusercontent.com/487999/54256645-0e638d80-45a0-11e9-9e09-74b2fece8956.png)


## back-end 설치 
```
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

## front-end 설치
```
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
