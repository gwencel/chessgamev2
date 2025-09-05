# Jogo de Xadrez em Java

Este é um jogo de xadrez simples implementado em Java, utilizando Swing para a interface gráfica.

## Descrição

Este projeto implementa um jogo de xadrez totalmente funcional com uma interface gráfica. Inclui recursos essenciais como o layout da grade, movimentação das peças e lógica do jogo.

## Funcionalidades

- Jogue xadrez contra um computador ou outro jogador.
- Interface interativa para a grade e as peças.
- Regras básicas do xadrez implementadas.

## Tecnologias Usadas

- Java
- GridLayout (para o layout do tabuleiro)
- Java Swing (para a interface gráfica)

## Como Executar

1. Certifique-se de que o [Java Development Kit (JDK) 17 ou superior](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) esteja instalado em sua máquina.
2. Baixe ou clone o repositório para o seu computador.
3. Navegue até a pasta do projeto (`ChessGame`).
4. Compile os arquivos Java:
   ```bash
   javac -d out src/controller/*.java src/model/board/*.java src/model/pieces/*.java src/view/*.java
   ```
5. Execute o jogo:
   ```bash
   java -cp out view.ChessGUI
   ```

## Modificações Recentes

- **Histórico de Movimentos Aprimorado**: O histórico de movimentos agora exibe a cor da peça (Branca/Preta) e o nome completo da peça (ex: Peão, Torre, Rei) em vez de apenas o símbolo.
- **Correção de Roque**: A lógica para o movimento de roque foi revisada e corrigida para garantir seu funcionamento adequado.

## Autor

Gabriel Wencel

