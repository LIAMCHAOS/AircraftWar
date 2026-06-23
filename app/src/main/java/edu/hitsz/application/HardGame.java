package edu.hitsz.application;

import javafx.scene.image.Image;

public class HardGame extends AbstractGame {

    private static final long DIFFICULTY_INTERVAL = 30000;

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
    protected String getDifficultyName() { return "Hard"; }

    @Override
    protected Image getBackgroundImage() { return ImageManager.BACKGROUND_IMAGE_HARD; }

    @Override
    protected int getEnemyMaxNumber() { return 10; }

    @Override
    protected double getEnemySpawnCycle() { return 10; }

    @Override
    protected int getHeroShootCycle() { return 8; }

    @Override
    protected int getEnemyShootCycle() { return 10; }

    @Override
    protected boolean canSpawnBoss() { return true; }

    @Override
    protected int getBossSpawnThreshold() { return 800; }

    @Override
    protected int getBossHp() { return 3000; }

    @Override
    protected double getMobEnemyProbability() { return 0.3; }

    @Override
    protected double getEliteEnemyProbability() { return 0.25; }

    @Override
    protected double getElitePlusProbability() { return 0.25; }

    @Override
    protected double getEliteProProbability() { return 0.20; }

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
            // 困难模式：减少敌机产生周期、增加速度、缩短射击周期
            currentEnemySpawnCycle = Math.max(4, getEnemySpawnCycle() - difficultyTier * 2);
            currentEnemySpeedMultiplier = 1 + difficultyTier;
            currentEnemyShootCycle = Math.max(5, getEnemyShootCycle() - difficultyTier);
            currentHeroShootCycle = Math.max(4, getHeroShootCycle() - difficultyTier);
            heroAircraft.shootCycle = currentHeroShootCycle;
            System.out.println("Tier " + difficultyTier +
                    "，EnemyCycle: " + currentEnemySpawnCycle +
                    "，Speed: " + currentEnemySpeedMultiplier +
                    "，EnemyShootCycle: " + currentEnemyShootCycle +
                    "，HeroShootCycle: " + currentHeroShootCycle);
        }
    }

    @Override
    protected boolean shouldIncreaseBossHp() {
        return true; // 困难模式Boss血量递增
    }
}
