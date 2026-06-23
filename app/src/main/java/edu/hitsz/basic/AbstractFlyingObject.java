package edu.hitsz.basic;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

import javafx.scene.image.Image;

/**
 * 可飞行对象的父类
 *
 * @author hitsz
 */
public abstract class AbstractFlyingObject {

    //locationX,locationY为图片中心位置坐标
    protected int locationX;
    protected int locationY;

    // x,y轴移动速度
    protected int speedX;
    protected int speedY;

    //图片, null 表示未设置
    protected Image image = null;

    //x 轴长度，根据图片尺寸获得, -1 表示未设置
    protected int width = -1;

    //y 轴长度，根据图片尺寸获得, -1 表示未设置
    protected int height = -1;

    //有效标记，标记为 false的对象会在下次刷新时清除
    protected boolean isValid = true;

    //冰冻状态
    protected boolean isFrozen = false;
    protected long freezeEndTime = 0;

    public AbstractFlyingObject() {
    }

    public AbstractFlyingObject(int locationX, int locationY, int speedX, int speedY) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    /**
     * 可飞行对象根据速度移动
     * 若飞行对象触碰到横向边界，横向速度反向
     */
    public void forward() {
        locationX += speedX;
        locationY += speedY;
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            // 横向超出边界后反向
            speedX = -speedX;
        }
    }

    /**
     * 碰撞检测，当对方坐标进入我方范围，判定我方击中<br>
     * 对方与我方覆盖区域有交叉即判定撞击。
     *  <br>
     * 非飞机对象区域：
     *  横向，[x - width/2, x + width/2]
     *  纵向，[y - height/2, y + height/2]
     *  <br>
     * 飞机对象区域：
     *  横向，[x - width/2, x + width/2]
     *  纵向，[y - height/4, y + height/4]
     *
     * @param flyingObject 撞击对方
     * @return true: 我方被击中; false 我方未被击中
     */
    public boolean crash(AbstractFlyingObject flyingObject) {
        // 缩放因子，用于控制 y轴方向区域范围
        int factor = this instanceof AbstractAircraft ? 2 : 1; //我方
        int fFactor = flyingObject instanceof AbstractAircraft ? 2 : 1;//对方

        //对方坐标、宽度、高度
        int x = flyingObject.getLocationX();
        int y = flyingObject.getLocationY();
        int fWidth = flyingObject.getWidth();
        int fHeight = flyingObject.getHeight();

        return x + (fWidth+this.getWidth())/2 > locationX
                && x - (fWidth+this.getWidth())/2 < locationX
                && y + ( fHeight/fFactor+this.getHeight()/factor )/2 > locationY
                && y - ( fHeight/fFactor+this.getHeight()/factor )/2 < locationY;
    }

    public int getLocationX() {
        return locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocation(double locationX, double locationY){
        this.locationX = (int) locationX;
        this.locationY = (int) locationY;
    }

    public int getSpeedY() {
        return speedY;
    }

    public Image getImage() {
        if (image == null){
            image = ImageManager.get(this);
        }
        return image;
    }

    public int getWidth() {
        if (width == -1){
            // 若未设置，则查询图片宽度并设置
            Image resolved = ImageManager.get(this);
            if (resolved == null) {
                throw new IllegalStateException(this.getClass().getName() + " has no image!");
            }
            width = (int) resolved.getWidth();
        }
        return width;
    }

    public int getHeight() {
        if (height == -1){
            // 若未设置，则查询图片高度并设置
            Image resolved = ImageManager.get(this);
            if (resolved == null) {
                throw new IllegalStateException(this.getClass().getName() + " has no image!");
            }
            height = (int) resolved.getHeight();
        }
        return height;
    }
    public boolean notValid() {
        return !this.isValid;
    }

    /**
     * 标记消失，
     * isValid = false.
     * notValid() => true.
     */
    public void vanish() {
        isValid = false;
    }

    public void freeze(long durationMs) {
        this.isFrozen = true;
        this.freezeEndTime = System.currentTimeMillis() + durationMs;
    }

    public boolean isFrozen() {
        if (isFrozen && System.currentTimeMillis() > freezeEndTime) {
            isFrozen = false;
        }
        return isFrozen;
    }

}

