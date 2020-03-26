kind: Service
apiVersion: v1
metadata:
  name: ${svcName}
  namespace: ${namespace}
spec:
  type: NodePort 
  ports:
  - name: http
    port: 80
    targetPort: ${containerPort?c}
  selector:
    k8s-app: ${appName}