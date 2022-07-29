# User-api-junit-jwt 
*Esse projeto é uma api de usuários com testes unitários, tratamento de exceções e spring security.*
#####  :warning: A aplicação ainda está em andamento.

## Tecnologias usadas

- String Framework
  - Spring Web
  - Spring Data Jpa
  - String security
  - Java 
  - Model mapper
  - Lombok
  - Junit5
  - Mockito
  - MySQL
  - Maven
  - h2
  
  
## Passos para configuração

**1. Clone a aplicação**

```bash
git clone https://github.com/LudmyllaArielly/user-api-junit-jwt.git
```
**2. Execute o aplicativo usando maven**

```bash
mvn user-api:run
```
**3. Pré-requisitos**
```bash
mvn --version
```
Veja a indicação da versão do maven instalada, bem como a versão do JDK, entre outras. Esses requisitos são obrigátorios, portanto é necessário definir corretamento as variáveis de ambiente JAVA_HOME, M2_HOME.
O aplicativo começará a ser executado em: <http://localhost:8080>

**4. Compila**

```bash
mvn compile
```
Este comando compila o projeto e deposita os resultados no diretório de destino.

**5. Executando a Rest Api**

```bash
java -jar target/api.jar
or
mvn user-api:run
```
Neste caminho
A Api foi geradada pelo pacote mvn package -P e esta sendo executada na porta 8080.

Exemplo de endereço: http://localhost:8080/user-api


