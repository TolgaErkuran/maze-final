public class TurnQueue {
    private static class QueueNode {
        int agentId;
        QueueNode next;

        QueueNode(int agentId) {
            this.agentId = agentId;
            this.next = null;
        }
    }

    private QueueNode front;
    private QueueNode rear;
    private int size;

    public TurnQueue() {
        this.front = this.rear = null;
        this.size = 0;
    }

    public void enqueue(int agentId) {
        QueueNode newNode = new QueueNode(agentId);
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public int dequeue() {
        if (isEmpty()) return -1;
        int id = front.agentId;
        front = front.next;
        if (front == null) rear = null;
        size--;
        return id;
    }

    public int peek() {
        return isEmpty() ? -1 : front.agentId;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public int getSize() {
        return size;
    }

    public void rotate() {
        if (size <= 1) return;
        int id = dequeue();
        enqueue(id);
    }
}
