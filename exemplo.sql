/*







[action] 
id  Ação                            
1   Criar vaga                      
2   Editar vaga                     
3   Visualizar vaga                 
4   Mover vaga entre fases          
5   Visualizar candidaturas         
6   Encerrar vaga                   
7   Excluir vaga                    
8   Criar/editar fases do Kanban    
9   Acessar aba Pessoas             
10  Convidar/gerenciar usuários     
11  Editar configurações da empresa 
12  Acessar o histórico completo da vaga
13  Visualizar estatísticas de candidatura
14  Clonar a vaga (caso queira criar uma nova semelhante)
15  Reabrir a vaga movendo-a novamente para "Backlog" (opcional)
15  pode gerenciar vagas e candidatos, mas não usuários
15  Convidar membros da equipe (ex: analistas, coordenadores, parceiros externos)
15  Atribuir permissões específicas para cada ação dentro da organização
15  Gerenciar quem pode ver, editar, publicar e movimentar vagas

[role]
id funções                    Descrição
1  usuario candidato          usuario padrão(normais) que se cadastrou e realiza canditadura de vagas, pesquisa, visualiza empresa...
2  usuario anonimo            usuario que não se cadastrou ele pode visualiza e realiza canditadura de vagas, pesquisa, visualiza empresa...
3  empresa                    empresa que abriu uma conta na plataforma
1  recrutador gestor          pessoa da empresa responsa cria os precesso, gerenciar pessoas
2  recrutador colaborador     pessoa da empresa responsavel por gerenciar processo de recrutamento  
6  convidado                  pessoa com acesso de visualizar processo de recrutamento
1. Recrutador Gestor          Pessoa da empresa responsável por criar os processos seletivos e gerenciar pessoas envolvidas.
2. Recrutador Colaborador     Pessoa da empresa responsável por colaborar na gestão e andamento dos processos de recrutamento.
3. Analista de RH             Responsável por triagem de currículos, agendamento de entrevistas e apoio à área de recrutamento.
4. Coordenador de RH          Supervisiona a equipe de RH e garante que os processos de recrutamento e seleção sigam as políticas da empresa.
5. Gerente de Departamento    Solicita novas vagas, participa da definição de perfis e aprova candidatos para sua área.
6. Entrevistador Técnico      Avalia candidatos com base em habilidades técnicas específicas durante as entrevistas.
7. Psicólogo Organizacional   Realiza avaliações comportamentais e aplica testes psicológicos em candidatos.
8. Analista de Atração de Talentos         Responsável por campanhas de divulgação de vagas e estratégias para atrair candidatos.
9. Parceiro Externo de Recrutamento        Consultor ou empresa terceirizada que atua em parceria para preencher vagas específicas.
10. Administrador da Conta Empresarial     Tem controle total da conta da empresa na plataforma, podendo adicionar usuários, editar permissões e visualizar relatórios completos.
11. Assistente de RH                       Auxilia nas atividades administrativas do setor de RH, como organização de documentos e contato com candidatos.
12. Auxiliar de Recrutamento               Apoia na triagem de currículos, envio de e-mails e agendamento de entrevistas.
13. Técnico em Recursos Humanos            Profissional técnico que executa rotinas de recrutamento, seleção e administração de pessoal.
14. Estagiário de RH                       Apoia as atividades de recrutamento sob supervisão, participando de triagens e suporte administrativo.
15. Assistente Administrativo de RH        Atua com foco em controles internos, cadastros e apoio aos processos seletivos.
16. Operador de Atendimento ao Candidato   Responsável por tirar dúvidas, confirmar presença e dar suporte aos candidatos no processo seletivo.
17. Monitor de Entrevistas                 Agenda, organiza e acompanha as entrevistas presenciais ou online, garantindo o fluxo correto.
18. Auxiliar de Departamento Pessoal       Apoia nos registros e integrações de novos colaboradores após o processo seletivo.
19. Técnico de Seleção                     Conduz entrevistas operacionais e aplica testes práticos para funções de base.
20. Assistente de Comunicação Interna      Responsável por divulgar internamente novas vagas, comunicados e ações relacionadas a recrutamento.



[permission]
id  id_role  id_action permitido         id_enterprise  id_recruiter
1   6        1         false             1              1
2   6        2         false             1              1
3   6        3         true              1              1
4   6        4         false             1              1
5   6        5         true              1              1
6   6        6         false             1              1
7   6        7         false             1              1
8   6        8         false             1              1
9   6        9         false             1              1
10  6        10        false             1              1
11  6        11        false             1              1  

[Group]
id grupo           descrição                    id_role id_enterprise
1  RH analytic     Apenas visualizar vagas      6       1

[user_group]
user_id  group_id
1        2

[user]
id email            
1 saulo@gmail.com
2 nubank@gmail.com
3 ana@gmail.com

[enterprise]
id name_enterprise  id_user          
1  nubank           2

[recruiter]
id name_recruiter   id_user          
1  ana              3

*/