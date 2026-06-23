package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.strategy.ShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 所有种类飞机的抽象父类：
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject {

    private ShootStrategy shootStrategy;
    private int shootTimer = 0;

    public int maxHp;
    protected int hp;
    protected int power;
    protected int direction;
    protected int shootNum;
    public int shootCycle;


    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
        }
    }

    public void setShootStrategy(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public int getHp() {
        return hp;
    }
    public int getPower() {return this.power; }
    public int getDirection() {return this.direction;}
    public int getShootNum() {return this.shootNum;}

    public List<BaseBullet> executeShoot() {
        shootTimer++;
        // 判定是否到了设定的射击周期
        if (shootTimer >= shootCycle) {
            shootTimer = 0; // 计数器归零
            return this.shoot(); // 调用真实的策略射击
        }
        // 没到周期，返回空列表，表示这一帧不发射子弹
        return new LinkedList<>();
    }

    public List<BaseBullet> shoot(){
        return shootStrategy.shoot(this);
    }
}


