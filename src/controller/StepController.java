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
public class StepController { // 这部分的代码用于悔棋，其实基本上就是复制粘贴了之前存档/读档的代码 20221218 2059
    private Chessboard chessboard;

    public StepController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public Chessboard getChessboard() {
        return chessboard;
    }
    public void saveSteps(){ // 用存档的方式实现类似保存步骤的功能 20221217 1931
        try {
//            String path = "stepdata/stepdata.txt";
            String path = String.format("stepdata/%d%s.txt",chessboard.stepCounter,chessboard.gameNum);
            File file = new File (path);
            if(file.exists()){
                file.delete();
            }
            FileWriter fw = null;
            try {
                fw = new FileWriter(file,true);
                for(int x = 0;x < 8; x ++){
                    for(int y = 0; y < 4; y++){
                        fw.write("O");
                    }
                    fw.write("\n");
                }
                for(int x = 0; x < 8; x++){
                    for(int y = 0; y < 4; y++){
                        if(chessboard.getChessComponents()[x][y] instanceof EmptySlotComponent){
                            fw.write("O");
                        }else {
                            if(chessboard.getChessComponents()[x][y].getChessColor() == ChessColor.RED){
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
                for(int i = 0; i < 8; i ++) { // 记录棋子翻转情况 20221217 1394
                    for (int j = 0; j < 4; j++) {
                        if(chessboard.getChessComponents()[i][j].isReversal()){
                            fw.write("Y");
                        }else {
                            fw.write("N");
                        }
                    }
                    fw.write("\n");
                }
                fw.write(String.format("%d\n%d\n",chessboard.getRedScore(),chessboard.getBlackScore()));
                if(chessboard.getCurrentColor() == ChessColor.RED){
                    fw.write("R");
                }else {
                    fw.write("B");
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                fw.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public List<String> readSteps(){
//        String path = "stepdata/stepdata.txt";
        if(chessboard.stepCounter>0) {
            chessboard.stepCounter--;
        }else {
            JOptionPane.showMessageDialog(null, "There is no step that you can regret");
            return Collections.emptyList();
        }
        ChessGameFrame.getStatusLabel7().setText(String.format("Step: %d",chessboard.stepCounter));
        String path = String.format("stepdata/%d%s.txt",chessboard.stepCounter,chessboard.gameNum);
        try {
            List<String> chessData = Files.readAllLines(Path.of(path));
            chessboard.loadGame(chessData);
            return chessData;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean judge(String gameNum,int stepCounter){
        String path = String.format("stepdata/%d%s.txt",stepCounter,gameNum);
        return new File(path).exists();
    }
    public List<String> playBack(String gameNum,int stepCounter){
        ChessGameFrame.getStatusLabel7().setText(String.format("Step: %d",stepCounter));
        String path = String.format("stepdata/%d%s.txt",stepCounter,gameNum);
        try {
            List<String> chessData = Files.readAllLines(Path.of(path));
            chessboard.loadGame(chessData);
            return chessData;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}