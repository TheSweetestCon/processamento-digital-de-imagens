# Trabalho Final de Processamento Digital de Imagens

Este projeto é uma aplicação Java com interface gráfica que utiliza a biblioteca [JFreeChart](https://sourceforge.net/projects/jfreechart/) para exibir histogramas de imagens, como parte do trabalho final da disciplina de PDI.

---

## Pré-requisitos

- Java JDK 17 ou superior instalado (Java 21 ou 24 recomendado)
- PowerShell (já vem instalado no Windows)
- JARs do JFreeChart e JCommon na pasta `lib`

---

## Como compilar e executar

### Etapa única via `.bat`:

1. Clique duas vezes no arquivo `App.bat`.
2. A janela do PowerShell será aberta e o projeto será compilado e executado automaticamente.

> Se a execução do script PowerShell for bloqueada, continue lendo abaixo.

Se ao tentar rodar o .ps1 você receber uma mensagem de erro dizendo que a execução de scripts está desabilitada, rode o seguinte comando no PowerShell como usuário normal (não precisa ser admin):
    > Set-ExecutionPolicy RemoteSigned -Scope CurrentUser

---

### Alternativa manual via PowerShell

1. Abra o PowerShell na pasta do projeto.
2. Execute o script:
    > .\execute.ps1