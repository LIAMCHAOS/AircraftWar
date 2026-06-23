package edu.hitsz.bullet;

import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.observer.Observer;

/**
 * 子弹基类
 * @author hitsz
 */
public abstract class BaseBullet extends AbstractFlyingObject implements Observer{

    private int power = 10;
    private int backupSpeedX;
    private int backupSpeedY;

    public BaseBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY);
        this.power = power;
    }

    @Override
    public void forward() {
        super.forward();

        // 判定 x 轴出界
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            vanish();
        }

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= Main.WINDOW_HEIGHT ) {
            // 向下飞行出界
            vanish();
        }else if (locationY <= 0){
            // 向上飞行出界
            vanish();
        }
    }

    public int getPower() {
        return power;
    }

    @Override
    public void update(int propType){
        if(propType == 1 && this instanceof EnemyBullet){
            this.vanish();
        }else if(propType == 2 && this instanceof EnemyBullet){
            if(!isFrozen()) {
                this.backupSpeedX = this.speedX;
                this.backupSpeedY = this.speedY;
            }
            this.speedX = 0;
            this.speedY = 0;
            new Thread(()->{
                try {
                    Thread.sleep(5000);
                    javafx.application.Platform.runLater(()->{
                        this.speedX = this.backupSpeedX;
                        this.speedY = this.backupSpeedY;
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
