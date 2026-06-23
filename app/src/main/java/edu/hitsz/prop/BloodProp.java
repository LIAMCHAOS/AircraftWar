package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

//血包
public class BloodProp extends AbstractProp {

    private int hpIncrease = 20;  // 恢复20滴血

    public BloodProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(HeroAircraft hero,List<BaseBullet> enemyBullets, List<AbstractEnemy> enemies) {
        hero = HeroAircraft.getInstance();
        hero.increaseHp(hpIncrease);
    }
}