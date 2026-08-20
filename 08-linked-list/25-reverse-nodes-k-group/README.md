# 25. Reverse Nodes in k-Group

**Difficulty:** Hard  
**Topics:** Linked List, Recursion  
**Status:** Solved  
**LeetCode:** https://leetcode.com/problems/reverse-nodes-in-k-group/

---

## Original Question

Given the `head` of a linked list, reverse the nodes of the list `k` at a time, and return the modified list.

`k` is a positive integer and is less than or equal to the length of the linked list.

If the number of nodes is not a multiple of `k`, the remaining nodes at the end should remain unchanged.

You may **not alter the values** in the nodes. Only the links between nodes may be changed.

---

## Example 1

```text
Input:
head = [1,2,3,4,5]
k = 2

Output:
[2,1,4,3,5]
```

Visualization:

```text
Original:

1 → 2 → 3 → 4 → 5
|---|   |---|   |
 k=2     k=2   leftover


Reverse each complete group:

2 → 1 → 4 → 3 → 5
```

---

## Example 2

```text
Input:
head = [1,2,3,4,5]
k = 3

Output:
[3,2,1,4,5]
```

Visualization:

```text
Original:

1 → 2 → 3 → 4 → 5
|-------|   |---|
   k=3     only 2 nodes


Reverse first group:

3 → 2 → 1 → 4 → 5
```

Nodes:

```text
4 → 5
```

remain unchanged because there are fewer than `k` nodes remaining.

---

# 1. Inputs

The inputs are:

```text
head
k
```

### `head`

Points to the first node of the singly linked list.

Example:

```text
head
 ↓
 1 → 2 → 3 → 4 → 5 → null
```

### `k`

Determines how many nodes should be reversed at a time.

For:

```text
k = 2
```

we divide conceptually into:

```text
[1,2] [3,4] [5]
```

For:

```text
k = 3
```

we divide conceptually into:

```text
[1,2,3] [4,5]
```

Only **complete groups of `k` nodes** are reversed.

---

# 2. Output

Return the head of the modified linked list.

For:

```text
head = [1,2,3,4,5]
k = 2
```

return:

```text
2 → 1 → 4 → 3 → 5 → null
↑
new head
```

---

# 3. Required Operation

This problem builds directly on:

```text
206. Reverse Linked List
```

There, we reversed:

```text
1 → 2 → 3 → 4 → 5
```

completely.

Here, we reverse only groups of `k`.

For:

```text
k = 2
```

instead of:

```text
5 → 4 → 3 → 2 → 1
```

we want:

```text
2 → 1 → 4 → 3 → 5
```

So the problem is essentially:

```text
Find k nodes
      ↓
Are there enough?
   /        \
 Yes         No
 ↓            ↓
Reverse      Stop
 ↓
Reconnect
 ↓
Move to next group
 ↓
Repeat
```

---

# 4. Main Challenge

Reversing `k` nodes is not the hardest part.

We already know how to reverse a linked list.

The difficult part is correctly reconnecting each reversed group.

Consider:

```text
1 → 2 → 3 → 4 → 5
```

with:

```text
k = 2
```

After reversing the first group:

```text
2 → 1
```

we still need:

```text
1 → 3
```

Then after reversing:

```text
3 → 4
```

into:

```text
4 → 3
```

we need:

```text
1 → 4
```

and:

```text
3 → 5
```

So we need pointers that remember the boundaries between groups.

---

# 5. Dummy Node

As with several linked-list problems, a dummy node simplifies handling changes to the head.

Create:

```java
ListNode dummy = new ListNode(0);
dummy.next = head;
```

Now:

```text
dummy → 1 → 2 → 3 → 4 → 5
  ↑
groupPrev
```

Why is this useful?

After reversing the first group:

```text
1 → 2
```

becomes:

```text
2 → 1
```

The head changes from:

```text
1
```

to:

```text
2
```

Without a dummy node, we would need special logic for the first group.

With a dummy node, every group—including the first—is connected in exactly the same way.

