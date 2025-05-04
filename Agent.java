import java.util.*;

public class Agent {
    int id;
    int currentX, currentY;
    AgentStack moveHistory;
    boolean hasReachedGoal;
    boolean hasPowerUp;
    int totalMoves;
    int backtracks;
    int trapsTriggered;

    public Agent(int id, int startX, int startY) {
        this.id = id;
        this.currentX = startX;
        this.currentY = startY;
        this.moveHistory = new AgentStack();
        this.moveHistory.push(startX, startY);
        this.hasReachedGoal = false;
        this.hasPowerUp = false;
        this.totalMoves = 0;
        this.backtracks = 0;
        this.trapsTriggered = 0;
    }

    public void move(String direction) {
        int newX = currentX;
        int newY = currentY;
        switch (direction.toUpperCase()) {
            case "UP": newX--; break;
            case "DOWN": newX++; break;
            case "LEFT": newY--; break;
            case "RIGHT": newY++; break;
            default: return;
        }
        currentX = newX;
        currentY = newY;
        moveHistory.push(currentX, currentY);
        totalMoves++;
    }

    public void backtrack() {
        if (moveHistory.getSize() > 1) {
            moveHistory.pop();
            int[] prev = moveHistory.peek();
            currentX = prev[0];
            currentY = prev[1];
            backtracks++;
        }
    }

    public void applyPowerUp() {
        hasPowerUp = false;
    }

    public String getMoveHistoryAsString() {
        StringBuilder sb = new StringBuilder();
        AgentStack temp = new AgentStack();
        while (!moveHistory.isEmpty()) {
            int[] pos = moveHistory.pop();
            sb.insert(0, "(" + pos[0] + "," + pos[1] + ") ");
            temp.push(pos[0], pos[1]);
        }
        while (!temp.isEmpty()) {
            int[] pos = temp.pop();
            moveHistory.push(pos[0], pos[1]);
        }
        return sb.toString();
    }

    public String chooseNextMove(MazeManager maze, int goalX, int goalY) {
        String[] directions = {"UP", "DOWN", "LEFT", "RIGHT"};
        int minDist = Integer.MAX_VALUE;
        String bestMove = null;

        for (String dir : directions) {
            int newX = currentX, newY = currentY;
            switch (dir) {
                case "UP": newX--; break;
                case "DOWN": newX++; break;
                case "LEFT": newY--; break;
                case "RIGHT": newY++; break;
            }
            if (newX < 0 || newX >= maze.width || newY < 0 || newY >= maze.height) continue;
            if (!maze.grid[newX][newY].isTraversable()) continue;

            int dist = Math.abs(newX - goalX) + Math.abs(newY - goalY);
            if (dist < minDist) {
                minDist = dist;
                bestMove = dir;
            }
        }

        return bestMove != null ? bestMove : "WAIT";
    }
}
