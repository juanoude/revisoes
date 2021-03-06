<?php
//Maven é uma ferramenta de build, gerencia dependencias, arquivos java e compilações.
//Primeiro passo é fazer o download a incorporar o bin para o terminal
mvn --help //ajuda

//Criaremos um projeto básico: --No windows talvez seja necessário aspas duplas nos valores(="valor")
mvn archetype:generate -DartifactId=produtos -DgroupId=br.com.alura.maven -DinteractiveMode=false -DarchetypeArtifactId=maven-archetype-quickstart
//O -DarchetypeArtifactId indica a estrutura inicial desejada, pode-se pegar uma com spring, hibernate, etc...
//Nesse caso ele criou a pasta produtos, com a pasta src(arquivos fonte) com a pasta main e test.
//Dentro do main está os pacotes java(br.com.alura.maven)

mvn compile //Compila o código fonte para o target

mvn test //Roda as classes de teste

pom.xml //É o modelo do projeto, configura tudo a respeito do projeto e aponta todas as dependências

mvn clean //Limpa o diretorio target

//certos plugins não possuem padrão maven, nesses casos se declara o plugin antes do comando:
mvn surefire-report:report
//Com a documentação do maven e os guias de referencia existem muita informação útil sobre os plugins

//Para empacotar todo as classes main em um arquivo jar, utiliza-se:
mvn package
//Lembrando que as configurações estão:
<packaging> jar </packaging>
<version> 1.0-SNAPSHOT </version>//Gerará o jar dessa versão

//Os comandos maven devem ser executados na raiz do projeto

//-----------------------------------------------------------------------------------------------
//Aula 02

//Para utilizar o Maven com o eclipse é simples, basta ir em import, pesquisar maven e "existing maven projects"
//Em seguida selecione o pom.xml respectivo

//Vamos pesquisar uma dependencia no mvnrepository, pegaremos o xstream e hibernate e colocaremos a dependencia no pom.xml
//No momento que salva ele já baixa todas as dependencias necessárias.
//Se excluir a dependencia, acontece o mesmo, em instantes limpa todas os arquivos desnecessários.

/*Uma das coisas interessantes de se utilizar o Maven, é que existe integração com as IDEs mais conhecidas no mercado.
Podemos facilmente abrir o nosso projeto Maven no Eclipse, Netbeans, Intellij IDEA, e continuar executando os comandos
pelo terminal.

Isso facilita, dentre outras coisas que os membros de uma equipe de desenvolvimento possam utilizar IDEs diferentes,
pois não estamos trabalhando com um projeto Java do Eclipse ou Netbeans. Estamos abrindo um projeto Maven na IDE.*/

//--------------------------------------------------------------------------------------------------------
//Aula 03

//O Maven possui um repositorio remoto e um local, a medida que você precisa de determinada dependencia, ele baixa
//para o local e nele se encontrará todos os pacotes que já utilizou.

//Para executar um comando sem ir ao repositorio remoto, utiliza-se -o, exemplo:
mvn -o test
mvn -o compile

//O repositorio remoto é a "central" do maven, onde existe todas as dependencias possíveis para um build
//É possível utilizar um outro repositório remoto com a tag <repositories>
//O repositorio local é compartilhado por todos os projetos e é preenchido gradativamente de acordo com a necessidade.

//------------------------------------------------------------------------------------------------------------
//Aula 04

//As fases de um build no maven são:

/*1 - Validação - verifica se o projeto possui todas as informações necessárias
* 2 - Compilação - Compila todo o source
* 3 - Teste - executa os testes do projeto
* 4 - Pacote - Gera o pacote do projeto (Ex: .jar)
* 5 - Teste de Integração - Realiza os testes de integração
* 6 - Verificação - Checa o pacote gerado. Pode-se criar regras de qualidade para essa etapa
* 7 - Instalação - Realiza a instalação do pacote no repositório local
* 8 - Implantação - Realiza a implantação no ambiente adequado.
*/

//Quando se executa uma das fases, automaticamente, o maven executa as anteriores também.
//Existem exceções via certos comandos, exemplo:
mvn -DskipTests=true package //pulará os testes


//Agora vamos atrelar um report a fase de verificação, criando um critério de qualidade ao código:
mvn pmd:pmd // Esse relatório analisa o código e detecta possíveis bugs
//Porém esse comando apenas gera o relatório, não estando incorporado no ciclo do build

