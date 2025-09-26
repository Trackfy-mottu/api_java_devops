# Deploy Azure - Trackfy Mottu

## Pré-requisitos
- Azure CLI instalado
- Java 17 configurado
- Gradle configurado

## 1. Autenticação no Azure
```bash
az login
```

## 2. Criação do Resource Group
```bash
az group create \
  --name "rg-trackfy-mottu" \
  --location "westus"
```

## 3. Criação do Servidor PostgreSQL
```bash
az postgres flexible-server create \
  --resource-group "rg-trackfy-mottu" \
  --name "servidor-sqlbd-rm557888" \
  --location "westus" \
  --admin-user "trackfyadmin" \
  --admin-password "Trackfy@2024#Secure" \
  --sku-name Standard_B1ms \
  --tier Burstable \
  --version 11 \
  --storage-size 32 \
  --public-access 0.0.0.0-255.255.255.255
```

## 4. Criação do Banco de Dados
```bash
az postgres flexible-server db create \
  --resource-group "rg-trackfy-mottu" \
  --server-name "servidor-sqlbd-rm557888" \
  --database-name "trackfy"
```

## 5. Configuração do Firewall
```bash
MY_IP=$(curl -4 -s ifconfig.me)
az postgres flexible-server firewall-rule create \
  --resource-group "rg-trackfy-mottu" \
  --name "servidor-sqlbd-rm557888" \
  --rule-name AllowMyIP \
  --start-ip-address $MY_IP \
  --end-ip-address $MY_IP
```

## 6. Criação do App Service Plan
```bash
az appservice plan create \
  --name plan-trackfy-mottu \
  --resource-group "rg-trackfy-mottu" \
  --location "westus" \
  --sku B1 \
  --is-linux
```

## 7. Verificação do Plano
```bash
az appservice plan show \
  --name plan-trackfy-mottu \
  --resource-group "rg-trackfy-mottu"
```

## 8. Criação da Web App
```bash
az webapp create \
  --resource-group "rg-trackfy-mottu" \
  --plan plan-trackfy-mottu \
  --name "app-trackfy-mottu-java" \
  --runtime "JAVA:17-java17" \
  --deployment-local-git
```

## 9. Configuração das Variáveis de Ambiente
```bash
az webapp config appsettings set \
  --resource-group "rg-trackfy-mottu" \
  --name "app-trackfy-mottu-java" \
  --settings \
    SPRING_DATASOURCE_URL="jdbc:postgresql://servidor-sqlbd-rm557888.postgres.database.azure.com:5432/trackfy?sslmode=require" \
    SPRING_DATASOURCE_USERNAME="trackfyadmin" \
    SPRING_DATASOURCE_PASSWORD="Trackfy@2024#Secure" \
    SPRING_JPA_HIBERNATE_DDL_AUTO="update" \
    SPRING_JPA_DATABASE_PLATFORM="org.hibernate.dialect.PostgreSQLDialect" \
    SERVER_PORT="8080" \
    JAVA_OPTS="-Xmx512m -Xms256m"
```

## 10. Configuração de Logs
```bash
az webapp log config \
  --resource-group "rg-trackfy-mottu" \
  --name "app-trackfy-mottu-java" \
  --application-logging filesystem \
  --level verbose \
  --web-server-logging filesystem
```

## 11. Obter Credenciais de Deploy
```bash
az webapp deployment list-publishing-credentials \
  --resource-group "rg-trackfy-mottu" \
  --name "app-trackfy-mottu-java" \
  --query "{username:publishingUserName, password:publishingPassword}" \
  --output json > credentials.json
```

## 12. Deploy Manual

⚠️ **IMPORTANTE**: Execute os comandos abaixo **dentro do repositório do projeto**

```bash
# Build da aplicação
./gradlew clean build -x test

# Deploy do JAR
az webapp deploy \
  --resource-group "rg-trackfy-mottu" \
  --name "app-trackfy-mottu-java" \
  --src-path build/libs/challenge_mottu_java-0.0.1-SNAPSHOT.jar \
  --type jar
```

## 13. Monitoramento

### Visualizar logs em tempo real
```bash
az webapp log tail \
  --resource-group "rg-trackfy-mottu" \
  --name "app-trackfy-mottu-java"
```

### Verificar status da aplicação
```bash
az webapp show \
  --resource-group "rg-trackfy-mottu" \
  --name "app-trackfy-mottu-java" \
  --query state
```

## 14. Limpeza de Recursos

⚠️ **CUIDADO**: Este comando deletará TODOS os recursos criados!

```bash
az group delete \
  --name "rg-trackfy-mottu" \
  --yes \
  --no-wait
```

---

## URLs Importantes
- **Aplicação**: `https://app-trackfy-mottu-java.azurewebsites.net/web/login`
- **Swagger**: `https://app-trackfy-mottu-java.azurewebsites.net/swagger-ui/index.html`
- **Banco PostgreSQL**: `servidor-sqlbd-rm557888.postgres.database.azure.com`

---

## Integrantes
 - Guilherme Alves - RM 555357
 - João Vitor Silva Nascimento - RM554694
 - Rafael Souza Bezerra - RM 557888
