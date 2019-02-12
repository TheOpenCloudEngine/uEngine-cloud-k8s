http http://localhost:8086/kube/v1/instance

http POST http://localhost:8086/kube/v1/namespaces/default/service/bbb

http POST http://localhost:8086/kube/v1/namespaces/default/deployments/tempapi-deploy
http DELETE http://localhost:8086/kube/v1/namespaces/default/deployments/tempapi-deploy

http POST http://localhost:8086/kube/v1/namespaces/default/pods/dowork-pod
http DELETE http://localhost:8086/kube/v1/namespaces/default/pods/dowork-pod