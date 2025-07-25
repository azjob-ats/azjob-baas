SELECT 
	g."name" as Grupo,
    r."name" AS Perfil,
    a."name" AS Ação,
    p.allowed as Ativo
FROM 
    tb_permission p
JOIN tb_action a ON p.action_id = a.id
JOIN tb_role r ON p.role_id = r.id
join tb_group g on p.group_id = g.id 
WHERE 
    p.group_id = 'b8db3f7e-fa29-4162-8441-2ee557eba3e5'
    and p.is_deleted = false
    and p.allowed = true
