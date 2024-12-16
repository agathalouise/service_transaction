# service-transaction - caju

backend monolito que representa um sistema de autorizaçao de pagamentos para uma carteira de beneficios.


---
### Rodando a aplicação SEM o  docker

abra o terminal na pasta do projeto e execute os comandos

```
mvn clean
mvn install
mvn spring-boot:run
```
A aplicação passará a responder na porta 8080.
Você precisará rodar também um banco de dados MySql na porta 3306.
As tabelas serão criadas e preenchidas quando o sistema iniciar,
você pode conferir os inserts sqls em ```/resources/import.sql```

---
### Rodando a aplicação COM docker

abra o terminal na pasta do projeto e execute os comandos

```
mvn clean
mvn install
docker compose build
docker compose up -d
```

A aplicação passará a responder na porta 8080. 
As tabelas serão criadas e preenchidas quando o sistema iniciar

### Curl

```
curl -X 'POST' \
  'http://localhost:8080/api/transactions' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "accountId": 1,
  "amount": 10,
  "mcc": "5411",
  "merchant": "Mercado do ze",
  "pinOrCvv": "1111"
}'
```

### Funções:
- Processar uma transação de um cartão de beneficios, deduzindo o saldo das respectivas categorias (Alimentação, refeição, cultura, transporte ou saldo livre - cash)

As possíveis respostas são:
- `{ "code": "51" }` se a transação é **rejeitada**, porque não tem saldo suficiente
- `{ "code": "00" }` se a transação é **aprovada**
- `{ "code": "07" }` se acontecer qualquer outro problema que impeça a transação de ser processada


### Stack
- Java 17
- Spring 3.4
- MySql


### Descrição dos arquivos de configuração do projeto

| Recurso                        | Descrição                                                                                            |
|--------------------------------|------------------------------------------------------------------------------------------------------|
| ./docker-compose               | Arquivo utilizado para definição do processo de construção da imagem do container para execução local |
| ./src/main/resource/import.sql | Arquivo com sqls para fazer inserts no banco de dados                                                |
| ./pom.xml                      | Arquivo de configuração da gestão de dependência deste projeto                                       |


### Endpoint de Monitoração

| Url                    | Descrição                                         |
|------------------------|---------------------------------------------------|
| /swagger-ui/index.html | Endpoint para verificar a documentação do projeto |