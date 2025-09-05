package model.pieces;

import java.util.ArrayList;
import java.util.List;
import model.board.Board;
import model.board.Position;

public class King extends Piece {
    public King(Board board, boolean isWhite) {
        super(board, isWhite);
    }

    @Override
    public List<Position> getPossibleMoves() {
        List<Position> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();

        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},          {0, 1},
            {1, -1},  {1, 0}, {1, 1}
        };

        for (int[] d : directions) {
            int newRow = row + d[0];
            int newCol = col + d[1];
            Position pos = new Position(newRow, newCol);

            if (pos.isValid()) {
                Piece p = board.getPieceAt(pos);
                if (p == null || p.isWhite() != isWhite) {
                    moves.add(pos);
                }
            }
        }

        // Adicionar movimentos de roque
        if (!hasMoved) {
            // Roque pequeno (lado do rei)
            if (board.getPieceAt(new Position(row, col + 1)) == null &&
                board.getPieceAt(new Position(row, col + 2)) == null) {
                Piece rook = board.getPieceAt(new Position(row, 7));
                if (rook instanceof Rook && !rook.hasMoved()) {
                    moves.add(new Position(row, col + 2));
                }
            }

            // Roque grande (lado da rainha)
            if (board.getPieceAt(new Position(row, col - 1)) == null &&
                board.getPieceAt(new Position(row, col - 2)) == null &&
                board.getPieceAt(new Position(row, col - 3)) == null) {
                Piece rook = board.getPieceAt(new Position(row, 0));
                if (rook instanceof Rook && !rook.hasMoved()) {
                    moves.add(new Position(row, col - 2));
                }
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return "k";
    }

    @Override
    public String getName() {
        return "Rei";
    }
}

