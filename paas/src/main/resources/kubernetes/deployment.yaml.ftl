kind: Deployment
apiVersion: apps/v1
metadata:
  name: ${deployName}
  namespace: ${namespace}
spec:
  minReadySeconds: 10
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 1
  replicas: ${instanceNum}
  selector:
    matchLabels:
      k8s-app: ${appName}
  template:
    metadata:
      labels:
        k8s-app: ${appName}
        version: ${appVersion}
    spec:
      containers:
      - name: ${appName}
        image: ${containerImage}
        imagePullPolicy: IfNotPresent
        command:
        args: 
        ports:
        - name: http
          containerPort: ${containerPort?c}
        env:
<#list envmap?keys as key>        
        - name: ${key}
          value: "${envmap[key]}"
</#list>
        resources:
          limits: 
        volumeMounts:
<#list volumelist as volume>        
        - name: ${volume.name}
          mountPath: "${volume.mountPath}"
</#list> 
      volumes:   
<#list volumelist as volume>          
      - name: ${volume.name}
        hostPath:
          path: "${volume.hostPath}"
</#list>