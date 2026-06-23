package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import java.util.LinkedList;
import java.util.List;

public class StraightShoot implements ShootStrategy {
    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY();
        int speedX = 0;
        int speedY = aircraft.getSpeedY() + aircraft.getDirection() * 10;
        int shootNum = aircraft.getShootNum();

        for (int i = 0; i < shootNum; i++) {
            int bulletX = x + (i * 2 - shootNum + 1) * 10;
            res.add(aircraft instanceof HeroAircraft ?
                    new HeroBullet(bulletX, y, speedX, speedY, aircraft.getPower()) :
                    new EnemyBullet(bulletX, y, speedX, speedY, aircraft.getPower()));
        }
        return res;
    }
}