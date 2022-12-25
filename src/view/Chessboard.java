package view;


import chessComponent.*;
import model.*;
import controller.ClickController;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 这个类表示棋盘组建，其包含：
 * SquareComponent[][]: 4*8个方块格子组件
 */
public class Chessboard extends JComponent {


    private static final int ROW_SIZE = 8;
    private static final int COL_SIZE = 4;

    private final SquareComponent[][] squareComponents = new SquareComponent[ROW_SIZE][COL_SIZE];
    private final int[][] flag = new int[ROW_SIZE][COL_SIZE]; // 记录被正常翻开的棋子的位置，用于作弊模式 20221128 2238
    //todo: you can change the initial player
    private ChessColor currentColor = ChessColor.BLACK; // 默认显示：黑方第一手 20221125 1505

    //all chessComponents in this chessboard are shared only one model controller
    public final ClickController clickController = new ClickController(this);
    private final int CHESS_SIZE;
    private int redScore = 0;
    private int blackScore = 0;
    private boolean isCheat = false;
    private boolean loadError = false;
    private final int[] number = new int[32];
    public int stepCounter = 0;
    public String gameNum;
    private boolean isPlayingBack = false;
    private ImageIcon image = new ImageIcon("resources/20201204084219498.jpg");
    public Chessboard(int width, int height) {
        setLayout(null); // Use absolute layout.
        setSize(width + 2, height);
        CHESS_SIZE = (height - 6) / 8;
        SquareComponent.setSpacingLength(CHESS_SIZE / 12);
        System.out.printf("chessboard [%d * %d], chess size = %d\n", width, height, CHESS_SIZE);
        // 生成一个0~31的随机数表 20221122 1425
        for (int counter = 0; counter < 32; counter++) {
            number[counter] = counter;
        }
        Random random = new Random();
        for (int counter = 0; counter < 32; counter++) {
            int p = random.nextInt(32);
            int tmp = number[counter];
            number[counter] = number[p];
            number[p] = tmp;
        }
        initAllChessOnBoard();
        gameNum = "000";
    }

    public int[] getNumber() {
        return number;
    }

    public SquareComponent[][] getChessComponents() {
        return squareComponents;
    }
    public int[][] getFlag() {
        return flag;
    }

    public ChessColor getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(ChessColor currentColor) {
        this.currentColor = currentColor;
    }

    public int getRedScore() {
        return redScore;
    }

    public void setRedScore(int redScore) {
        this.redScore = redScore;
    }

    public int getBlackScore() {
        return blackScore;
    }

    public void setBlackScore(int blackScore) {
        this.blackScore = blackScore;
    }

    public boolean isLoadError() {
        return loadError;
    }

    public boolean isCheat() {
        return isCheat;
    }

    public void setCheat(boolean cheat) {
        isCheat = cheat;
    }

    public boolean isPlayingBack() {
        return isPlayingBack;
    }

    public void setPlayingBack(boolean playingBack) {
        isPlayingBack = playingBack;
    }

    /**
     * 将SquareComponent 放置在 ChessBoard上。里面包含移除原有的component及放置新的component
     *
     * @param squareComponent
     */
    public void putChessOnBoard(SquareComponent squareComponent) {
        int row = squareComponent.getChessboardPoint().getX(), col = squareComponent.getChessboardPoint().getY();
        if (squareComponents[row][col] != null) {
            remove(squareComponents[row][col]);
        }
        add(squareComponents[row][col] = squareComponent);
    }

    /**
     * 交换chess1 chess2的位置
     *
     * @param chess1
     * @param chess2
     */
    public boolean swapChessComponents(SquareComponent chess1, SquareComponent chess2) {
        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
        boolean swap = false;
        if (!(chess2 instanceof EmptySlotComponent)) {
            remove(chess2);
            add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController, CHESS_SIZE));
            swap = true;
        }
        chess1.swapLocation(chess2);
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        squareComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        squareComponents[row2][col2] = chess2;
        //只重新绘制chess1 chess2，其他不变
        chess1.repaint();
        chess2.repaint();
        return swap;
    }

    //    public void swapChessComponents(SquareComponent chess1, SquareComponent chess2) {
