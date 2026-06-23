package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.CircularShoot;

import java.util.LinkedList;
import java.util.List;

/**
 * Boss敌机
 * 可射击、掉落三个随机道具
 * 环射弹道，3000血量
 * 分数每达到1000激活一次
 * @author hitsz
 */
public class BossEnemy extends AbstractEnemy {

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.direction = 1;
        this.power = 30;
        this.shootCycle = 12;
        this.setShootStrategy(new CircularShoot());
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    public int getScore() {
        return 200; // Boss敌机 200 分
    }

    @Override
    public List<AbstractProp> generateProps() {
        List<AbstractProp> res = new LinkedList<>();
        String[] types = {"Blood", "Bomb", "Bullet", "BulletPlus", "Freeze"};
        for (int i = 0; i < 3; i++) {
            String type = types[(int) (Math.random() * 5)];
            int offset = (i - 1) * 20;
            res.add(PropFactory.createProp(type, this.getLocationX() + offset, this.getLocationY()));
        }//掉落三个道具
        return res;
    }

    public double getHpRatio() {
        return Math.max(0, (double) hp / maxHp);
    }
}

