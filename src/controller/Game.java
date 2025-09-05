package controller;

import javax.swing.JOptionPane;
import model.board.Board;
import model.board.Position;
import model.pieces.*;

public class Game {
    private Board board;
    private boolean isWhiteTurn;
    private boolean isGameOver;
    private Piece selectedPiece;

    public Game() {
        board = new Board();
        isWhiteTurn = true;
        isGameOver = false;
        setupPieces();
    }

    private void setupPieces() {
        // Peças brancas
        board.placePiece(new Rook(board, true), new Position(7, 0));
        board.placePiece(new Knight(board, true), new Position(7, 1));
        board.placePiece(new Bishop(board, true), new Position(7, 2));
        board.placePiece(new Queen(board, true), new Position(7, 3));
        board.placePiece(new King(board, true), new Position(7, 4));
        board.placePiece(new Bishop(board, true), new Position(7, 5));
        board.placePiece(new Knight(board, true), new Position(7, 6));
        board.placePiece(new Rook(board, true), new Position(7, 7));
        for (int col = 0; col < 8; col++) {
            board.placePiece(new Pawn(board, true), new Position(6, col));
        }

        // Peças pretas
        board.placePiece(new Rook(board, false), new Position(0, 0));
        board.placePiece(new Knight(board, false), new Position(0, 1));
        board.placePiece(new Bishop(board, false), new Position(0, 2));
        board.placePiece(new Queen(board, false), new Position(0, 3));
        board.placePiece(new King(board, false), new Position(0, 4));
        board.placePiece(new Bishop(board, false), new Position(0, 5));
        board.placePiece(new Knight(board, false), new Position(0, 6));
        board.placePiece(new Rook(board, false), new Position(0, 7));
        for (int col = 0; col < 8; col++) {
            board.placePiece(new Pawn(board, false), new Position(1, col));
        }
    }

    public Board getBoard() {
        return board;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void selectPiece(Position position) {
        Piece piece = board.getPieceAt(position);
        if (piece != null && piece.isWhite() == isWhiteTurn) {
            selectedPiece = piece;
        }
    }

    public void clearSelection() {
        selectedPiece = null;
    }

    public boolean movePiece(Position destination) {
        if (selectedPiece == null || isGameOver) {
            return false;
        }

        if (!selectedPiece.canMoveTo(destination)) {
            return false;
        }

        if (moveCausesCheck(selectedPiece, destination)) {
            return false;
        }

        Position originalPosition = selectedPiece.getPosition();
        Piece capturedPiece = board.getPieceAt(destination);

        // Lógica para o roque
        if (selectedPiece instanceof King && Math.abs(originalPosition.getColumn() - destination.getColumn()) == 2) {
            // É um roque
            int rookOriginalCol;
            int rookDestinationCol;
            if (destination.getColumn() > originalPosition.getColumn()) { // Roque pequeno
                rookOriginalCol = 7;
                rookDestinationCol = originalPosition.getColumn() + 1;
            } else { // Roque grande
                rookOriginalCol = 0;
                rookDestinationCol = originalPosition.getColumn() - 1;
            }
            Piece rook = board.getPieceAt(new Position(originalPosition.getRow(), rookOriginalCol));
            board.removePiece(new Position(originalPosition.getRow(), rookOriginalCol));
            board.placePiece(rook, new Position(originalPosition.getRow(), rookDestinationCol));
            rook.setHasMoved(true);
        }

        board.removePiece(originalPosition);
        board.placePiece(selectedPiece, destination);
        selectedPiece.setHasMoved(true);

        checkSpecialConditions(selectedPiece, destination);

        // ✅ Primeiro troca o turno
        isWhiteTurn = !isWhiteTurn;

        // ✅ Depois verifica se o adversário ficou em xeque, xeque-mate ou empate
        checkGameStatus();

        selectedPiece = null;
        return true;
    }

    /**
     * Simula mover uma peça e verifica se deixa o próprio rei em xeque
     */
    private boolean moveCausesCheck(Piece piece, Position destination) {
        Position from = piece.getPosition();
        Piece captured = board.getPieceAt(destination);

        boolean originalHasMoved = piece.hasMoved();
        boolean capturedOriginalHasMoved = (captured != null) ? captured.hasMoved() : false;

        board.removePiece(from);
        if (captured != null) board.removePiece(destination);
        board.placePiece(piece, destination);

        boolean inCheck;
        try {
            Position myKing = findKingPosition(piece.isWhite());
            if (myKing == null) {
                return true;
            }
            inCheck = isSquareAttacked(myKing, !piece.isWhite());
        } finally {
            board.removePiece(destination);
            board.placePiece(piece, from);
            piece.setHasMoved(originalHasMoved);
            if (captured != null) board.placePiece(captured, destination);
            if (captured != null) captured.setHasMoved(capturedOriginalHasMoved);
        }

        return inCheck;
    }

    private Position findKingPosition(boolean whiteKing) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Position p = new Position(r, c);
                Piece piece = board.getPieceAt(p);
                if (piece instanceof King && piece.isWhite() == whiteKing) {
                    return p;
                }
            }
        }
        return null;
    }

    private boolean isSquareAttacked(Position square, boolean byWhite) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Position from = new Position(r, c);
                Piece p = board.getPieceAt(from);
                if (p == null || p.isWhite() != byWhite) continue;
                if (from.equals(square)) continue;
                if (p.canMoveTo(square)) return true;
            }
        }
        return false;
    }

    private void checkSpecialConditions(Piece piece, Position destination) {
        // Promoção de peão
        if (piece instanceof Pawn) {
            if ((piece.isWhite() && destination.getRow() == 0) ||
                (!piece.isWhite() && destination.getRow() == 7)) {

                String[] options = {"Rainha", "Torre", "Bispo", "Cavalo"};
                int choice = JOptionPane.showOptionDialog(null,
                        "Escolha uma peça para promoção:",
                        "Promoção de Peão",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, options, options[0]);

                Piece newPiece;
                switch (choice) {
                    case 1 -> newPiece = new Rook(board, piece.isWhite());
                    case 2 -> newPiece = new Bishop(board, piece.isWhite());
                    case 3 -> newPiece = new Knight(board, piece.isWhite());
                    default -> newPiece = new Queen(board, piece.isWhite());
                }

                board.removePiece(destination);
                board.placePiece(newPiece, destination);
            }
        }
    }

    private void checkGameStatus() {
        Position kingPos = findKingPosition(isWhiteTurn);
        if (kingPos == null) return;

        boolean inCheck = isSquareAttacked(kingPos, !isWhiteTurn);

        boolean hasMove = false;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPieceAt(new Position(r, c));
                if (p != null && p.isWhite() == isWhiteTurn) {
                    for (Position move : p.getPossibleMoves()) {
                        if (!moveCausesCheck(p, move)) {
                            hasMove = true;
                            break;
                        }
                    }
                }
            }
        }

        if (inCheck && !hasMove) {
            isGameOver = true;
            JOptionPane.showMessageDialog(null,
                    (isWhiteTurn ? "Brancas" : "Pretas") + " estão em XEQUE-MATE!");
        } else if (!inCheck && !hasMove) {
            isGameOver = true;
            JOptionPane.showMessageDialog(null, "EMPATE por afogamento!");
        } else if (inCheck) {
            JOptionPane.showMessageDialog(null,
                    (isWhiteTurn ? "Brancas" : "Pretas") + " estão em XEQUE!");
        }
    }
}