//        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
//        if (!(chess2 instanceof EmptySlotComponent)) {
//            remove(chess2);
//            add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController, CHESS_SIZE));
//        }
//        chess1.swapLocation(chess2);
//        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
//        squareComponents[row1][col1] = chess1;
//        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
//        squareComponents[row2][col2] = chess2;
//
//        //只重新绘制chess1 chess2，其他不变
//        chess1.repaint();
//        chess2.repaint();
//    } 原方法是void返回值类型，为方便进行计分器的判断改成了boolean返回类型 20221127 0023
    public int getNoReversalNum() { // 记录背面朝上的棋子数，用于判断第一次翻开棋子是否应该切换下棋方 20221125 2328
        int counter = 0;
        for (int i = 0; i < squareComponents.length; i++) {
            for (int j = 0; j < squareComponents[i].length; j++) {
                if (!(squareComponents[i][j] instanceof EmptySlotComponent) && !(squareComponents[i][j].isReversal())) {
                    counter++;
                }
            }
        }
        return counter;
    }

    //FIXME:   Initialize chessboard for testing only. (Has been done in 20221122 1435)
    private void initAllChessOnBoard() {
        int index = 0;
        for (int i = 0; i < squareComponents.length; i++) {
            for (int j = 0; j < squareComponents[i].length; j++) {
//                ChessColor color = random.nextInt(2) == 0 ? ChessColor.RED : ChessColor.BLACK;
//                SquareComponent squareComponent;
//                if (random.nextInt(2) == 0) {
//                    squareComponent = new ChariotChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
//                } else {
//                    squareComponent = new SoldierChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
//                }
//                squareComponent.setVisible(true);
//                putChessOnBoard(squareComponent);
                ChessColor color = number[index] < 16 ? ChessColor.RED : ChessColor.BLACK;
                SquareComponent squareComponent = switch (number[index]) {
                    case 0, 16 -> // 将军，一方一个 20221122 1425
                            new GeneralChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                    case 1, 2, 17, 18 -> // 士，一方两个 20221122 1430
                            new AdvisorChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                    case 3, 4, 19, 20 -> // 相，一方两个 20221122 1430
                            new MinisterChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                    case 5, 6, 21, 22 -> // 马，一方两个 20221122 1430
                            new HorseChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                    case 7, 8, 23, 24 -> // 车，一方两个 20221122 1430
                            new ChariotChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                    case 9, 10, 25, 26 -> // 炮，一方两个 20221122 1430
                            new CannonChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                    default -> // 剩下每方5个兵 20221122 1430
                            new SoldierChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                };
                squareComponent.setVisible(true);
                putChessOnBoard(squareComponent);
                index++; // 随机放棋子 20221122 1435
            }
        }
    }

    public void setLoadError(boolean loadError) {
        this.loadError = loadError;
    }

    /**
     * 绘制棋盘格子
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //g.setColor(Color.PINK);
        //g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.drawImage(image.getImage(), 0, 0, this);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    /**
     * 将棋盘上行列坐标映射成Swing组件的Point
     *
     * @param row 棋盘上的行
     * @param col 棋盘上的列
     * @return
     */
    private Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE + 3, row * CHESS_SIZE + 3);
    }

    /**
     * 通过GameController调用该方法
     *
     * @param chessData
     */
