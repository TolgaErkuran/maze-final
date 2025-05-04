public class TurnManager {
    private TurnQueue agentQueue;
    private int currentRound;

    public TurnManager() {
        this.agentQueue = new TurnQueue();
        this.currentRound = 0;
    }

    public void addAgent(Agent agent) {
        agentQueue.enqueue(agent.id);
    }

    public void advanceTurn() {
        currentRound++;
        agentQueue.rotate();
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public Agent getCurrentAgent(Agent[] allAgents) {
        int id = agentQueue.peek();
        for (Agent agent : allAgents) {
            if (agent.id == id) return agent;
        }
        return null;
    }

    public boolean allAgentsFinished(Agent[] allAgents) {
        for (Agent agent : allAgents) {
            if (!agent.hasReachedGoal) return false;
        }
        return true;
    }

    public void logTurnSummary(Agent a) {
        System.out.println("Turn " + currentRound + ": Agent " + a.id + " at (" + a.currentX + "," + a.currentY + ")");
        System.out.println("Moves: " + a.totalMoves + ", Backtracks: " + a.backtracks + ", Power-up: " + a.hasPowerUp);
        System.out.println("Recent Path: " + a.getMoveHistoryAsString());
        System.out.println();
    }
}