mvn verify // Ao utilizar esse comando não se vê vinculo com o pmd
mvn pmd:check //Com esse comando verifica-se as inconsistências no código e interrompe o build caso exista alguma
//Os critérios/regras são customizáveis
//De acordo com a documentação o comando corresponde a fase de verify
//Para efetivamente vincular o comando com a fase verify, necessitamos:
<build>
  <plugins>
    <plugin>

      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-pmd-plugin</artifactId>
      <version>3.6</version>

      <executions>
        <execution>
          <phase>verify</phase>
          <goals>
            <goal>check</goal>
          </goals>
        </execution>
      </executions>

    </plugin>
  </plugins>
</build>

//dessa forma, vinculamos o verify com o comando pmd:check


//Ao adicionar um plugin ou dependencia no pom, é importante colocar a tag de versionamento
//porque se não a colocamos ele sempre builda com a última versão. Caso estejamos tentando
//buildar um projeto antigo, poderemos ter problemas.

//Se mantermos atualizados, temos menos problemas de segurança e falhas no plugin
//porém se não estabelecemos uma versão, fica problemático buildar projetos antigos.

//Vamos adicionar mais um report de cobertura de código por testes:
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.7.5.201505241946</version>
  <executions>
    <execution>
        <goals>
            <goal>prepare-agent</goal>//Se não colocamos a fase ele executa uma padrão do plugin
            <goal>report</goal>
        </goals>
    </execution>
</executions>
</plugin>

//No eclipse também é possível executar uma build do maven. Nas opções de execução


//------------------------------------------------------------------------------------------
//Aula 5

//Para criar um projeto maven através do eclipse, existe a opção maven project
//Acaba que faremos as mesmas configurações da linha de comando, via GUI.
//Colocamos o archetypeId: org.apache.maven.archetypes maven-archetype-webapp

//Por ser um projeto web, utilizaremos o Jetty como container:
  <plugins>
      <plugin>
          <groupId>org.eclipse.jetty</groupId>
          <artifactId>jetty-maven-plugin</artifactId>
          <version>9.3.7.v20160115</version>
      </plugin>
  </plugins>

//Executaremos o comando
mvn jetty:run //Para instalar o plugin no nosso projeto.

//Para tirar a mensagem de erro, colocaremos a servlet api 3.1:
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>3.1.0</version>
    <scope>provided</scope>
</dependency>

//Também iremos  no web.xml e colocaremos uma estrutura na versão 3.1:
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
    http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
    version="3.1">
</web-app>

//Sempre fazemos isso ao criar um projeto maven novo.

//Em src/main/resources podemos incluir arquivos properties, TXT, e assim por diante.
//Em src/main/java ficarão armazenadas as classes Java. Criaremos essa pasta.

//Agora adicionaremos o servlet no main java, só é possivel devido a dependencia no pom.xml
package br.com.alura.maven.lojaweb

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns={"/contato"})
public class ContatoServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpResponse resp) throws ServletException, IOException {
    PrintWriter writer = resp.getWriter();
    writer.println("<html><h2>Entre em contato</h2></html>");
    writer.close();
  }
}

//Para conseguirmos visualizar as mudanças temos que reinicializar nosso programa:
mvn jetty:run
//Para não termos que reinicializar frequentemente colocaremos uma configuração no jetty:
<plugin>
  <groupId>org.eclipse.jetty</groupId>
  <artifactId>jetty-maven-plugin</artifactId>
  <version>9.3.7.v2016115</version>
  <configuration>
    <scanIntervalSeconds>10</scanIntervalSeconds>
  </configuration>
</plugin>

//----------------------------------------------------------------------------------------
//Aula 06

//Iremos adicionar mais uma dependencia ao projeto e gerar o arquivo para deploy:
<dependency>
    <groupId>br.com.caelum.stella</groupId>
    <artifactId>caelum-stella-core</artifactId>
    <version>2.1.2</version>
</dependency>

//Colocando um trecho de código do stella:
new CPF("2222222222").isValido();

//Para empacotar sabemos que é o comando package, executamos via cmd/eclipse e pronto,
//teremos nosso arquivo empacotado e pronto para uso, repare que tanto o jar quanto o war
//são zips, ao deszipá-los obtemos a estrutura de pastas e arquivos do projeto.


//=============
//A tag <scope> define o escopo da dependência, são eles:

//Compile - É o escopo default - disponível em todos os classpaths de um projeto. São propagadas aos projetos dependentes.
//o pacote é considerado na compilação. Se houver empacotamento, a dependência é inclusa no pacote.

