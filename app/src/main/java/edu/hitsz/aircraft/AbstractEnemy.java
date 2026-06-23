package edu.hitsz.aircraft;

import edu.hitsz.observer.Observer;
import edu.hitsz.prop.AbstractProp;
import java.util.List;

/**
 * 所有敌机的抽象父类
 */
public abstract class AbstractEnemy extends AbstractAircraft implements Observer {
    private int backupSpeedX;
    private int backupSpeedY;
    private boolean killedByBomb =  false;

    public AbstractEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    /**
     * 敌机被销毁时的得分
     */
    public abstract int getScore();

    /**
     * 敌机坠毁时生成的道具列表
     */
    public abstract List<AbstractProp> generateProps();

    public void update(int propType) {
        if(propType == 1){
            if(this instanceof EliteProEnemy){
                this.decreaseHp(30);
            }else if(!(this instanceof BossEnemy)){
                this.killedByBomb = true;
                this.vanish();
            }
        }else if(propType == 2){
            FreezeEffect();
        }
    }

    private void FreezeEffect() {
        if(this instanceof BossEnemy){
            return;
        }if(!isFrozen()){
            this.backupSpeedX = this.speedX;
            this.backupSpeedY = this.speedY;
            isFrozen = true;
        }
        long duration;
        if (this instanceof MobEnemy) {
            this.speedX = 0;
            this.speedY = 0;
            this.freeze(Long.MAX_VALUE); // 永久冻结
            return;
        } else if (this instanceof EliteEnemy) {
            this.speedX = 0;
            this.speedY = 0;
            duration = 4000;
        } else if (this instanceof ElitePlusEnemy) {
            this.speedX = 0;
            this.speedY = 0;
            duration = 3000;
        } else if (this instanceof EliteProEnemy) {
            this.speedX /= 2;
            this.speedY /= 2;
            duration = 5000;
        } else {
            duration = 0;
        }

        this.freeze(duration);

        new Thread(()->{
            try{
                Thread.sleep(duration);
                javafx.application.Platform.runLater(()->{
                    this.speedX = backupSpeedX;
                    this.speedY = backupSpeedY;
                    this.isFrozen = false;
                });
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }).start();
    }
    public boolean isKilledByBomb() {
        return killedByBomb;
    }
}