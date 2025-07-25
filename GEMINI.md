#  Padrões de Projeto
Este guia apresenta os padrões de projeto, nomenclaturas e boas práticas amplamente aceitas e utilizadas no ecossistema Java com Spring Boot.

## 1 Estrutura do Projeto

- Arquitetura em camadas: `controller → service → repository → dto → entity → mapper`

- Padrão de pacotes organizados por funcionalidades/módulos (ex: `auth`, `todo`)

- Uso de DTOs para entrada/saída da API

- Utilização de MapStruct para mapeamento entre DTO ↔ Entity

## 2 API Design

- RESTful API seguindo convenções HTTP

- Versionamento de endpoints via URI: `/api/v1/`

- Documentação automática com springdoc-openapi (gera OpenAPI 3 + Swagger UI)

- Suporte a paginação e ordenação padronizada (Spring Data Pageable)

## 3 Segurança

- CORS configurado para ambientes específicos

- Proteções ativas contra:

  - CSRF (em apps web stateful)

  - XSS e SQL Injection (validação e ORM seguro)

- Rate Limiting

## 4 Qualidade de Código

- Cobertura de testes mínima de 80% com JUnit + Mockito

- Formatação automática (Google Java Format)

## 5 Monitoramento e Observabilidade

- Logs estruturados com SLF4J + Logback e MDC (ex: requestId, userId)

## 6 Banco de Dados e Persistência

- ORM: Hibernate com Spring Data JPA

- Migrations versionadas: Flyway

- Índices e constraints aplicados via migrations

- Uso de transações: `@Transactional` no nível de service

- Estratégia de exclusão lógica (isDeleted)

| Campo         | Tipo            | Obrigatório | Descrição                                       | Restrições                                   |
|---------------|-----------------|-------------|-------------------------------------------------|----------------------------------------------|
| isDeleted     | BOOLEAN         | sim         | Exclusão lógica                                 | Padrão: false                                |

## 7 Organização de Pacotes
### 7.1 Estrutura por funcionalidade (modularizada)

```text
src/main/java/com/app/boot_app/
├── core/
├── domain/
│   ├── auth/
│   │   ├── constant/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── enums/
│   │   ├── mapper/
│   │   ├── repository/
│   │   └── service/
│   ├── todo/
│   │   ├── constant/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── enums/
│   │   ├── mapper/
│   │   ├── repository/
│   │   └── service/
└── shared/
    ├── exception/
    ├── infra/
    │    ├── auth/
    │    │   ├── firebase_sdk/
    │    │   │   ├── constant/
    │    │   │   ├── model/
    │    │   │   └── service/
    │    │   └── AuthAdapter.java
    ├── response/
    ├── constant/
    └── util/
```

### 7.2 Camadas

- `controller`: expõe os endpoints da API

- `service`: lógica de negócio

- `repository`: acesso ao banco (Spring Data JPA)

- `dto`: objetos de transporte para request/response

- `entity`: modelo persistente com JPA

- `shared`: Compartilhamento de utilitários globais, não ligados a domínio específico.

- `utils`: Metodos auxiliares (ex.: formatar datas, validar e-mails).

- `mapper`: mapeamento entre DTO ↔ Entity (MapStruct)

- `exception`: validações e erros de domínio

- `infra`: ferramenta e bibliotecas externas

### 7.3 Nomenclatura por Camada

| Camada     | Nomenclatura sugerida         | Descrição                                              |
|------------|-------------------------------|--------------------------------------------------------|
| Controller | `XxxController`               | Recebe e responde a requisições HTTP                   |
| Service    | `XxxService`, `XxxServiceImpl`| Lógica de negócio (interface + implementação opcional) |
| Repository | `XxxRepository`               | Interface JPA com `@Repository`                        |
| DTO        | `XxxRequest`, `XxxResponse`   | Transporte de dados para entrada e saída               |
| Entity     | `Xxx`                         | Modelo persistente (`@Entity`)                         |
| Mapper     | `XxxMapper`                   | Mapeia entre Entity e DTO                              |
| Exception  | `XxxNotFoundException`        | Exceções específicas por domínio                       |
| Test       | `XxxServiceTest`              | Testes unitários                                       |


