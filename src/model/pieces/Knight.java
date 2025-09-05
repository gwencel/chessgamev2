package model.pieces;

import model.board.Board;
import model.board.Position;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(Board board, boolean isWhite) {
        super(board, isWhite);
    }

    @Override
    public List<Position> getPossibleMoves() {
        List<Position> moves = new ArrayList<>();
        if (position == null) return moves;

        // 8 movimentos possíveis em L
        int[][] moveset = {
                {-2, -1}, {-2, 1}, // cima
                {-1, -2}, {-1, 2}, // esquerda/direita
                {1, -2}, {1, 2},   // esquerda/direita
                {2, -1}, {2, 1}    // baixo
        };

        for (int[] move : moveset) {
            int row = position.getRow() + move[0];
            int col = position.getColumn() + move[1];
            Position newPos = new Position(row, col);

            if (newPos.isValid()) {
                Piece target = board.getPieceAt(newPos);
                if (target == null || target.isWhite() != this.isWhite()) {
                    moves.add(newPos);
                }
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return "n"; // N para Knight
    }

    @Override
    public String getName() {
        return "Cavalo";
    }
}

