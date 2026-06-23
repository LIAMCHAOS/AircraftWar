package edu.hitsz.factory;
import edu.hitsz.prop.*;

public class PropFactory {
    public static AbstractProp createProp(String type, int x, int y) {
        switch (type) {
            case "Blood": return new BloodProp(x, y, 0, 5);
            case "Bomb": return new BombProp(x, y, 0, 5);
            case "Bullet": return new BulletProp(x, y, 0, 5);
            case "BulletPlus": return new BulletPlusProp(x, y, 0, 5);
            case "Freeze": return new FreezeProp(x, y, 0, 5);
            default: return null;
        }
    }
}
