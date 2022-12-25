package controller;


import chessComponent.*;
import model.ChessColor;
import view.ChessGameFrame;
import view.Chessboard;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;

public class ClickController {
    private final Chessboard chessboard;
    private SquareComponent first; // 用于记录棋盘上（唯一）被选中的棋子的中间变量 20221125 1340

    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
        this.stepController = new StepController(chessboard);
    }
    public StepController stepController;

    public void onClick(SquareComponent squareComponent) {
        if((!chessboard.isCheat())&&(!chessboard.isLoadError())&&(chessboard.getRedScore() < 60)&&(chessboard.getBlackScore() < 60)&&(! chessboard.isPlayingBack())){
        //判断第一次点击
        if (first == null) { // 如果点击的这个位置的棋子没有被选中，且棋盘上没有任何一个被选中的棋子 20221125 1328
            if (handleFirst(squareComponent)) { // 如果点击的棋子正面朝上并且点击的棋子与当前棋盘上显示的下棋方颜色相同 20221125 1319
                squareComponent.setSelected(true); // 将棋子设置为选中状态 20221125 1319
                first = squareComponent; // 点击的位置有棋子被选中 20221125 1320
                first.repaint(); // 在图像上将棋子变为选中的状态（加红色的圆描边） 20221125 1322
            }

        } else { // 代表棋盘上存在被选中的棋子 20221125 1328
            if (first == squareComponent) { // 如果点击的位置恰好是被选中的棋子的位置，再次点击取消选取 20221125 1329
                squareComponent.setSelected(false);
                SquareComponent recordFirst = first; // 中间变量 20221125 1322
                first = null; // 棋盘变为没有被选中的棋子的状态 202221125 1344
                recordFirst.repaint(); // java.awt中的方法，去掉红色圆圈描边 20221125 1351
            } else if (handleSecond(squareComponent)) { // 点击的位置不是被选中的棋子的位置（后面会进行能不能走的判断） 20221125 1408
                //repaint in swap chess method.
//              chessboard.swapChessComponents(first, squareComponent);
                stepController.saveSteps();
                boolean swap = chessboard.swapChessComponents(first, squareComponent);// 进行吃子的方法（不一定真的吃子/换位置） 20221125 1356
                if(squareComponent instanceof EmptySlotComponent){
                    stepAudio();
                }
                if (swap){ // 这个if用于对棋局进行计分 20221126 0044
                    chessboard.getFlag()[squareComponent.getChessboardPoint().getX()][squareComponent.getChessboardPoint().getY()] ++;
                    if (squareComponent.getChessColor() == ChessColor.RED){
                        if (squareComponent instanceof GeneralChessComponent){
                            chessboard.clickController.blackPlus30();
                            String temp = ChessGameFrame.getStatusLabel4().getText();
                            ChessGameFrame.getStatusLabel4().setText(temp + "帥");
                            killAudio();
                        } else if (squareComponent instanceof AdvisorChessComponent) {
                            chessboard.clickController.blackPlus10();
                            String temp = ChessGameFrame.getStatusLabel4().getText();
                            ChessGameFrame.getStatusLabel4().setText(temp + "仕");
                            killAudio();
                        } else if (squareComponent instanceof SoldierChessComponent) {
                            chessboard.clickController.blackPlus1();
                            String temp = ChessGameFrame.getStatusLabel4().getText();
                            ChessGameFrame.getStatusLabel4().setText(temp + "兵");
                            killAudio();
                        } else {
                            chessboard.clickController.blackPlus5();
                            if(squareComponent instanceof MinisterChessComponent){
                                String temp = ChessGameFrame.getStatusLabel4().getText();
                                ChessGameFrame.getStatusLabel4().setText(temp + "相");
                                killAudio();
                            } else if (squareComponent instanceof ChariotChessComponent) {
                                String temp = ChessGameFrame.getStatusLabel4().getText();
                                ChessGameFrame.getStatusLabel4().setText(temp + "俥");
                                killAudio();
                            } else if (squareComponent instanceof HorseChessComponent) {
                                String temp = ChessGameFrame.getStatusLabel4().getText();
                                ChessGameFrame.getStatusLabel4().setText(temp + "傌");
                                killAudio();
                            }else {
                                String temp = ChessGameFrame.getStatusLabel4().getText();
                                ChessGameFrame.getStatusLabel4().setText(temp + "炮");
                                killAudio();
                            }
                        }
                    }else {
                        if (squareComponent instanceof GeneralChessComponent){
                            chessboard.clickController.redPlus30();
                            String temp = ChessGameFrame.getStatusLabel5().getText();
                            ChessGameFrame.getStatusLabel5().setText(temp + "將");
                            killAudio();
                        } else if (squareComponent instanceof AdvisorChessComponent) {
                            chessboard.clickController.redPlus10();
                            String temp = ChessGameFrame.getStatusLabel5().getText();
                            ChessGameFrame.getStatusLabel5().setText(temp + "士");
                            killAudio();
                        } else if (squareComponent instanceof SoldierChessComponent) {
                            chessboard.clickController.redPlus1();
                            String temp = ChessGameFrame.getStatusLabel5().getText();
                            ChessGameFrame.getStatusLabel5().setText(temp + "卒");
                            killAudio();
                        } else {
                            chessboard.clickController.redPlus5();
                            if(squareComponent instanceof MinisterChessComponent){
                                String temp = ChessGameFrame.getStatusLabel5().getText();
                                ChessGameFrame.getStatusLabel5().setText(temp + "象");
                                killAudio();
                            } else if (squareComponent instanceof ChariotChessComponent) {
                                String temp = ChessGameFrame.getStatusLabel5().getText();
                                ChessGameFrame.getStatusLabel5().setText(temp + "車");
                                killAudio();
                            } else if (squareComponent instanceof HorseChessComponent) {
                                String temp = ChessGameFrame.getStatusLabel5().getText();
                                ChessGameFrame.getStatusLabel5().setText(temp + "馬");
                                killAudio();
                            }else {
                                String temp = ChessGameFrame.getStatusLabel5().getText();
                                ChessGameFrame.getStatusLabel5().setText(temp + "砲");
                                killAudio();
                            }
                        }
                    }
                }
                // todo: 把吃子的方法进行相应的修改。 20221125 1428
                chessboard.clickController.swapPlayer();
                first.setSelected(false);
                first = null; // 恢复棋盘上没有棋子被选中的状态 20221125 1357
                chessboard.stepCounter++;
                ChessGameFrame.getStatusLabel7().setText(String.format("Step: %d",chessboard.stepCounter));
            }
            if(chessboard.getRedScore() >= 60){
                winAudio();
                JOptionPane.showMessageDialog(null, "Red win");
                ChessGameFrame.getStatusLabel6().setText("Red win");
                stepController.saveSteps();
            }
            if(chessboard.getBlackScore() >= 60){
                winAudio();
                JOptionPane.showMessageDialog(null, "Black win");
                ChessGameFrame.getStatusLabel6().setText("Black win");
                stepController.saveSteps();
            }
        }
        }
    }
    /**
     * @param squareComponent 目标选取的棋子
     * @return 目标选取的棋子是否与棋盘记录的当前行棋方颜色相同
     */

    private boolean handleFirst(SquareComponent squareComponent) {
        if(!(squareComponent instanceof EmptySlotComponent)){
            // 修复demo中第一次点击空白位置时下棋方会切换的bug，增加了点击位置不是空棋子的判断 20221125 2316
            if (!squareComponent.isReversal()) {  // 如果这个棋子不处于正面朝上的状态 20221125 1316
                stepController.saveSteps();
                reversalAudio();
                squareComponent.setReversal(true);
                System.out.printf("onClick to reverse a chess [%d,%d]\n", squareComponent.getChessboardPoint().getX(), squareComponent.getChessboardPoint().getY());
                squareComponent.repaint();
                chessboard.getFlag()[squareComponent.getChessboardPoint().getX()][squareComponent.getChessboardPoint().getY()] ++;
                chessboard.clickController.swapPlayer();
                if(chessboard.getNoReversalNum() == 31){
                    // 默认状态下无论第一个点开了什么棋子都会切换下棋方，这里增加如果点开的第一个是红色棋子，则下棋方会再转换回来 20221125 2317
                    if(squareComponent.getChessColor() == ChessColor.RED){
                        chessboard.clickController.swapPlayer();
                    }
                }
                chessboard.stepCounter++;
                ChessGameFrame.getStatusLabel7().setText(String.format("Step: %d",chessboard.stepCounter));
                return false;
            }
        }
        return squareComponent.getChessColor() == chessboard.getCurrentColor();
    }

    /**
     * @param squareComponent first棋子目标移动到的棋子second
     * @return first棋子是否能够移动到second棋子位置
     */

    private boolean handleSecond(SquareComponent squareComponent) {

        //没翻开或空棋子，进入if
        if(first.getChessGrade() != 6) { // 原本点的不是炮 20221203 1443
            if(!((first.getChessGrade() >= squareComponent.getChessGrade())||(first.getChessGrade() == 0 && squareComponent.getChessGrade() == 5)||(first.getChessGrade()!=0 && squareComponent.getChessGrade()==6))){
                return false;
            }
            if(first.getChessGrade() == 5 && squareComponent.getChessGrade() == 0){ // 将不能吃兵 20221206 1455
                return false;
            }
            if (!squareComponent.isReversal()) { // 点击位置的棋子没有处于正面朝上的状态（包括棋子没有被翻开和没有棋子的情况） 20221125 1417
                //没翻开且非空棋子不能走
                if (!(squareComponent instanceof EmptySlotComponent)) { // 如果点击的位置不是空的棋子 20221125 1424
                    return false;
                } // 注意，当进入这个if就已经说明了点击的这个位置有没被翻开的棋子或者没有棋子，所以只有在没有棋子的情况下才能进行移动 20221125 1427
            }
            return squareComponent.getChessColor() != chessboard.getCurrentColor() &&
                    first.canMoveTo(chessboard.getChessComponents(), squareComponent.getChessboardPoint(), first.getChessboardPoint());
        }else { // 炮的走法比较特殊，能不分等级的吃已经翻开的棋子 20221203 1446
            if(squareComponent.isReversal()){
                if(squareComponent.getChessColor() == chessboard.getCurrentColor()){
                    return false; // 棋子翻开的时候只能吃不同方的
                }
                return first.canMoveTo(chessboard.getChessComponents(), squareComponent.getChessboardPoint(), first.getChessboardPoint());
            }
            return first.canMoveTo(chessboard.getChessComponents(), squareComponent.getChessboardPoint(), first.getChessboardPoint());
        }
    } // 如果点击的位置已经有棋子，则需要在这个位置的棋子与当前下棋方颜色不同，且符合其它吃子的规则时，才可进行移动 20221125 1436
    // todo: 进行canMoveTo 方法的补全
