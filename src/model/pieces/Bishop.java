package model.pieces;

import model.board.Board;
import model.board.Position;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(Board board, boolean isWhite) {
        super(board, isWhite);
    }

    @Override
    public List<Position> getPossibleMoves() {
        List<Position> moves = new ArrayList<>();
        if (position == null) return moves;

        // Quatro direções diagonais
        int[][] directions = {
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
                        moves.add(newPos); 
                    }
                    break; 
                }
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return "b";
    }

    @Override
    public String getName() {
        return "Bispo";
    }
}

