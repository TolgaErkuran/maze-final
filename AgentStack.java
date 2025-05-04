public class AgentStack {
    private static class StackNode {
        int x, y;
        StackNode next;

        StackNode(int x, int y) {
            this.x = x;
            this.y = y;
            this.next = null;
        }
    }

    private StackNode top;
    private int size;

    public AgentStack() {
        this.top = null;
        this.size = 0;
    }

    public void push(int x, int y) {
        StackNode newNode = new StackNode(x, y);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public int[] pop() {
        if (isEmpty()) return null;
        int[] coords = {top.x, top.y};
        top = top.next;
        size--;
        return coords;
    }

    public int[] peek() {
        if (isEmpty()) return null;
        return new int[] {top.x, top.y};
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int getSize() {
        return size;
    }

    public void clear() {
        top = null;
        size = 0;
    }
}
