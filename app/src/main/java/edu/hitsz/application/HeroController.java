package edu.hitsz.application;

import edu.hitsz.aircraft.HeroAircraft;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * 英雄机控制类
 * 监听鼠标，控制英雄机的移动
 * @author hitsz
 */
public class HeroController {
    private final HeroAircraft heroAircraft;

    public HeroController(Node inputNode, HeroAircraft heroAircraft){
        this.heroAircraft = heroAircraft;

        inputNode.addEventHandler(MouseEvent.MOUSE_MOVED, this::handleMouse);
        inputNode.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleMouse);
    }

    private void handleMouse(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        if (x < 0 || x > Main.WINDOW_WIDTH || y < 0 || y > Main.WINDOW_HEIGHT) {
            return;
        }
        heroAircraft.setLocation(x, y);
    }
}
