package ui;

import javax.swing.*;

import algorithms.AStar;
import algorithms.Dijkstra;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PathfindingGrid extends JFrame {
    private final int rows = 20;
    private final int cols = 20;
    private final int cellSize = 30;
    private final Color startColor = Color.ORANGE;
    private final Color endColor = Color.YELLOW;
    private final Color obstacleColor = Color.BLACK;
    private final Color defaultColor = Color.WHITE;
    private final Color dijkstraPathColor = Color.BLUE; // Color for Dijkstra's path
    private final Color aStarPathColor = Color.GREEN;   // Color for A* path

    private JPanel gridPanel;
    private JLabel dijkstraLabel;
    private JLabel aStarLabel;

    private int[][] grid;

    private int startX = -1;
    private int startY = -1;
    private int endX = -1;
    private int endY = -1;

    public PathfindingGrid() {
        setTitle("Pathfinding Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        gridPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        Color color;
                        switch (grid[i][j]) {
                            case 1:
                                color = startColor;
                                break;
                            case 2:
                                color = endColor;
                                break;
                            case 3:
                                color = obstacleColor;
                                break;
                            case 4:
                                color = dijkstraPathColor; // Dijkstra's path
                                break;
                            case 5:
                                color = aStarPathColor; // A* path
                                break;
                            default:
                                color = defaultColor;
                        }
                        g.setColor(color);
                        g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                        g.setColor(Color.BLACK);
                        g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                }
            }
        };
        gridPanel.setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        gridPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = e.getY() / cellSize;
                int col = e.getX() / cellSize;
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (grid[row][col] != 1 && grid[row][col] != 2) {
                        if (startX == -1 && startY == -1) {
                            startX = row;
                            startY = col;
                            grid[startX][startY] = 1; // Start point
                        } else if (endX == -1 && endY == -1) {
                            endX = row;
                            endY = col;
                            grid[endX][endY] = 2; // End point
                        } else {
                            grid[row][col] = 3; // Obstacle
                        }
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    if (grid[row][col] == 1) {
                        startX = -1;
                        startY = -1;
                    } else if (grid[row][col] == 2) {
                        endX = -1;
                        endY = -1;
                    }
                    grid[row][col] = 0; // Empty cell
                }
                repaint();
            }
        });
        add(gridPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton startBtn = new JButton("Start Visualization");
        startBtn.addActionListener(e -> startVisualization());
        JButton resetBtn = new JButton("Reset Grid");
        resetBtn.addActionListener(e -> resetGrid());
        JButton clearBtn = new JButton("Clear Obstacles");
        clearBtn.addActionListener(e -> clearObstacles());
        controlPanel.add(startBtn);
        controlPanel.add(resetBtn);
        controlPanel.add(clearBtn);
        add(controlPanel, BorderLayout.SOUTH);

        // Labels to indicate which color represents which algorithm
        dijkstraLabel = new JLabel("Dijkstra's Path: Blue");
        aStarLabel = new JLabel("A* Path: Green");
        JPanel labelPanel = new JPanel();
        labelPanel.add(dijkstraLabel);
        labelPanel.add(aStarLabel);
        add(labelPanel, BorderLayout.NORTH);

        grid = new int[rows][cols];
    }

    private void startVisualization() {
        // Call Dijkstra's algorithm
        Dijkstra dijkstra = new Dijkstra(grid, startX, startY, endX, endY);
        List<int[]> dijkstraPath = dijkstra.findShortestPath();
    
        // Call A* algorithm
        AStar aStar = new AStar(grid, startX, startY, endX, endY);
        List<int[]> aStarPath = aStar.findShortestPath();
    
        // Mark paths with corresponding colors
        markPath(dijkstraPath, dijkstraPathColor); // Use blue color for Dijkstra's algorithm
        // Add delay between paths
        Timer timer = new Timer();
        int delay = 2000;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                markPath(aStarPath, aStarPathColor); // Use green color for A* algorithm
            }
        }, delay);
    }

    private void markPath(List<int[]> path, Color color) {
        // Mark each cell in the path gradually
        Timer timer = new Timer();
        int delay = 100; // Delay between marking each cell (adjust as needed)
        int[] currentCellIndex = {0}; // Index to keep track of the current cell in the path
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currentCellIndex[0] < path.size()) {
                    int[] cell = path.get(currentCellIndex[0]);
                    int x = cell[0];
                    int y = cell[1];
                    if (grid[x][y] != 1 && grid[x][y] != 2) {
                        grid[x][y] = color.equals(dijkstraPathColor) ? 4 : 5; // Path (4 for Dijkstra, 5 for A*)
                    }
                    currentCellIndex[0]++;
                    repaint();
                } else {
                    // Stop the timer when all cells are marked
                    timer.cancel();
                    timer.purge();
                }
            }
        }, delay, delay);
    }

    private void resetGrid() {
        grid = new int[rows][cols];
        startX = -1;
        startY = -1;
        endX = -1;
        endY = -1;
        repaint();
    }

    private void clearObstacles() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 3) {
                    grid[i][j] = 0;
                }
            }
        }
        repaint();
    }

    public void setStartX(int x) {
        this.startX = x;
        repaint();
    }

    public void setStartY(int y) {
        this.startY = y;
        repaint();
    }

    public void setEndX(int x) {
        this.endX = x;
        repaint();
    }

    public void setEndY(int y) {
        this.endY = y;
        repaint();
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PathfindingGrid grid = new PathfindingGrid();
            grid.setVisible(true);
        });
    }
}
