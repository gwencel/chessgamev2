package view;

import controller.Game;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import model.board.Position;
import model.pieces.Piece;

public class ChessGUI extends JFrame {
    private Game game;
    private JPanel boardPanel;
    private JButton[][] squares;
    private Map<String, ImageIcon> pieceIcons;

    // Novos componentes
    private JTextArea moveHistory;
    private JLabel turnLabel;

    public ChessGUI() {
        game = new Game();
        initializeGUI();
        loadPieceIcons();
        updateBoardDisplay();
    }

    private void initializeGUI() {
        setTitle("Jogo de Xadrez em Java");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 630); // aumentei para caber o painel lateral
        setLayout(new BorderLayout());

        // Painel superior com informações do turno
        turnLabel = new JLabel("Vez das Brancas", SwingConstants.CENTER);
        turnLabel.setPreferredSize(new Dimension(600, 30));
        add(turnLabel, BorderLayout.NORTH);

        // Painel do tabuleiro
        boardPanel = new JPanel(new GridLayout(8, 8));
        add(boardPanel, BorderLayout.CENTER);

        // Criar as casas do tabuleiro
        squares = new JButton[8][8];
        boolean isWhite = true;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = new JButton();
                squares[row][col].setPreferredSize(new Dimension(70, 70));
                squares[row][col].setBackground(isWhite ? Color.WHITE : Color.GRAY);

                final int r = row;
                final int c = col;
                squares[row][col].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleSquareClick(r, c);
                    }
                });

                boardPanel.add(squares[row][col]);
                isWhite = !isWhite;
            }
            isWhite = !isWhite;
        }

        // Painel lateral para histórico e botão reiniciar
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(200, 600));

        moveHistory = new JTextArea();
        moveHistory.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moveHistory);
        sidePanel.add(scrollPane, BorderLayout.CENTER);

        JButton restartButton = new JButton("Reiniciar Jogo");
        restartButton.addActionListener(e -> restartGame());
        sidePanel.add(restartButton, BorderLayout.SOUTH);

        add(sidePanel, BorderLayout.EAST);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadPieceIcons() {
        pieceIcons = new HashMap<>();

        // símbolos das peças no getSymbol(): k, q, r, b, n, p
        String[] symbols = {"k", "q", "r", "b", "n", "p"};
        String[] colors = {"w", "b"}; // w = branco, b = preto

        for (String color : colors) {
            for (String sym : symbols) {
                String key = color + sym;
                String path = "resources/" + key + ".png";

                ImageIcon icon = new ImageIcon(path);
                if (icon.getIconWidth() <= 0) {
                    System.out.println("⚠️ Não encontrei: " + path);
                } else {
                    // redimensiona para caber no botão
                    Image scaled = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(scaled);
                }

                pieceIcons.put(key, icon);
            }
        }
    }

    private void updateBoardDisplay() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = game.getBoard().getPieceAt(new Position(row, col));
                if (piece == null) {
                    squares[row][col].setIcon(null);
                } else {
                    String key = (piece.isWhite() ? "w" : "b") + piece.getSymbol();
                    squares[row][col].setIcon(pieceIcons.get(key));
                }
            }
        }
        // Atualizar turno
        turnLabel.setText(game.isWhiteTurn() ? "Vez das Brancas" : "Vez das Pretas");
    }

    private void handleSquareClick(int row, int col) {
        Position position = new Position(row, col);

        if (game.getSelectedPiece() == null) {
            // Primeira seleção: escolher uma peça
            game.selectPiece(position);

            // Destacar a peça selecionada
            if (game.getSelectedPiece() != null) {
                squares[row][col].setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));

                // Destacar movimentos possíveis
                for (Position pos : game.getSelectedPiece().getPossibleMoves()) {
                    squares[pos.getRow()][pos.getColumn()].setBorder(
                            BorderFactory.createLineBorder(Color.GREEN, 3));
                }
            }
        } else {
            // Segunda seleção: mover a peça ou selecionar outra
            Piece selectedPiece = game.getSelectedPiece();

            // Limpar todos os destaques
            clearHighlights();

            if (selectedPiece.getPosition().equals(position)) {
                // Clicou na mesma peça, deselecionar
                game.selectPiece(null);
            } else if (game.getBoard().getPieceAt(position) != null &&
                    game.getBoard().getPieceAt(position).isWhite() == game.isWhiteTurn()) {
                // Clicou em outra peça do mesmo lado, selecionar essa nova peça
                game.selectPiece(position);
                squares[row][col].setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));

                // Destacar movimentos possíveis da nova peça
                for (Position pos : game.getSelectedPiece().getPossibleMoves()) {
                    squares[pos.getRow()][pos.getColumn()].setBorder(
                            BorderFactory.createLineBorder(Color.GREEN, 3));
                }
            } else {
                // Tentar mover a peça
                boolean moveSuccessful = game.movePiece(position);
                if (moveSuccessful) {
                    updateBoardDisplay();

                    // Adicionar ao histórico
                    String pieceColor = selectedPiece.isWhite() ? "Branca" : "Preta";
                    moveHistory.append(pieceColor + " " + selectedPiece.getName() + " -> " + position + "\n");

                    // Verificar fim de jogo
                    if (game.isGameOver()) {
                        String winner = game.isWhiteTurn() ? "Pretas" : "Brancas";
                        JOptionPane.showMessageDialog(this,
                                winner + " vencem! Jogo terminado.");
                    }
                }
            }
        }
    }

    private void clearHighlights() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                squares[r][c].setBorder(null);
            }
        }
    }

    private void restartGame() {
        game = new Game();
        moveHistory.setText("");
        updateBoardDisplay();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessGUI::new);
    }
}