# 1. feature: Authentication

## Descrição
Implementar a funcionalidade de `{Authentication}` e `{Role}` com operações RESTful completas, utilizando Spring Boot. A entidade deve ser protegida por autenticação e autorização baseada em roles.

## Especificação da Entidade `User`

- **Nome da entidade**: `User`
- **Descrição**: Representa um usuário do sistema com informações pessoais, credenciais e preferências
- **Campos**:

| Campo         | Tipo            | Obrigatório | Descrição                                       | Restrições                                   |
|---------------|-----------------|-------------|-------------------------------------------------|----------------------------------------------|
| id            | UUID            | sim         | Identificador único                             | Gerado automaticamente                       |
| email         | VARCHAR(255)    | sim         | E-mail do usuário                               | Formato válido, único                        |
| password      | VARCHAR(255)    | sim         | Senha criptografada                             | Mínimo 8 caracteres                          |
| first_name    | VARCHAR(100)    | sim         | Nome do usuário                                 |                                              |
| last_name     | VARCHAR(100)    | não         | Sobrenome do usuário                            |                                              |
| username      | VARCHAR(50)     | sim         | Nome de usuário único                           | Sem espaços ou caracteres especiais          |
| phone         | VARCHAR(20)     | não         | Telefone                                        | Formato válido                               |
| birth_date    | DATE            | não         | Data de nascimento                              | Data válida no passado                       |
| gender        | VARCHAR(20)     | não         | Gênero                                          |                                              |
| address       | VARCHAR(255)    | não         | Endereço físico                                 |                                              |
| city          | VARCHAR(100)    | não         | Cidade                                          |                                              |
| state         | VARCHAR(100)    | não         | Estado/Província                                |                                              |
| country       | VARCHAR(100)    | não         | País                                            |                                              |
| zip_code      | VARCHAR(20)     | não         | Código postal                                   | Formato válido                               |
| avatar        | VARCHAR(255)    | não         | URL da imagem de perfil                         | URL válida                                   |
| bio           | TEXT            | não         | Biografia do usuário                            | Máximo 1000 caracteres                       |
| role_id       | UUID            | sim         | Função principal do usuário                     | Referência a roles(id)                       |
| time_zone     | VARCHAR(50)     | sim         | Fuso horário                                    | Valor válido                                 |
| created_at    | TIMESTAMP       | sim         | Data de criação                                 | Valor padrão: CURRENT_TIMESTAMP              |
| updated_at    | TIMESTAMP       | sim         | Data de atualização                             | Atualizado automaticamente                   |
| deleted_at    | TIMESTAMP       | não         | Data de desativação                             |                                              |
| is_active     | BOOLEAN         | sim         | Status de ativação                              | Padrão: true                                |
| is_verified   | BOOLEAN         | sim         | Verificação de e-mail                           | Padrão: false                               |
| is_blocked    | BOOLEAN         | sim         | Status de bloqueio                              | Padrão: false                               |
| isDeleted     | BOOLEAN         | sim         | Exclusão lógica                                 | Padrão: false                               |

- **Regras de validação**:
  - Campos obrigatórios:
        - email, 
        - password, 
        - first_name, 
  - Valores padrão:
        - is_active: true
        - is_verified: false
        - is_blocked: false
        - created_at: now()
  - Campos não editáveis via API: 
        - id, 
        - created_at
  - Campos opcionais: 
    - birth_date, 
    - gender, 
    - address, 
    - bio, 
    - deleted_at
    - last_name
    - username
    - phone
    - city
    - state
    - country
    - zip_code
    - avatar
    - role_id
    - time_zone
  - Outras regras:
    - E-mail deve ser verificado antes de permitir login
    - Senha deve ser criptografada com algoritmo seguro (bcrypt)
    - Username deve ser único e sem espaços, deve criar um nome simbolico apartir do email e nome
    - avatar tem url como padrão ´https://www.azjob.com.br/image/user-default.png´

