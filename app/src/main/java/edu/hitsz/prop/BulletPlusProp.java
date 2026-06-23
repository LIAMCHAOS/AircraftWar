package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.strategy.CircularShoot;
import edu.hitsz.strategy.ScatteredShoot;
import edu.hitsz.strategy.StraightShoot;

import java.util.List;

//火力Plus道具
public class BulletPlusProp extends AbstractProp {

    public BulletPlusProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    /*@Override
    public void effect() {
        System.out.println("Bullet Supply active!");
        HeroAircraft hero = HeroAircraft.getInstance();
        hero.setShootStrategy( new CircularShoot() );
    }*/
    @Override
    public void effect(HeroAircraft hero, List<BaseBullet> bullets, List<AbstractEnemy> enemies) {
        System.out.println("Fire Supply Active!");
        hero = HeroAircraft.getInstance();
        hero.setShootStrategy(new CircularShoot());
        long taskTime = System.currentTimeMillis();
        hero.setLastPropTime(taskTime);
        // 实现 Runnable 接口进行异步计时
        HeroAircraft finalHero = hero;
        Runnable r = () -> {
            try {
                Thread.sleep(10000); // 持续10秒

                if (taskTime == finalHero.getLastPropTime()) {
                    // 如果是，说明中途没吃到新道具，恢复直射
                    javafx.application.Platform.runLater(() -> {
                        finalHero.setShootStrategy(new StraightShoot());
                        System.out.println("Fire Supply Over.");
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        new Thread(r).start(); // 启动线程
    }
}