---

# 6. Important Pointers

We use several pointers.

## `groupPrev`

Points to the node immediately **before** the group we want to reverse.

Initially:

```text
dummy → 1 → 2 → 3 → 4 → 5
  ↑
groupPrev
```

---

## `kth`

Points to the `k`th node of the current group.

For:

```text
k = 2
```

and:

```text
groupPrev = dummy
```

we find:

```text
dummy → 1 → 2 → 3 → 4 → 5
            ↑
           kth
```

This tells us that:

```text
1 → 2
```

is a complete group.

---

## `groupNext`

Points to the node immediately after the current group.

```java
ListNode groupNext = kth.next;
```

Example:

```text
dummy → 1 → 2 → 3 → 4 → 5
            ↑   ↑
           kth groupNext
```

This pointer is extremely important because after reversing:

```text
1 → 2
```

we still need to reconnect the group to:

```text
3
```

---

# 7. First Step — Find the kth Node

Before reversing anything, we must verify that at least `k` nodes remain.

We can create a helper:

```java
private ListNode getKth(ListNode current, int k)
```

Starting from `groupPrev`, move forward `k` times.

```java
while (current != null && k > 0) {
    current = current.next;
    k--;
}
```

Then return:

```java
return current;
```

If the result is:

```text
null
```

there are fewer than `k` nodes remaining.

Therefore we stop without reversing them.

---

# 8. Why Check for k Nodes First?

Consider:

```text
1 → 2 → 3 → 4 → 5
```

with:

```text
k = 3
```

First group:

```text
1 → 2 → 3
```

contains exactly 3 nodes.

Reverse it:

```text
3 → 2 → 1
```

Remaining:

```text
4 → 5
```

Only two nodes remain.

Since:

```text
2 < k
```

they must stay unchanged.

So before reversing a group, we must know:

```text
Are there at least k nodes?
```

If not:

```java
if (kth == null) {
    break;
}
```

---

# 9. Reversing One Group

Suppose:

```text
1 → 2 → 3
```

must be reversed, and `groupNext` points to:

```text
4
```

We use the same pointer reversal idea from **206. Reverse Linked List**.

Normally we started with:

```java
ListNode previous = null;
```

But here, we can start with:

```java
ListNode previous = groupNext;
```

Why?

Because after reversal, the old first node should connect directly to the next group.

Example:

```text
Before:

1 → 2 → 3 → 4
            ↑
        group boundary
```

After:

```text
3 → 2 → 1 → 4
```

Setting:

```java
previous = groupNext;
```

allows the connection to `4` to be built naturally during reversal.

---

# 10. Group Reversal

Initialize:

```java
ListNode previous = groupNext;
ListNode current = groupPrev.next;
```

Then:

```java
while (current != groupNext) {

    ListNode next = current.next;

    current.next = previous;

    previous = current;

    current = next;
}
```

This is almost exactly the same pattern as **206. Reverse Linked List**.

Remember:

```text
SAVE
 ↓
REVERSE
 ↓
MOVE PREVIOUS
 ↓
MOVE CURRENT
```

The difference is the stopping condition.

### Reverse Linked List

```java
while (current != null)
```

### Reverse k nodes

```java
while (current != groupNext)
```

We stop at the boundary of the current group instead of at the end of the entire linked list.

---

# 11. Why `current != groupNext`?

Suppose:

```text
1 → 2 → 3 → 4 → 5
```

and:

```text
k = 3
```

The group is:

```text
1 → 2 → 3
```

and:

```text
groupNext = 4
```

We want to process:

```text
1
2
3
```

but **not**:

```text
4
```

Therefore:

```java
while (current != groupNext)
```

means:

> Reverse nodes until we reach the first node outside the current group.

This is more appropriate than:

```java
while (current != null)
```

because that would reverse the entire remaining linked list.

---

# 12. Reconnecting the Group

Before reversal:

```text
groupPrev
    ↓
dummy → 1 → 2 → 3 → 4
        ↑       ↑
      oldHead  kth
```

