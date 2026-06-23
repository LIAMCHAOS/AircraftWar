package edu.hitsz.application;

import edu.hitsz.thread.MusicThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MusicManager {
    // 持有背景音乐线程的引用，以便随时停止
    private static MusicThread bgmThread;
    private static MusicThread bossBgmThread;
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(5);

    // 播放普通音效（短促，不循环，播完线程自动销毁）
    public static void playSFX(String name) {
        threadPool.execute(new MusicThread("/videos/" + name, false));
    }

    // 播放背景音乐（长久，循环）
    public static void playBGM() {
        if (bgmThread == null || !bgmThread.isAlive()) {
            bgmThread = new MusicThread("/videos/bgm.wav", true);
            bgmThread.setDaemon(true);
            bgmThread.start();
        }
    }

    public static void stopBGM() {
        if (bgmThread != null) {
            bgmThread.setStop(true);
        }
    }

    // Boss 专用音效控制
    public static void playBossBGM() {
        stopBGM(); // 切换 Boss BGM 前先关普通 BGM
        bossBgmThread = new MusicThread("/videos/bgm_boss.wav", true);
        bossBgmThread.start();
    }

    public static void stopBossBGM() {
        if (bossBgmThread != null) {
            bossBgmThread.setStop(true);
            bossBgmThread =  null;
        }
    }
}