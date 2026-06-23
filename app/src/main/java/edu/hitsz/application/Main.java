package edu.hitsz.application;

import edu.hitsz.ui.NameInputMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import edu.hitsz.ui.DifficultyMenu;

/**
 * 程序入口
 * @author hitsz & doctxing
 */
public class Main extends Application {

    public static final int WINDOW_WIDTH = 600;
    public static final int WINDOW_HEIGHT = 900;
    private Stage primaryStage;
    private String playerName = "Player";

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        showNameInput();
    }

    public void showNameInput() {
        NameInputMenu nameMenu = new NameInputMenu(primaryStage, (name) -> {
            this.playerName = name;
            showDifficultyMenu(); // 进入难度选择
        });

        Scene nameScene = nameMenu.getScene();
        primaryStage.setTitle("飞机大战 - 玩家名称");
        primaryStage.setScene(nameScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void showDifficultyMenu() {
        DifficultyMenu menu = new DifficultyMenu(primaryStage, playerName, (difficulty) -> {
            startGame(difficulty);
        });

        Scene menuScene = menu.getScene();
        primaryStage.setTitle("飞机大战 - 难度选择");
        primaryStage.setScene(menuScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        System.out.println("Aircraft War Start!");
        launch(args);
    }

    public void startGame(String difficulty) {
        AbstractGame game = switch (difficulty) {
            case "Easy" -> new EasyGame();
            case "Normal" -> new NormalGame();
            case "Hard" -> new HardGame();
            default -> new NormalGame();
        };

        game.setPlayerName(playerName);

        game.setOnReturnToMenu(() -> {
            javafx.application.Platform.runLater(() -> {
                showDifficultyMenu();
            });
        });

        Scene scene = new Scene(game, WINDOW_WIDTH, WINDOW_HEIGHT);

        primaryStage.setTitle("Aircraft War");
        primaryStage.setScene(scene);

        // 固定窗口大小
        primaryStage.setResizable(false);
        primaryStage.setMaxWidth(WINDOW_WIDTH);
        primaryStage.setMaxHeight(WINDOW_HEIGHT);
        primaryStage.show();

        game.action();

        game.requestFocus();

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }
}
