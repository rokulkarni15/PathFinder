package algorithms;

import java.util.*;

public class AStar {
    private final int[][] grid;
    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;

    public AStar(int[][] grid, int startX, int startY, int endX, int endY) {
        this.grid = grid;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public List<int[]> findShortestPath() {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int[][] distance = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(distance[i], Integer.MAX_VALUE);
        }
        distance[startX][startY] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> distance[a[0]][a[1]] + heuristic(a[0], a[1])));
        pq.offer(new int[]{startX, startY});

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int x = current[0];
            int y = current[1];
            if (visited[x][y]) continue;
            visited[x][y] = true;
            if (x == endX && y == endY) break;

            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && grid[newX][newY] != 3 && !visited[newX][newY]) {
                    int newDistance = distance[x][y] + 1;
                    if (newDistance < distance[newX][newY]) {
                        distance[newX][newY] = newDistance;
                        pq.offer(new int[]{newX, newY});
                    }
                }
            }
        }

        List<int[]> path = new ArrayList<>();
        int x = endX;
        int y = endY;
        while (x != startX || y != startY) {
            path.add(new int[]{x, y});
            int minDistance = Integer.MAX_VALUE;
            int newX = -1, newY = -1;
            for (int[] dir : directions) {
                int newX1 = x + dir[0];
                int newY1 = y + dir[1];
                if (newX1 >= 0 && newX1 < rows && newY1 >= 0 && newY1 < cols && distance[newX1][newY1] < minDistance) {
                    minDistance = distance[newX1][newY1];
                    newX = newX1;
                    newY = newY1;
                }
            }
            x = newX;
            y = newY;
        }
        path.add(new int[]{startX, startY});
        Collections.reverse(path);
        return path;
    }

    private int heuristic(int x, int y) {
        // Manhattan distance heuristic
        return Math.abs(x - endX) + Math.abs(y - endY);
    }
}
