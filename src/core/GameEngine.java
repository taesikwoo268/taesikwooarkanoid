package core;

import game.GameScene;
import ui.MenuScene;

import javax.swing.*;
import java.awt.*;

public class GameEngine {
    private final JFrame frame;
    private final InputHandler input;
    private final GameScene gameScene;
    private final MenuScene menuScene;

    private Thread gameThread;
    private boolean running = false;

    // Panel chứa game để add vào frame
    private final JPanel gamePanel;

    public GameEngine(JFrame frame) {
        this.frame = frame;
        input = new InputHandler();

        // Tạo game scene logic
        gameScene = new GameScene(input);

        // Panel để vẽ game (Component hợp lệ để add vào JFrame)
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
                gameScene.render(g); // gọi render của gameScene
            }
        };
        gamePanel.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(input);

        // Tạo menu scene + callback
        menuScene = new MenuScene(
                input,
                this::startGame,                     // Play
                () -> System.out.println("SHOP chưa làm!"), // Shop
                () -> System.exit(0)                 // Quit
        );
        menuScene.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
    }

    public JPanel getMenuScene() {
        return menuScene;
    }

    private void startGame() {
        frame.getContentPane().removeAll();  // gỡ menu
        frame.add(gamePanel);                // add panel game
        frame.revalidate();
        frame.repaint();

        gamePanel.requestFocusInWindow();

        running = true;
        gameThread = new Thread(this::runGameLoop);
        gameThread.start();
    }

    private void runGameLoop() {
        final int FPS = 60;
        final double TIME_PER_TICK = 1e9 / FPS;
        long lastTime = System.nanoTime();
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / TIME_PER_TICK;
            lastTime = now;

            while (delta >= 1) {
                gameScene.update();
                gamePanel.repaint(); // repaint panel, không gọi gameScene.repaint()
                delta--;
            }
        }
    }
}
