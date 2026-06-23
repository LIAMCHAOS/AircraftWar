package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

// 普通机工厂
public class BossFactory implements EnemyFactory {
    private int hp;

    public BossFactory(int hp){
        this.hp = hp;
    }

    @Override
    public AbstractEnemy createEnemy() {
        return new BossEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.BOSS_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                -2, // speedX
                0, // speedY
                hp // hp
        );
    }
}