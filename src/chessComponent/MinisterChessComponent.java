package chessComponent;
import controller.ClickController;
import model.ChessColor;
import model.ChessboardPoint;

import java.awt.*;

public class MinisterChessComponent extends ChessComponent{
    public MinisterChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size, String imagePath1, String imagePath2) {
        super(chessboardPoint, location, chessColor, clickController, size, imagePath1, imagePath2);
        if (this.getChessColor() == ChessColor.RED) {
            name = "相";
        } else {
            name = "象";
        }
    }
    private final int chessGrade = 3;

    public int getChessGrade() {
        return chessGrade;
    }
}
