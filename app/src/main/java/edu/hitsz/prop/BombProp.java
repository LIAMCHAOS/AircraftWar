package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

//炸弹道具
public class BombProp extends AbstractProp {

    public BombProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(HeroAircraft hero, List<BaseBullet> bullets, List<AbstractEnemy> enemies) {
        System.out.println("Bomb!!!");
        for(AbstractEnemy enemy: enemies){
            if(!(enemy instanceof BossEnemy) && !enemy.notValid()){
                enemy.update(1);
            }
        }
        for(BaseBullet bullet: bullets){
            bullet.update(1);
        }
    }
}