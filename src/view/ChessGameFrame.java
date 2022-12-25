package view;

import controller.GameController;
import controller.Restart;
import controller.StepController;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * 这个类表示游戏窗体，窗体上包含：
 * 1 Chessboard: 棋盘
 * 2 JLabel:  标签
 * 3 JButton： 按钮
 */
public class ChessGameFrame extends JFrame {
    private final int WIDTH;
    private final int HEIGHT;
    public final int CHESSBOARD_SIZE;
    private GameController gameController;
    private StepController stepController;
    private static JLabel statusLabel; // 显示下棋方 20221125 2359
    private static JLabel statusLabel2; // 显示红方分数 20221125 2359
    private static JLabel statusLabel3; // 显示黑方分数 20221125 2359
    private static JLabel statusLabel4; // 显示红方被吃掉的棋子 20221204 2248
    private static JLabel statusLabel5; // 显示黑方被吃掉的棋子 20221204 2248
    private static JLabel statusLabel6; // 显示in progress、cheating 状态、错误代码、获胜信息等 20221217 1939
    private static JLabel statusLabel7; // 显示这是第几步 20221218 1521
    private static JLabel statusLabel8; // 显示当前的棋局号 20221219 1148


    public ChessGameFrame(int width, int height) {
//      setTitle("2022 CS109 Project Demo"); //设置标题
        setTitle("Cirno's Perfect Math Class"); // 琪 露 诺 的 完 美 算 数 教 室 20221125 1550
        this.WIDTH = width;
        this.HEIGHT = height;
        this.CHESSBOARD_SIZE = HEIGHT * 4 / 5;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);
        addChessboard();
        addLabel();
        addHelloButton();
        addLoadButton();
        addRestartButton();
        addCheatingButton();
        addSaveButton();
        addRegretButton();
        addChangeGameNumButton();
        addRecordButton();
    }


    /**
     * 在游戏窗体中添加棋盘
     */
    private void addChessboard() {
        Chessboard chessboard = new Chessboard(CHESSBOARD_SIZE / 2, CHESSBOARD_SIZE);
        gameController = new GameController(chessboard);
        stepController = new StepController(chessboard);
        chessboard.setLocation(HEIGHT / 10, HEIGHT / 10);
        add(chessboard);
    }

    /**
     * 在游戏窗体中添加标签
     */
    private void addLabel() {
        statusLabel = new JLabel("BLACK's TURN");
        statusLabel.setLocation(WIDTH * 3 / 5, HEIGHT / 10 +20);
        statusLabel.setSize(500, 60);
        statusLabel.setFont(new Font("黑体", Font.BOLD, 20));
        add(statusLabel);
        statusLabel2 = new JLabel("RED's SCORE :0");
        statusLabel2.setLocation(WIDTH * 3 / 5, (HEIGHT / 10) + 40);
        statusLabel2.setSize(500, 60);
        statusLabel2.setFont(new Font("黑体", Font.BOLD, 20));
        add(statusLabel2);
        statusLabel3 = new JLabel("BLACK's SCORE :0");
        statusLabel3.setLocation(WIDTH * 3 / 5, (HEIGHT / 10) + 60);
        statusLabel3.setSize(500, 60);
        statusLabel3.setFont(new Font("黑体", Font.BOLD, 20));
        add(statusLabel3);
        statusLabel4 = new JLabel("Red side being eaten: ");
        statusLabel4.setLocation(10, (HEIGHT / 10) - 90);
        statusLabel4.setSize(1000, 60);
        statusLabel4.setFont(new Font("黑体", Font.BOLD, 20));
        add(statusLabel4);
        statusLabel5 = new JLabel("Black side being eaten: ");
        statusLabel5.setLocation(10, (HEIGHT / 10) - 70);
        statusLabel5.setSize(1000, 60);
        statusLabel5.setFont(new Font("黑体", Font.BOLD, 20));
        add(statusLabel5);
        statusLabel6 = new JLabel("In progress");
        statusLabel6.setLocation(WIDTH * 3 / 5, HEIGHT / 10);
        statusLabel6.setSize(500, 60);
        statusLabel6.setFont(new Font("黑体", Font.BOLD, 20));
        add(statusLabel6);
        statusLabel7 = new JLabel("Step: 0");
        statusLabel7.setLocation(WIDTH * 3 / 5, HEIGHT / 10 - 20);
        statusLabel7.setSize(500, 60);
        statusLabel7.setFont(new Font("黑体", Font.BOLD, 20));
        add(statusLabel7);
        statusLabel8 = new JLabel(String.format("Game number: %s", gameController.getChessboard().gameNum));
        statusLabel8.setLocation(WIDTH * 3 / 5, HEIGHT / 10 - 40);
        statusLabel8.setSize(500, 60);
        statusLabel8.setFont(new Font("黑体", Font.BOLD, 20));
        add(statusLabel8);
    }

    public static JLabel getStatusLabel() {
        return statusLabel;
    }

    public static JLabel getStatusLabel2() {
        return statusLabel2;
    }

    public static JLabel getStatusLabel3() {
        return statusLabel3;
    }

    public static JLabel getStatusLabel4() {
        return statusLabel4;
    }

    public static JLabel getStatusLabel5() {
        return statusLabel5;
    }
    public static JLabel getStatusLabel6(){
        return statusLabel6;
    }

    public static JLabel getStatusLabel7() {
        return statusLabel7;
    }
    public static JLabel getStatusLabel8() {
        return statusLabel8;
    }

    /**
     * 在游戏窗体中增加一个按钮，如果按下的话就会显示Hello, world!
     */

    private void addHelloButton() {
        JButton button = new JButton("Show Hello");
        button.addActionListener((e) -> {
            JOptionPane.showMessageDialog(this, "Hello, world!");
        });
        button.setLocation(WIDTH * 3 / 5, HEIGHT / 10 + 100);
        button.setSize(180, 45);
        button.setFont(new Font("黑体", Font.BOLD, 20));
        add(button);
    }


    private void addLoadButton() {
        JButton button = new JButton("Load");
        button.setLocation(WIDTH * 3 / 5, HEIGHT / 10 + 150);
        button.setSize(180, 45);
        button.setFont(new Font("黑体", Font.BOLD, 20));
//      button.setBackground(Color.LIGHT_GRAY);
        button.addActionListener(e -> {
            if((! gameController.getChessboard().isPlayingBack())) {
                System.out.println("Click load");
                String path = JOptionPane.showInputDialog(this, "Input Path here");
                if(! new File(path).exists()){
                    JOptionPane.showMessageDialog(null, "The file is not exist, please try again.");
                    return;
                }
                gameController.loadGameFromFile(path);
            }
        });
        add(button);
    }
    private void addSaveButton(){
        JButton button = new JButton("Save");
        button.setLocation(WIDTH * 3 / 5, HEIGHT / 10 + 200);
        button.setSize(180, 45);
        button.setFont(new Font("黑体", Font.BOLD, 20));
        add(button);
        button.addActionListener(e -> {
            if((!gameController.getChessboard().isLoadError()) &&(! gameController.getChessboard().isPlayingBack())) {
                System.out.println("Click save");
                String path = JOptionPane.showInputDialog(this, "Input Path here");
                gameController.saveGameInfile(path);
            }
        });
    }
    private void addRestartButton(){ // 进行重启棋局按钮的添加 20221126 0055
        JButton button = new JButton("Restart");
        button.setLocation(WIDTH * 3 / 5, HEIGHT / 10 + 250);
        button.setSize(180, 45);
        button.setFont(new Font("黑体", Font.BOLD, 20));
        button.addActionListener(e ->{
            dispose(); // 关闭原来的窗口 20221127 0018
            Restart re = new Restart();
            re.start(); // 创建一个新的线程并让其运行，线程中方法与Main一致 20221127 0019
            System.out.println("The game has been restarted");
        }); // todo: complete this method 20221126 0105 (Has been done in 20221127 0020)
        add(button);
    }
    private void addCheatingButton(){
        JButton button = new JButton("Cheating");
        button.setLocation(WIDTH * 3 / 5, HEIGHT / 10 + 300);
        button.setSize(180, 45);
        button.setFont(new Font("黑体", Font.BOLD, 20));
        button.addActionListener(e -> {
            if((gameController.getChessboard().getRedScore() < 60)&&(gameController.getChessboard().getBlackScore() < 60) &&(!gameController.getChessboard().isLoadError()) &&(! gameController.getChessboard().isPlayingBack())){
            if(!gameController.getChessboard().isCheat()) {
                statusLabel6.setText("Cheating");
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 4; y++) {
                        if (gameController.cheating(x, y)) {
                            gameController.getChessboard().getChessComponents()[x][y].setReversal(true);
                            gameController.getChessboard().getChessComponents()[x][y].repaint();
                        }
                    }
                }
                gameController.getChessboard().setCheat(true);
            }else {
                for(int x = 0; x < 8; x++){
                    for (int y = 0; y < 4;y ++){
                        if(gameController.cheating(x,y)){
                            gameController.getChessboard().getChessComponents()[x][y].setReversal(false);
                            gameController.getChessboard().getChessComponents()[x][y].repaint();
                        }
                    }
                }
                statusLabel6.setText("In progress");
                gameController.getChessboard().setCheat(false);
            }
        }
        });
        add(button);
    }
    private void addRegretButton(){
        JButton button = new JButton("Regret");
        button.setLocation(WIDTH * 3 / 5, HEIGHT / 10 + 350);
        button.setSize(180, 45);
        button.setFont(new Font("黑体", Font.BOLD, 20));
        button.addActionListener(e -> {

            if(!(gameController.getChessboard().getNoReversalNum() == 32) && (! gameController.getChessboard().isLoadError())&&(! gameController.getChessboard().isPlayingBack())) {
                stepController.readSteps();
            }
            if((gameController.getChessboard().isPlayingBack())){
                if(stepController.judge(gameController.getChessboard().gameNum, gameController.getChessboard().stepCounter - 1)){
                    gameController.getChessboard().stepCounter--;
                    stepController.playBack(gameController.getChessboard().gameNum,gameController.getChessboard().stepCounter);
                }else {
                    JOptionPane.showMessageDialog(null, "This is the first step.");
                }
            }
        });

        add(button);
    }
    public void addChangeGameNumButton(){
        JButton button = new JButton("Change num");
        button.setLocation(WIDTH * 3 / 5, HEIGHT / 10 + 400);
        button.setSize(180, 45);
        button.setFont(new Font("黑体", Font.BOLD, 20));
        button.addActionListener(e -> {
            if((gameController.getChessboard().getNoReversalNum() == 32) && (! gameController.getChessboard().isLoadError())&&(! gameController.getChessboard().isPlayingBack())) {
                String gameNum = JOptionPane.showInputDialog(this, "Input a game number here");
                if(gameNum == null || gameNum.equals("")){
                    JOptionPane.showMessageDialog(null, "Please input a game number again");
                    return;
                }
                gameController.getChessboard().gameNum = gameNum;
                File dir = new File("stepdata");
                File fs [] = dir.listFiles();
                for(File f :fs){
                    String filename = f.getName();
                    if(filename.endsWith(String.format("%s.txt",gameNum))){
                        f.delete();
                    }
                }
                statusLabel8.setText(String.format("Game number: %s", gameController.getChessboard().gameNum));
            }else {
                JOptionPane.showMessageDialog(null, "Cannot change game number after the game starts.");
            }
        });
        add(button);
    }
    public void addRecordButton(){
        JButton button = new JButton("Playback");
        button.setLocation(WIDTH * 3 / 5, HEIGHT / 10 + 450);
        button.setSize(180, 45);
        button.setFont(new Font("黑体", Font.BOLD, 20));
        button.addActionListener( e -> {
            if((gameController.getChessboard().getRedScore() < 60)&&(gameController.getChessboard().getBlackScore() < 60) &&(!gameController.getChessboard().isLoadError()) &&(! gameController.getChessboard().isPlayingBack())) {
                String gameNum = JOptionPane.showInputDialog(this, "Input a game number that you want to playback.");
                if (!stepController.judge(gameNum, 0)) {
                    JOptionPane.showMessageDialog(null, "Cannot find the record document, please input a game number again.");
                    return;
                }
                gameController.getChessboard().stepCounter =0;
                gameController.getChessboard().setPlayingBack(true);
                statusLabel8.setText(String.format("Playing back: %s", gameNum));
                gameController.getChessboard().gameNum = gameNum;
                stepController.playBack(gameController.getChessboard().gameNum,gameController.getChessboard().stepCounter);
            }
            if((gameController.getChessboard().isPlayingBack())){

                if(stepController.judge(gameController.getChessboard().gameNum, gameController.getChessboard().stepCounter+1)){
                    gameController.getChessboard().stepCounter++;
                    stepController.playBack(gameController.getChessboard().gameNum,gameController.getChessboard().stepCounter);
                }else {
                    JOptionPane.showMessageDialog(null, "This is the last step.");
                }
            }
        });
        add(button);
    }
}
// WindowConstants.DISPOSE_ON_CLOSE- 关闭框架并且不一定终止程序的执行。