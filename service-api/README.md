http http://localhost:8086/kube/v1/instance

http POST http://localhost:8086/kube/v1/service/namespaces/default/bbb

http POST http://localhost:8086/kube/v1/deployment/namespaces/default/tempapi-deploy
http DELETE http://localhost:8086/kube/v1/deployment/namespaces/default/tempapi-deploy

http POST http://localhost:8086/kube/v1/pod/namespaces/default/dowork-pod
http DELETE http://localhost:8086/kube/v1/pod/namespaces/default/dowork-pod