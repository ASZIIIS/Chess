package chessComponent;

import controller.ClickController;
import model.ChessColor;
import model.ChessboardPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * 这个类是一个抽象类，主要表示8*4棋盘上每个格子的棋子情况。
 * 有两个子类：
 * 1. EmptySlotComponent: 空棋子
 * 2. ChessComponent: 表示非空棋子
 */
public abstract class SquareComponent extends JComponent { // 代表棋盘上每一个棋子的状态 20221128 2111
    // 这是一个抽象类，JComponent 在javax.swing中 20221125 1442
    private static final Color squareColor = new Color(255, 100, 100); // 棋盘底板颜色 20221124 2335
    protected static int spacingLength;
    protected static final Font CHESS_FONT = new Font("隶书", Font.BOLD, 36); // 棋子显示字体格式 20221124 2341
//  protected static final Font CHESS_FONT = new Font("Rockwell", Font.BOLD, 36);
    protected int chessGrade = -1;

    /**
     * chessboardPoint: 表示8*4棋盘中，当前棋子在棋格对应的位置，如(0, 0), (1, 0)等等
     * chessColor: 表示这个棋子的颜色，有红色，黑色，无色三种
     * isReversal: 表示是否翻转
     * selected: 表示这个棋子是否被选中
     */
    private ChessboardPoint chessboardPoint;
    protected final ChessColor chessColor;
    protected boolean isReversal;
    private boolean selected;

    /**
     * handle click event
     */
    private final ClickController clickController;

    protected SquareComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK); // 接受鼠标（点击）事件 20221128 2111
        setLocation(location);
        setSize(size, size);
        this.chessboardPoint = chessboardPoint;
        this.chessColor = chessColor;
        this.selected = false;
        this.clickController = clickController;
        this.isReversal = false;
    }

    public boolean isReversal() {
        return isReversal;
    }

    public void setReversal(boolean reversal) {
        isReversal = reversal;
    }

    public static void setSpacingLength(int spacingLength) {
        SquareComponent.spacingLength = spacingLength;
    }

    public ChessboardPoint getChessboardPoint() {
        return chessboardPoint;
    }

    public void setChessboardPoint(ChessboardPoint chessboardPoint) {
        this.chessboardPoint = chessboardPoint;
    }

    public ChessColor getChessColor() {
        return chessColor;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @param another 主要用于和另外一个棋子交换位置
     *                <br>
     *                调用时机是在移动棋子的时候，将操控的棋子和对应的空位置棋子(EmptySlotComponent)做交换
     */
    public void swapLocation(SquareComponent another) {
        ChessboardPoint chessboardPoint1 = getChessboardPoint(), chessboardPoint2 = another.getChessboardPoint();
        Point point1 = getLocation(), point2 = another.getLocation();
        setChessboardPoint(chessboardPoint2);
        setLocation(point2);
        another.setChessboardPoint(chessboardPoint1);
        another.setLocation(point1);
    }

    /**
     * @param e 响应鼠标监听事件
     *          <br>
     *          当接收到鼠标动作的时候，这个方法就会自动被调用，调用监听者的onClick方法，处理棋子的选中，移动等等行为。
     */
    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        if (e.getID() == MouseEvent.MOUSE_PRESSED) { // 点击事件 20221128 2112
            System.out.printf("Click [%d,%d]\n", chessboardPoint.getX(), chessboardPoint.getY()); // 用于提示点击位置 20221128 2113
            clickController.onClick(this);
        }
    }

    /**
     * @param chessboard  棋盘
     * @param destination 目标位置，如(0, 0), (0, 1)等等
     * @return this棋子对象的移动规则和当前位置(chessboardPoint)能否到达目标位置
     * <br>
     * 这个方法主要是检查移动的合法性，如果合法就返回true，反之是false。
     */
    //todo: Override this method for Cannon
    public boolean canMoveTo(SquareComponent[][] chessboard, ChessboardPoint destination ,ChessboardPoint first) {
        SquareComponent destinationChess = chessboard[destination.getX()][destination.getY()];
        return (destinationChess.isReversal|| destinationChess instanceof EmptySlotComponent) &&
                ((destination.getY() == first.getY() && destination.getX() == first.getX() - 1) || // 向上 20221203 1003
                        (destination .getY() == first.getY() && destination.getX() == first.getX() + 1) || // 向下 20221203 1004
                        (destination .getX() == first.getX() && destination.getY() == first.getY() - 1) || // 向左 20221203 1004
                        (destination .getX() == first.getX() && destination.getY() == first.getY() + 1)); // 向右 20221203 1005
        // 空棋子不能动 20221128 2114
        //todo: complete this method
    }
//    public boolean canMoveTo(SquareComponent[][] chessboard, ChessboardPoint destination) {
//        SquareComponent destinationChess = chessboard[destination.getX()][destination.getY()];
//        return destinationChess.isReversal|| destinationChess instanceof EmptySlotComponent; // 空棋子不能动 20221128 2114
//    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        System.out.printf("repaint chess [%d,%d]\n", chessboardPoint.getX(), chessboardPoint.getY());
        //g.setColor(squareColor);
        //g.fillRect(1, 1, this.getWidth() - 2, this.getHeight() - 2);
    }

    public int getChessGrade() {
        return chessGrade;
    }
}