- **Relacionamento**:
  - `@ManyToOne Role role` (função principal do usuário)
  - `@ManyToMany Group groups` (grupos aos quais o usuário pertence)
  - `@OneToMany GroupMember memberships` (detalhes de associação a grupos)

## Especificação da Entidade `PinCode`
- **Nome da entidade**: `PinCode`
- **Descrição**: Representa um código de verificação temporário enviado por e-mail para validação de contas de usuário
- **Campos**:

| Campo         | Tipo            | Obrigatório  | Descrição                                 | Restrições                                   |
|---------------|-----------------|--------------|-------------------------------------------|----------------------------------------------|
| id            | UUID            | sim          | Identificador único                       | Gerado automaticamente                       |
| user_id       | UUID            | sim          | Usuário associado                         | Referência a users(id)                       |
| code          | VARCHAR(6)      | sim          | Código numérico de 4 dígitos              | Único, apenas números                        |
| email         | VARCHAR(255)    | sim          | E-mail de destino                         | Formato válido                               |
| expires_at    | TIMESTAMP       | sim          | Data de expiração                         | Deve ser futuro (padrão: now + 15 minutos)   |
| is_used       | BOOLEAN         | sim          | Se o código foi utilizado                 | Padrão: false                                |
| created_at    | TIMESTAMP       | sim          | Data de criação                           | Valor padrão: CURRENT_TIMESTAMP              |

### Regras de validação

- **Campos obrigatórios**:
    - user_id
    - code
    - email
    - expires_at

- **Valores padrão**:
    - is_used: false
    - created_at: now()
    - expires_at: now() + 15 minutos

- **Campos não editáveis via API**:
    - id
    - created_at

- **Outras regras**:
  - O código deve conter exatamente 4 dígitos numéricos
  - Cada usuário pode ter apenas 1 PIN ativo por vez
  - O PIN expirado não pode ser utilizado
  - O PIN utilizado não pode ser reutilizado
  - O mesmo código não pode ser atribuído a múltiplos usuários

### Relacionamentos
- `@ManyToOne User user` (PIN pertence a um usuário)

---

## Especificação da Entidade `UserGroup`

- **Nome da entidade**: `UserGroup`
- **Descrição**: Representa a associação entre um usuário e um grupo dentro de uma empresa. Quais grupos o usuario pertence.

- **Campos**:

| Campo    | Tipo | Obrigatório | Descrição                       | Restrições ou Observações     |
|----------|------|-------------|---------------------------------|-------------------------------|
| user_id  | UUID | sim         | Referência ao usuário           | FK para `users(id)`           |
| group_id | UUID | sim         | Referência ao grupo             | FK para `group(id)`           |

- **Regras de validação**:
  - Campos obrigatórios: `user_id`, `group_id`
  - Chave primária composta: (`user_id`, `group_id`)

- **Relacionamentos**:
  - `@ManyToOne users user` – Usuário associado ao grupo
  - `@ManyToOne group group` – Grupo ao qual o usuário pertence

## Especificação da Entidade `Permission`

- **Nome da entidade**: `Permission`
- **Descrição**: Define se um grupo tem permissão para executar uma ação específica dentro de uma empresa, baseado no papel do usuário.

- **Campos**:

| Campo         | Tipo   | Obrigatório | Descrição                            | Restrições ou Observações                   |
|---------------|--------|-------------|--------------------------------------|---------------------------------------------|
| id            | UUID   | sim         | Identificador da permissão           | Gerado automaticamente                      |
| id_role       | UUID   | sim         | Papel relacionado                    | FK para `role(id)`                          |
| id_action     | UUID   | sim         | Ação permitida ou não                | FK para `action(id)`                        |
| allowed     | BOOLEAN| sim         | Define se a ação é permitida         | Valor padrão: FALSE                         |
| id_enterprise | UUID   | sim         | Empresa relacionada à permissão      | FK para `enterprise(id)`                    |
| id_group      | UUID   | não         | Grupo ao qual se aplica a permissão  | FK para `group(id)`                         |

