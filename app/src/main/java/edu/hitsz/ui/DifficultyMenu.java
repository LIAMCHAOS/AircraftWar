package edu.hitsz.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DifficultyMenu {
    private VBox root;

    public DifficultyMenu(Stage stage, String playerName, DifficultyListener listener) {
        root = new VBox(20); // 垂直布局
        root.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("欢迎， " + playerName + "！");
        welcomeLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label selectLabel = new Label("请选择难度");
        selectLabel.setStyle("-fx-font-size: 18px;");

        Button easyBtn = new Button("简单模式");
        Button normalBtn = new Button("普通模式");
        Button hardBtn = new Button("困难模式");

        double btnWidth = 200;
        double btnHeight = 50;

        easyBtn.setPrefSize(btnWidth, btnHeight);
        normalBtn.setPrefSize(btnWidth, btnHeight);
        hardBtn.setPrefSize(btnWidth, btnHeight);

        // 点击按钮后，通知监听器Main类切换到游戏场景
        easyBtn.setOnAction(e -> listener.onDifficultySelected("Easy"));
        normalBtn.setOnAction(e -> listener.onDifficultySelected("Normal"));
        hardBtn.setOnAction(e -> listener.onDifficultySelected("Hard"));

        root.getChildren().addAll(easyBtn, normalBtn, hardBtn);
    }

    public Scene getScene() {
        return new Scene(root, 512, 768); // 与 Main.WINDOW_WIDTH 一致
    }

    public interface DifficultyListener {
        void onDifficultySelected(String difficulty);
    }
}