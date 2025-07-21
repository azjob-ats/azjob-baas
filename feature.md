# 1. feature: Authentication

## Descrição

Implementar a funcionalidade de `{Authentication}` e `{Role}` com operações RESTful completas, utilizando Spring Boot. A entidade deve ser protegida por autenticação e autorização baseada em roles.

## Especificação da Entidade `Group`

- **Nome da entidade**: `Group`
- **Descrição**: Representa um grupo organizacional que agrega usuários com funções e permissões comuns (ex: RH, TI, Financeiro)

- **Campos**:

| Campo           | Tipo          | Obrigatório | Descrição                | Restrições                               |
|-----------------|---------------|-------------|---------------------------|------------------------------------------|
| id              | UUID          | sim         | Identificador único       | Gerado automaticamente                   |
| name            | VARCHAR(100)  | sim         | Nome do grupo             | Único, mínimo 3 caracteres               |
| description     | TEXT          | não         | Descrição do grupo        | Máximo 1000 caracteres                   |
| default_role_id | UUID          | não         | Função padrão do grupo    | Referência a roles(id)                   |
| created_at      | TIMESTAMP     | sim         | Data de criação           | Valor padrão: CURRENT_TIMESTAMP          |
| updated_at      | TIMESTAMP     | sim         | Data de atualização       | Atualizado automaticamente               |
| created_by      | UUID          | sim         | Criador do grupo          | Referência a users(id)                   |
| is_active       | BOOLEAN       | sim         | Status do grupo           | Valor padrão: TRUE                       |


- **Regras de validação**:
  - Campos obrigatórios: name, created_by
  - Valores padrão: 
    - is_active: true
    - created_at: now()
  - Campos não editáveis via API: id, created_at, created_by
  - Campos opcionais: description, default_role_id
  - Outras regras:
    - Nome deve ser único no sistema
    - Apenas usuários com permissão 'MANAGE_GROUPS' podem criar grupos

- **Relacionamento**:
  - `@OneToMany GroupMember members` (um grupo tem muitos membros)
  - `@ManyToOne User createdBy` (grupo criado por um usuário)
  - `@ManyToOne Role defaultRole` (função padrão do grupo)

---

## Especificação da Entidade `GroupMember`

- **Nome da entidade**: `GroupMember`
- **Descrição**: Representa a associação entre um usuário e um grupo, podendo ter funções específicas

- **Campos**:

| Campo                | Tipo          | Obrigatório | Descrição                             | Restrições                                    |
|----------------------|---------------|-------------|---------------------------------------|-----------------------------------------------|
| id                   | UUID          | sim         | Identificador único                   | Gerado automaticamente                        |
| group_id             | UUID          | sim         | Grupo associado                       | Referência a groups(id) com ON DELETE CASCADE |
| user_id              | UUID          | sim         | Usuário membro                        | Referência a users(id) com ON DELETE CASCADE  |
| assigned_role_id     | UUID          | não         | Função específica do membro           | Referência a roles(id)                        |
| joined_at            | TIMESTAMP     | sim         | Data de entrada no grupo              | Valor padrão: CURRENT_TIMESTAMP               |
| invited_by           | UUID          | não         | Usuário que convidou                  | Referência a users(id)                        |
| invitation_expires_at| TIMESTAMP     | não         | Data de expiração do convite          |                                               |
| is_active            | BOOLEAN       | sim         | Status do membro                      | Valor padrão: TRUE                            |

- **Regras de validação**:
  - Campos obrigatórios: group_id, user_id
  - Valores padrão:
    - joined_at: now()
    - is_active: true
  - Campos não editáveis via API: id, joined_at
  - Campos opcionais: assigned_role_id, invited_by, invitation_expires_at
  - Outras regras:
    - Um usuário não pode ser membro do mesmo grupo mais de uma vez
    - Se invitation_expires_at for definido, deve ser no futuro

- **Relacionamento**:
  - `@ManyToOne Group group` (membro pertence a um grupo)
  - `@ManyToOne User user` (membro é um usuário)
  - `@ManyToOne Role assignedRole` (função específica do membro)

---

## Especificação da Entidade `GroupAuditLog`

- **Nome da entidade**: `GroupAuditLog`
- **Descrição**: Registra todas as ações significativas realizadas em grupos para auditoria e rastreamento

- **Campos**:

