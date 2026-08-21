import java.util.LinkedList;
import java.util.Queue;

class MyStack {

    private Queue<Integer> queue;

    public MyStack() {
        queue = new LinkedList<>();
    }
    
    public void push(int x) {

        // Add the new element to the back
        queue.offer(x);

        // Number of elements after adding x
        int size = queue.size();

        // Rotate all OLD elements behind x
        for (int i = 0; i < size - 1; i++) {
            queue.offer(queue.poll());
        }
    }
    
    public int pop() {

        // Front of queue represents top of stack
        return queue.poll();
    }
    
    public int top() {

        // Look at the top without removing it
        return queue.peek();
    }
    
    public boolean empty() {

        return queue.isEmpty();
    }
}

/**
 * Your MyStack object will be instantiated and called as such:
 * MyStack obj = new MyStack();
 * obj.push(x);
 * int param_2 = obj.pop();
 * int param_3 = obj.top();
 * boolean param_4 = obj.empty();
 */