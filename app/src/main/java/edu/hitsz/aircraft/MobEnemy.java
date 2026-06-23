package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.AbstractProp;

import java.util.LinkedList;
import java.util.List;

/**
 * 普通敌机
 * 不可射击、不掉落道具
 * @author hitsz
 */
public class MobEnemy extends AbstractEnemy {

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
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
    public List<BaseBullet> shoot() {
        return new LinkedList<>();
    }

    @Override
    public int getScore() {
        return 10; // 普通敌机 10 分
    }

    @Override
    public List<AbstractProp> generateProps() {
        return new LinkedList<>(); // 普通敌机不掉落，返回空列表
    }
}