| Campo           | Tipo          | Obrigatório | Descrição                          | Restrições                                    |
|-----------------|---------------|-------------|------------------------------------|-----------------------------------------------|
| id              | UUID          | sim         | Identificador único                | Gerado automaticamente                        |
| group_id        | UUID          | sim         | Grupo auditado                     | Referência a groups(id) com ON DELETE CASCADE |
| action          | VARCHAR(50)   | sim         | Tipo de ação                       | Valores: ADD_MEMBER, REMOVE_MEMBER, UPDATE_GROUP |
| performed_by    | UUID          | sim         | Usuário que executou a ação        | Referência a users(id)                        |
| performed_at    | TIMESTAMP     | sim         | Data da ação                       | Valor padrão: CURRENT_TIMESTAMP               |
| target_user_id  | UUID          | não         | Usuário afetado                    | Referência a users(id)                        |
| old_value       | JSONB         | não         | Valor anterior                     |                                               |
| new_value       | JSONB         | não         | Novo valor                         |                                               |
| ip_address      | VARCHAR(45)   | não         | IP de origem                       |                                               |

- **Regras de validação**:
  - Campos obrigatórios: group_id, action, performed_by
  - Valores padrão: performed_at: now()
  - Campos não editáveis via API: id, performed_at
  - Campos opcionais: target_user_id, old_value, new_value, ip_address
  - Outras regras:
    - Ação deve ser um dos tipos pré-definidos
    - old_value e new_value devem ser objetos JSON válidos

- **Relacionamento**:
  - `@ManyToOne Group group` (log pertence a um grupo)
  - `@ManyToOne User performedBy` (ação realizada por um usuário)
  - `@ManyToOne User targetUser` (usuário afetado pela ação)

---

## Especificação da Entidade `GroupInvitation`

- **Nome da entidade**: `GroupInvitation`
- **Descrição**: Gerencia convites para usuários ingressarem em grupos com prazos de validade

- **Campos**:

| Campo       | Tipo          | Obrigatório | Descrição                          | Restrições                                    |
|-------------|---------------|-------------|------------------------------------|-----------------------------------------------|
| id          | UUID          | sim         | Identificador único                | Gerado automaticamente                        |
| group_id    | UUID          | sim         | Grupo convidado                    | Referência a groups(id) com ON DELETE CASCADE |
| email       | VARCHAR(255)  | sim         | E-mail do convidado                | Formato válido                                |
| token       | VARCHAR(100)  | sim         | Token único do convite             |                                               |
| role_id     | UUID          | não         | Função sugerida                    | Referência a roles(id)                        |
| invited_by  | UUID          | sim         | Usuário que enviou o convite       | Referência a users(id)                        |
| created_at  | TIMESTAMP     | sim         | Data de criação                    | Valor padrão: CURRENT_TIMESTAMP               |
| expires_at  | TIMESTAMP     | sim         | Data de expiração                  | Padrão: CURRENT_TIMESTAMP + 7 dias            |
| status      | VARCHAR(20)   | sim         | Status do convite                  | Valores: PENDING, ACCEPTED, EXPIRED           |

- **Regras de validação**:
  - Campos obrigatórios: group_id, email, invited_by
  - Valores padrão:
    - created_at: now()
    - expires_at: now() + 7 dias
    - status: 'PENDING'
  - Campos não editáveis via API: id, created_at, token
  - Campos opcionais: role_id
  - Outras regras:
    - Token deve ser único e gerado automaticamente
    - Não pode convidar o mesmo e-mail para o mesmo grupo mais de uma vez com status PENDING

- **Relacionamento**:
  - `@ManyToOne Group group` (convite para um grupo)
  - `@ManyToOne User invitedBy` (convite enviado por um usuário)
  - `@ManyToOne Role role` (função sugerida)

---

## Especificação da Entidade `Role`

- **Nome da entidade**: `Role`
- **Descrição**: Define funções com conjuntos específicos de permissões que podem ser atribuídas a usuários ou grupos

- **Campos**:

| Campo          | Tipo           | Obrigatório | Descrição                                       | Restrições                           |
|----------------|----------------|-------------|-------------------------------------------------|--------------------------------------|
| id             | UUID           | sim         | Identificador único da role                     | Gerado automaticamente               |
| name           | VARCHAR(100)   | sim         | Nome da role                                    | Único, min 3 caracteres              |
| description    | VARCHAR(255)   | não         | Descrição das permissões da role                | Máximo 255 caracteres                |
| permissions    | VARCHAR[]      | sim         | Lista de permissões                             | Valores pré-definidos                |
| is_default     | BOOLEAN        | sim         | Se é atribuída a novos usuários                 | Padrão: true                        |
| level          | INTEGER        | sim         | Nível hierárquico da role                       | 1-100                                |
| created_at     | TIMESTAMP      | sim         | Data de criação                                 | Valor padrão: CURRENT_TIMESTAMP      |
| updated_at     | TIMESTAMP      | sim         | Data da última atualização                      | Atualizado automaticamente           |

- **Regras de validação**:
  - Campos obrigatórios: name, permissions
  - Valores padrão:
    - is_default: true
    - created_at: now() 
    - level: 1
  - Campos não editáveis via API: id, created_at
  - Campos opcionais: description
  - Outras regras:
    - Nome deve ser em UPPERCASE e único
    - Permissões devem ser validadas contra enum pré-definido
    - Roles com nível maior podem modificar roles com nível menor

