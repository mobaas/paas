kind: Ingress
apiVersion: extensions/v1beta1
metadata:
  name: ${ingName}
  namespace: ${namespace}
  annotations:
    nginx.ingress.kubernetes.io/proxy-body-size: 200m
spec:
  rules:
<#list domains as domn >  
  - host: ${domn}
    http:
      paths:
      - path: /
        backend:
          serviceName: ${svcName}
          servicePort: 80
</#list>    
<#if enableTls >
  tls:
  - hosts:
<#list domains as domn >  
    - ${domn}
</#list>
    secretName: ${tlsSecretName}  
</#if>      