//    public void loadGame(List<String> chessData) {
//        chessData.forEach(System.out::println);
//    }
    public void loadGame(List<String> chessData) {
        for (int counter = 8; counter < 16; counter++) {
            if (chessData.get(counter).length() != 4) {
                JOptionPane.showMessageDialog(null, "Error 102: Illegal chessboard size.");
                loadError = true;
                ChessGameFrame.getStatusLabel6().setText("Error 102");
                return;
            }
        }
        char[][] loadChessBoard = new char[8][4];
        for (int i = 8; i < 16; i++) { // 前八行（8-15）：读取棋盘上放的棋子 20221209 1639
            for (int j = 0; j < 4; j++) {
                loadChessBoard[i - 8][j] = chessData.get(i).charAt(j);
            }
        }
        if(! (chessData.get(16).charAt(0) == 'Y' || chessData.get(16).charAt(0) == 'N')){
            JOptionPane.showMessageDialog(null, "Error 102: Illegal chessboard size.");
            loadError = true;
            ChessGameFrame.getStatusLabel6().setText("Error 102");
            return;
        }
        System.out.println(Arrays.deepToString(loadChessBoard));
        char[][] reversal = new char[8][4];
        for (int i = 16; i < 24; i++) { // 往下八行（16-23）：读取棋子的翻转状态 20221209 1641
            for (int j = 0; j < 4; j++) {
                reversal[i - 16][j] = chessData.get(i).charAt(j);
            }
        }
        redScore = Integer.parseInt(String.valueOf(chessData.get(24))); // 24行：存储红方分数 20221209 1642
        blackScore = Integer.parseInt(String.valueOf(chessData.get(25))); // 25行：存储黑方分数 20221209 1642
        System.out.println(redScore);
        System.out.println(blackScore);
        initialBoardByChar(loadChessBoard);
        setChessReversal(reversal);
        ChessGameFrame.getStatusLabel2().setText(String.format("RED's SCORE :%d", redScore));
        ChessGameFrame.getStatusLabel3().setText(String.format("BLACK's SCORE :%d", blackScore));
        if(chessData.size()<27){
            JOptionPane.showMessageDialog(null, "Error 104: Lack of next player.");
            loadError = true;
            ChessGameFrame.getStatusLabel6().setText("Error 104");
        }else {
            if (chessData.get(26).equals("B")) { // 26行：存储行棋方 20221209 1647
                currentColor = ChessColor.BLACK;
                ChessGameFrame.getStatusLabel().setText("BLACK's TURN");
            } else if (chessData.get(26).equals("R")) {
                currentColor = ChessColor.RED;
                ChessGameFrame.getStatusLabel().setText("RED's TURN");
            } else {//else if(chessData.get(26)== null || !(chessData.get(26).equals("B") || chessData.get(26).equals("R"))){
                JOptionPane.showMessageDialog(null, "Error 104: Lack of next player.");
                loadError = true;
                ChessGameFrame.getStatusLabel6().setText("Error 104");
            }
        }
    }

    /*
    Red: C S
    Black: c s
     */
    public void initialBoardByChar(char[][] chars) {
        int[] counter = new int[14];
        /*
        数组用于记录已经寄掉的棋子数量，便于显示已经吃掉的棋子以及查错误 20221208 2335
        0/1：红/黑 将 1
        2/3：红/黑 士 2
        4/5：红/黑 相 2
        6/7：红/黑 车 2
        8/9：红/黑 马 2
        10/11：红/黑 兵 5
        12/13：红/黑 炮 2
         */
        counter[0] = 1;
        counter[1] = 1;
        counter[2] = 2;
        counter[3] = 2;
        counter[4] = 2;
        counter[5] = 2;
        counter[6] = 2;
        counter[7] = 2;
        counter[8] = 2;
        counter[9] = 2;
        counter[10] = 5;
        counter[11] = 5;
        counter[12] = 2;
        counter[13] = 2;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                SquareComponent squareComponent = null;
                switch (chars[i][j]) {
                    case 'C': // 红车 20221208 2125
                        squareComponent = new ChariotChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), ChessColor.RED, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                        counter[6]--;
                        break;
                    case 'c': // 黑车 20221208 2128
                        squareComponent = new ChariotChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), ChessColor.BLACK, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                        counter[7]--;
                        break;
                    case 'S': // 红兵 20221208 2128
                        squareComponent = new SoldierChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), ChessColor.RED, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                        counter[10]--;
                        break;
                    case 's': // 黑兵 20221208 2128
                        squareComponent = new SoldierChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), ChessColor.BLACK, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                        counter[11]--;
                        break;
                    case 'H': // 红马 20221208 2130
                        squareComponent = new HorseChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), ChessColor.RED, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                        counter[8]--;
                        break;
                    case 'h': // 黑马 20221208 2130
                        squareComponent = new HorseChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), ChessColor.BLACK, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                        counter[9]--;
                        break;
                    case 'M': // 红相 20221208 2133
                        squareComponent = new MinisterChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), ChessColor.RED, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                        counter[4]--;
                        break;
                    case 'm': // 黑相 20221208 2133
                        squareComponent = new MinisterChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), ChessColor.BLACK, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                        counter[5]--;
                        break;
                    case 'A': // 红士 20221208 2134
                        squareComponent = new AdvisorChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), ChessColor.RED, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                        counter[2]--;
                        break;
                    case 'a': // 黑士 20221208 2135
                        squareComponent = new AdvisorChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), ChessColor.BLACK, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                        counter[3]--;
                        break;
                    case 'P': // 红炮 20221208 2136
                        squareComponent = new CannonChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), ChessColor.RED, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                        counter[12]--;
                        break;
                    case 'p': // 黑炮 20221208 2136
                        squareComponent = new CannonChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), ChessColor.BLACK, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                        counter[13]--;
                        break;
                    case 'G': // 红将 20221208 2137
                        squareComponent = new GeneralChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), ChessColor.RED, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                        counter[0]--;
                        break;
                    case 'g': // 黑将 20221208 2137
                        squareComponent = new GeneralChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), ChessColor.BLACK, clickController, CHESS_SIZE, "resources/flower1.png", "resources/flower2.png");
                        counter[1]--;
                        break;
                    case 'O': // 空位置 20221207 2147
                        squareComponent = new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, CHESS_SIZE);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Error 103: Illegal chess has appeared.");
                        loadError = true;
                        ChessGameFrame.getStatusLabel6().setText("Error 103");
                        return;
                }
                squareComponent.setVisible(true);
                putChessOnBoard(squareComponent);
            }
        }
        repaint();
        for (int a = 0; a < 14; a++) { // 用于检查棋子数量的正确性
            if (counter[a] < 0) {
                JOptionPane.showMessageDialog(null, "Error 103: Illegal chess has appeared.");
                loadError = true;
                ChessGameFrame.getStatusLabel6().setText("Error 103");
                return;
            }
        }
        int initialRedScore = 0;
        int initialBlackScore = 0;
        int c0 = 0;
        ChessGameFrame.getStatusLabel4().setText("Red side being eaten: ");
        ChessGameFrame.getStatusLabel5().setText("Black side being eaten: ");
        while (c0 < counter[0]) { // 0号：红将 20221209 1736
            initialBlackScore += 30;
            String temp = ChessGameFrame.getStatusLabel4().getText();
            ChessGameFrame.getStatusLabel4().setText(temp + "帥");
            c0++;
        }
        int c1 = 0;
        while (c1 < counter[1]) { // 1号：黑将 20221209 1736
            initialRedScore += 30;
            String temp = ChessGameFrame.getStatusLabel5().getText();
            ChessGameFrame.getStatusLabel5().setText(temp + "將");
            c1++;
        }
        int c2 = 0;
        while (c2 < counter[2]) { // 2号：红士 20221209 1739
            initialBlackScore += 10;
            String temp = ChessGameFrame.getStatusLabel4().getText();
            ChessGameFrame.getStatusLabel4().setText(temp + "仕");
            c2++;
        }
        int c3 = 0;
        while (c3 < counter[3]) { // 3号：黑士 20221209 1740
            initialRedScore += 10;
            String temp = ChessGameFrame.getStatusLabel5().getText();
            ChessGameFrame.getStatusLabel5().setText(temp + "士");
            c3++;
        }
        int c4 = 0;
        while (c4 < counter[4]) { // 4号：红相 20221209 1742
            initialBlackScore += 5;
            String temp = ChessGameFrame.getStatusLabel4().getText();
            ChessGameFrame.getStatusLabel4().setText(temp + "相");
            c4++;
        }
        int c5 = 0;
        while (c5 < counter[5]) { // 5号：黑相 20221209 1742
            initialRedScore += 5;
            String temp = ChessGameFrame.getStatusLabel5().getText();
            ChessGameFrame.getStatusLabel5().setText(temp + "象");
            c5++;
        }
        int c6 = 0;
        while (c6 < counter[6]) { // 6号：红车 20221209 1743
            initialBlackScore += 5;
            String temp = ChessGameFrame.getStatusLabel4().getText();
            ChessGameFrame.getStatusLabel4().setText(temp + "俥");
            c6++;
        }
        int c7 = 0;
        while (c7 < counter[7]) { // 7号：黑车 20221209 1744
            initialRedScore += 5;
            String temp = ChessGameFrame.getStatusLabel5().getText();
            ChessGameFrame.getStatusLabel5().setText(temp + "車");
            c7++;
        }
        int c8 = 0;
        while (c8 < counter[8]) { // 8号：红马 20221209 1744
            initialBlackScore += 5;
            String temp = ChessGameFrame.getStatusLabel4().getText();
            ChessGameFrame.getStatusLabel4().setText(temp + "傌");
            c8++;
        }
        int c9 = 0;
        while (c9 < counter[9]) { // 9号：黑马 20221209 1750
            initialRedScore += 5;
            String temp = ChessGameFrame.getStatusLabel5().getText();
            ChessGameFrame.getStatusLabel5().setText(temp + "馬");
            c9++;
        }
        int c10 = 0;
        while (c10 < counter[10]) { // 10号：红兵 20221209 1750
            initialBlackScore += 1;
            String temp = ChessGameFrame.getStatusLabel4().getText();
            ChessGameFrame.getStatusLabel4().setText(temp + "兵");
            c10++;
        }
        int c11 = 0;
        while (c11 < counter[11]) { // 11号：黑兵 20221209 2136
            initialRedScore += 1;
            String temp = ChessGameFrame.getStatusLabel5().getText();
            ChessGameFrame.getStatusLabel5().setText(temp + "卒");
            c11++;
        }
        int c12 = 0;
        while (c12 < counter[12]) { // 12号：红炮 20221209 2141
            initialBlackScore += 5;
            String temp = ChessGameFrame.getStatusLabel4().getText();
            ChessGameFrame.getStatusLabel4().setText(temp + "炮");
            c12++;
        }
        int c13 = 0;
        while (c13 < counter[13]) { // 13号：黑炮 20221209 2141
            initialRedScore += 5;
            String temp = ChessGameFrame.getStatusLabel5().getText();
            ChessGameFrame.getStatusLabel5().setText(temp + "砲");
            c13++;
        }
        System.out.println(initialRedScore);
        System.out.println(initialBlackScore);
        if(initialRedScore != redScore || initialBlackScore != blackScore){
            JOptionPane.showMessageDialog(null, "Error 105: Error in chess steps.");
            loadError = true;
            ChessGameFrame.getStatusLabel6().setText("Error 105");
        }
    }
    public void setChessReversal ( char[][] chars){
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 4; y++) {
                switch (chars[x][y]) {
                    case 'Y':
                        squareComponents[x][y].setReversal(true);
                        squareComponents[x][y].repaint();
                        flag[x][y]++;
                        break;
                    case 'N':
                        flag [x][y]=0;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Error 103: Illegal chess has appeared.");
                        loadError = true;
                        ChessGameFrame.getStatusLabel6().setText("Error 103");
                        return;
                }
            }
        }
    }
}