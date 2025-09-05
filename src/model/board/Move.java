package model.board;

import model.pieces.Piece;
import java.io.Serializable;

public class Move implements Serializable {
    private static final long serialVersionUID = 1L;

    private Position from;
    private Position to;
    private Piece piece;
    private Piece capturedPiece;
    private boolean isPromotion;
    private boolean isCastling;
    private boolean isEnPassant;

    public Move(Position from, Position to, Piece piece, Piece capturedPiece) {
        this.from = from;
        this.to = to;
        this.piece = piece;
        this.capturedPiece = capturedPiece;
        this.isPromotion = false;
        this.isCastling = false;
        this.isEnPassant = false;
    }

    // Getters
    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Piece getPiece() {
        return piece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    public boolean isCastling() {
        return isCastling;
    }

    public boolean isEnPassant() {
        return isEnPassant;
    }

    // Setters
    public void setPromotion(boolean promotion) {
        this.isPromotion = promotion;
    }

    public void setCastling(boolean castling) {
        this.isCastling = castling;
    }

    public void setEnPassant(boolean enPassant) {
        this.isEnPassant = enPassant;
    }

    @Override
    public String toString() {
        String symbol = (piece != null) ? piece.getSymbol() : "?";
        return symbol + from.toString() + "-" + to.toString();
    }
}