- **Relacionamento**:
  - `@OneToMany User users` (usuários com esta role)
  - `@OneToMany Group groups` (grupos que usam esta role como padrão)

---

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


### Grupos (Groups)

| Método | Endpoint                        | Descrição                          | Autenticado | Permissão Requerida          |
|--------|---------------------------------|------------------------------------|-------------|------------------------------|
| GET    | `/api/v1/groups`                | Listar todos grupos                | sim          | `groups:read`               |
| GET    | `/api/v1/groups/:id`            | Obter grupo por ID                 | sim          | `groups:read`               |
| POST   | `/api/v1/groups`                | Criar novo grupo                   | sim          | `groups:create`             |
| PUT    | `/api/v1/groups/:id`            | Atualizar grupo                    | sim          | `groups:update`             |
| DELETE | `/api/v1/groups/:id`            | Excluir grupo                      | sim          | `groups:delete`             |
| GET    | `/api/v1/groups/:id/members`    | Listar membros do grupo            | sim          | `groups:read`               |

### Membros de Grupos (Group Members)

| Método | Endpoint                                | Descrição                          | Autenticado  | Permissão Requerida         |
|--------|-----------------------------------------|------------------------------------|--------------|-----------------------------|
| POST   | `/api/v1/groups/:groupId/members`       | Adicionar membro ao grupo          | sim          | `group-members:create`      |
| PUT    | `/api/v1/groups/:groupId/members/:userId` | Atualizar função do membro       | sim          | `group-members:update`      |
| DELETE | `/api/v1/groups/:groupId/members/:userId` | Remover membro do grupo          | sim          | `group-members:delete`      |

### Convites (Group Invitations)

| Método | Endpoint                                | Descrição                          | Autenticado  | Permissão Requerida         |
|--------|-----------------------------------------|------------------------------------|--------------|-----------------------------|
| POST   | `/api/v1/groups/:groupId/invitations`   | Enviar convite para grupo          | sim          | `invitations:create`        |
| GET    | `/api/v1/invitations/:token`            | Validar token de convite           | não          | -                           |
| POST   | `/api/v1/invitations/:token/accept`     | Aceitar convite                    | sim          | -                           |
| DELETE | `/api/v1/invitations/:id`               | Cancelar convite                   | sim          | `invitations:delete`        |

### Funções (Roles)

| Método | Endpoint                        | Descrição                          | Autenticado | Permissão Requerida         |
|--------|---------------------------------|------------------------------------|-------------|-----------------------------|
| GET    | `/api/v1/roles`                 | Listar todas funções               | sim          | `roles:read`                |
| GET    | `/api/v1/roles/:id`             | Obter função por ID                | sim          | `roles:read`                |
| POST   | `/api/v1/roles`                 | Criar nova função                  | sim          | `roles:create`              |
| PUT    | `/api/v1/roles/:id`             | Atualizar função                   | sim          | `roles:update`              |
| DELETE | `/api/v1/roles/:id`             | Excluir função                     | sim          | `roles:delete`              |

### Logs de Auditoria (Audit Logs)

| Método | Endpoint                        | Descrição                          | Autenticado | Permissão Requerida         |
|--------|---------------------------------|------------------------------------|-------------|-----------------------------|
| GET    | `/api/v1/audit-logs`            | Listar logs de auditoria           | sim          | `audit-logs:read`           |
| GET    | `/api/v1/audit-logs/:id`        | Obter log específico               | sim          | `audit-logs:read`           |
| GET    | `/api/v1/groups/:id/audit-logs` | Listar logs de um grupo            | sim          | `audit-logs:read`           |

## Autenticação, Autorização, Refresh-token e Token deve ser feito Firebase Admin SDK 
 - Adicionar o Firebase Admin SDK no Projeto
 - A Chave privada mantenha-o em um local seguro
 - Segue a chave privada: { "type": "", "project_id": "", "private_key_id": "", "private_key": "", "client_email": "", "client_id": "", "auth_uri": "", "token_uri": "", "auth_provider_x509_cert_url": "", "client_x509_cert_url": "", "universe_domain": "" }
 
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

- Proteção via Spring Security + JWT

- Anotações esperadas: `@PreAuthorize("hasRole('USER')")`

## Observações

- Gere os arquivos com pacotes corretos

- Inclua validações com `javax.validation`

- Use `@Transactional` nas operações críticas

- Não esquecer da associação `User` na entidade

- A responsabilidade do usuário deve vir do `SecurityContextHolder`

- Proceda com a instalação da dependência, se aplicável

- Gere o código em inglês

 

 

 