#!/usr/bin/env bash
mvn -Dtest=false -DfailIfNoTests=false clean install $*

POD=$(kubectl get pod -l app=fabric8-jenkins -ojsonpath="{.items[*].metadata.name}")

echo "Found Jenkins pod ${POD}"

if [ -z  "$POD" ]; then 
    echo "No Jenkins pod found!"
    exit 1
fi

kubectl cp target/fabric8-pipelines.hpi $POD:/var/jenkins_home/plugins/fabric8-pipelines.jbi
kubectl exec $POD -- rm -rf /var/jenkins_home/plugins/fabric8-pipelines
kubectl delete pod $POD