After reversing the nodes internally:

```text
3 → 2 → 1 → 4
```

But we still need `groupPrev` to point to the new first node:

```text
dummy → 3
```

The new first node is:

```text
kth
```

Therefore:

```java
groupPrev.next = kth;
```

---

# 13. Moving `groupPrev`

This is one of the most important parts of the problem.

Before reversal:

```text
1 → 2 → 3
↑
old first node
```

After reversal:

```text
3 → 2 → 1
        ↑
    old first node
```

The old first node becomes the **last node of the reversed group**.

That node must become the new:

```text
groupPrev
```

for the next iteration.

So before changing connections, save:

```java
ListNode oldGroupStart = groupPrev.next;
```

After reversal:

```java
groupPrev = oldGroupStart;
```

---

# 14. Full Group Transformation

Before:

```text
groupPrev
    ↓
    P → 1 → 2 → 3 → 4
        |-------|
           k=3
```

Save:

```text
oldGroupStart = 1
kth = 3
groupNext = 4
```

Reverse:

```text
3 → 2 → 1 → 4
```

Reconnect:

```text
P → 3 → 2 → 1 → 4
```

Move:

```text
groupPrev = 1
```

Now:

```text
P → 3 → 2 → 1 → 4 ...
                ↑
            groupPrev
```

The algorithm is ready to find the next group.

---

# 15. Pseudocode

```text
create dummy node

dummy.next = head

groupPrev = dummy


while true

    find kth node starting after groupPrev

    if kth does not exist
        stop


    groupNext = kth.next

    oldGroupStart = groupPrev.next


    previous = groupNext

    current = oldGroupStart


    while current != groupNext

        save current.next

        reverse current.next

        move previous

        move current


    connect groupPrev.next to kth

    move groupPrev to oldGroupStart


return dummy.next
```

---

# 16. Java Solution

```java
class Solution {

    public ListNode reverseKGroup(ListNode head, int k) {

        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode groupPrev = dummy;

        while (true) {

            // Find the kth node of the current group
            ListNode kth = getKth(groupPrev, k);

            // Fewer than k nodes remain
            if (kth == null) {
                break;
            }

            // First node after the current group
            ListNode groupNext = kth.next;

            // Old first node becomes the last node
            // after reversal
            ListNode oldGroupStart = groupPrev.next;

            // Reverse the current group
            ListNode previous = groupNext;
            ListNode current = oldGroupStart;

            while (current != groupNext) {

                ListNode next = current.next;

                current.next = previous;

                previous = current;

                current = next;
            }

            // Connect previous group to new group head
            groupPrev.next = kth;

            // Old group head is now the group tail
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
```

---

# 17. Manual Walkthrough — k = 2

Input:

```text
1 → 2 → 3 → 4 → 5
```

Set:

```text
k = 2
```

Add dummy:

```text
dummy → 1 → 2 → 3 → 4 → 5
  ↑
groupPrev
```

---

## Group 1

Find the 2nd node:

```text
dummy → 1 → 2 → 3 → 4 → 5
            ↑
           kth
```

Therefore:

```text
kth = 2
groupNext = 3
oldGroupStart = 1
```

We need to reverse:

```text
1 → 2
```

Initialize:

```text
previous = 3
current = 1
```

### First reversal

Save:

```text
next = 2
```

Reverse:

```text
1 → 3
```

Move:

```text
previous = 1
current = 2
```

### Second reversal

Save:

```text
next = 3
```

Reverse:

```text
2 → 1
```

Move:

```text
previous = 2
current = 3
```

Now:

```text
current == groupNext
```

Stop.

Connect:

```text
dummy → 2 → 1 → 3 → 4 → 5
```

Move:

```text
groupPrev = 1
```

---

## Group 2

Current list:

```text
dummy → 2 → 1 → 3 → 4 → 5
                ↑
            groupPrev
```

Find next 2 nodes:

```text
3 → 4
```

So:

