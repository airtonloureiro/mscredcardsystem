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
 
 Após realizar a criação do realm, precisa 
