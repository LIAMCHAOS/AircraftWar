package edu.hitsz.observer;

public interface Observer {
    /**
     * @param propType 1-炸弹, 2-冰冻
     */
    void update(int propType);
}
