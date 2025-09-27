package ui;

import core.Constants;
import core.InputHandler;
import core.GameEngine;
import game.GameScene;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

class Button {
    String text;
    Rectangle bound;
    boolean onBound;


    public Button(String text,int x,int y,FontMetrics fm) {
        this.text = text;
        int width = fm.stringWidth(text);
        int height = fm.getAscent();
        int Rx = x-width/2; // x la giua man hinh
        int Ry = y;
        this.bound = new Rectangle(Rx,Ry-height,width,height);
    }
    public void draw(Graphics2D g) {
        g.setFont(new Font("Serif",Font.PLAIN, 32));
        if (onBound) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.WHITE);
        }
        g.drawString(text,bound.x,bound.y+bound.height);
    }
    public boolean contains(int mx,int my) {
        return bound.contains(mx,my);
    }
}

public class MenuScene extends JPanel  {
    private final List<Button> buttons = new ArrayList<>();
    private final InputHandler input;
    private GameEngine engine;

    //Constructor
    public MenuScene(InputHandler input) {
        this.input = input;
        setBackground(Color.BLACK);

        Font font = new Font("Serif",Font.PLAIN,32);
        FontMetrics fm = getFontMetrics(font);

        String[] texts = {"PLAY","SHOP","QUIT"};
        int startY = 250;
        int spacing = 50;

        for (int i=0;i<texts.length;i++) {
            buttons.add(new Button(texts[i], Constants.PADDLE_WIDTH/2, startY+i*spacing,fm));
        }
    }
    private void update() {
        int mx = input.getMouseX();
        int my = input.getMouseY();

        for (Button button : buttons) {
            button.onBound = button.contains(mx,my);
            if(button.onBound && input.isMousePressed()) {
                System.out.println("Clicked"+button.text);
                switch (button.text) {
                    case "PLAY":
                        engine.setRunning(true);
                        break;
                    case "SHOP":
                        engine.setShopping(true);
                        break;
                    case "QUIT":
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    protected void painComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("Serif",Font.PLAIN,32));

        update();

        for(Button button : buttons) {
            button.draw(g2);
        }
    }
}