- **Regras de validação**:
  - Campos obrigatórios: `id_role`, `id_action`, `permitido`, `id_enterprise`
  - Campos não editáveis via API: `id`

- **Relacionamentos**:
  - `@ManyToOne role role` – Permissão está ligada a um papel
  - `@ManyToOne action action` – Permissão está ligada a uma ação
  - `@ManyToOne group group` – (opcional) permissão pode ser limitada a um grupo

## Especificação da Entidade `Group`

- **Nome da entidade**: `Group`
- **Descrição**: Representa um grupo organizacional que agrega usuários com permissões em comum dentro de uma empresa.

- **Campos**:

| Campo         | Tipo          | Obrigatório | Descrição                     | Restrições ou Observações          |
|---------------|---------------|-------------|-------------------------------|------------------------------------|
| id            | UUID          | sim         | Identificador do grupo        | Gerado automaticamente             |
| name          | VARCHAR(100)  | sim         | Nome do grupo                 | Deve ser único por empresa         |
| description   | TEXT          | não         | Descrição do grupo            |                                    |
| id_enterprise | UUID          | sim         | Empresa a que o grupo pertence| FK para `enterprise(id)`          |

- **Regras de validação**:
  - Campos obrigatórios: `name`, `id_enterprise`
  - Campos não editáveis via API: `id`
  - Outras regras:
    - Nome deve ser único dentro da mesma empresa

- **Relacionamentos**:
  - `@ManyToOne enterprise enterprise` – Grupo pertence a uma empresa
  - `@OneToMany permission permissions` – Grupo pode ter várias permissões
  - `@OneToMany user_group users` – Vários usuários podem pertencer ao grupo

## Especificação da Entidade `Role`

- **Nome da entidade**: `Role`
- **Descrição**: Representa o papel ou função do usuário dentro de uma organização (ex: gestor, colaborador, convidado).

- **Campos**:

| Campo      | Tipo          | Obrigatório | Descrição                     | Restrições ou Observações    |
|------------|---------------|-------------|-------------------------------|------------------------------|
| id         | UUID          | sim         | Identificador do papel        | Gerado automaticamente       |
| name       | VARCHAR(100)  | sim         | Nome do papel                 | Deve ser único               |
| description| TEXT          | não         | Descrição da função/papel     |                              |

- **Regras de validação**:
  - Campos obrigatórios: `name`
  - Campos não editáveis via API: `id`
  - Outras regras:
    - Nome deve ser único

- **Relacionamentos**:
  - `@OneToMany permission permissions` – Um papel pode ter várias permissões

## Especificação da Entidade `Action`

- **Nome da entidade**: `Action`
- **Descrição**: Define ações específicas que podem ser permitidas ou negadas dentro da estrutura de permissões (ex: criar vaga, visualizar vaga).

- **Campos**:

| Campo | Tipo         | Obrigatório | Descrição              | Restrições ou Observações        |
|-------|--------------|-------------|------------------------|----------------------------------|
| id    | UUID         | sim         | Identificador da ação  | Gerado automaticamente           |
| name  | VARCHAR(255) | sim         | Nome da ação           | Deve ser único                   |

- **Regras de validação**:
  - Campos obrigatórios: `name`
  - Campos não editáveis via API: `id`
  - Outras regras:
    - Nome deve ser único

- **Relacionamentos**:
  - `@OneToMany permission permissions` – A ação pode ser associada a várias permissões

## Especificação da Entidade `Recruiter`

- **Nome da entidade**: `Recruiter`
- **Descrição**: Representa um recrutador vinculado a uma empresa, com permissões específicas no processo de recrutamento.

- **Campos**:

| Campo           | Tipo         | Obrigatório | Descrição                         | Restrições ou Observações                |
|-----------------|--------------|-------------|-----------------------------------|------------------------------------------|
| id              | UUID         | sim         | Identificador único               | Gerado automaticamente                   |
| name_recruiter  | VARCHAR(255) | sim         | Nome do recrutador                |                                          |
| id_user         | UUID         | sim         | Referência ao usuário             | FK para `users(id)`                      |

