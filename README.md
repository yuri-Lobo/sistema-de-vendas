Sistema de Vendas

Este é um sistema de vendas desenvolvido com Spring Boot e Angular.

Pré-requisitos

Antes de começar, você precisará ter instalado em sua máquina:

- Java JDK (versão 11 ou superior)
- Maven
- Node.js e npm
- Angular CLI

Instalação do Banco de Dados H2

Passo 1: Baixar o Driver do H2

1. Acesse o [site oficial do H2 Database](http://www.h2database.com/).
2. Na seção de downloads, escolha a versão mais recente do H2 Database.
3. Baixe o arquivo `.zip` ou `.tar.gz`.
4. Extraia o conteúdo do arquivo baixado em um diretório de sua escolha.

Passo 2: Configurar o Banco de Dados

O H2 é um banco de dados em memória por padrão, e você não precisa instalar nada além do driver.

### Passo 3: Conectar ao H2

1. Execute o H2 com o seguinte comando no terminal:
   ```bash
   java -jar h2*.jar

Acesse o console do H2 através do navegador: http://localhost:8082.
Use as seguintes configurações para se conectar:
JDBC URL: jdbc:h2:mem:sistemavendas
Username: admin
Password: 123456


Configuração do Projeto

Clonar o Repositório
Clone este repositório em sua máquina:

git clone https://github.com/yuri-Lobo/sistema-de-vendas.git

Compile e execute o projeto utilizando Maven:

mvn spring-boot:run

Executar a Aplicação Frontend
Navegue até o diretório do frontend 

cd sistema-vendas-front

Instale as dependências do Angular:

npm install

Execute a aplicação Angular:

ng serve


A aplicação estará disponível em http://localhost:4200.


