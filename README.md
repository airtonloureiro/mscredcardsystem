# mscredcardsystem

A ideia do microserviço era criar um prototipo de sistema de cartão de crédito, lembrando que é apenas um "protótipo". Colocando em prática arquitetura DDD

O sistema precisa que sejam rodado dois serviços off project (ambos adicionado na camada de INFRA) rabbitMQ & Keycloak dockerizado

Será necessário instalar na máquina, ou usar iniciar aplicação com docker. 

# RabbitMQ 3.11

Comando docker para iniciar serviço

 `docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.11-management`
 
 Depois de iniciado, acesse o http://localhost:15672/
 
 username: guest
 password: guest
 
 
 Crie uma queue, nomeando-a de `emissoes-cartoes`
 
 ![image](https://user-images.githubusercontent.com/69084147/202445797-862b3a5c-e826-4c1f-99e2-a14a6b2b1b69.png)

 
 Salve.. Pronto! Tudo certo com o RabbitMQ. 
 
 
 # Keycloak
 
 Comando docker para iniciar serviço 
 
 `docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:20.0.1 start-dev`
 
 Depois de iniciado, acesso http://localhost:8080
 
 Criar um novo realm, pode colocar qualquer coisa. Exemplo "xablaurealm"  ou coisa do tipo. 
 
 Após realizar a criação do realm, precisa pegar os dados de OpenID Endpoint Configuration. Para adicionar "issuer" na properties.yml > GatewayService
 
 ![image](https://user-images.githubusercontent.com/69084147/202483740-0e6769d3-81d9-4a35-a131-72ab14d7b8ee.png)

![image](https://user-images.githubusercontent.com/69084147/202484581-49750e8c-6e13-420b-9f11-991db6edb210.png)

![image](https://user-images.githubusercontent.com/69084147/202485022-c7cee55a-3221-4915-861c-3ff323537af0.png)


Quando finalizado a configuração dos microservices, podemos estar testando os açoẽs de criar clientes, obter cliente, criar cartão, solicitar, emitir.


## Cliente
Salvar Cliente (POST) - input Json {cpf:string, nome:string, idade:number}

	"cpf" : "00000000000",
	"nome" : "Xablau Fulano de Tal",
	"idade": "99"
	
Obter Usuário (GET) - queryParam {cpf:string}

`http://localhost:{portGateway}/cliente?cpf=00000000000`


## Cartão
Salvar Cartão (POST) - input Json {nome:string, bandeira: string {enum: MASTERCARD, VISA}, renda: number, limite: number} 

	"nome" : "XABLAU CARD", 
	"bandeira" : "MASTERCARD",
	"renda" : 3000, 
	"limite" : 15000
 
 
 Obter Cartão Renda Até (GET) - queryParam {cpf:string}

`http://localhost:{portGateway}/cartoes?cpf=00000000000`


## Avaliador Crédito
Situação Cliente (GET) queryParam (cpf:string)

`http://localhost:{portGateway}/avaliacoes-credito/situacao-cliente?cpf=00000000000`

Avaliação Cliente (POST) input Json {cpf:string, renda: number}

	"cpf" : "01791624200",
	"renda" : 15000


Solicitando Emissão Cartão (POST) input Json {idCartao: number, cpf: string, endereco: string, limiteLiberado: number}

	"idCartao" : 1,
	"cpf" : "00000000000",
	"endereco": "Street Zé Carias, 9, Morro do Alemão - RJ", 
	"limiteLiberado" : 10000



