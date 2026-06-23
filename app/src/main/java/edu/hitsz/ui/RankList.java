package edu.hitsz.ui;

import edu.hitsz.dao.ScoreRecord;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class RankList {
    private TableView<ScoreRecord> table = new TableView<>();

    public void showRank(List<ScoreRecord> records) {
        TableColumn<ScoreRecord, String> nameCol = new TableColumn<>("用户");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<ScoreRecord, Integer> scoreCol = new TableColumn<>("分数");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));

        table.setItems(FXCollections.observableArrayList(records));
        table.getColumns().addAll(nameCol, scoreCol);
        // ... 添加到布局并显示 ...
    }
}
