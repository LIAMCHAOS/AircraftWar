package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import java.util.LinkedList;
import java.util.List;
import edu.hitsz.bullet.BaseBullet;

public class ScatteredShoot implements ShootStrategy {
    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int shootNum = 3; // 固定散射3发
        for (int i = 0; i < shootNum; i++) {
            int speedX = i - 1;
            int speedY = aircraft.getSpeedY() + aircraft.getDirection() * 10;
            res.add(aircraft instanceof HeroAircraft ?
                    new HeroBullet(aircraft.getLocationX(), aircraft.getLocationY(), speedX, speedY, aircraft.getPower()) :
                    new EnemyBullet(aircraft.getLocationX(), aircraft.getLocationY(), speedX, speedY, aircraft.getPower()));
        }
        return res;
    }
}
