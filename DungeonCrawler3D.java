import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DungeonCrawler3D extends JPanel implements KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int MAP_WIDTH = 10;
    private static final int MAP_HEIGHT = 10;
    private static final int TILE_SIZE = 50;
    private static final double FOV = Math.PI / 3; // Field of view (60 degrees)
    private static final double MAX_DEPTH = 10;

    private double playerX = 4.5, playerY = 4.5; // Player's starting position
    private double playerAngle = 0; // Player's viewing angle (0 = facing east)
    private double moveSpeed = 0.1;
    private double turnSpeed = Math.PI / 32;

    // Dungeon map (1 = wall, 0 = floor)
    private int[][] dungeonMap = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 1, 1},
            {1, 0, 1, 1, 1, 1, 0, 0, 1, 1},
            {1, 0, 0, 0, 0, 1, 0, 0, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 0, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    public DungeonCrawler3D() {
        JFrame frame = new JFrame("3D Dungeon Crawler");
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(this);
        frame.setFocusable(true);
        frame.setVisible(true);

        // Start the game loop
        new Thread(() -> {
            while (true) {
                repaint();
                try {
                    Thread.sleep(16); // 60 FPS
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void render3DView(Graphics2D g2) {
        // For each vertical strip on the screen (raycasting)
        for (int x = 0; x < WIDTH; x++) {
            // Calculate ray angle based on player viewing angle and FOV
            double rayAngle = (playerAngle - FOV / 2) + (x / (double) WIDTH) * FOV;

            // Cast the ray and find the distance to the nearest wall
            double distance = castRay(rayAngle);

            // Calculate the height of the wall (scale based on distance)
            int wallHeight = (int) (HEIGHT / (distance + 0.0001)); // +0.0001 to avoid division by zero

            // Draw the wall slice
            int startY = (HEIGHT - wallHeight) / 2;
            int endY = startY + wallHeight;
            g2.setColor(Color.GRAY);
            g2.fillRect(x, startY, 1, endY - startY);
        }
    }

    private double castRay(double angle) {
        double x = playerX;
        double y = playerY;

        double dx = Math.cos(angle);
        double dy = Math.sin(angle);

        for (double depth = 0; depth < MAX_DEPTH; depth += 0.1) {
            x += dx * 0.1;
            y += dy * 0.1;

            int mapX = (int) x;
            int mapY = (int) y;

            // Check if the ray hits a wall (map value is 1)
            if (mapX < 0 || mapX >= MAP_WIDTH || mapY < 0 || mapY >= MAP_HEIGHT || dungeonMap[mapY][mapX] == 1) {
                return depth;
            }
        }
        return MAX_DEPTH;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.clearRect(0, 0, WIDTH, HEIGHT);

        render3DView(g2);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) { // Move forward
            moveForward();
        } else if (e.getKeyCode() == KeyEvent.VK_S) { // Move backward
            moveBackward();
        } else if (e.getKeyCode() == KeyEvent.VK_A) { // Turn left
            turnLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_D) { // Turn right
            turnRight();
        }
    }

    private void moveForward() {
        double newX = playerX + Math.cos(playerAngle) * moveSpeed;
        double newY = playerY + Math.sin(playerAngle) * moveSpeed;
        if (dungeonMap[(int) newY][(int) newX] == 0) {
            playerX = newX;
            playerY = newY;
        }
    }

    private void moveBackward() {
        double newX = playerX - Math.cos(playerAngle) * moveSpeed;
        double newY = playerY - Math.sin(playerAngle) * moveSpeed;
        if (dungeonMap[(int) newY][(int) newX] == 0) {
            playerX = newX;
            playerY = newY;
        }
    }

    private void turnLeft() {
        playerAngle -= turnSpeed;
    }

    private void turnRight() {
        playerAngle += turnSpeed;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DungeonCrawler3D::new);
    }
}
