import view.ChessGameFrame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessGameFrame mainFrame = new ChessGameFrame(720, 720);
            mainFrame.setVisible(true);
        });
        try {
            //创建相当于音乐播放器的对象
            Clip clip = AudioSystem.getClip();
            String location = "resources/music.wav";
            File musicPath = new File(location);
            //将传入的文件转成可播放的文件
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
            //播放器打开这个文件
            clip.open(audioInput);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch(Exception ex){
            ex.printStackTrace();
        }
        File dir = new File("stepdata");
        File fs [] = dir.listFiles();
        for(File f :fs){
            String filename = f.getName();
            if(filename.endsWith("000.txt")){
                f.delete();
            }
        }
//        String path = "stepdata/stepdata.txt";
//        File file = new File (path);
//        if(file.exists()){
//            file.delete();
//        }
    }
}
