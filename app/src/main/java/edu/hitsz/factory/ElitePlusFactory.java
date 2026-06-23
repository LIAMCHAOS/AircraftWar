package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.ElitePlusEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

// 普通机工厂
public class ElitePlusFactory implements EnemyFactory {
    @Override
    public AbstractEnemy createEnemy() {
        return new ElitePlusEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_PLUS_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                2, // speedX
                5, // speedY
                30 // hp
        );
    }
}
