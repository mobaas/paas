kind: Service
apiVersion: v1
metadata:
  name: ${svcName}
  namespace: ${namespace}
  annotations:
    prometheus.io/scrape: "true"
    prometheus.io/port: "${metricsPort?c}"
    prometheus.io/path: "/prometheus"
spec:
  type: NodePort 
  ports:
  - name: http
    port: 80
    targetPort: ${containerPort?c}
  selector:
    k8s-app: ${appName}