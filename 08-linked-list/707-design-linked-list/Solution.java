class MyLinkedList {

    private class Node {
        int val;
        Node next;

        Node(int val) {
            this.val = val;
        }
    }

    private Node dummy;
    private int size;

    public MyLinkedList() {
        dummy = new Node(0);
        size = 0;
    }

    public int get(int index) {

        if (index < 0 || index >= size) {
            return -1;
        }

        Node current = dummy.next;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.val;
    }

    public void addAtHead(int val) {
        addAtIndex(0, val);
    }

    public void addAtTail(int val) {
        addAtIndex(size, val);
    }

    public void addAtIndex(int index, int val) {

        if (index < 0 || index > size) {
            return;
        }

        Node previous = dummy;

        for (int i = 0; i < index; i++) {
            previous = previous.next;
        }

        Node newNode = new Node(val);

        newNode.next = previous.next;
        previous.next = newNode;

        size++;
    }

    public void deleteAtIndex(int index) {

        if (index < 0 || index >= size) {
            return;
        }

        Node previous = dummy;

        for (int i = 0; i < index; i++) {
            previous = previous.next;
        }

        previous.next = previous.next.next;

        size--;
    }
}

/**
 * Your MyLinkedList object will be instantiated and called as such:
 * MyLinkedList obj = new MyLinkedList();
 * int param_1 = obj.get(index);
 * obj.addAtHead(val);
 * obj.addAtTail(val);
 * obj.addAtIndex(index,val);
 * obj.deleteAtIndex(index);
 */