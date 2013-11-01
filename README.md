Monitora, Brasil!
======================
 
O projeto Monitora, Brasil! é uma ferramenta que possibilita ao usuário pesquisar e monitorar as ações dos Deputados Federais. Por meio do aplicativo, é possível verificar a assiduidade, os projetos propostos, a timeline do Twitter, os gastos com cota parlamentar, além de salvar os projetos e deputados favoritos.
 
Outra importante característica do projeto é a interação com as redes sociais, em especial com o Twiter, o que facilita o diálogo com os representantes eleitos e a participação social. Faz-se necessário, entretanto, implementar mais funcionalidades para aprimorar essa característica.
 
Metodologia
======================
 
Os dados são extraídos do site da Câmara dos Deputados (http://www2.camara.leg.br/transparencia/dados-abertos) por meio de um cron job e armazenados em uma base de dados Mysql. O app faz requisição via POST ou GET, e o servidor retorna os dados no formato json.
Ex.: http://www.gamfig.com/mbrasilwsdl/buscacota.php?idsubcota=9 (em breve, todas as url's de consulta estarão documentadas).
 
Para disponibilizar as informações das doações recebidas pelos Deputados eleitos em 2010, foram utilizados os dados do TSE.
 
O app foi desenvolvido por meio do Android Developer Tools -http://developer.android.com/tools/help/adt.html - na linguagem Java.
 
 
Link para App em Produção
=======================
https://play.google.com/store/apps/details?id=com.gamfig.monitorabrasil
 
Configuração
======================
 
Para a utilização da funcionalidade de login, é necessária a configuração da API do Facebook (https://developers.facebook.com/docs/android/getting-started/)
e do Google+ (http://developer.android.com/google/play-services/setup.html)
 
É necessário também criar um perfil developer e cadastrar o app no Facebook Developer (https://developers.facebook.com/apps ) para se ter um user key, assim como no Twitter (https://dev.twitter.com/ ). Sem eles, nem o login do Facebook nem a timeline do Twitter dos politicos funcionam. .
 
 
Licença do projeto
======================
AGPL v3
 
Considerações
======================
A iniciativa visa criar uma ferramenta capaz de aflorar nos cidadãos um interesse maior pela política, pela formulação de políticas públicas e pelo exercício democrático da cidadania no País. 
 
O objetivo do app não é só a transparência dos dados, mas também o exercício da democracia participativa, o monitoramento pelo cidadão e a prestação de contas pelos representantes eleitos.  A tecnologia disponível hoje permite a participação mais direta do cidadão no processo legislativo, o que há muito tempo era praticado pelos gregos nas pólis. 
 
A política vem caindo em descrédito nos últimos anos. Por isso, é necessário ampliar o leque de opções que o cidadão possui para interagir com seus representantes e poder, de fato, influenciar suas decisões e ações. Divulgar os atos de um parlamentar nas redes sociais pode provocar importantes mudanças de comportamento. As novas tecnologias utilizadas a partir de dispositivos móveis são grandes aliadas nesse processo, dado o aumento exponencial da utilização de smartphones pela população brasileira,. 
 
Espera-se que, com a ajuda daqueles que se identifiquem com o projeto, a ferramenta evolua e, consequentemente, se torne uma referência na área. Qualquer um pode colaborar:  programadores, designers, cientistas políticos, eleitores, políticos, cidadãos. Qualquer sugestão e, principalmente, ação serão bem-vindas.
 
 
Caso queira coloborar com o projeto de alguma forma, fique à vontade. 
Há muito trabalho a ser feito e a participação da comunidade é de extrema importância para o sucesso do projeto. Portanto, vamos monitorar juntos, Brasil!

 
Geraldo Augusto de Morais Figueiredo
Cidadão brasileiro
