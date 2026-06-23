package edu.hitsz.dao;
import java.io.*;
import java.util.*;

public class ScoreDaoImpl implements ScoreDao {
    private List<ScoreRecord> records;
    private String filePath;

    public ScoreDaoImpl(String difficulty) {
        String dirPath = "out";
        // 根据难度选择不同的文件
        this.filePath = dirPath + File.separator + difficulty + "_score.dat";
        this.records = loadFromFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(records);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private List<ScoreRecord> loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<ScoreRecord>) ois.readObject();
        } catch (Exception e) { return new ArrayList<>(); }
    }

    @Override
    public List<ScoreRecord> getAllRecords() {
        // 返回前按得分降序排序
        records.sort((r1, r2) -> r2.getScore() - r1.getScore());
        return records;
    }

    @Override
    public void addRecord(ScoreRecord record) {
        records.add(record);
        saveToFile();
    }

    @Override
    public void deleteRecord(int index) {
        if (index >= 0 && index < records.size()){
            ScoreRecord removed = records.remove(index);
            System.out.println("Success Delet Record：" + removed);
            saveToFile();
        }else{
            System.out.println("Delet Meet Something Wrong!");
        }
    }
}
