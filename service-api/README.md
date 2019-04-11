http http://localhost:8086/kube/v1/instance

http POST http://localhost:8086/kube/v1/service/namespaces/default/bbb

http POST http://localhost:8086/kube/v1/deployment/namespaces/default/tempapi-deploy
http DELETE http://localhost:8086/kube/v1/deployment/namespaces/default/tempapi-deploy

http POST http://localhost:8086/kube/v1/pod/namespaces/default/dowork-pod
http DELETE http://localhost:8086/kube/v1/pod/namespaces/default/dowork-pod

오는 메세지 타입 정의 statusTopic  
{"msg":"클러스터 DB 삭제","code":"DELETE_ALL"}  
{"msg":"클러스터 등록 실패","code":"CLUSTER_ADD_ERROR"}  
{"msg":"클러스터 등록 성공","code":"CLUSTER_ADD_SUCCESS"}  

{"msg":{"host":"https:\/\/api.k8s.bzdvops.com","namespace":"default","type":"DEPLOY","body":{"metadata":{"name":"aaa","labels":{"app":"aaa"}},"apiVersion":"apps\/v1","kind":"Deployment","spec":{"template":{"metadata":{"labels":{"app":"aaa"}},"spec":{"containers":[{"image":"sanaloveyou\/monitor-k8s","name":"aaa"}]}},"replicas":2,"selector":{"matchLabels":{"app":"aaa"}}}},"command":"CREATE"},"code":"PROGRESS"}
{"msg":{"host":"https:\/\/api.k8s.bzdvops.com","namespace":"default","type":"DEPLOY","body":{"metadata":{"name":"aaa","labels":{"app":"aaa"}},"apiVersion":"apps\/v1","kind":"Deployment","spec":{"template":{"metadata":{"labels":{"app":"aaa"}},"spec":{"containers":[{"image":"sanaloveyou\/monitor-k8s","name":"aaa"}]}},"replicas":2,"selector":{"matchLabels":{"app":"aaa"}}}},"command":"CREATE","code":"SUCCESS"}