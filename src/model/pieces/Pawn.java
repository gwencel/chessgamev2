package model.pieces;

import java.util.ArrayList;
import java.util.List;
import model.board.Board;
import model.board.Position;

public class Pawn extends Piece {

    public Pawn(Board board, boolean isWhite) {
        super(board, isWhite);
    }

    @Override
    public List<Position> getPossibleMoves() {
        List<Position> moves = new ArrayList<>();
        int direction = isWhite ? -1 : 1; // Branco sobe (-1), Preto desce (+1)

        // Movimento simples para frente
        Position oneStep = new Position(position.getRow() + direction, position.getColumn());
        if (oneStep.isValid() && board.isPositionEmpty(oneStep)) {
            moves.add(oneStep);

            // Movimento duplo (apenas se ainda na posição inicial)
            boolean startingRow = (isWhite && position.getRow() == 6) || (!isWhite && position.getRow() == 1);
            if (startingRow) {
                Position twoSteps = new Position(position.getRow() + 2 * direction, position.getColumn());
                if (twoSteps.isValid() && board.isPositionEmpty(twoSteps)) {
                    moves.add(twoSteps);
                }
            }
        }

        // Capturas nas diagonais
        int[] diagCols = {position.getColumn() - 1, position.getColumn() + 1};
        for (int col : diagCols) {
            Position diag = new Position(position.getRow() + direction, col);
            if (diag.isValid()) {
                Piece target = board.getPieceAt(diag);
                if (target != null && target.isWhite() != this.isWhite()) {
                    moves.add(diag);
                }
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return "p"; // usado para carregar resources/w_p.png e resources/b_p.png
    }

    @Override
    public String getName() {
        return "Peão";
    }
}

