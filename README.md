Monitora, Brasil!
======================

O aplicativo Monitora, Brasil! é uma ferramenta que possibilita a qualquer pessoa pesquisar e monitorar o que os Deputados Federais estão fazendo na Câmara dos Deputados.
É possível verificar a assiduidade, os projetos propostos, rankings, Twitter, gastos com cota parlamentar e outras informações. 
Outra importante característica do projeto é a interação com as redes sociais. Faz-se necessário implementar mais funcionalidades para aprimorar tal característica.

Os dados são extraídos do site da Câmara dos Deputados http://www2.camara.leg.br/transparencia/dados-abertos por meio de um cron job e armazenado em uma base que os retorna no formato via json
Ex.: http://www.gamfig.com/mbrasilwsdl/buscacota.php?idsubcota=9 (em breve todas as url's de consulta estarão documentadas)

A ideia de disponibilizar o projeto no GitHub é de que qualquer um que se sinta interessado em desenvolver algo que possa ajudar o País contribuir com a evolução da ferramenta.

Configuração
======================

O projeto na versão para Android é necessário a configuração da api do Facebook https://developers.facebook.com/docs/android/getting-started/
e do Google+ http://developer.android.com/google/play-services/setup.html

É necessário criar um perfil developer e cadastrar a app no facebook developer para ter seu user key, assim como no twitter. Sem eles não vai funcionar o login do Facebook nem a timeline do Twitter dos políticos.


Licença do projeto
======================
AGPL v3

Considerações Finais
======================
A ideia de desenvolver o app veio da vontade de fazer algo para contribuir com a atividade cidadã. Você não vai encontrar aqui um código de excelente qualidade pois comecei a fazer sozinho e sem muito tempo para dedicar ao projeto ( e também por não ser um exímio programdor). Espero que com a ajuda de outros que se identifiquem com o projeto possam melhorar a ferramenta, em todos os apectos. 
Isto fará com que aumente a capacidade de informar e de permitir que o cidadão atue mais na política do País, que precisa tanto de ser ajudada.
Exerça sua cidadania, dialogue com os Parlamentares e contribua para uma atividade legislativa mais transparente e eficiente.
Monitora, Brasil!