- **Regras de validação**:
  - Campos obrigatórios: `name_recruiter`, `id_user`
  - Campos não editáveis via API: `id`

- **Relacionamentos**:
  - `@ManyToOne users user` – Referência ao usuário

## Especificação da Entidade `Enterprise`

- **Nome da entidade**: `Enterprise`
- **Descrição**: Representa uma empresa que possui uma conta na plataforma, podendo cadastrar recrutadores, grupos e gerenciar permissões.

- **Campos**:

| Campo           | Tipo         | Obrigatório | Descrição                         | Restrições ou Observações                |
|-----------------|--------------|-------------|-----------------------------------|------------------------------------------|
| id              | UUID         | sim         | Identificador único da empresa    | Gerado automaticamente                   |
| name_enterprise | VARCHAR(255) | sim         | Nome da empresa                   | Deve ser único                           |
| id_user         | UUID         | sim         | Usuário responsável pela empresa  | FK para `users(id)`                      |

- **Regras de validação**:
  - Campos obrigatórios: `name_enterprise`, `id_user`
  - Campos não editáveis via API: `id`
  - Outras regras:
    - O nome da empresa deve ser único

- **Relacionamentos**:
  - `@ManyToOne users owner` – Usuário dono da empresa
  - `@OneToMany group groups` – Empresa pode conter vários grupos

## API Endpoints

### PIN Code

| Método | Endpoint                                | Descrição                          | Autenticado | Body Request (exemplo)                    |
|--------|-----------------------------------------|------------------------------------|-------------|-------------------------------------------|
| POST   | `/api/v1/auth/send-verification-code`   | Envia novo PIN por e-mail          | não          | `{email: "user@ex.com"}`                 |
| POST   | `/api/v1/auth/verify-account`           | Valida conta com PIN               | não          | `{email: "user@ex.com", code: "123456"}` |
| POST   | `/api/v1/auth/resend-verification-code` | Reenvia PIN (invalida o anterior)  | não          | `{email: "user@ex.com"}`                 |

### Autenticação

| Método | Endpoint                                            | Descrição                          | Autenticado  | Body Request (exemplo)                                 |
|--------|-----------------------------------------------------|------------------------------------|--------------|--------------------------------------------------------|
| GET    | `/api/v1/auth/user`                                 | Retorna usuário logado             | sim          | -                                                      |
| POST   | `/api/v1/auth/sign-in-with-email-and-password`      | Autenticar usuário                 | não          | `{email: "user@ex.com", password: "123"}`              |
| POST   | `/api/v1/auth/sign-up-with-email-and-password`      | Cadastrar usuário                  | não          | `{email: "new@ex.com", password: "123", name: "John"}` |
| PUT    | `/api/v1/auth/user`                                 | Atualiza usuário logado            | sim          | `{name: "New Name", phone: "99999999"}`                |
| POST   | `/api/v1/auth/refresh-token`                        | Renovar token de acesso            | sim          | `{refreshToken: "token"}`                              |
| POST   | `/api/v1/auth/forgot-password`                      | Solicitar reset de senha           | não          | `{email: "user@ex.com"}`                               |
| POST   | `/api/v1/auth/reset-password`                       | Resetar senha                      | não          | `{token: "xyz", newPassword: "new123"}`                |

### user_group
| Método | Endpoint             | Descrição                                 | Autenticado | Body Request (exemplo)          |
| ------ | -------------------- | ----------------------------------------- | ----------- | ------------------------------- |
| GET    | `/api/v1/auth/user-group` | Lista os grupos que o usuário pertence    | sim         | —                               |
| POST   | `/api/v1/auth/user-group` | Adiciona o usuário autenticado a um grupo | sim         | `{ group_id: "uuid-do-grupo" }` |
| DELETE | `/api/v1/auth/user-group` | Remove o usuário autenticado de um grupo  | sim         | `{ group_id: "uuid-do-grupo" }` |