```text
kth = 4
groupNext = 5
```

Reverse:

```text
3 → 4
```

into:

```text
4 → 3
```

Reconnect:

```text
dummy → 2 → 1 → 4 → 3 → 5
```

Move:

```text
groupPrev = 3
```

---

## Remaining Nodes

Only:

```text
5
```

remains.

We need:

```text
k = 2
```

nodes.

`getKth()` returns:

```text
null
```

Therefore:

```java
break;
```

Node `5` remains unchanged.

Final result:

```text
2 → 1 → 4 → 3 → 5
```

---

# 18. Manual Walkthrough — k = 3

Input:

```text
1 → 2 → 3 → 4 → 5
```

First group:

```text
1 → 2 → 3
```

Reverse:

```text
3 → 2 → 1
```

Reconnect:

```text
3 → 2 → 1 → 4 → 5
```

Now only:

```text
4 → 5
```

remain.

Since:

```text
2 < k
```

they remain unchanged.

Final:

```text
3 → 2 → 1 → 4 → 5
```

---

# 19. Control Flow — Outer `while`

We use:

```java
while (true)
```

because we repeatedly process groups until we discover that a complete group no longer exists.

The actual termination condition is found inside the loop:

```java
if (kth == null) {
    break;
}
```

Conceptually:

```text
Keep trying to process another group.

If a complete group exists:
    reverse it.

If not:
    stop.
```

---

# 20. Why Not `while (groupPrev != null)`?

That would not be enough.

The question is not:

> Does another node exist?

The question is:

> Do at least `k` nodes exist?

For example:

```text
4 → 5
```

still contains nodes.

But if:

```text
k = 3
```

those nodes must **not** be reversed.

Therefore we specifically need:

```text
getKth(groupPrev, k)
```

to determine whether a complete group exists.

---

# 21. Control Flow — `if (kth == null)`

We use:

```java
if (kth == null) {
    break;
}
```

because this represents one special condition:

```text
fewer than k nodes remain
```

If true:

```text
stop processing
```

If false:

```text
continue normally
```

There is no need for:

```java
else
```

because after `break`, execution cannot continue inside the loop anyway.

---

# 22. Control Flow — Inner `while`

The inner loop is:

```java
while (current != groupNext)
```

Its job is completely different from the outer loop.

### Outer loop

Controls:

```text
which group are we processing?
```

### Inner loop

Controls:

```text
which node inside this group are we reversing?
```

The inner loop stops when it reaches:

```text
groupNext
```

because that node belongs to the next group.

---

# 23. Why Not Use a `for` Loop for Reversal?

We know that exactly `k` nodes should be reversed, so technically we could write:

```java
for (int i = 0; i < k; i++)
```

However, after identifying:

```text
groupNext
```

the pointer-based condition:

```java
current != groupNext
```

makes the group boundary explicit.

It says directly:

> Reverse until reaching the node outside this group.

A `for` loop is also valid, but the boundary-based `while` fits linked-list pointer reasoning naturally.

---

# 24. Why Use a `while` in `getKth()`?

The helper uses:

```java
while (current != null && k > 0)
```

We have two reasons to continue:

1. There are still nodes.
2. We have not yet moved `k` positions.

If:

```text
current == null
```

the list ended too early.

If:

```text
k == 0
```

we successfully reached the kth node.

---

# 25. Why `&&` in `getKth()`?

```java
while (current != null && k > 0)
```

Both conditions must be true.

We can only continue moving when:

```text
there is another node
AND
we still need to move farther
```

Using `||` would be incorrect because it could continue when `current` is already `null`.

---

# 26. Pointer Invariant

During reversal, maintain:

```text
previous = head of reversed portion

current = first node not yet reversed
```

Initially:

```text
previous = groupNext

current = first node of group
```

After every iteration:

```text
one additional node moves
from the unprocessed portion
to the reversed portion
```

This is the same invariant used in **206. Reverse Linked List**.

---

# 27. Why Start `previous = groupNext`?

