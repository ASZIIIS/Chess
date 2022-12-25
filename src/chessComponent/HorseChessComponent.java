package chessComponent;
import controller.ClickController;
import model.ChessColor;
import model.ChessboardPoint;

import java.awt.*;
public class HorseChessComponent extends ChessComponent{
    public HorseChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size, String imagePath1, String imagePath2) {
        super(chessboardPoint, location, chessColor, clickController, size, imagePath1, imagePath2);
        if (this.getChessColor() == ChessColor.RED) {
            name = "傌";
        } else {
            name = "馬";
        }
    }
    private final int chessGrade = 1;

    public int getChessGrade() {
        return chessGrade;
    }
}
