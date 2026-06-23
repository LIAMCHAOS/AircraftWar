package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.factory.*;
import edu.hitsz.prop.BombProp;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import edu.hitsz.dao.ScoreDao;
import edu.hitsz.dao.ScoreDaoImpl;
import edu.hitsz.dao.ScoreRecord;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractGame extends Pane {

    private int backGroundTop = 0;
    private int timeInterval = 40;

    protected final HeroAircraft heroAircraft;
    protected final List<AbstractEnemy> enemyAircrafts;
    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;
    protected final List<AbstractProp> props;

    // 当前难度参数（由子类在 initGame 中初始化）
    protected int currentEnemyMaxNumber;
    protected double currentEnemySpawnCycle;
    protected int currentEnemyShootCycle;
    protected int currentHeroShootCycle;
    protected int currentEnemySpeedMultiplier = 1;

    private int enemySpawnCounter = 0;
    private int score = 0;
    private boolean gameOverFlag = false;

    private final Canvas canvas;
    private final GraphicsContext gc;

    private AnimationTimer timer;
    private long lastNanoTime = 0L;
    private long nanoAccumulator = 0L;

    // Boss相关
    private int scoreSinceLastBoss = 0;
    private boolean bossExists = false;
    protected int bossSpawnCount = 0;

    // 难度递进计时
    protected long gameStartTime = 0;
    protected int difficultyTier = 0;

    public AbstractGame() {
        setPrefSize(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        setPickOnBounds(true);

        canvas = new Canvas(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        getChildren().add(canvas);

        heroAircraft = HeroAircraft.getInstance();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();

        new HeroController(this, heroAircraft);

        this.setFocusTraversable(true);
        this.setOnKeyPressed(e -> {
            if (gameOverFlag && e.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                returnToMenu();
            }
        });
    }

    public final void action() {
        if (timer != null) return;

        heroAircraft.reset();
        score = 0;
        gameOverFlag = false;
        enemyAircrafts.clear();
        heroBullets.clear();
        enemyBullets.clear();
        props.clear();
        bossSpawnCount = 0;
        scoreSinceLastBoss = 0;
        bossExists = false;
        difficultyTier = 0;

        initGame(); // 子类实现：初始化难度参数
        gameStartTime = System.currentTimeMillis();
        MusicManager.playBGM();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameOverFlag) {
                    stop();
                    return;
                }
                if (lastNanoTime == 0L) {
                    lastNanoTime = now;
                    return;
                }
                long delta = now - lastNanoTime;
                lastNanoTime = now;
                nanoAccumulator += delta;

                long stepNanos = timeInterval * 1000000L;
                while (nanoAccumulator >= stepNanos && !gameOverFlag) {
                    nanoAccumulator -= stepNanos;
                    tick();
                }
                render();
            }
        };
        timer.start();

        this.setOnKeyPressed(e -> {
            if (gameOverFlag && e.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                returnToMenu();
            }
        });
        // 确保组件可以接收键盘事件
        this.setFocusTraversable(true);
    }

    public boolean isGameOver() {
        return gameOverFlag;
    }

    //抽象方法
    protected abstract void initGame();
    protected abstract String getDifficultyName();
    protected abstract Image getBackgroundImage();
    protected abstract int getEnemyMaxNumber();
    protected abstract double getEnemySpawnCycle();
    protected abstract int getHeroShootCycle();
    protected abstract int getEnemyShootCycle();
    protected abstract boolean canSpawnBoss();
    protected abstract int getBossSpawnThreshold();
    protected abstract int getBossHp();
    protected abstract double getMobEnemyProbability();
    protected abstract double getEliteEnemyProbability();
    protected abstract double getElitePlusProbability();
    protected abstract double getEliteProProbability();
    protected abstract int getEnemySpeedMultiplier();

    protected boolean shouldIncreaseDifficulty() {
        return false; // 默认不递进
    }

    protected void increaseDifficulty() {
        // 默认空实现
    }

    protected boolean shouldIncreaseBossHp() {
        return false; // 默认Boss血量不递增
    }

    private void tick() {
        if (shouldIncreaseDifficulty()) {
            increaseDifficulty();
        }
        enemySpawn();
        shootAction();
        bulletsMoveAction();
        aircraftMoveAction();
        crashCheckAction();

        for (AbstractProp prop : props) {
            prop.forward();
        }

        postProcessAction();
        checkResultAction();
    }

    private void enemySpawn() {
        enemySpawnCounter++;
        if (enemySpawnCounter >= currentEnemySpawnCycle) {
            enemySpawnCounter = 0;
            if (enemyAircrafts.size() < currentEnemyMaxNumber) {
                if (canSpawnBoss() && scoreSinceLastBoss >= getBossSpawnThreshold() && !bossExists) {
                    MusicManager.playBossBGM();
                    bossSpawnCount++;
                    enemyAircrafts.add(createBossEnemy());
                    bossExists = true;
                    scoreSinceLastBoss = 0;
                } else {
                    enemyAircrafts.add(createRandomEnemy());
                }
            }
        }
    }

    private AbstractEnemy createRandomEnemy() {
        EnemyFactory factory;
        double mobProb = getMobEnemyProbability();
        double eliteProb = getEliteEnemyProbability();
        double elitePlusProb = getElitePlusProbability();
        double eliteProProb = getEliteProProbability();

        double rand = Math.random();
        if (rand < mobProb) {
            factory = new MobFactory();
        } else if (rand < mobProb + eliteProb) {
            factory = new EliteFactory();
        } else if (rand < mobProb + eliteProb + elitePlusProb) {
            factory = new ElitePlusFactory();
        } else if (rand < mobProb + eliteProb + elitePlusProb + eliteProProb) {
            factory = new EliteProFactory();
        } else {
            factory = new MobFactory();
        }
        return factory.createEnemy();
    }

    private AbstractEnemy createBossEnemy() {
        int bossHp = getBossHp();

        if (shouldIncreaseBossHp() && bossSpawnCount > 1) {
            bossHp = bossHp + ((bossSpawnCount - 1) * 300);
        }
        BossFactory factory = new BossFactory(bossHp);
        BossEnemy boss = (BossEnemy) factory.createEnemy();

        boss.decreaseHp(boss.getHp() - bossHp);
        return boss;
    }

    private void shootAction() {
        heroBullets.addAll(heroAircraft.executeShoot());
        for (AbstractAircraft enemy : enemyAircrafts) {
            enemyBullets.addAll(enemy.executeShoot());
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) bullet.forward();
        for (BaseBullet bullet : enemyBullets) bullet.forward();
    }

    private void aircraftMoveAction() {
        for (AbstractEnemy enemy : enemyAircrafts) enemy.forward();
    }

    private void crashCheckAction() {
        // 敌机子弹 -> 英雄机
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) continue;
            if (heroAircraft.crash(bullet)) {
                MusicManager.playSFX("bullet_hit.wav");
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹 -> 敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) continue;
            for (AbstractEnemy enemy : enemyAircrafts) {
                if (enemy.notValid()) continue;
                if (enemy.crash(bullet)) {
                    enemy.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemy.notValid()) {
                        int earnedScore = enemy.getScore();
                        score += earnedScore;
                        props.addAll(enemy.generateProps());
                        if (enemy instanceof BossEnemy && bossExists) {
                            bossExists = false;
                            MusicManager.stopBossBGM();
                            MusicManager.playBGM();
                        }
                        if (!bossExists) {
                            scoreSinceLastBoss += earnedScore;
                        }
                    }
                }
                // 英雄机与敌机相撞
                if (enemy.crash(heroAircraft) || heroAircraft.crash(enemy)) {
                    enemy.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 道具碰撞
        for (AbstractProp prop : props) {
            if (prop.notValid()) continue;
            if (heroAircraft.crash(prop)) {
                if (prop instanceof BombProp) {
                    MusicManager.playSFX("bomb_explosion.wav");
                    for (AbstractEnemy enemy : enemyAircrafts) {
                        if (!(enemy instanceof BossEnemy) && !enemy.notValid()) {
                            score += enemy.getScore();
                        }
                    }
                } else {
                    MusicManager.playSFX("get_supply.wav");
                }
                prop.effect(heroAircraft, enemyBullets, enemyAircrafts);
                prop.vanish();
            }
        }
    }

    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }

    private void checkResultAction() {
        if (heroAircraft.getHp() <= 0 && !gameOverFlag) {
            gameOverFlag = true;
            System.out.println("Game Over!");
            MusicManager.stopBGM();
            MusicManager.stopBossBGM();
            MusicManager.playSFX("game_over.wav");

            ScoreDao scoreDao = new ScoreDaoImpl(getDifficultyName());
            ScoreRecord currentRecord = new ScoreRecord(getPlayerName(), score, LocalDateTime.now());
            scoreDao.addRecord(currentRecord);
            List<ScoreRecord> allRecords = scoreDao.getAllRecords();

            int rank = 1;
            for (ScoreRecord record : allRecords) {
                System.out.println("No. " + rank + " : " + record);
                rank++;
            }
        }
    }

    private void render() {
        gc.clearRect(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        gc.drawImage(getBackgroundImage(), 0, backGroundTop - Main.WINDOW_HEIGHT, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        gc.drawImage(getBackgroundImage(), 0, backGroundTop, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        backGroundTop += 1;
        if (backGroundTop >= Main.WINDOW_HEIGHT) backGroundTop = 0;

        paintImageWithPositionRevised(enemyBullets);
        paintImageWithPositionRevised(heroBullets);
        paintImageWithPositionRevised(enemyAircrafts);
        paintImageWithPositionRevised(props);

        gc.drawImage(ImageManager.HERO_IMAGE,
                heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2.0,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2.0);

        paintScoreAndLife();

        drawBossHpBar();

        if (gameOverFlag){
            drawGameOver();
        }
    }

    private void paintImageWithPositionRevised(List<? extends AbstractFlyingObject> objects) {
        for (AbstractFlyingObject object : objects) {
            Image image = object.getImage();

            double x = object.getLocationX() - image.getWidth() / 2.0;
            double y = object.getLocationY() - image.getHeight() / 2.0;

            gc.drawImage(image, x, y);

            if (object.isFrozen()) {
                drawFreezeOverlay(x, y, image.getWidth(), image.getHeight());
            }
        }
    }

    private void paintScoreAndLife() {
        gc.setFill(Color.rgb(255, 0, 0));
        gc.setFont(Font.font("SansSerif", FontWeight.BOLD, 22));
        gc.fillText("SCORE:" + score, 10, 25);
        gc.fillText("LIFE:" + heroAircraft.getHp(), 10, 50);
        gc.fillText("DIFFICULTY:" + getDifficultyName(), 10, 75);
        if (difficultyTier > 0) {
            gc.fillText("TIER:" + difficultyTier, 10, 100);
        }
    }



    private String playerName = "Player";

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    protected String getPlayerName() {
        return playerName;
    }

    private void drawBossHpBar() {
        BossEnemy boss = null;
        for (AbstractEnemy enemy : enemyAircrafts) {
            if (enemy instanceof BossEnemy && !enemy.notValid()) {
                boss = (BossEnemy) enemy;
                break;
            }
        }

        if (boss == null) {
            return; // 没有Boss，不绘制血条
        }

        // 血条参数
        double barWidth = Main.WINDOW_WIDTH * 0.6;   // 占屏幕宽度的60%
        double barHeight = 20;
        double barX = (Main.WINDOW_WIDTH - barWidth) / 2;  // 居中
        double barY = 30;  // 距顶部30像素
        double borderThickness = 2;

        // 背景（深灰色）
        gc.setFill(Color.rgb(50, 50, 50));
        gc.fillRect(barX - borderThickness, barY - borderThickness,
                barWidth + borderThickness * 2, barHeight + borderThickness * 2);

        // 血条背景（黑色）
        gc.setFill(Color.BLACK);
        gc.fillRect(barX, barY, barWidth, barHeight);

        // 当前血量（红色）
        double hpRatio = boss.getHpRatio();
        int red, green;

        if (hpRatio > 0.5) {
            // 绿色到黄色
            double ratio = (hpRatio - 0.5) * 2; // 0.5~1.0 映射到 0~1
            red = (int) (ratio * 255);
            green = 255;
        } else {
            // 黄色到红色
            double ratio = hpRatio * 2; // 0~0.5 映射到 0~1
            red = 255;
            green = (int) (ratio * 255);
        }
        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));

        Color hpColor = Color.rgb(red, green, 0);
        gc.setFill(hpColor);
        gc.fillRect(barX, barY, barWidth * hpRatio, barHeight);

        // Boss名称和血量文字（白色，居中）
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("SansSerif", FontWeight.BOLD, 14));
        String bossInfo = "BOSS" + "  HP: " + boss.getHp() + " / " + boss.maxHp;
        // 计算文字宽度
        javafx.scene.text.Text text = new javafx.scene.text.Text(bossInfo);
        text.setFont(Font.font("SansSerif", FontWeight.BOLD, 14));
        double textWidth = text.getLayoutBounds().getWidth();
        gc.fillText(bossInfo, barX + (barWidth - textWidth) / 2, barY + barHeight - 5);
    }

    /**
     * 绘制冰冻覆盖效果（纯代码）
     */
    private void drawFreezeOverlay(double x, double y, double w, double h) {
        // 1. 半透明冰蓝底色
        gc.setFill(Color.rgb(80, 180, 255, 0.35));
        gc.fillRect(x, y, w, h);

        // 2. 冰晶外边框
        gc.setStroke(Color.rgb(140, 210, 255, 0.9));
        gc.setLineWidth(2.5);
        gc.strokeRoundRect(x + 1, y + 1, w - 2, h - 2, 6, 6);

        // 3. 内层浅色边框
        gc.setStroke(Color.rgb(200, 235, 255, 0.5));
        gc.setLineWidth(1);
        gc.strokeRoundRect(x + 3, y + 3, w - 6, h - 6, 4, 4);

        // 4. 冰裂纹理（中心十字 + 对角线）
        double cx = x + w / 2;
        double cy = y + h / 2;

        gc.setStroke(Color.rgb(200, 230, 255, 0.45));
        gc.setLineWidth(1.2);

        // 十字冰纹
        gc.strokeLine(cx, y + 4, cx, y + h - 4);
        gc.strokeLine(x + 4, cy, x + w - 4, cy);

        // 对角线冰纹
        gc.strokeLine(x + 4, y + 4, x + w - 4, y + h - 4);
        gc.strokeLine(x + w - 4, y + 4, x + 4, y + h - 4);

        // 5. 细碎冰晶亮点
        gc.setFill(Color.rgb(255, 255, 255, 0.3));
        gc.fillOval(cx - 3, cy - 3, 6, 6);
        gc.setFill(Color.rgb(255, 255, 255, 0.2));
        gc.fillOval(x + w * 0.25, y + h * 0.3, 3, 3);
        gc.fillOval(x + w * 0.75, y + h * 0.6, 3, 3);
    }

    private void drawGameOver() {
        // 半透明黑色遮罩
        gc.setFill(Color.rgb(0, 0, 0, 0.6));
        gc.fillRect(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);

        // "GAME OVER" 主文字
        gc.setFill(Color.rgb(255, 30, 30));  // 红色
        gc.setFont(Font.font("SansSerif", FontWeight.BOLD, 60));

        String text = "GAME OVER";
        javafx.scene.text.Text measureText = new javafx.scene.text.Text(text);
        measureText.setFont(Font.font("SansSerif", FontWeight.BOLD, 60));
        double textWidth = measureText.getLayoutBounds().getWidth();
        double textX = (Main.WINDOW_WIDTH - textWidth) / 2;
        double textY = Main.WINDOW_HEIGHT / 2 - 20;

        // 文字阴影
        gc.setFill(Color.rgb(150, 0, 0, 0.5));
        gc.fillText(text, textX + 3, textY + 3);

        // 文字本体
        gc.setFill(Color.rgb(255, 30, 30));
        gc.fillText(text, textX, textY);

        // 副标题
        gc.setFill(Color.rgb(220, 220, 220));
        gc.setFont(Font.font("SansSerif", FontWeight.NORMAL, 20));
        String subText = "按 ESC 返回主菜单";
        javafx.scene.text.Text subMeasure = new javafx.scene.text.Text(subText);
        subMeasure.setFont(Font.font("SansSerif", FontWeight.NORMAL, 20));
        double subWidth = subMeasure.getLayoutBounds().getWidth();
        gc.fillText(subText, (Main.WINDOW_WIDTH - subWidth) / 2, textY + 60);

        // 最终分数
        gc.setFill(Color.rgb(255, 215, 0));  // 金色
        gc.setFont(Font.font("SansSerif", FontWeight.BOLD, 24));
        String scoreText = "最终得分: " + score;
        javafx.scene.text.Text scoreMeasure = new javafx.scene.text.Text(scoreText);
        scoreMeasure.setFont(Font.font("SansSerif", FontWeight.BOLD, 24));
        double scoreWidth = scoreMeasure.getLayoutBounds().getWidth();
        gc.fillText(scoreText, (Main.WINDOW_WIDTH - scoreWidth) / 2, textY - 50);
    }

    private Runnable onReturnToMenu;

    public void setOnReturnToMenu(Runnable callback) {
        this.onReturnToMenu = callback;
    }

    private void returnToMenu() {
        MusicManager.stopBGM();
        MusicManager.stopBossBGM();
        if (onReturnToMenu != null) {
            onReturnToMenu.run();
        }
    }
}