This is an important optimization.

We could reverse the group with:

```java
previous = null;
```

and then separately connect the old first node to `groupNext`.

But instead:

```java
previous = groupNext;
```

automatically makes the old first node point to the next group during reversal.

Example:

```text
group:
1 → 2 → 3

groupNext:
4
```

Starting:

```text
previous = 4
```

eventually produces:

```text
3 → 2 → 1 → 4
```

without needing another connection afterward.

---

# 28. Why Save `oldGroupStart`?

Before reversal:

```text
groupPrev → 1 → 2 → 3
            ↑
       oldGroupStart
```

After reversal:

```text
groupPrev → 3 → 2 → 1
                    ↑
               oldGroupStart
```

The old group start becomes the new group tail.

We need it because the next group's `groupPrev` should be:

```text
1
```

Therefore save it before modifying the links:

```java
ListNode oldGroupStart = groupPrev.next;
```

---

# 29. Why Return `dummy.next`?

The dummy node is temporary.

Suppose:

```text
dummy → 1 → 2 → 3
```

After reversing the first group:

```text
dummy → 2 → 1 → 3
```

The actual head is:

```text
dummy.next
```

which is:

```text
2
```

Therefore:

```java
return dummy.next;
```

---

# 30. Edge Cases

## k = 1

Input:

```text
1 → 2 → 3
```

with:

```text
k = 1
```

Every group contains one node.

Reversing one node changes nothing.

Output:

```text
1 → 2 → 3
```

Correct.

---

## k = List Length

Input:

```text
1 → 2 → 3 → 4
```

with:

```text
k = 4
```

The entire list is one group.

Output:

```text
4 → 3 → 2 → 1
```

This becomes equivalent to reversing the whole linked list.

---

## Length Not Divisible by k

Input:

```text
1 → 2 → 3 → 4 → 5
```

with:

```text
k = 3
```

Reverse:

```text
1 → 2 → 3
```

into:

```text
3 → 2 → 1
```

Leave:

```text
4 → 5
```

unchanged.

Output:

```text
3 → 2 → 1 → 4 → 5
```

---

## Exactly One Complete Group

```text
head = [1,2]
k = 2
```

Output:

```text
2 → 1
```

---

# 31. Complexity Analysis

Let:

```text
n = number of nodes
```

Each node is visited a constant number of times.

`getKth()` scans groups, and reversal processes the same nodes again, but the total work is still proportional to `n`.

Therefore:

```text
Time Complexity:
O(n)
```

The iterative solution only uses a constant number of pointers:

```text
dummy
groupPrev
kth
groupNext
oldGroupStart
previous
current
next
```

The number of variables does not increase with `n`.

Therefore:

```text
Space Complexity:
O(1)
```

---

# 32. Why Not Store Nodes in an Array?

We could:

```text
linked list
    ↓
store nodes in array
    ↓
reverse groups in array
    ↓
reconnect nodes
```

But that would require:

```text
O(n)
```

extra space.

The pointer solution achieves:

```text
O(1)
```

auxiliary space.

---

# 33. Why Not Change Node Values?

The problem explicitly states that node values may not be altered.

So this is not allowed conceptually:

```text
1 → 2

swap values

2 → 1
```

Instead, we must change the actual links:

```text
Before:

1 → 2

After:

2 → 1
```

This is a pointer manipulation problem.

---

# 34. Relationship to Reverse Linked List

This problem is an extension of:

```text
206. Reverse Linked List
```

The core reversal remains:

```java
ListNode next = current.next;

current.next = previous;

previous = current;

current = next;
```

The difference is the stopping condition.

### Reverse entire list

```java
while (current != null)
```

### Reverse one k-group

```java
while (current != groupNext)
```

This is an important pattern:

> Once you understand how to reverse an entire linked list, you can reverse a section by changing the boundaries.

---

# 35. Relationship to Merge Problems

Earlier linked-list problems used a dummy node for constructing results:

```text
21. Merge Two Sorted Lists
23. Merge k Sorted Lists
```

