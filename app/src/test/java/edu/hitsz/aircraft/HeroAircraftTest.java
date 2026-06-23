package edu.hitsz.aircraft;

import org.junit.jupiter.api.*;
import edu.hitsz.bullet.EnemyBullet;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("英雄机类单元测试")
class HeroAircraftTest {
    private HeroAircraft hero;

    @BeforeEach
    void setUp() {
        hero = HeroAircraft.getInstance();
        hero.increaseHp(300);
    }

    @Test
    @DisplayName("测试减少生命值逻辑")
    void decreaseHp() {
        System.out.println("decreaseHp 测试");
        int initialHp = hero.getHp();

        hero.decreaseHp(50);
        assertEquals(initialHp - 50, hero.getHp(), "正常扣血后血量不匹配");

        hero.decreaseHp(1000);
        assertEquals(0, hero.getHp(), "血量扣减超过上限时应固定为 0");

        assertTrue(hero.notValid(), "血量为0时，飞机应标记为无效(notValid)");
    }

    @Test
    @DisplayName("测试增加生命值逻辑（上限保护）")
    void increaseHp() {
        System.out.println("正在执行：increaseHp 测试");
        // 先扣掉一部分血
        hero.decreaseHp(100);
        int currentHp = hero.getHp();

        // 正常回血
        hero.increaseHp(20);
        assertEquals(currentHp + 20, hero.getHp(), "正常回血后数值不匹配");

        hero.increaseHp(5000);
        assertEquals(300, hero.getHp(), "回血后血量不应超过初始 maxHp");
    }

    @Test
    @DisplayName("测试碰撞检测逻辑（坐标算法验证）")
    void crash() {
        System.out.println("正在执行：crash 碰撞测试");
        // 设置英雄机位置
        hero.setLocation(100, 100);

        // 1. 创建一个在同一位置的敌机子弹（预期碰撞）
        EnemyBullet bulletIn = new EnemyBullet(100, 100, 0, 5, 10);
        assertTrue(hero.crash(bulletIn), "重合位置应判定为碰撞");

        // 2. 创建一个在远处的敌机子弹（预期不碰撞）
        EnemyBullet bulletOut = new EnemyBullet(500, 500, 0, 5, 10);
        assertFalse(hero.crash(bulletOut), "远处位置不应判定为碰撞");
    }
}