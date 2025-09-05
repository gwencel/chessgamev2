package model.pieces;

import model.board.Board;
import model.board.Position;
import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen(Board board, boolean isWhite) {
        super(board, isWhite);
    }

    @Override
    public List<Position> getPossibleMoves() {
        List<Position> moves = new ArrayList<>();
        if (position == null) return moves;

        // Oito direções: horizontais, verticais e diagonais
        int[][] directions = {
                {-1, 0}, // cima
                {1, 0},  // baixo
                {0, -1}, // esquerda
                {0, 1},  // direita
                {-1, -1}, // cima-esquerda
                {-1, 1},  // cima-direita
                {1, -1},  // baixo-esquerda
                {1, 1}    // baixo-direita
        };

        for (int[] dir : directions) {
            int row = position.getRow();
            int col = position.getColumn();

            while (true) {
                row += dir[0];
                col += dir[1];
                Position newPos = new Position(row, col);

                if (!newPos.isValid()) break;

                Piece target = board.getPieceAt(newPos);
                if (target == null) {
                    moves.add(newPos);
                } else {
                    if (target.isWhite() != this.isWhite()) {
                        moves.add(newPos); // captura
                    }
                    break; // para no bloqueio
                }
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return "q";
    }

    @Override
    public String getName() {
        return "Rainha";
    }
}

