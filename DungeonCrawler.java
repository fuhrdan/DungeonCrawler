import javax.swing.*;
import java.awt.*;

public class DungeonCrawler extends JPanel {
    private int playerX = 1; // Player's X position
    private int playerY = 1; // Player's Y position
    private int playerDirection = 0; // 0 = North, 1 = East, 2 = South, 3 = West

    private final int[][] dungeon = {
            {1, 1, 1, 1, 1},
            {1, 0, 0, 0, 1},
            {1, 0, 1, 0, 1},
            {1, 0, 0, 0, 1},
            {1, 1, 1, 1, 1}
    };

    public DungeonCrawler() {
        JFrame frame = new JFrame("3D Dungeon Crawler");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        JButton upButton = new JButton("Forward");
        JButton downButton = new JButton("Back");
        JButton leftButton = new JButton("Left");
        JButton rightButton = new JButton("Right");

        upButton.addActionListener(e -> moveForward());
        downButton.addActionListener(e -> moveBackward());
        leftButton.addActionListener(e -> turnLeft());
        rightButton.addActionListener(e -> turnRight());

        controlPanel.add(leftButton);
        controlPanel.add(upButton);
        controlPanel.add(downButton);
        controlPanel.add(rightButton);

        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.add(this, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void moveForward() {
        int newX = playerX, newY = playerY;
        switch (playerDirection) {
            case 0: // North
                newY--;
                break;
            case 1: // East
                newX++;
                break;
            case 2: // South
                newY++;
                break;
            case 3: // West
                newX--;
                break;
        }
        if (dungeon[newY][newX] == 0) {
            playerX = newX;
            playerY = newY;
        }
        repaint();
    }

    private void moveBackward() {
        int newX = playerX, newY = playerY;
        switch (playerDirection) {
            case 0:
                newY++;
                break;
            case 1:
                newX--;
                break;
            case 2:
                newY--;
                break;
            case 3:
                newX++;
                break;
        }
        if (dungeon[newY][newX] == 0) {
            playerX = newX;
            playerY = newY;
        }
        repaint();
    }

    private void turnLeft() {
        playerDirection = (playerDirection + 3) % 4; // Turn counterclockwise
        repaint();
    }

    private void turnRight() {
        playerDirection = (playerDirection + 1) % 4; // Turn clockwise
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Simple rendering: top-down view
        for (int y = 0; y < dungeon.length; y++) {
            for (int x = 0; x < dungeon[y].length; x++) {
                if (dungeon[y][x] == 1) {
                    g2.setColor(Color.BLACK);
                    g2.fillRect(x * 50, y * 50, 50, 50);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.fillRect(x * 50, y * 50, 50, 50);
                }
            }
        }

        // Draw player
        g2.setColor(Color.RED);
        g2.fillOval(playerX * 50 + 10, playerY * 50 + 10, 30, 30);

        // Indicate direction
        g2.setColor(Color.BLUE);
        int dirX = 0, dirY = 0;
        switch (playerDirection) {
            case 0: // North
                dirY = -10;
                break;
            case 1: // East
                dirX = 10;
                break;
            case 2: // South
                dirY = 10;
                break;
            case 3: // West
                dirX = -10;
                break;
        }
        g2.drawLine(playerX * 50 + 25, playerY * 50 + 25,
                    playerX * 50 + 25 + dirX, playerY * 50 + 25 + dirY);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DungeonCrawler::new);
    }
}
