package edu.hitsz.application;


import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.BulletProp;
import edu.hitsz.prop.BulletPlusProp;
import edu.hitsz.prop.BloodProp;
import edu.hitsz.prop.FreezeProp;

import javafx.scene.image.Image;

import java.awt.*;
//import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 *
 * @author hitsz
 */
public class ImageManager {

    /**
     * 类名-图片 映射，存储各基类的图片
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    private static final Map<String, Image> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public static Image BACKGROUND_IMAGE_EASY;
    public static Image BACKGROUND_IMAGE_NORMAL;
    public static Image BACKGROUND_IMAGE_HARD;
    public static Image HERO_IMAGE;
    public static Image HERO_BULLET_IMAGE;
    public static Image ENEMY_BULLET_IMAGE;
    public static Image MOB_ENEMY_IMAGE;
    public static Image ELITE_ENEMY_IMAGE;
    public static Image ELITE_PLUS_ENEMY_IMAGE;
    public static Image ELITE_PRO_ENEMY_IMAGE;
    public static Image BOSS_ENEMY_IMAGE;
    public static Image BLOOD_PROP_IMAGE;
    public static Image BULLET_PROP_IMAGE;
    public static Image BULLET_PLUS_PROP_IMAGE;
    public static Image BOMB_PROP_IMAGE;
    public static Image FREEZE_PROP_IMAGE;

    static {
        try {

            BACKGROUND_IMAGE_EASY = loadFromResources("/images/bg.jpg");
            BACKGROUND_IMAGE_NORMAL = loadFromResources("/images/bg3.jpg");
            BACKGROUND_IMAGE_HARD = loadFromResources("/images/bg5.jpg");
            HERO_IMAGE = loadFromResources("/images/hero.png");
            MOB_ENEMY_IMAGE = loadFromResources("/images/mob.png");
            ELITE_ENEMY_IMAGE = loadFromResources("/images/elite.png");
            ELITE_PLUS_ENEMY_IMAGE = loadFromResources("/images/elitePlus.png");
            ELITE_PRO_ENEMY_IMAGE = loadFromResources("/images/elitePro.png");
            BOSS_ENEMY_IMAGE = loadFromResources("/images/boss.png");
            HERO_BULLET_IMAGE = loadFromResources("/images/bullet_hero.png");
            ENEMY_BULLET_IMAGE = loadFromResources("/images/bullet_enemy.png");
            BLOOD_PROP_IMAGE = loadFromResources("/images/prop_blood.png");
            BULLET_PROP_IMAGE = loadFromResources("/images/prop_bullet.png");
            BULLET_PLUS_PROP_IMAGE = loadFromResources("/images/prop_bulletPlus.png");
            BOMB_PROP_IMAGE = loadFromResources("/images/prop_bomb.png");
            FREEZE_PROP_IMAGE = loadFromResources("/images/prop_freeze.png");

            CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_IMAGE);
            CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ELITE_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(ElitePlusEnemy.class.getName(), ELITE_PLUS_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteProEnemy.class.getName(), ELITE_PRO_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(), BOSS_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BloodProp.class.getName(), BLOOD_PROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BulletProp.class.getName(), BULLET_PROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BulletPlusProp.class.getName(), BULLET_PLUS_PROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(FreezeProp.class.getName(), FREEZE_PROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BombProp.class.getName(), BOMB_PROP_IMAGE);


        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static Image get(String className){
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public static Image get(Object obj){
        if (obj == null){
            return null;
        }
        return get(obj.getClass().getName());
    }


    private static Image loadFromResources(String resourcePath) throws IOException {
        try (InputStream input = ImageManager.class.getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return new Image(input);
        }
    }
}
