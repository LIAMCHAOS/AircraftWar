package edu.hitsz.thread;

import javax.sound.sampled.*;
import java.io.*;

public class MusicThread extends Thread {
    private String filename;
    private boolean isLoop;
    // 使用 volatile 关键字保证多线程下的可见性，确保能立即停止
    private volatile boolean isStop = false;

    public MusicThread(String filename, boolean isLoop) {
        this.filename = filename;
        this.isLoop = isLoop;
    }

    public void setStop(boolean stop) {
        this.isStop = stop;
    }

    @Override
    public void run() {
        // 如果需要循环，就在 while 里跑；如果不需要，跑一次就结束
        do {
            playMusic();
            // 如果中途被叫停，立刻跳出
            if (isStop) {
                break;
            }
        } while (isLoop);
    }

    private void playMusic() {
        try {
            // 从资源文件加载音频
            InputStream is = getClass().getResourceAsStream(filename);
            // 增加缓冲流提高兼容性
            InputStream bufferedIn = new BufferedInputStream(is);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
            AudioFormat format = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

            line.open(format);
            line.start();

            byte[] buffer = new byte[1024 * 64];
            int count;
            // 🚩 关键：在读写循环中也要检查 isStop，实现秒停
            while ((count = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
                if(isStop){
                    line.stop();
                    break;
                }
                line.write(buffer, 0, count);
            }
            if(!isStop){
                line.drain();
            }
            line.close();
            audioInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}