## 8 Padrões de Projeto Mais Utilizados

| Padrão de Projeto                 | Onde usar                                       | Observações                        |
|----------------------------------|--------------------------------------------------|------------------------------------|
| **Controller-Service-Repository**| Arquitetura base em Spring                      | Padrão clássico                    |
| **DTO (Data Transfer Object)**   | Comunicação entre camadas/API                   | Evita exposição de entidades       |
| **Builder**                      | Criação de objetos imutáveis (ex: DTOs, testes) | Via Lombok ou manual               |
| **Factory**                      | Instanciação dinâmica controlada                | Ex: autenticação, strategies       |
| **Strategy**                     | Lógica com variações de comportamento           | Ex: validação, envio, notificações |
| **Adapter**                      | Integração com serviços externos                | Ex: gateway de pagamento           |
| **Template Method**              | Lógica com passos fixos e variáveis             | Ex: operações em lote              |
| **Decorator**                    | Extensão de funcionalidades sem alterar código  | Ex: logging, metrics               |
| **Observer/Event**               | Eventos assíncronos                             | Ex: Spring Events, Kafka           |
| **Specification**                | Consultas dinâmicas em JPA                      | Combinação de filtros              |
| **Singleton**                    | Beans gerenciados pelo Spring                   | Default no contexto Spring         |


## 9 Convenções de Nomes
### 9.1 Entidades

- Sempre no singular: `User`, `Order`, `Todo`

- Relacionamentos explícitos via `@ManyToOne`, `@OneToMany`, etc.

- Evite nomes técnicos demais: Task é melhor que SysOperation

### 9.2 DTOs

- Entrada: `CreateTodoRequest`, `UpdateTodoRequest`

- Saída: `TodoResponse`, `TodoSummaryResponse`

- Sempre usar validações: `@NotNull`, `@Size`, etc.


### 9.3 Controllers

```java
    import com.app.boot_app.shared.response.ApiResponse;
    import com.app.boot_app.shared.response.Response;
    import com.app.boot_app.core.security.FirebaseRequest;
    import org.springframework.context.MessageSource;
    import com.app.boot_app.domain.auth.service.AuthService;
    import com.app.boot_app.domain.auth.dto.UserResponseDTO;

	@RestController
    @RequestMapping("/api/v1/{domain}")
    @RequiredArgsConstructor
    public class XxxController {

        private final XxxService xxxService;
        private final AuthService authService;

        @GetMapping("/{rota}")
        public ApiResponse<XxxResponseDTO> xxx(HttpServletRequest request) {
            String email = firebaseRequest.getFromRequest(request).getEmail();

            UserResponseDTO user =  authService.getUserByEmail(email);

             List<XxxResponseDTO> result = xxxService.xxxByUserId(user.getId());

            String message = messageSource.getMessage(
                "{domain.method.message}", 
                null,
                LocaleContextHolder.getLocale()
            );

            return Response.ok(
                message, 
                result
            );
        }
    }

```

```java
    import com.app.boot_app.shared.response.ApiResponse;
    import com.app.boot_app.shared.response.Response;
    import com.app.boot_app.core.security.FirebaseRequest;
    import org.springframework.context.MessageSource;
    import com.app.boot_app.domain.auth.service.AuthService;
    import com.app.boot_app.domain.auth.dto.UserResponseDTO;

    @RestController
    @RequestMapping("/api/v1/auth/group")
    @RequiredArgsConstructor
    public class GroupController {

        private final GroupService groupService;
        private final AuthService authService;
        private final FirebaseRequest firebaseRequest;

        @DeleteMapping("user/{userId}/group/{groupId}/enterprise/{enterpriseId}")
        public ApiResponse<Void> removeUserFromGroup(
            HttpServletRequest request,
            @PathVariable UUID userId,
            @PathVariable UUID groupId,
            @PathVariable UUID enterpriseId
        ) {


            String email = firebaseRequest.getFromRequest(request).getEmail();
            UserResponseDTO user =  authService.getUserByEmail(email);

            /*
             * Impedir usuários comuns de modificar grupos/empresas de outros
             * Proteger contra IDOR (acesso direto a IDs)
            */
            boolean hasPermission = groupService.userHasPermissionToRemove(
                authenticatedUser.getId(), groupId, enterpriseId
            );

            if (!hasPermission) {
                throw new AccessDeniedException("Você não tem permissão para remover usuários deste grupo.");
            }
            
            groupService.removeUserFromGroup(
                userId,
                groupId,
                enterpriseId
            );

            return Response.ok(
                "User removed from group successfully", 
                null
            );
        }
    }

```

