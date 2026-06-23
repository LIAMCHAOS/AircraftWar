package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.StraightShoot;

import java.util.LinkedList;
import java.util.List;

/**
 * 精锐敌机
 * 可射击、掉落四种道具
 * 双发射击
 * @author hitsz
 */
public class ElitePlusEnemy extends AbstractEnemy {

    public ElitePlusEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.direction = 1;
        this.power = 30;
        this.shootNum = 2;
        this.shootCycle = 10;
        this.setShootStrategy(new StraightShoot());
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
        return 30; // 精锐敌机 30 分
    }

    @Override
    public List<AbstractProp> generateProps() {
        if(isKilledByBomb()){
            return new LinkedList<>();
        }
        List<AbstractProp> res = new LinkedList<>();
        if (Math.random() < 0.5) { // 50%总掉落率
            // 使用简单工厂创建道具
            String[] types = {"Blood", "Bomb", "Bullet", "BulletPlus"};
            String type = types[(int)(Math.random() * 4)];
            res.add(PropFactory.createProp(type, this.getLocationX(), this.getLocationY()));
        }
        return res;
    }
}