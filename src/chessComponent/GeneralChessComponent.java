package chessComponent;
import controller.ClickController;
import model.ChessColor;
import model.ChessboardPoint;

import java.awt.*;
public class GeneralChessComponent extends ChessComponent{
    public GeneralChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size, String imagePath1, String imagePath2) {
        super(chessboardPoint, location, chessColor, clickController, size, imagePath1, imagePath2);
        if (this.getChessColor() == ChessColor.RED) {
            name = "帥";
        } else {
            name = "將";
        }
    }
    private final int chessGrade = 5;

    public int getChessGrade() {
        return chessGrade;
    }
}
