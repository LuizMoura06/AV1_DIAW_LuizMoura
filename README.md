# Candidatos TSE

Aplicação para consultar candidatos do material didático da AV1 de DIAW. A página apresenta fotos e informações dos candidatos, com filtros por nome ou número, cargo e partido.

## Tecnologias

- Java 25
- Spring Boot 3.5.16
- Apache Maven
- Thymeleaf, HTML e CSS
- OpenCSV 5.12.0

## Como executar

Instale o JDK 25 e o Maven. Na pasta do projeto, confirme as versões:

```bash
java -version
mvn -version
```

O Maven também deve estar usando o Java 25. Para iniciar a aplicação:

```bash
mvn spring-boot:run
```

Acesse [http://localhost:8080/](http://localhost:8080/). Se a porta 8080 estiver ocupada, encerre a outra aplicação antes de iniciar este projeto. A primeira execução precisa de acesso à internet para baixar as dependências.

Para compilar e executar os testes:

```bash
mvn clean verify
```

Também é possível gerar e executar o arquivo JAR:

```bash
mvn package
java -jar target/candidatos-tse-1.0.0.jar
```

## Filtros

Todos os filtros são opcionais e podem ser combinados. A busca textual procura trechos do nome civil, do nome de urna ou do número do candidato. Ela ignora diferenças entre maiúsculas e minúsculas, mas mantém a distinção entre letras com e sem acento.

O botão **Filtrar** envia os parâmetros `texto`, `cargo` e `partido` por GET para `/`. Os valores escolhidos permanecem no formulário. O botão **Limpar** volta à listagem completa.

Na base fornecida, a listagem contém 1.832 registros, incluindo 11 para governador e 18 para senador. Esses números se referem ao material da atividade.

## Estrutura

```text
src/
├── main/
│   ├── java/com/example/CandidatosTSE/
│   │   ├── application/CandidatosTseApplication.java
│   │   ├── controller/CandidatosTseController.java
│   │   ├── model/Candidato.java
│   │   └── service/CandidatosTseService.java
│   └── resources/
│       ├── application.properties
│       ├── data/candidatos/consulta_cand_2026_MG.csv
│       ├── static/
│       │   ├── css/style.css
│       │   └── images/
│       │       ├── candidatos/
│       │       └── sem-foto.svg
│       └── templates/index.html
└── test/java/com/example/CandidatosTSE/
    ├── controller/
    ├── model/
    └── service/
pom.xml
```

O serviço carrega o CSV na inicialização e mantém os registros em memória. O controlador fornece ao Thymeleaf a lista filtrada, a contagem, as opções e os valores selecionados. As 1.830 fotos fornecidas ficam nos recursos estáticos. O serviço recupera a foto de Gustavo Galassi a partir de outro registro da mesma pessoa. Antônio Divino e Zelinho não têm foto correspondente nos anexos e recebem a imagem “Sem foto”.

## Origem dos arquivos

O enunciado, o CSV, as fotos e as classes `Candidato.java` e `CandidatosTseService.java` foram fornecidos como anexos da atividade. As duas classes foram preservadas. A base é utilizada como material didático, sem validação de sua correspondência com o cadastro eleitoral oficial atual.

Para publicação, foi utilizada uma cópia do CSV com os valores de CPF, e-mail e título eleitoral removidos. O cabeçalho, a quantidade e a ordem das colunas foram mantidos para preservar a leitura do serviço. Essa remoção não afeta os filtros nem a associação das fotos: apesar do nome `resolverFotosPorCpf`, o método fornecido agrupa os registros pelo nome civil e pelo número do candidato.

## Checklist técnico da atividade

| Questão | Implementação |
| --- | --- |
| 1 — Controlador | `@Controller`, serviço injetado pelo construtor, endpoint `GET /`, parâmetros opcionais, chamada a `filtrar(cargo, partido, texto)`, preenchimento do `Model` e retorno da view `index`. |
| 2 — Página | Namespace do Thymeleaf, CSS e links com caminhos resolvidos pelo template, formulário GET, opções dinâmicas, filtros preservados, cards com foto e dados, contagem, mensagem de lista vazia e ocultação de campos sem valor útil. |
| 3 — Estilo | Grade responsiva com CSS Grid, colunas de no mínimo 150 px, espaçamento de 12 px e fotos com proporção `161 / 225` e `object-fit: cover`. |
| Recursos | CSV em `resources/data/candidatos` e fotos em `resources/static/images/candidatos`. |

Esta lista verifica requisitos técnicos; não substitui as regras de realização e entrega da avaliação.

## Validação

Foram executados 31 testes automatizados de modelo, serviço, controlador e renderização da página, sem falhas. Foram conferidos também os filtros e a imagem substituta no navegador, as 1.830 imagens JPEG e o layout responsivo em uma largura de 390 px. A aplicação também foi iniciada com a configuração padrão na porta 8080, sem parâmetros de substituição. A página inicial, os filtros de governador e senador e as fotos responderam corretamente em http://localhost:8080/.
