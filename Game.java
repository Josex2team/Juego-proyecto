import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JPanel implements KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PLAYER_WIDTH = 50;
    private static final int PLAYER_HEIGHT = 50;
    private static final int ENEMY_WIDTH = 30;
    private static final int ENEMY_HEIGHT = 30;
    private static final int COIN_WIDTH = 20;
    private static final int COIN_HEIGHT = 20;

    private int playerX, playerY;
    private boolean movingLeft, movingRight;
    private ArrayList<Point> enemies;
    private ArrayList<Point> coins;
    private int score;

    public Game() {
        playerX = WIDTH / 2 - PLAYER_WIDTH / 2;
        playerY = HEIGHT - PLAYER_HEIGHT - 50;
        enemies = new ArrayList<>();
        coins = new ArrayList<>();
        score = 0;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.GREEN);
        setFocusable(true);
        addKeyListener(this);

        Timer timer = new Timer(10, e -> update());
        timer.start();

        Timer enemyTimer = new Timer(1000, e -> spawnEnemy());
        enemyTimer.start();

        Timer coinTimer = new Timer(3000, e -> spawnCoin());
        coinTimer.start();
    }

    private void update() {
        if (movingLeft && playerX > 0) {
            playerX -= 3; // Adjust the player's speed as needed
        }
        if (movingRight && playerX < WIDTH - PLAYER_WIDTH) {
            playerX += 3; // Adjust the player's speed as needed
        }

        for (Point enemy : enemies) {
            enemy.y += 2; // Adjust the speed of enemies falling
            if (new Rectangle(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT).intersects(new Rectangle(enemy.x, enemy.y, ENEMY_WIDTH, ENEMY_HEIGHT))) {
                score -= 3;
                enemies.remove(enemy);
                break;
            }
        }

        for (Point coin : coins) {
            coin.y += 2; // Adjust the speed of coins falling
            if (new Rectangle(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT).intersects(new Rectangle(coin.x, coin.y, COIN_WIDTH, COIN_HEIGHT))) {
                score += 1;
                coins.remove(coin);
                break;
            }
        }

        if (score < 0) {
            System.exit(0); // Close the program if score is negative
        }

        repaint();
    }

    private void spawnEnemy() {
        Random random = new Random();
        int x = random.nextInt(WIDTH - ENEMY_WIDTH);
        enemies.add(new Point(x, 0));
    }

    private void spawnCoin() {
        Random random = new Random();
        int x = random.nextInt(WIDTH - COIN_WIDTH);
        coins.add(new Point(x, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT);

        g.setColor(Color.RED);
        for (Point enemy : enemies) {
            g.fillRect(enemy.x, enemy.y, ENEMY_WIDTH, ENEMY_HEIGHT);
        }

        g.setColor(Color.YELLOW);
        for (Point coin : coins) {
            g.fillRect(coin.x, coin.y, COIN_WIDTH, COIN_HEIGHT);
        }

        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 20, 20);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            movingLeft = true;
            movingRight = false; // Reset the other direction when changing direction
        } else if (key == KeyEvent.VK_RIGHT) {
            movingRight = true;
            movingLeft = false; // Reset the other direction when changing direction
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            movingLeft = false;
            movingRight = false;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Platformer Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().add(new Game());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
