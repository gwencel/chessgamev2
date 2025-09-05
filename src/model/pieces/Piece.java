package model.pieces;

import java.util.List;
import model.board.Board;
import model.board.Position;

public abstract class Piece {
    protected Position position;
    protected boolean isWhite;
    protected Board board;

    // ✅ Nova flag para controle de roque e movimentos
    protected boolean hasMoved = false;

    public Piece(Board board, boolean isWhite) {
        this.board = board;
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    // ✅ Controle de movimento
    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    // Método abstrato que será implementado por cada tipo de peça
    public abstract List<Position> getPossibleMoves();

    // Verifica se a peça pode se mover para a posição especificada
    public boolean canMoveTo(Position position) {
        List<Position> possibleMoves = getPossibleMoves();
        return possibleMoves.contains(position);
    }

    // Retorna o nome abreviado da peça (k para rei, q para rainha, etc.)
    public abstract String getSymbol();

    public abstract String getName();
}

