# AircraftWar

AircraftWar 是一个基于 JavaFX 开发的飞机大战小游戏。玩家通过鼠标控制英雄机移动，击落敌机获得分数，并在不同难度下挑战更高的生存时间和排行榜成绩。

## 功能特点

- 支持玩家名称输入和难度选择
- 提供简单、普通、困难三种游戏模式
- 支持普通敌机、精英敌机、精英加强敌机、王牌敌机和 Boss 敌机
- 支持回血、炸弹、火力增强、环形火力、冻结等道具效果
- 支持背景音乐、Boss 音乐和游戏音效
- 支持按难度保存得分记录
- 使用工厂模式、策略模式、观察者模式、DAO 等设计结构组织代码
- 功能展示视频链接：https://www.bilibili.com/video/BV1bBjD6TEbQ/?share_source=copy_web&vd_source=0f3c3eeb895bd3683ba03b099641f021

## 技术栈

- Java 21
- JavaFX 21.0.2
- Gradle Kotlin DSL
- JUnit Jupiter

## 项目结构

```text
AircraftWar/
├── app/
│   ├── build.gradle.kts
│   ├── out/                         # 各难度分数记录文件
│   └── src/main/
│       ├── java/edu/hitsz/
│       │   ├── aircraft/            # 飞机与敌机类
│       │   ├── application/         # 游戏主流程、难度模式、资源管理
│       │   ├── basic/               # 飞行物基础类
│       │   ├── bullet/              # 子弹类
│       │   ├── dao/                 # 分数存取
│       │   ├── factory/             # 敌机和道具工厂
│       │   ├── observer/            # 观察者接口
│       │   ├── prop/                # 道具类
│       │   ├── strategy/            # 射击策略
│       │   ├── thread/              # 音乐线程
│       │   └── ui/                  # 菜单和排行榜界面
│       └── resources/
│           ├── images/              # 图片资源
│           └── videos/              # 音频资源
├── docs/                            # JavaFX / Gradle 学习文档和图片
├── uml/                             # UML 设计图
├── gradle/
├── gradlew
├── gradlew.bat
└── settings.gradle.kts
```

## 运行环境

请确保本机已安装或可通过 Gradle Toolchain 获取 Java 21。

macOS / Linux:

```bash
./gradlew run
```

Windows:

```bat
gradlew.bat run
```

## 常用命令

```bash
# 运行游戏
./gradlew run

# 执行测试
./gradlew test

# 构建项目
./gradlew build
```

## 游戏操作

1. 启动游戏后输入玩家名称，点击确认或按回车进入难度选择界面。
2. 选择简单模式、普通模式或困难模式开始游戏。
3. 游戏中移动鼠标控制英雄机位置。
4. 英雄机自动发射子弹，击毁敌机获得分数。
5. 游戏结束后按 `ESC` 返回主菜单。

## 难度说明

| 难度 | 特点 |
| --- | --- |
| 简单模式 | 敌机数量较少，不生成 Boss，难度不递增 |
| 普通模式 | 会生成 Boss，游戏过程中敌机生成速度和移动速度逐步提升 |
| 困难模式 | 敌机更密集，Boss 出现更频繁，敌机射击和 Boss 血量会随进程增强 |

## 道具说明

| 道具 | 效果 |
| --- | --- |
| BloodProp | 回复英雄机生命值 |
| BombProp | 清除屏幕上的非 Boss 敌机和敌方子弹 |
| BulletProp | 临时切换为散射子弹 |
| BulletPlusProp | 临时切换为环形子弹 |
| FreezeProp | 临时冻结敌机和敌方子弹 |

## 分数记录

游戏分数按难度分别保存到 `app/out/` 目录下：

- `Easy_score.dat`
- `Normal_score.dat`
- `Hard_score.dat`

记录通过 `ScoreDaoImpl` 使用对象序列化读写，并按分数降序展示。

## 设计模式

- 工厂模式：`EnemyFactory` 和 `PropFactory` 负责创建敌机与道具对象
- 策略模式：`ShootStrategy` 定义不同射击方式，包括直线、散射和环形射击
- 观察者模式：用于炸弹、冻结等效果对敌机和子弹的统一通知
- DAO 模式：`ScoreDao` / `ScoreDaoImpl` 负责分数记录的持久化

## 入口类

程序入口位于：

```text
app/src/main/java/edu/hitsz/application/Main.java
```

Gradle 配置中的主类为：

```text
edu.hitsz.application.Main
```
