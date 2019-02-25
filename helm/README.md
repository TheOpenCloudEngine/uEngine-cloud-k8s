*** back-end 설치 ***

1. 설치할 helm folder로 이동
 - cd uengine-kube

2. values.xml 값 수정
 - kubehost: k8s가 설치되어 있는 도메인 주소
 - kubetoken: k8s 접근하기 위한 토큰 정보

3. helm install
 - helm install --name uengine-kube  --set mysql.mysqlRootPassword=test,mysql.mysqlUser=test,mysql.mysqlPassword=test,mysql.mysqlDatabase=uengine .
  -- DB 유저 및 tablespace 명을 바꾸면 실행되지 않는다.

4. nginx-ingress 의 External-IP 취득
 - kubectl get svc 
  -- EXTERNAL-IP 값 복사 저장해 둔다.



*** front-end 설치 ***
1. 설치할 helm folder로 이동
 - cd uengine-kube-ui

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
