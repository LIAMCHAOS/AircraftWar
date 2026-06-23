package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import java.util.LinkedList;
import java.util.List;
import edu.hitsz.bullet.BaseBullet;

public class CircularShoot implements ShootStrategy {
    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int shootNum = 20; // 环向20发
        double angleStep = 2 * Math.PI / shootNum;
        for (int i = 0; i < shootNum; i++) {
            int speedX = (int) (Math.cos(i * angleStep) * 7);
            int speedY = (int) (Math.sin(i * angleStep) * 7);
            res.add(aircraft instanceof HeroAircraft ?
                    new HeroBullet(aircraft.getLocationX(), aircraft.getLocationY(), speedX, speedY, aircraft.getPower()) :
                    new EnemyBullet(aircraft.getLocationX(), aircraft.getLocationY(), speedX, speedY, aircraft.getPower()));
        }
        return res;
    }
}
