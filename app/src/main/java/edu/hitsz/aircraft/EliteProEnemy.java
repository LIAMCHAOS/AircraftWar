package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.ScatteredShoot;

import java.util.LinkedList;
import java.util.List;

/**
 * 王牌敌机
 * 可射击、掉落五种道具
 * 三发散射
 * @author hitsz
 */
public class EliteProEnemy extends AbstractEnemy {

    public EliteProEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.direction = 1;
        this.power = 30;
        this.shootCycle = 10;
        this.setShootStrategy(new ScatteredShoot());
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
        return 40; // 王牌敌机 40 分
    }

    @Override
    public List<AbstractProp> generateProps() {
        if(isKilledByBomb()){
            return new LinkedList<>();
        }
        List<AbstractProp> res = new LinkedList<>();
        if (Math.random() < 0.5) { // 50%总掉落率
            // 使用简单工厂创建道具
            String[] types = {"Blood", "Bomb", "Bullet", "BulletPlus", "Freeze"};
            String type = types[(int)(Math.random() * 5)];
            res.add(PropFactory.createProp(type, this.getLocationX(), this.getLocationY()));
        }
        return res;
    }
}

