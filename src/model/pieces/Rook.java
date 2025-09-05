package model.pieces;

import java.util.ArrayList;
import java.util.List;
import model.board.Board;
import model.board.Position;

public class Rook extends Piece {
    public Rook(Board board, boolean isWhite) {
        super(board, isWhite);
    }

    @Override
    public List<Position> getPossibleMoves() {
        List<Position> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();

        // Para cima
        for (int r = row - 1; r >= 0; r--) {
            Position pos = new Position(r, col);
            if (board.getPieceAt(pos) == null) {
                moves.add(pos);
            } else {
                if (board.getPieceAt(pos).isWhite() != isWhite) {
                    moves.add(pos);
                }
                break;
            }
        }

        // Para baixo
        for (int r = row + 1; r < 8; r++) {
            Position pos = new Position(r, col);
            if (board.getPieceAt(pos) == null) {
                moves.add(pos);
            } else {
                if (board.getPieceAt(pos).isWhite() != isWhite) {
                    moves.add(pos);
                }
                break;
            }
        }

        // Para esquerda
        for (int c = col - 1; c >= 0; c--) {
            Position pos = new Position(row, c);
            if (board.getPieceAt(pos) == null) {
                moves.add(pos);
            } else {
                if (board.getPieceAt(pos).isWhite() != isWhite) {
                    moves.add(pos);
                }
                break;
            }
        }

        // Para direita
        for (int c = col + 1; c < 8; c++) {
            Position pos = new Position(row, c);
            if (board.getPieceAt(pos) == null) {
                moves.add(pos);
            } else {
                if (board.getPieceAt(pos).isWhite() != isWhite) {
                    moves.add(pos);
                }
                break;
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return "r";
    }

    @Override
    public String getName() {
        return "Torre";
    }
}

