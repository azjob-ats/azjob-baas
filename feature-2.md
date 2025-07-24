# 2. feature: Gestão de Grupos, Permissões e Associações de Usuários

Aqui está uma lista de métodos de interface Java, derivados do esquema de banco de dados, com foco em regras de negócio e não em operações CRUD simples.


### Interface: `GroupService`

Esta interface lida com a lógica de negócio para gerenciar grupos dentro de uma empresa.

```java
import java.util.UUID;
import java.util.List;

public interface GroupService {

    /**
     * Cria um novo grupo dentro de uma empresa específica.
     * A lógica de negócio aqui garante que apenas usuários autorizados
     * possam criar grupos para a empresa à qual pertencem.
     *
     * @param groupName O nome do novo grupo.
     * @param description Uma descrição para o novo grupo.
     * @param enterpriseId O ID da empresa onde o grupo será criado.
     * @return O Group recém-criado.
     */
    Group createGroupForEnterprise(String groupName, String description, UUID enterpriseId);

    /**
     * Lista todos os grupos ativos pertencentes a uma empresa específica.
     *
     * @param enterpriseId O ID da empresa.
     * @return Uma lista de grupos.
     */
    List<Group> listGroupsByEnterprise(UUID enterpriseId);

    /**
     * "Exclui" um grupo, marcando-o como deletado (soft delete)
     * Isso preserva o histórico e garante que as referências não sejam quebradas.
     * 
     * Deve usar isDeleted como false para marcando-o como deletado 
     *
     * @param groupId O ID do grupo a ser desativado.
     * @param enterpriseId O ID da empresa para verificação de propriedade.
     */
    void deactivateGroup(UUID groupId, UUID enterpriseId);
}
```

### Interface: PermissionService
Gerencia a lógica complexa de atribuição e verificação de permissões para roles e grupos.

```java
import java.util.UUID;
import java.util.List;

public interface PermissionService {

    /**
     * Concede uma permissão específica a um Role dentro de uma empresa.
     *
     * @param roleId O ID do Role que receberá a permissão.
     * @param actionId O ID da Ação a ser permitida.
     * @param enterpriseId O ID da empresa onde a permissão é válida.
     */
    void grantPermissionToRole(UUID roleId, UUID actionId, UUID enterpriseId);

    /**
     * Revoga uma permissão específica de um Role.
     *
     * @param roleId O ID do Role.
     * @param actionId O ID da Ação a ser revogada.
     * @param enterpriseId O ID da empresa.
     */
    void revokePermissionFromRole(UUID roleId, UUID actionId, UUID enterpriseId);

    /**
     * Concede uma permissão específica a um Grupo. Todos os usuários
     * no grupo herdarão essa permissão.
     *
     * @param groupId O ID do Grupo.
     * @param actionId O ID da Ação a ser permitida.
     * @param enterpriseId O ID da empresa.
     */
    void grantPermissionToGroup(UUID groupId, UUID actionId, UUID enterpriseId);

    /**
     * Revoga uma permissão de um Grupo.
     *
     * @param groupId O ID do Grupo.
     * @param actionId O ID da Ação a ser revogada.
     * @param enterpriseId O ID da empresa.
     */
    void revokePermissionFromGroup(UUID groupId, UUID actionId, UUID enterpriseId);

    /**
     * Verifica se um usuário tem permissão para realizar uma determinada ação.
     * Esta é uma regra de negócio crucial que pode precisar verificar
     * as permissões diretas do usuário, bem como as permissões herdadas de seus grupos.
     *
     * @param userId O ID do usuário a ser verificado.
     * @param actionName O nome da ação (ex: "delete_post", "view_report").
     * @param enterpriseId O ID da empresa onde a ação está sendo tentada.
     * @return true se o usuário tiver permissão, false caso contrário.
     */
    boolean userHasPermissionForAction(UUID userId, String actionName, UUID enterpriseId);
}
```

### Interface: UserGroupService
Lida com a associação de usuários a grupos.
```java
import java.util.UUID;
import java.util.List;

public interface UserGroupService {

    /**
     * Adiciona um usuário a um grupo específico.
     * A lógica pode incluir verificações, como se o usuário e o grupo
     * pertencem à mesma empresa.
     *
     * @param userId O ID do usuário a ser adicionado.
     * @param groupId O ID do grupo.
     * @param enterpriseId O ID da empresa para validação.
     */
    void addUserToGroup(UUID userId, UUID groupId, UUID enterpriseId);

    /**
     * Remove um usuário de um grupo.
     *
     * @param userId O ID do usuário a ser removido.
     * @param groupId O ID do grupo.
     * @param enterpriseId O ID da empresa para validação.
     */
    void removeUserFromGroup(UUID userId, UUID groupId, UUID enterpriseId);

    /**
     * Lista todos os usuários que são membros de um grupo específico.
     *
     * @param groupId O ID do grupo.
     * @return Uma lista de usuários.
     */
    List<User> listUsersInGroup(UUID groupId);

    /**
     * Lista todos os grupos aos quais um usuário pertence.
     *
     * @param userId O ID do usuário.
     * @return Uma lista de grupos.
     */
    List<Group> listGroupsForUser(UUID userId);
}

```