This problem uses the same general dummy-node principle:

```text
dummy
  ↓
stable node before the real head
```

The difference is that here the dummy node helps us reconnect reversed groups rather than append sorted nodes.

---

# 36. Common Mistakes

## Mistake 1: Reversing Before Checking k Nodes

Do not begin reversing until you know a complete group exists.

Otherwise:

```text
k = 3

remaining:
4 → 5
```

might incorrectly become:

```text
5 → 4
```

It must remain:

```text
4 → 5
```

---

## Mistake 2: Losing `groupNext`

Before reversing, save:

```java
ListNode groupNext = kth.next;
```

Otherwise you may lose the connection to the rest of the linked list.

---

## Mistake 3: Reversing Until `null`

Wrong:

```java
while (current != null)
```

That reverses everything remaining.

Correct:

```java
while (current != groupNext)
```

This reverses exactly the current group.

---

## Mistake 4: Forgetting to Reconnect `groupPrev`

After reversal:

```java
groupPrev.next = kth;
```

is required.

Otherwise the previous portion of the list may not point to the new head of the reversed group.

---

## Mistake 5: Moving `groupPrev` to `kth`

After reversal, `kth` is the **first** node of the reversed group.

But we need `groupPrev` to point to the **last** node before processing the next group.

Therefore this would be wrong:

```java
groupPrev = kth;
```

Instead:

```java
groupPrev = oldGroupStart;
```

because the old group start became the new group tail.

---

## Mistake 6: Changing Node Values

Do not solve this by swapping:

```java
node.val
```

The nodes themselves must be rearranged.

---

# 37. Interview Explanation

I use a dummy node so that reversing the first group does not require special handling.

For each group, I first find the kth node. If it doesn't exist, fewer than `k` nodes remain, so I stop and leave those nodes unchanged.

I save `kth.next` as `groupNext` because it marks the boundary after the group.

Then I reverse the current group using the standard three-pointer linked-list reversal pattern. The key difference from reversing an entire list is that I stop when `current == groupNext`.

I initialize `previous` to `groupNext`, which automatically connects the tail of the reversed group to the remaining list.

After reversal, I connect `groupPrev.next` to `kth`, which is now the new head of the group.

Finally, I move `groupPrev` to the old first node of the group because that node became the group's tail.

The algorithm processes every node a constant number of times, so it runs in `O(n)` time and uses `O(1)` auxiliary space.

---

# 38. What I Learned

- Reverse Nodes in k-Group extends the standard linked-list reversal pattern.
- Before modifying pointers, first verify that a complete group of `k` nodes exists.
- A dummy node eliminates special handling for changes to the real head.
- `groupPrev` identifies the node before the group.
- `kth` identifies the final node in the group.
- `groupNext` identifies the first node outside the group.
- The old group head becomes the new group tail.
- Save pointers before modifying links.
- `previous = groupNext` automatically reconnects the reversed group to the remaining list.
- The inner reversal stops at `groupNext`, not `null`.
- The outer loop controls groups; the inner loop controls nodes within a group.
- The iterative solution runs in `O(n)` time and `O(1)` auxiliary space.

---

# Pattern Recognition

When I see:

```text
linked list
+
reverse every k nodes
+
leave incomplete group unchanged
```

I should think:

```text
Dummy Node
    ↓
Find Group Boundary
    ↓
Do k nodes exist?
   /        \
 No          Yes
 ↓            ↓
Stop       Save groupNext
               ↓
          Reverse group
               ↓
          Reconnect group
               ↓
          Move groupPrev
               ↓
          Find next group
```

The core reversal pattern remains:

```java
ListNode next = current.next;

current.next = previous;

previous = current;

current = next;
```

But instead of:

```java
while (current != null)
```

use:

```java
while (current != groupNext)
```

The key mental model is:

```text
FIND → SAVE BOUNDARIES → REVERSE → RECONNECT → ADVANCE
```

That is the core pattern for reversing sections of a linked list.