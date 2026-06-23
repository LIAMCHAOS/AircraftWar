package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

//冰冻道具
public class FreezeProp extends AbstractProp {

    public FreezeProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    public void effect(HeroAircraft hero, List<BaseBullet> bullets, List<AbstractEnemy> enemies) {
        System.out.println("Freeze, Don't move!");
        for (AbstractEnemy enemy : enemies) {
            enemy.update(2);
        }
        for (BaseBullet bullet : bullets) {
            bullet.update(2);
        }
    }
}
