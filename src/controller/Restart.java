package controller;

import view.ChessGameFrame;

import javax.swing.*;
import java.io.File;

public class Restart extends Thread{
    @Override
    public void run() {
        super.run();
        // 下面的四行是从Main中直接复制过来的 20221127 0020
        SwingUtilities.invokeLater(() -> {
            ChessGameFrame mainFrame = new ChessGameFrame(720, 720);
            mainFrame.setVisible(true);
        });
        File dir = new File("stepdata");
        File fs [] = dir.listFiles();
        for(File f :fs){
            String filename = f.getName();
            if(filename.endsWith("000.txt")){
                f.delete();
                System.out.println("delete");
            }
        }
    }
}
