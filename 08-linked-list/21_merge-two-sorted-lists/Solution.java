class Solution {

    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {

        // Dummy node gives us a fixed starting point
        ListNode dummy = new ListNode(0);

        // Current points to the end of the merged list
        ListNode current = dummy;

        // Continue while BOTH lists still have nodes
        while (list1 != null && list2 != null) {

            // Choose the smaller current node
            if (list1.val <= list2.val) {

                // Attach list1's current node
                current.next = list1;

                // Move list1 forward
                list1 = list1.next;

            } else {

                // Attach list2's current node
                current.next = list2;

                // Move list2 forward
                list2 = list2.next;
            }

            // Move current to the node we just attached
            current = current.next;
        }

        // One list may still contain nodes.
        // Attach the entire remaining portion.
        if (list1 != null) {
            current.next = list1;
        } else {
            current.next = list2;
        }

        // Skip the temporary dummy node
        return dummy.next;
    }
}