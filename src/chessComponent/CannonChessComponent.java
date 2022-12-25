package chessComponent;
import controller.ClickController;
import model.ChessColor;
import model.ChessboardPoint;

import java.awt.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class CannonChessComponent extends ChessComponent{
    public CannonChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size, String imagePath1, String imagePath2) {
        super(chessboardPoint, location, chessColor, clickController, size, imagePath1, imagePath2);
        if (this.getChessColor() == ChessColor.RED) {
            name = "炮";
        } else {
            name = "砲";
        }
    }
    private final int chessGrade = 6;

    public int getChessGrade() {
        return chessGrade;
    }
    // todo: 重写canMoveTo (炮单独的方法，在子类中写) 20221128 2119
    public boolean canMoveTo(SquareComponent[][] chessboard, ChessboardPoint destination ,ChessboardPoint first) {
        SquareComponent destinationChess = chessboard[destination.getX()][destination.getY()];
        if(destinationChess instanceof EmptySlotComponent){
            return false;
        }
        if(destination.getY() == first.getY() && destination.getX() - first.getX() >= 2){ // 向下 20221203 1732
            int counter = 0;
            for (int x = first.getX() + 1;x <= destination.getX();x++){
                if(! (chessboard[x][first.getY()] instanceof EmptySlotComponent)){
                    counter++;
                }
            }
            if(counter == 2){
                return true;
            }
        }
        if(destination.getY() == first.getY() && destination.getX() - first.getX() <= -2){ // 向上 20221203 1732
            int counter = 0;
            for (int x = first.getX() - 1;x >= destination.getX();x--){
                if(! (chessboard[x][first.getY()] instanceof EmptySlotComponent)){
                    counter++;
                }
            }
            if(counter == 2){
                return true;
            }
        }
        if(destination.getX() == first.getX() && destination.getY() - first.getY() >= 2){ // 向右 20221203 1735
            int counter = 0;
            for (int y = first.getY() + 1;y <= destination.getY();y++){
                if(! (chessboard[first.getX()][y] instanceof EmptySlotComponent)){
                    counter++;
                }
            }
            if(counter == 2){
                return true;
            }
        }
        if(destination.getX() == first.getX() && destination.getY() - first.getY() <= -2){ // 向左 20221203 1732
            int counter = 0;
            for (int y = first.getY() -1;y >= destination.getY();y--){
                if(! (chessboard[first.getX()][y] instanceof EmptySlotComponent)){
                    counter++;
                }
            }
            if(counter == 2){
                return true;
            }
        }
        return false;
    }
}
//        (destination.getX() == first.getX() && destination.getY() - first.getY() >= 2) ||
//        (destination.getX() == first.getX() && destination.getY() - first.getY() <= -2))