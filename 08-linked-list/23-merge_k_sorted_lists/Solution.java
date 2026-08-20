
import java.util.PriorityQueue;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { 
 *         this.val = val; 
 *         this.next = next; 
 *     }
 * }
 */

class Solution {
    public ListNode mergeKLists(ListNode[] lists) {

        // Min-heap ordered by node value
        PriorityQueue<ListNode> minHeap =
            new PriorityQueue<>(
                (a, b) -> Integer.compare(a.val, b.val)
            );

        // Add the head of each non-empty linked list
        for (ListNode node : lists) {
            if (node != null) {
                minHeap.offer(node);
            }
        }

        // Dummy node simplifies construction
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        // Continue while there are candidate nodes
        while (!minHeap.isEmpty()) {

            // Get the smallest current node
            ListNode smallest = minHeap.poll();

            // Attach it to the merged list
            current.next = smallest;
            current = current.next;

            // Add the next node from the same list
            if (smallest.next != null) {
                minHeap.offer(smallest.next);
            }
        }

        return dummy.next;
    }
}