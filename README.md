# uEnginecloud for Kubernetes
- [UI 설명서](https://github.com/TheOpenCloudEngine/uEngine-cloud-k8s/wiki/UI-%EC%84%A4%EB%AA%85)
  - [리소스 조회 모니터링 (Pod, Service, Deployment)](https://github.com/TheOpenCloudEngine/uEngine-cloud-k8s/wiki/1.-%EC%A3%BC%EC%9A%94-%EC%82%AC%EC%9A%A9%EB%B0%A9%EB%B2%95-_-%EC%A1%B0%ED%9A%8C) 
  - [리소스 생성 Yaml 템플리에서 리소스 생성](https://github.com/TheOpenCloudEngine/uEngine-cloud-k8s/wiki/2.-%EC%A3%BC%EC%9A%94-%EC%82%AC%EC%9A%A9%EB%B0%A9%EB%B2%95-_-%EC%B6%94%EA%B0%80)
  - [리소스 삭제](https://github.com/TheOpenCloudEngine/uEngine-cloud-k8s/wiki/3.-%EC%A3%BC%EC%9A%94-%EC%82%AC%EC%9A%A9%EB%B0%A9%EB%B2%95-_-%EC%88%98%EC%A0%95)
  - [리소스 수정](https://github.com/TheOpenCloudEngine/uEngine-cloud-k8s/wiki/4.-%EC%A3%BC%EC%9A%94-%EC%82%AC%EC%9A%A9%EB%B0%A9%EB%B2%95-_-%EC%82%AD%EC%A0%9C)

- spring-boot, spring-cloud 를 사용하여 MSA 개발요소 sample
- kafka, redis 접근 및 활용 방법 sample
- zull 과 spring-cloud-gateway 의 routing 방법 비교 및 인증방법 sample
- Spring-security 를 사용하여 소셜 로그인 및 JWT 토큰방식 Oauth 인증
- kubernetes API 접근 방법과, 활용하여 각종 Object 모니터링, 로깅 예제
- VueJs 를 사용하여 UI 개발 방법
- SSE(Servcer sent Event) 를 사용하여 back-end 와 front-end 통신방법
- UI 에서 kubernetes Object 컨트롤 및 yaml 파일을 활용하여 Object 수정
- Helm 을 활용한 배포 sample
- GCB 를 활용하여 클라우드 배포 방법


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
  // 만약 dependency update가 되지 않는다면,
   # helm repo add incubator https://kubernetes-charts.storage.googleapis.com/
   # helm repo add chart https://kubernetes-charts.storage.googleapis.com/
   명령어로 repo를 추가해주어야함.
   
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
