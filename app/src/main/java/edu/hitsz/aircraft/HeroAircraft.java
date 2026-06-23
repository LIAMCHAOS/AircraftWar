package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.strategy.StraightShoot;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    private volatile static HeroAircraft instance;

    private long lastPropTime = 0;

    public void setLastPropTime(long time) {
        this.lastPropTime = time;
    }

    public long getLastPropTime() {
        return lastPropTime;
    }

    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.power = 30;
        this.direction = -1;
        this.shootNum = 1;
        this.shootCycle = 6;
        this.setShootStrategy(new StraightShoot());
    }

    public static HeroAircraft getInstance() {
        if (instance == null) {
            instance = new HeroAircraft(
                    Main.WINDOW_WIDTH / 2,
                    Main.WINDOW_HEIGHT - (int)ImageManager.HERO_IMAGE.getHeight(),
                    0, 0, 300);
        }
        return instance;
    }

    public void increaseHp(int amount) {
        this.hp = Math.min(this.hp + amount, this.maxHp);
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    public void reset() {
        this.hp = this.maxHp;
        this.isValid = true;
        this.setShootStrategy(new StraightShoot());
        this.shootNum = 1;
        this.shootCycle = 6;
        this.lastPropTime = 0;
        this.locationX = Main.WINDOW_WIDTH / 2;
        this.locationY = Main.WINDOW_HEIGHT - (int) ImageManager.HERO_IMAGE.getHeight();
    }
}
