package edu.hitsz.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NameInputMenu {
    private VBox root;
    private TextField nameField;

    public NameInputMenu(Stage stage, NameInputListener listener) {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("请输入玩家名称");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        nameField = new TextField();
        nameField.setPromptText("输入你的名字...");
        nameField.setMaxWidth(250);
        nameField.setPrefHeight(40);
        nameField.setStyle("-fx-font-size: 16px;");

        // 错误提示标签（默认隐藏）
        Label errorLabel = new Label("名称不能为空！");
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        errorLabel.setVisible(false);

        Button confirmBtn = new Button("确认");
        confirmBtn.setPrefSize(200, 50);
        confirmBtn.setStyle("-fx-font-size: 18px;");

        confirmBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                errorLabel.setVisible(true);
            } else {
                listener.onNameConfirmed(name);
            }
        });

        // 回车键也能确认
        nameField.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                errorLabel.setVisible(true);
            } else {
                listener.onNameConfirmed(name);
            }
        });

        root.getChildren().addAll(titleLabel, nameField, errorLabel, confirmBtn);
    }

    public Scene getScene() {
        return new Scene(root, 512, 768);
    }

    public interface NameInputListener {
        void onNameConfirmed(String playerName);
    }
}
