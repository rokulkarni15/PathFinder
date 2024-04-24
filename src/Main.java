import javax.swing.*;

import algorithms.AStar;
import algorithms.Dijkstra;
import ui.PathfindingGrid;

public class Main {
    public static void main(String[] args) {
        PathfindingGrid grid = new PathfindingGrid();
        grid.setVisible(true);

        // Prompt the user to input start and end points
        int startX, startY, endX, endY;
        try {
            startX = Integer.parseInt(JOptionPane.showInputDialog("Enter start X coordinate (0-19):"));
            startY = Integer.parseInt(JOptionPane.showInputDialog("Enter start Y coordinate (0-19):"));
            endX = Integer.parseInt(JOptionPane.showInputDialog("Enter end X coordinate (0-19):"));
            endY = Integer.parseInt(JOptionPane.showInputDialog("Enter end Y coordinate (0-19):"));
        } catch (NumberFormatException e) {
            System.err.println("Invalid input. Please enter integers between 0 and 19.");
            return;
        }

        if (startX < 0 || startX >= 20 || startY < 0 || startY >= 20 || endX < 0 || endX >= 20 || endY < 0 || endY >= 20) {
            System.err.println("Coordinates out of bounds. Please enter integers between 0 and 19.");
            return;
        }

        grid.setStartX(startX);
        grid.setStartY(startY);
        grid.setEndX(endX);
        grid.setEndY(endY);

        // Retrieve the grid data from PathfindingGrid
        int[][] gridData = grid.getGrid();

        // Use Dijkstra's algorithm
        Dijkstra dijkstra = new Dijkstra(gridData, grid.getStartX(), grid.getStartY(), grid.getEndX(), grid.getEndY());
        // Use A* algorithm
        AStar aStar = new AStar(gridData, grid.getStartX(), grid.getStartY(), grid.getEndX(), grid.getEndY());

        // Call findShortestPath() method and visualize the path
        // For example:
        System.out.println("Dijkstra's Shortest Path:");
        dijkstra.findShortestPath().forEach(arr -> System.out.println("(" + arr[0] + ", " + arr[1] + ")"));

        System.out.println("A* Shortest Path:");
        aStar.findShortestPath().forEach(arr -> System.out.println("(" + arr[0] + ", " + arr[1] + ")"));
    }
}