//Provided - Similar ao complile, mas espera que a JDK ou um container disponibilizará a dependencia em tempo de execução.
//O pacote é considerado na compilação, mas não é incluso no pacote se houver empacotamento.

//runtime - Indica que a dependencia não é requerida para a compilação, mas para a execução.
//Indica que a dependência não precisa estar na compilação, mas precisa estar na execução.

//test - Indica que a dependencia não é requerida no uso normal da aplicação. Só é disponivel para a compilação de testes
// e para a fase de execução. Indica que a dependência é usada apenas na fase de testes.

//system -  Esse escopo é similar ao provided exceto que deve-se prover o JAR que o contém explicitamente. O artefato
//O artefato está sempre disponível e não será procurado em um repositório. Semelhante ao provided, mas para JARs.
//Os JARs precisam ser especificados explicitamente.

//import - É somente disponível em dependencias do tipo 'pom' na seção <dependencyManagement>. Indica a substituição da
//dependencia na lista especificada na POM.
//==============

//Repare que as classes de escopo teste não aparecem no arquivo final.
//Adicionaremos um outro projeto nosso como dependência:
<dependency>
    <groupId>br.com.alura.maven</groupId>
    <artifactId>produtos</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

//dessa forma, lojaweb e produtos são interdependentes, e quando um for "buildado"
//será necessário o arquivo .jar de outro.
//Porém ao gerar o package ocorre erro de build. Ele não encontra o arquivo jar e falha
//Portanto, teremos que instalar o projeto no repositório local com
mvn install
//Assim teremos o arquivo jar no repositorio local


//Aprendemos que quando temos o projeto referenciado como dependencia no próprio project explorer
//do eclipse, ele puxa a referencia como um link interno, um apontamento inteligente e não uma
//referência ao arquivo .jar. O problema disso é que todas as classes do projeto estarão disponíveis
//para acesso, inclusive as de escopo limitado, sem acusar erro de compilação. A vantagem é que atualiza
//automáticamente sem ter que gerar outro .jar a cada pequeno ajuste.

//O maven não puxará apenas as dependencias diretas. Existe uma hierarquia/árvore de dependencias,
//ou seja, dependencias das dependencias.

//Para o servlet, não faz sentido ter o escopo compile (presença na compilação e no empacotamento)
//Nós precisamos do servlet para compilação, mas na produção o serviço fornecerá isso, portanto, sua situação
//se enquadra no escopo 'provided'.
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servelt-api</artifactId>
    <version>3.1.0</version>
    <scope>provided</scope>
</dependency> //Pois utilizaremos em compilação mas em produção será provido
//É bom limparmos com mvn clean antes de empacotar novamente, pois podem ficar sujeiras.

//O escopo contrário do provided é o 'runtime', ou seja, será dispensado na compilação, porém
//presente no pacote final. Faremos ele com o stella:
<dependency>
    <groupId>br.com.caelum.stella</groupId>
    <artifactId>caelum-stella-core</artifactId>
    <version>2.1.2</version>
    <scope>runtime</scope>
</dependency>
//O problema dessa variação no eclipse, é que como podemos precisar rodar para testar as classes. Ele deixará de acusar
//com antecedencia alguns erros de compilação, que ocorrerão na hora do build. Um bom exemplo desse escopo é o driver JDBC,
//por exemplo o mysql-connector. Esse driver não é preciso para compilar, pois as interfaces do JDBC (e o DriverManager)
//já fazem parte do JRE. No entanto, para rodar aplicação e estabelecer a conexão concreta é preciso ter o driver no classpath.


//Outra mecânica interessante do funcionamento de servlets, é a preferência explicita. Quando se tem uma mesma dependência
//se repetindo ao longo da árvor, a ocorrência feita diretamente (explicita) será a definitiva. Portanto se tivermos
//a declaração em versões diferentes, podemos ter problemas com a discrepância. Por exemplo, o xtream do projeto
//principal ser o 1.4.1 e o sobrescrito ser o 1.4.8, ao tentar rodar um recurso da versão recente, pode ocorrer problemas.

//Finalmente, podemos também excluir algumas subdependencias em uma declaração. Dispensando alguns arquivos:
<dependency>
    <groupId>com.thoughtworks.xstream</groupId>
    <artifactId>xstream</artifactId>
    <version>1.4.1</version>
        <exclusions>
            <exclusion>
                <groupId>xmlpull</groupId>
                <artifactId>xmlpull</artifactId>
            </exclusion>
        </exclusions>
</dependency>
//Como a xstream possui dois arquivos filhos que são colocados no projeto, podemos excluir um deles com esse comando.
