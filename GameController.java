import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

public class GameController {
    MazeManager maze;
    TurnManager turns;
    int maxTurns = 100;
    int turnCount = 0;
    Agent[] agents;
    int goalX, goalY;

    public void initializeGame(int numAgents, int width, int height, int maxTurns) {
        this.maxTurns = maxTurns;
        maze = new MazeManager(width, height, numAgents);
        turns = new TurnManager();
        agents = new Agent[numAgents];

        goalX = width - 1;
        goalY = height - 1;

        Random rand = new Random();
        for (int i = 0; i < numAgents; i++) {
            int x, y;
            do {
                x = rand.nextInt(width);
                y = rand.nextInt(height);
            } while (!maze.grid[x][y].isTraversable());

            agents[i] = new Agent(i + 1, x, y);
            maze.grid[x][y].hasAgent = true;
            turns.addAgent(agents[i]);
        }
    }

    public void runSimulation() {
        while (turnCount < maxTurns && !turns.allAgentsFinished(agents)) {
            Agent current = turns.getCurrentAgent(agents);

            int oldX = current.currentX;
            int oldY = current.currentY;
            String move = current.chooseNextMove(maze, goalX, goalY);

            if (!move.equals("WAIT")) {
                current.move(move);
                maze.updateAgentLocation(current, oldX, oldY);
                MazeTile tile = maze.getTile(current.currentX, current.currentY);
                checkTileEffect(current, tile);
                if (tile.type == 'G') current.hasReachedGoal = true;
            }

            turns.logTurnSummary(current);
            printMazeVisual();
            maze.rotateCorridor(turnCount % maze.height);
            turns.advanceTurn();
            turnCount++;
        }
        printFinalStatistics();
        logGameSummaryToFile("simulation_log.txt");
    }

    public void checkTileEffect(Agent a, MazeTile tile) {
        if (tile.type == 'T') {
            a.trapsTriggered++;
            a.backtrack();
            System.out.println("Agent " + a.id + " triggered a trap! Backtracking...");
        } else if (tile.type == 'P') {
            a.hasPowerUp = true;
            System.out.println("Agent " + a.id + " collected a power-up!");
        }
    }

    public void printMazeVisual() {
        System.out.println("Maze State:");
        StringBuilder topBottomBorder = new StringBuilder("+");
        for (int j = 0; j < maze.height; j++) {
            topBottomBorder.append("-");
        }
        topBottomBorder.append("+");
        System.out.println(topBottomBorder);

        for (int i = 0; i < maze.width; i++) {
            System.out.print("|");
            for (int j = 0; j < maze.height; j++) {
                MazeTile tile = maze.grid[i][j];
                boolean printedAgent = false;
                for (Agent a : agents) {
                    if (a.currentX == i && a.currentY == j) {
                        System.out.print(a.id);
                        printedAgent = true;
                        break;
                    }
                }
                if (!printedAgent) {
                    switch (tile.type) {
                        case 'W': System.out.print("#"); break;
                        case 'T': System.out.print("T"); break;
                        case 'P': System.out.print("P"); break;
                        case 'G': System.out.print("G"); break;
                        default: System.out.print(" "); break;
                    }
                }
            }
            System.out.println("|");
        }

        System.out.println(topBottomBorder);
        System.out.println();
    }

    public void printFinalStatistics() {
        System.out.println("\nSimulation Complete!");
        for (Agent a : agents) {
            System.out.println("Agent " + a.id + " Stats:");
            System.out.println("  Reached Goal: " + a.hasReachedGoal);
            System.out.println("  Total Moves: " + a.totalMoves);
            System.out.println("  Backtracks: " + a.backtracks);
            System.out.println("  Traps Triggered: " + a.trapsTriggered);
            System.out.println("  Max Stack Depth: " + a.moveHistory.getSize());
        }
        System.out.println("Total Rounds: " + turnCount);
    }

    public void logGameSummaryToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Game Summary:\n");
            for (Agent a : agents) {
                writer.write("Agent " + a.id + ":\n");
                writer.write("  Reached Goal: " + a.hasReachedGoal + "\n");
                writer.write("  Moves: " + a.totalMoves + ", Backtracks: " + a.backtracks + "\n");
                writer.write("  Traps: " + a.trapsTriggered + ", Power-up: " + a.hasPowerUp + "\n");
                writer.write("  Stack Depth: " + a.moveHistory.getSize() + "\n\n");
            }
            writer.write("Total Rounds: " + turnCount + "\n");
        } catch (IOException e) {
            System.out.println("Failed to write log: " + e.getMessage());
        }
    }
}
