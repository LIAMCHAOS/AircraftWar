package edu.hitsz.application;

import javafx.scene.image.Image;

public class EasyGame extends AbstractGame {

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
    protected String getDifficultyName() { return "Easy"; }

    @Override
    protected Image getBackgroundImage() { return ImageManager.BACKGROUND_IMAGE_EASY; }

    @Override
    protected int getEnemyMaxNumber() { return 5; }

    @Override
    protected double getEnemySpawnCycle() { return 20; }

    @Override
    protected int getHeroShootCycle() { return 6; }

    @Override
    protected int getEnemyShootCycle() { return 15; }

    @Override
    protected boolean canSpawnBoss() { return false; }

    @Override
    protected int getBossSpawnThreshold() { return 1000; }

    @Override
    protected int getBossHp() { return 1200; }

    @Override
    protected double getMobEnemyProbability() { return 0.7; }

    @Override
    protected double getEliteEnemyProbability() { return 0.2; }

    @Override
    protected double getElitePlusProbability() { return 0.05; }

    @Override
    protected double getEliteProProbability() { return 0.05; }

    @Override
    protected int getEnemySpeedMultiplier() { return 1; }

    @Override
    protected boolean shouldIncreaseDifficulty() { return false; }

    @Override
    protected boolean shouldIncreaseBossHp() { return false; }
}