### permission
| Método | Endpoint                  | Descrição                                              | Autenticado | Body Request (exemplo)                                  |
| ------ | ------------------------- | ------------------------------------------------------ | ----------- | ------------------------------------------------------- |
| GET    | `/api/v1/auth/permissions`     | Lista permissões do grupo do usuário autenticado       | sim         | —                                                       |
| POST   | `/api/v1/auth/permissions`     | Cria nova permissão dentro do grupo atual (admin only) | sim         | `{ id_role: "...", id_action: "...", permitido: true }` |
| PUT    | `/api/v1/auth/permissions/:id` | Atualiza uma permissão específica                      | sim         | `{ permitido: false }`                                  |
| DELETE | `/api/v1/auth/permissions/:id` | Remove uma permissão específica                        | sim         | —                                                       |

### group
| Método | Endpoint             | Descrição                                    | Autenticado | Body Request (exemplo)                       |
| ------ | -------------------- | -------------------------------------------- | ----------- | -------------------------------------------- |
| GET    | `/api/v1/auth/groups`     | Lista grupos da empresa vinculada ao usuário | sim         | —                                            |
| POST   | `/api/v1/auth/groups`     | Cria novo grupo para a empresa do usuário    | sim         | `{ name: "RH", description: "Grupo de RH" }` |
| PUT    | `/api/v1/auth/groups/:id` | Atualiza informações do grupo                | sim         | `{ name: "RH Básico" }`                      |
| DELETE | `/api/v1/auth/groups/:id` | Remove um grupo da empresa                   | sim         | —                                            |

### role
| Método | Endpoint            | Descrição                          | Autenticado | Body Request (exemplo) |
| ------ | ------------------- | ---------------------------------- | ----------- | ---------------------- |
| GET    | `/api/v1/auth/roles`     | Lista todas as funções disponíveis | sim         | —                      |
| GET    | `/api/v1/auth/roles/:id` | Retorna detalhes de uma função     | sim         | —                      |

### action
| Método | Endpoint              | Descrição                       | Autenticado | Body Request (exemplo) |
| ------ | --------------------- | ------------------------------- | ----------- | ---------------------- |
| GET    | `/api/v1/auth/actions`     | Lista todas as ações possíveis  | sim         | —                      |
| GET    | `/api/v1/auth/actions/:id` | Detalhes de uma ação específica | sim         | —                      |

### recruiter
| Método | Endpoint            | Descrição                                   | Autenticado | Body Request (exemplo)            |
| ------ | ------------------- | ------------------------------------------- | ----------- | --------------------------------- |
| GET    | `/api/v1/auth/recruiter` | Visualiza dados do recrutador autenticado   | sim         | —                                 |
| POST   | `/api/v1/auth/recruiter` | Cria recrutador (caso não exista)           | sim         | `{ name_recruiter: "Ana" }`       |
| PUT    | `/api/v1/auth/recruiter` | Atualiza os dados do recrutador autenticado | sim         | `{ name_recruiter: "Ana Maria" }` |

### enterprise
| Método | Endpoint             | Descrição                             | Autenticado | Body Request (exemplo)                     |
| ------ | -------------------- | ------------------------------------- | ----------- | ------------------------------------------ |
| GET    | `/api/v1/auth/enterprise` | Retorna os dados da empresa vinculada | sim         | —                                          |
| POST   | `/api/v1/auth/enterprise` | Cria empresa vinculada ao usuário     | sim         | `{ name_enterprise: "Nubank" }`            |
| PUT    | `/api/v1/auth/enterprise` | Atualiza dados da empresa             | sim         | `{ name_enterprise: "Nubank Tecnologia" }` |


### Observações gerais:
- O group_id (e outros dados sensíveis como enterprise_id) não são enviados no body ou query params — devem ser extraídos do token JWT no backend.
- Todas as rotas são autenticadas.
- O usuário só pode acessar dados vinculados a ele.




