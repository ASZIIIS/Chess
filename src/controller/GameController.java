package controller;

import chessComponent.*;
import model.ChessColor;
import view.ChessGameFrame;
import view.Chessboard;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * 这个类主要完成由窗体上组件触发的动作。
 * 例如点击button等
 * ChessGameFrame中组件调用本类的对象，在本类中的方法里完成逻辑运算，将运算的结果传递至chessboard中绘制
 */
public class GameController {
    private Chessboard chessboard;

    public GameController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public Chessboard getChessboard() {
        return chessboard;
    }

    public List<String> loadGameFromFile(String path) { // task2 读档 20221126 2217
        if(!(path.endsWith(".txt"))){
            JOptionPane.showMessageDialog(null, "Error 101: Error in file format");
            chessboard.setLoadError(true);
            ChessGameFrame.getStatusLabel6().setText("Error 101"); // 101: 文件格式错误 20221213 1513
            return Collections.emptyList();
        }
        try {
            List<String> chessData = Files.readAllLines(Path.of(path));
            chessboard.loadGame(chessData);
            return chessData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean cheating(int x,int y){
        return chessboard.getFlag()[x][y] == 0;
    }
    public void saveGameInfile(String path){
        try { // 异常处理，指下面的代码可能会出现异常，如果出现了之后会直接跳过try的部分，继续执行后面的代码，防止程序的终止 20221213 1423
            File file = new File(path);
            if (!file.exists()){
                file.createNewFile();
            }else {
                JOptionPane.showMessageDialog(null, "The file has existed, please try to input a path again.");
                return;
            }
            FileWriter fw = null; // FileWriter 将字符零六转化成字节流，并访问硬盘写入到文件里 20221213 1427
            try {
                fw = new FileWriter(file,true);
                int index = 0;
                for(int counter = 0; counter < 32; counter++){
                    switch (chessboard.getNumber()[counter]) {
                        case 0 -> fw.write("G");
                        case 1, 2 -> fw.write("A");
                        case 3, 4 -> fw.write("M");
                        case 5, 6 -> fw.write("H");
                        case 7, 8 -> fw.write("C");
                        case 9, 10 -> fw.write("P");
                        case 11, 12, 13, 14, 15 -> fw.write("S");
                        case 16 -> fw.write("g");
                        case 17, 18 -> fw.write("a");
                        case 19, 20 -> fw.write("m");
                        case 21, 22 -> fw.write("h");
                        case 23, 24 -> fw.write("c");
                        case 25, 26 -> fw.write("p");
                        default -> fw.write("s");
                    }
                    if((counter + 1) % 4 == 0){
                        fw.write("\n");
                    }
                }
                for(int x = 0; x < 8; x++){ // 记录当前棋盘 20221213 1537
                    for(int y = 0; y < 4; y++){
                        if(chessboard.getChessComponents()[x][y] instanceof EmptySlotComponent){
                            fw.write("O");
                        }else {
                            if(chessboard.getChessComponents()[x][y].getChessColor() == ChessColor.RED){ // 红方棋子 20221211 2215
                                if(chessboard.getChessComponents()[x][y] instanceof ChariotChessComponent){
                                    fw.write("C");
                                } else if (chessboard.getChessComponents()[x][y] instanceof SoldierChessComponent) {
                                    fw.write("S");
                                } else if (chessboard.getChessComponents()[x][y] instanceof HorseChessComponent) {
                                    fw.write("H");
                                } else if (chessboard.getChessComponents()[x][y] instanceof MinisterChessComponent) {
                                    fw.write("M");
                                } else if (chessboard.getChessComponents()[x][y] instanceof AdvisorChessComponent) {
                                    fw.write("A");
                                } else if (chessboard.getChessComponents()[x][y] instanceof CannonChessComponent) {
                                    fw.write("P");
                                }else if (chessboard.getChessComponents()[x][y] instanceof GeneralChessComponent){
                                    fw.write("G");
                                }
                            }else { // 黑方棋子 20221211 2220
                                if(chessboard.getChessComponents()[x][y] instanceof ChariotChessComponent){
                                    fw.write("c");
                                } else if (chessboard.getChessComponents()[x][y] instanceof SoldierChessComponent) {
                                    fw.write("s");
                                } else if (chessboard.getChessComponents()[x][y] instanceof HorseChessComponent) {
                                    fw.write("h");
                                } else if (chessboard.getChessComponents()[x][y] instanceof MinisterChessComponent) {
                                    fw.write("m");
                                } else if (chessboard.getChessComponents()[x][y] instanceof AdvisorChessComponent) {
                                    fw.write("a");
                                } else if (chessboard.getChessComponents()[x][y] instanceof CannonChessComponent) {
                                    fw.write("p");
                                }else if (chessboard.getChessComponents()[x][y] instanceof GeneralChessComponent){
                                    fw.write("g");
                                }
                            }
                        }
                    }
                    fw.write("\n");
                }
                for(int i = 0; i < 8; i ++) { // 记录棋子翻转情况 20221213 1539
                    for (int j = 0; j < 4; j++) {
                        if(chessboard.getChessComponents()[i][j].isReversal()){
                            fw.write("Y");
                        }else {
                            fw.write("N");
                        }
                    }
                    fw.write("\n");
                }
                fw.write(String.format("%d\n%d\n",chessboard.getRedScore(),chessboard.getBlackScore())); // 记录当前分数 20221213 1540
                if(chessboard.getCurrentColor() == ChessColor.RED){ // 记录当前行棋方 20221213 1540
                    fw.write("R");
                }else {
                    fw.write("B");
                }
            }catch (IOException e){ // catch用于捕获上面try里面出现的异常 20221213 1424
                e.printStackTrace();
            }finally {
                fw.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
// todo：1.重新加载游戏；2.