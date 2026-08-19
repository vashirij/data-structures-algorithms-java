class Solution {
    public ListNode reverseList(ListNode head) {

        // Previous node in the reversed portion
        ListNode previous = null;

        // Node currently being processed
        ListNode current = head;

        // Continue until every node has been processed
        while (current != null) {

            // 1. Save the next node before changing the link
            ListNode next = current.next;

            // 2. Reverse the current node's pointer
            current.next = previous;

            // 3. Move previous forward
            previous = current;

            // 4. Move current forward using the saved node
            current = next;
        }

        // Previous now points to the new head
        return previous;
    }
}
