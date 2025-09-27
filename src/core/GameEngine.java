package core;

import game.GameScene;
import ui.MenuScene;

import javax.swing.*;
import java.awt.*;

public class GameEngine extends JPanel implements Runnable{
    private Thread gameThread;
    private boolean menu = false;
    private boolean shopping = false;
    private boolean running = false;

    private InputHandler input;
    private GameScene gameScene;
    private MenuScene menuScene;

    public GameEngine() {
        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        setFocusable(true);
        requestFocus();

        input = new InputHandler();
        addKeyListener(input);

        gameScene = new GameScene(input);
        menuScene = new MenuScene(input);
    }
    public void startMenu() {
        if(menu) return;
        menu = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void start() {
        if (running) return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        final int FPS = 60;
        final double TIME_PER_TICK = 1e9 / FPS;
        long lastTime = System.nanoTime();
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / TIME_PER_TICK;
            lastTime = now;

            while (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {
        gameScene.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        gameScene.render(g);
    }
    public void setRunning(boolean running) {this.running=running;};
    public void setShopping(boolean shopping) {this.shopping=shopping;};
    public void setMenu(boolean menu) {this.menu = menu;};

}