//    private boolean handleSecond(SquareComponent squareComponent) {
//
//        //没翻开或空棋子，进入if
//        if (!squareComponent.isReversal()) { // 点击位置的棋子没有处于正面朝上的状态（包括棋子没有被翻开和没有棋子的情况） 20221125 1417
//            //没翻开且非空棋子不能走
//            if (!(squareComponent instanceof EmptySlotComponent)) { // 如果点击的位置不是空的棋子 20221125 1424
//                return false;
//            } // 注意，当进入这个if就已经说明了点击的这个位置有没被翻开的棋子或者没有棋子，所以只有在没有棋子的情况下才能进行移动 20221125 1427
//        }
//        return squareComponent.getChessColor() != chessboard.getCurrentColor() &&
//                first.canMoveTo(chessboard.getChessComponents(), squareComponent.getChessboardPoint());
//    } // 如果点击的位置已经有棋子，则需要在这个位置的棋子与当前下棋方颜色不同，且符合其它吃子的规则时，才可进行移动 20221125 1436
//    // todo: 进行canMoveTo 方法的补全

    public void swapPlayer() {
        chessboard.setCurrentColor(chessboard.getCurrentColor() == ChessColor.BLACK ? ChessColor.RED : ChessColor.BLACK);
        ChessGameFrame.getStatusLabel().setText(String.format("%s's TURN", chessboard.getCurrentColor().getName()));
    }
    public void redPlus30(){ // 下面这一堆用于更新双方分数 20221126 0015
        int temp = chessboard.getRedScore();
        chessboard.setRedScore(temp + 30);
        ChessGameFrame.getStatusLabel2().setText(String.format("RED's SCORE :%d",chessboard.getRedScore()));
    }
    public void redPlus10(){
        int temp = chessboard.getRedScore();
        chessboard.setRedScore(temp + 10);
        ChessGameFrame.getStatusLabel2().setText(String.format("RED's SCORE :%d",chessboard.getRedScore()));
    }
    public void redPlus5(){
        int temp = chessboard.getRedScore();
        chessboard.setRedScore(temp + 5);
        ChessGameFrame.getStatusLabel2().setText(String.format("RED's SCORE :%d",chessboard.getRedScore()));
    }
    public void redPlus1(){
        int temp = chessboard.getRedScore();
        chessboard.setRedScore(temp + 1);
        ChessGameFrame.getStatusLabel2().setText(String.format("RED's SCORE :%d",chessboard.getRedScore()));
    }
    public void blackPlus30(){
        int temp = chessboard.getBlackScore();
        chessboard.setBlackScore(temp + 30);
        ChessGameFrame.getStatusLabel3().setText(String.format("BLACK's SCORE :%d",chessboard.getBlackScore()));
    }
    public void blackPlus10(){
        int temp = chessboard.getBlackScore();
        chessboard.setBlackScore(temp + 10);
        ChessGameFrame.getStatusLabel3().setText(String.format("BLACK's SCORE :%d",chessboard.getBlackScore()));
    }
    public void blackPlus5(){
        int temp = chessboard.getBlackScore();
        chessboard.setBlackScore(temp + 5);
        ChessGameFrame.getStatusLabel3().setText(String.format("BLACK's SCORE :%d",chessboard.getBlackScore()));
    }
    public void blackPlus1(){
        int temp = chessboard.getBlackScore();
        chessboard.setBlackScore(temp + 1);
        ChessGameFrame.getStatusLabel3().setText(String.format("BLACK's SCORE :%d",chessboard.getBlackScore()));
    }
    public void killAudio(){
        try {
            //创建相当于音乐播放器的对象
            Clip clip = AudioSystem.getClip();
            String location = "resources/chizi.wav";
            File musicPath = new File(location);
            //将传入的文件转成可播放的文件
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
            //播放器打开这个文件
            clip.open(audioInput);
            clip.start();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void winAudio(){
        try {
            //创建相当于音乐播放器的对象
            Clip clip = AudioSystem.getClip();
            String location = "resources/huosheng.wav";
            File musicPath = new File(location);
            //将传入的文件转成可播放的文件
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
            //播放器打开这个文件
            clip.open(audioInput);
            clip.start();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void reversalAudio(){
        try {
            //创建相当于音乐播放器的对象
            Clip clip = AudioSystem.getClip();
            String location = "resources/fankai.wav";
            File musicPath = new File(location);
            //将传入的文件转成可播放的文件
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
            //播放器打开这个文件
            clip.open(audioInput);
            clip.start();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void stepAudio(){
        try {
            //创建相当于音乐播放器的对象
            Clip clip = AudioSystem.getClip();
            String location = "resources/yidong.wav";
            File musicPath = new File(location);
            //将传入的文件转成可播放的文件
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
            //播放器打开这个文件
            clip.open(audioInput);
            clip.start();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