## Autenticação, Autorização, Refresh-token e Token deve ser feito Firebase Admin SDK 
 - Adicionar o Firebase Admin SDK no Projeto
 - A Chave privada mantenha-o em um local seguro
 - Segue a chave privada:
```json
  {
    "type":"",
    "project_id":"",
    "private_key_id":"",
    "private_key":"",
    "client_email":"",
    "client_id":"",
    "auth_uri":"",
    "token_uri":"",
    "auth_provider_x509_cert_url":"",
    "client_x509_cert_url":"",
    "universe_domain":"" 
  }
```

## Regras gerais

 - Após a criação da conta, deve enviar um e-mail de verificação com PIN code 
 ```text
    Assunto: Verifique sua conta - Código: {{pin_code}}

    Olá {{user_name}},

    Obrigado por criar uma conta na {{app_name}}! Para completar seu cadastro, por favor utilize o seguinte código de verificação:

    Código: {{pin_code}} 
    (Expira em 15 minutos)

    Ou clique no link para verificação automática:
    {{verification_link}}

    Se você não solicitou este código, por favor ignore este e-mail.

    Atenciosamente,
    Equipe {{app_name}}

    ---
    © {{current_year}} {{app_name}}. Todos os direitos reservados.
    Política de Privacidade: {{privacy_policy}}
    Central de Ajuda: {{help_center}}
 ```
 - Se o usuário tentar fazer login com email e senha corretos e estiver com email verificado, deve retornar:

```json
{
  "success": true,
  "message": "Login realizado com sucesso",
  "statusCode": 200,
  "data": {
    "accessToken": "<token>",
    "refreshToken": {
      "token": "<refreshToken>",
      "timestamp": "2025-07-17T23:00:00.000Z",
      "expiresIn": "2025-07-17T23:30:00.000Z",
      "userId": 1
    }
  },
  "timestamp": "2025-07-17T23:00:00.000Z"
}
```

 -  Se o usuário tentar fazer login com email correto mas **não verificado**, deve retornar:

```json
{
  "success": false,
  "message": "E-mail não verificado",
  "statusCode": 400,
  "errors": {
    "code": "auth/wrong-email-not-verified",
    "message": "E-mail não verificado"
  },
  "timestamp": "2025-07-17T23:00:00.000Z"
}
```

 -  Se o usuário tentar fazer login com email correto mas **senha incorreta**, deve retornar:

```json
{
  "success": false,
  "message": "Erro de validação",
  "statusCode": 400,
  "errors": {
    "code": "auth/wrong-password",
    "message": "Senha não confere"
  },
  "timestamp": "2025-07-17T23:00:00.000Z"
}
```

 -  Se o usuário tentar fazer login com **email inexistente**, deve retornar:

```json
{
  "success": false,
  "message": "Erro de validação",
  "statusCode": 400,
  "errors": {
    "code": "auth/wrong-email",
    "message": "E-mail não encontrado"
  },
  "timestamp": "2025-07-17T23:00:00.000Z"
}
```

 -  Se o usuário tentar criar uma conta com um email existente, deve retornar:

```json
{
  "success": false,
  "message": "Email já existe",
  "statusCode": 404,
  "errors": {
    "code": "auth/wrong-email-exists",
    "message": "E-mail já existe"
  },
  "timestamp": "2025-07-17T23:00:00.000Z"
}
```

 -  Se o usuário tentar recuperar senha com email existente, deve retornar:

```json
{
  "success": true,
  "message": "E-mail existe",
  "statusCode": 200,
  "data": {
    "token": "3f5e2fc4-ccbb-4f85-b73a-b112f3f447ae",
    "expiresIn": "2025-07-17T23:01:00.000Z"
  },
  "timestamp": "2025-07-17T23:00:00.000Z"
}
```

 -  Se tentar recuperar senha com email inexistente:

```json
{
  "success": false,
  "message": "Email não existe",
  "statusCode": 404,
  "errors": {
    "code": "auth/wrong-email",
    "message": "E-mail não encontrado"
  },
  "timestamp": "2025-07-17T23:00:00.000Z"
}
```

 -  Se o PIN estiver expirado na validação de redefinição de senha:

```json
{
  "success": false,
  "message": "PIN expirado",
  "statusCode": 400,
  "errors": {
    "code": "auth/wrong-pin-expired",
    "message": "PIN expirado"
  },
  "timestamp": "2025-07-17T23:00:00.000Z"
}
```

 -  Se o PIN for válido:

```json
{
  "success": true,
  "message": "Código válido",
  "statusCode": 200,
  "data": {
    "token": "3f5e2fc4-ccbb-4f85-b73a-b112f3f447ae",
    "expiresIn": "2025-07-17T23:01:00.000Z"
  },
  "timestamp": "2025-07-17T23:00:00.000Z"
}
```

 -  Se o PIN for inválido:

```json
{
  "success": false,
  "message": "Código inválido",
  "statusCode": 404,
  "errors": {
    "code": "auth/wrong-pin",
    "message": "O código expirou, Volte para tentar um novo código."
  },
  "timestamp": "2025-07-17T23:00:00.000Z"
}
```

 -  Se o PIN enviado para confirmação de conta for inválido:

```json
{
  "success": false,
  "message": "Código inválido",
  "statusCode": 404,
  "errors": {
    "code": "auth/wrong-pin-not-found",
    "message": "PIN Não confere com o enviado para o email, verifique o email e tente novamente."
  },
  "timestamp": "2025-07-17T23:00:00.000Z"
}
```

 -  Se o PIN enviado para confirmação de conta for válido:

```json
{
  "success": true,
  "message": "Código confirmado com sucesso",
  "statusCode": 200,
  "data": {
    "accessToken": "<token>",
    "refreshToken": {
      "token": "<refreshToken>",
      "timestamp": "2025-07-17T23:00:00.000Z",
      "expiresIn": "2025-07-17T23:05:00.000Z",
      "userId": 1
    }
  },
  "timestamp": "2025-07-17T23:00:00.000Z"
}
```

 -  Se o token de redefinição de senha estiver expirado:

```json
{
  "success": false,
  "message": "PIN expirado",
  "statusCode": 400,
  "errors": {
    "code": "auth/wrong-pin-expired",
    "message": "PIN expirado"
  },
  "timestamp": "2025-07-17T23:00:00.000Z"
}
```

 -  Se o token for válido e a senha for atualizada:

```json
{
  "success": true,
  "message": "password updated",
  "statusCode": 200,
  "data": true,
  "timestamp": "2025-07-17T23:00:00.000Z"
}
```

 -  Se o usuário for encontrado por ID:

```json
{
  "success": true,
  "message": "user found",
  "statusCode": 200,
  "data": {
    "id": 1,
    "email": "...",
    ... outros dados do usuário ...
  },
  "timestamp": "2025-07-17T23:00:00.000Z"
}
```

 
## Testes Recomendados

- Testes unitários para `service` (JUnit + Mockito)

- Testes de integração para `controller` (MockMvc ou WebTestClient)

- Cobertura mínima: 80%

## Documentação

- Todos os endpoints devem aparecer no Swagger

- DTOs anotados com `@Schema` (`springdoc-openapi`)

## Extras Opcionais

- Paginação com `Pageable`

- Ordenação por `createdAt DESC`

- Filtro por `done = true/false` via query param

## Output Esperado

- Código fonte completo e funcional

- Atualização automática do Swagger

- Testes criados e passando

- Nenhum dado exposto de outro usuário

## Segurança

- Cada usuário pode acessar e gerenciar apenas seus próprios registros

## Observações

- Gere os arquivos com pacotes corretos

- Inclua validações com `javax.validation`

- Use `@Transactional` nas operações críticas

- Proceda com a instalação da dependência, se aplicável

- Gere o código em inglês

 

 

 