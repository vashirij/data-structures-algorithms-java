/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */

class Solution {

    public ListNode reverseKGroup(ListNode head, int k) {

        // Dummy node handles changes to the head cleanly
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        // Points to the node before the group being reversed
        ListNode groupPrev = dummy;

        while (true) {

            // Find the kth node of the current group
            ListNode kth = getKth(groupPrev, k);

            // Fewer than k nodes remain
            if (kth == null) {
                break;
            }

            // First node after the group
            ListNode groupNext = kth.next;

            // Save the current group's first node.
            // After reversal, it becomes the group's last node.
            ListNode oldGroupStart = groupPrev.next;

            // Reverse the current group
            ListNode previous = groupNext;
            ListNode current = oldGroupStart;

            while (current != groupNext) {

                // Save next node before changing current.next
                ListNode next = current.next;

                // Reverse the pointer
                current.next = previous;

                // Move pointers forward
                previous = current;
                current = next;
            }

            // kth is now the first node of the reversed group
            groupPrev.next = kth;

            // Old first node is now the last node of the group
            groupPrev = oldGroupStart;
        }

        return dummy.next;
    }


    private ListNode getKth(ListNode current, int k) {

        while (current != null && k > 0) {
            current = current.next;
            k--;
        }

        return current;
    }
}