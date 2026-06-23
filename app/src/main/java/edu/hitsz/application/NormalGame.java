package edu.hitsz.application;

import javafx.scene.image.Image;

public class NormalGame extends AbstractGame {

    private static final long DIFFICULTY_INTERVAL = 30000; // 30秒

    @Override
    protected void initGame() {
        currentEnemyMaxNumber = getEnemyMaxNumber();
        currentEnemySpawnCycle = getEnemySpawnCycle();
        currentHeroShootCycle = getHeroShootCycle();
        currentEnemyShootCycle = getEnemyShootCycle();
        currentEnemySpeedMultiplier = getEnemySpeedMultiplier();
        heroAircraft.shootCycle = currentHeroShootCycle;
    }

    @Override
    protected String getDifficultyName() { return "Normal"; }

    @Override
    protected Image getBackgroundImage() { return ImageManager.BACKGROUND_IMAGE_NORMAL; }

    @Override
    protected int getEnemyMaxNumber() { return 8; }

    @Override
    protected double getEnemySpawnCycle() { return 15; }

    @Override
    protected int getHeroShootCycle() { return 6; }

    @Override
    protected int getEnemyShootCycle() { return 12; }

    @Override
    protected boolean canSpawnBoss() { return true; }

    @Override
    protected int getBossSpawnThreshold() { return 1000; }

    @Override
    protected int getBossHp() { return 3000; }

    @Override
    protected double getMobEnemyProbability() { return 0.5; }

    @Override
    protected double getEliteEnemyProbability() { return 0.25; }

    @Override
    protected double getElitePlusProbability() { return 0.15; }

    @Override
    protected double getEliteProProbability() { return 0.10; }

    @Override
    protected int getEnemySpeedMultiplier() { return 1; }

    @Override
    protected boolean shouldIncreaseDifficulty() {
        return true;
    }

    @Override
    protected void increaseDifficulty() {
        long elapsed = System.currentTimeMillis() - gameStartTime;
        int newTier = (int) (elapsed / DIFFICULTY_INTERVAL);
        if (newTier > difficultyTier) {
            difficultyTier = newTier;
            // 普通模式：减少敌机产生周期、增加速度
            currentEnemySpawnCycle = Math.max(5, getEnemySpawnCycle() - difficultyTier * 2);
            currentEnemySpeedMultiplier = 1 + difficultyTier;
            System.out.println("Tier " + difficultyTier +
                    "，EnemyCycle: " + currentEnemySpawnCycle +
                    "，Speed: " + currentEnemySpeedMultiplier);
        }
    }

    @Override
    protected boolean shouldIncreaseBossHp() {
        return false; // 普通模式Boss血量不变
    }
}