### 9.4 Service
```java
@Service
@RequiredArgsConstructor
public class XxxServiceImpl implements XxxService {

    private final XxxService xxxService;
}

```

### 9.5 Padrão de conversão do retorno
```java

    public class ApiResponse<T> {
        private boolean success;
        private String message;
        private int statusCode;
        private T data;
        private Error error;
        private String timestamp;
    }

    public static class Error {
        private String code;
        private String message;
    }

```

### 9.6 Padrão de retorno Sucesso e Erro
```json
    {
        "success": false,
        "message": "E-mail não verificado",
        "statusCode": 400,
        "errors": {
            "code": "auth/wrong-email-not-verified",
            "message": "E-mail não verificado"
        }
    }
```

### 9.7 Padrão de retorno Sucesso e Erro
```json
    {
        "success": true,
        "message": "Login realizado com sucesso",
        "statusCode": 200,
        "data": ".eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6I",
        "timestamp": "2025-07-17T03:34:30.702Z"
    }
```

## 10 Prompt Genérico para criar especificação de Entidades

### Especificação da Entidade `{NomeDaEntidade}`

- **Nome da entidade**: `{NomeDaEntidade}`
- **Descrição**: `{Breve descrição da entidade, explicando sua função no sistema}`

- **Campos**:

| Campo           | Tipo          | Obrigatório | Descrição                     | Restrições ou Observações                        |
|-----------------|---------------|-------------|-------------------------------|--------------------------------------------------|
| `{campo1}`      | `{tipo}`      | `{sim|não}` | `{descrição do campo}`        | `{restrições ou comportamento especial}`        |
| `{campo2}`      | `{tipo}`      | `{sim|não}` | `{descrição do campo}`        | `{restrições ou comportamento especial}`        |
| ...             | ...           | ...         | ...                           | ...                                              |

- **Regras de validação**:
  - Campos obrigatórios: `{campo1}`, `{campo2}`
  - Valores padrão:
    - `{campo}`: `{valor}`
  - Campos não editáveis via API: `{campo1}`, `{campo2}`
  - Campos opcionais: `{campo1}`, `{campo2}`
  - Outras regras:
    - `{ex: Campo 'name' deve ser único}`
    - `{ex: Apenas usuários com permissão 'MANAGE_ENTITIES' podem criar}`

- **Relacionamentos**:
  - `@OneToMany {ClasseDestino} {nomeCampo}` – `{descrição do relacionamento}`
  - `@ManyToOne {ClasseOrigem} {nomeCampo}` – `{descrição do relacionamento}`
  - `{outros relacionamentos, se houver}`


## 11 Prompt para Especificação de Endpoints

### {Nome da Funcionalidade}

| Método | Endpoint                             | Descrição                         | Autenticado | Body Request (exemplo)                    |
|--------|--------------------------------------|-----------------------------------|-------------|-------------------------------------------|
| {GET|POST|PUT|DELETE} | `/api/v1/{rota}`               | {O que este endpoint faz}         | {sim|não}   | `{ campo1: "valor", campo2: "valor" }`   |
| {GET|POST|PUT|DELETE} | `/api/v1/{rota}`               | {O que este endpoint faz}         | {sim|não}   | `{ campo1: "valor", campo2: "valor" }`   |
