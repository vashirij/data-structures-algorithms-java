# 206. Reverse Linked List

**Difficulty:** Easy  
**Topics:** Linked List, Recursion  
**Status:** Solved  
**LeetCode:** https://leetcode.com/problems/reverse-linked-list/

---

## Problem

Given the `head` of a singly linked list, reverse the list and return the reversed list.

### Example 1

```text
Input:  head = [1,2,3,4,5]
Output: [5,4,3,2,1]
```

### Example 2

```text
Input:  head = [1,2]
Output: [2,1]
```

### Example 3

```text
Input:  head = []
Output: []
```

### Constraints

- The number of nodes is in the range `[0, 5000]`.
- `-5000 <= Node.val <= 5000`

### Follow-up

A linked list can be reversed either:

1. Iteratively
2. Recursively

Implement both approaches.

---

# 1. Inputs

The input is:

```text
head
```

`head` is a reference to the first node of a singly linked list.

For example:

```text
head
 ↓
 1 → 2 → 3 → 4 → 5 → null
```

Each node contains:

```java
class ListNode {
    int val;
    ListNode next;
}
```

The important part for this problem is:

```java
next
```

because `next` determines which node comes after the current node.

---

# 2. Output

The function must return the new head after reversing the linked list.

Before:

```text
head
 ↓
 1 → 2 → 3 → 4 → 5 → null
```

After:

```text
head
 ↓
 5 → 4 → 3 → 2 → 1 → null
```

The node values themselves do not change.

Instead, the links between the nodes change direction.

---

# 3. Required Operation

The original links are:

```text
1 → 2 → 3 → 4 → 5 → null
```

We want:

```text
null ← 1 ← 2 ← 3 ← 4 ← 5
```

For each node, the operation is essentially:

```java
current.next = previous;
```

This changes:

```text
current → next
```

into:

```text
previous ← current
```

However, changing `current.next` immediately creates an important problem.

---

# 4. The Main Problem: Losing the Rest of the List

Consider:

```text
1 → 2 → 3 → 4
```

Suppose `current` points to `2`.

```text
1     2 → 3 → 4
↑     ↑
prev  current
```

If we immediately execute:

```java
current.next = previous;
```

we get:

```text
1 ← 2
```

But we have overwritten:

```text
2 → 3
```

If we did not save node `3`, we would lose access to:

```text
3 → 4
```

Therefore, before reversing the pointer, we must save the next node.

```java
ListNode next = current.next;
```

This leads to the three-pointer solution.

---

# 5. Iterative Approach

## Three Pointers

We use:

```text
previous
current
next
```

Each pointer has one responsibility.

### `previous`

Points to the node that should come after `current` once the link is reversed.

Initially:

```java
ListNode previous = null;
```

Why `null`?

Because the original first node eventually becomes the last node.

Before:

```text
1 → 2 → 3
```

After:

```text
3 → 2 → 1 → null
```

So node `1` must eventually point to `null`.

---

### `current`

Points to the node currently being processed.

Initially:

```java
ListNode current = head;
```

---

### `next`

Temporarily stores the next node before the current link is changed.

```java
ListNode next = current.next;
```

Without `next`, the remaining unprocessed portion of the list could be lost.

---

# 6. Iterative Algorithm

The algorithm follows four important operations:

```text
SAVE
 ↓
REVERSE
 ↓
MOVE PREVIOUS
 ↓
MOVE CURRENT
```

More specifically:

```text
1. previous = null
2. current = head

3. While current is not null:

       a. Save current.next in next

       b. Reverse the link:
          current.next = previous

       c. Move previous:
          previous = current

       d. Move current:
          current = next

4. Return previous
```

A useful memory phrase is:

> **Save → Reverse → Advance → Advance**

---

# 7. Why This Order Matters

Consider:

```text
previous    current       next
    ↓          ↓            ↓
   null        1     →      2 → 3
```

First:

```java
ListNode next = current.next;
```

Now the rest of the list is safe.

Then:

```java
current.next = previous;
```

produces:

```text
null ← 1       2 → 3
```

Then:

```java
previous = current;
```

moves `previous` forward.

Finally:

```java
current = next;
```

moves `current` to the next unprocessed node.

The order cannot be changed carelessly.

For example, if we reverse `current.next` before saving it, we can lose the rest of the list.

---

# 8. Control-Flow Reasoning

## Why Use `while`?

The iterative solution uses:

```java
while (current != null)
```

The meaning is:

> Keep processing nodes while another unprocessed node exists.

This is a natural `while` condition because termination depends on the state of a pointer.

We continue until:

```text
current = null
```

which means there are no more nodes to process.

---

## Why Not Use a `for` Loop?

A `for` loop is most natural when the traversal range is known in advance.

For example:

```java
for (int i = 0; i < nums.length; i++)
```

works naturally for arrays because we have indexes and a known length.

A linked list does not normally provide:

```text
list[0]
list[1]
list[2]
```

Instead, we move through references:

```java
current = current.next;
```

The important termination condition is:

```java
current != null
```

rather than an index reaching a specific number.

A `for` loop could technically be written, but `while` expresses the pointer-based termination condition more clearly.

---

## Why No `if` Inside the Loop?

Every node requires exactly the same operations:

```text
save next
reverse pointer
move previous
move current
```

There is no decision such as:

```text
Is the value larger?
Is the node duplicated?
Should this node be removed?
```

Therefore, an `if` statement is unnecessary inside the main iterative loop.

The `while` condition already determines whether another node exists to process.

---

# 9. Iterative Java Solution

```java
class Solution {

    public ListNode reverseList(ListNode head) {

        ListNode previous = null;
        ListNode current = head;

        while (current != null) {

            ListNode next = current.next;

            current.next = previous;

            previous = current;
            current = next;
        }

        return previous;
    }
}
```

---

# 10. Manual Walkthrough

Given:

```text
1 → 2 → 3 → null
```

Initialize:

```text
previous = null
current  = 1
```

---

## Iteration 1

### Before

```text
previous    current
    ↓          ↓
   null        1 → 2 → 3 → null
```

Save:

```java
next = current.next;
```

Therefore:

```text
next = 2
```

Reverse:

```java
current.next = previous;
```

Now:

```text
null ← 1       2 → 3 → null
```

Move:

```java
previous = current;
current = next;
```

Now:

```text
null ← 1       2 → 3 → null
       ↑       ↑
    previous current
```

---

## Iteration 2

Save:

```text
next = 3
```

Reverse:

```text
1 ← 2
```

Move:

```text
previous = 2
current = 3
```

Current state:

```text
null ← 1 ← 2       3 → null
             ↑     ↑
          previous current
```

---

## Iteration 3

Save:

```text
next = null
```

Reverse:

```text
null ← 1 ← 2 ← 3
```

Move:

```text
previous = 3
current = null
```

Now:

```text
null ← 1 ← 2 ← 3
                 ↑
              previous

current = null
```

The loop stops.

Return:

```java
return previous;
```

which points to:

```text
3 → 2 → 1 → null
```

---

# 11. Pointer-State Table

For:

```text
1 → 2 → 3 → null
```

| Iteration | `previous` before | `current` | saved `next` | `previous` after |
|---:|---|---|---|---|
| Start | `null` | `1` | — | — |
| 1 | `null` | `1` | `2` | `1` |
| 2 | `1` | `2` | `3` | `2` |
| 3 | `2` | `3` | `null` | `3` |

Finally:

```text
current = null
previous = 3
```

Therefore `previous` is the new head.

---

# 12. Why Return `previous` Instead of `current`?

The loop terminates when:

```java
current == null
```

Therefore `current` cannot be the new head.

During the final iteration:

```java
previous = current;
current = next;
```

moves `previous` onto the final node while `current` becomes `null`.

Therefore:

```text
previous → new head
current  → null
```

So we return:

```java
return previous;
```

---

# 13. Edge Cases

## Empty List

Input:

```text
head = null
```

Initialization:

```text
previous = null
current = null
```

The condition:

```java
current != null
```

is false immediately.

Return:

```text
null
```

Correct output:

```text
[]
```

No special `if` statement is required because the general algorithm already handles this case.

---

## One Node

Input:

```text
1 → null
```

Initialize:

```text
previous = null
current = 1
```

Save:

```text
next = null
```

Reverse:

```text
1 → null
```

Move:

```text
previous = 1
current = null
```

Return:

```text
1 → null
```

Correct.

---

## Two Nodes

Input:

```text
1 → 2 → null
```

After reversal:

```text
2 → 1 → null
```

Correct.

---

# 14. Iterative Complexity Analysis

## Time Complexity

```text
O(n)
```

Every node is visited exactly once.

For each node, we perform a constant number of pointer operations.

Therefore:

```text
n nodes × O(1) work per node
= O(n)
```

---

## Space Complexity

```text
O(1)
```

We only maintain three references:

```text
previous
current
next
```

The number of extra variables does not increase with the size of the linked list.

Therefore the iterative solution uses constant auxiliary space.

---

# 15. Recursive Approach

The linked list can also be reversed recursively.

Consider:

```text
1 → 2 → 3 → null
```

Instead of reversing node `1` immediately, ask recursion to reverse:

```text
2 → 3 → null
```

Suppose recursion returns:

```text
3 → 2 → null
```

We still have:

```text
1 → 2
```

We need to place `1` after `2`.

So we change:

```text
2 → null
```

into:

```text
2 → 1
```

This can be expressed as:

```java
head.next.next = head;
```

Then the old forward link must be removed:

```java
head.next = null;
```

---

# 16. Recursive Base Case

Every recursive algorithm needs a stopping condition.

We stop when:

```java
head == null
```

or:

```java
head.next == null
```

Therefore:

```java
if (head == null || head.next == null) {
    return head;
}
```

Why?

### Empty list

```text
null
```

There is nothing to reverse.

### One-node list

```text
1 → null
```

A single node is already reversed.

---

# 17. Recursive Algorithm

```text
1. If head is null:
       return head

2. If head.next is null:
       return head

3. Recursively reverse everything after head.

4. Make the next node point back to head.

5. Make head point to null.

6. Return the new head obtained from recursion.
```

---

# 18. Recursive Java Solution

```java
class Solution {

    public ListNode reverseList(ListNode head) {

        if (head == null || head.next == null) {
            return head;
        }

        ListNode newHead = reverseList(head.next);

        head.next.next = head;
        head.next = null;

        return newHead;
    }
}
```

---

# 19. Understanding `head.next.next = head`

This is the most difficult line in the recursive solution.

Consider:

```text
1 → 2 → 3
↑
head
```

After recursively reversing the list beginning at `2`, conceptually we have:

```text
3 → 2
    ↑
 head.next
```

We need:

```text
3 → 2 → 1
```

`head.next` refers to node `2`.

Therefore:

```java
head.next.next
```

refers to node `2`'s `next`.

Setting:

```java
head.next.next = head;
```

means:

```text
2.next = 1
```

which creates:

```text
2 → 1
```

---

# 20. Why `head.next = null`?

After:

```java
head.next.next = head;
```

the original link still exists:

```text
1 → 2
```

while we just created:

```text
2 → 1
```

Without removing the original link, we would create a cycle:

```text
1 → 2
↑   ↓
└───┘
```

Therefore:

```java
head.next = null;
```

breaks the original forward link.

The result becomes:

```text
2 → 1 → null
```

---

# 21. Recursive Control-Flow Reasoning

The recursive solution uses:

```java
if (head == null || head.next == null)
```

because recursion requires a base case.

The `if` asks:

> Has the recursive problem become small enough that no more reversal is necessary?

If yes:

```java
return head;
```

Otherwise, recursion continues.

Unlike the iterative approach, we do not need a `while` loop because the recursive calls themselves perform the repeated processing.

---

# 22. Recursive Complexity Analysis

## Time Complexity

```text
O(n)
```

Each node is processed once.

---

## Space Complexity

```text
O(n)
```

Although no new linked list is created, every recursive call occupies space on the call stack.

For:

```text
1 → 2 → 3 → 4 → 5
```

the calls conceptually become:

```text
reverse(1)
    reverse(2)
        reverse(3)
            reverse(4)
                reverse(5)
```

The recursion depth can grow to `n`.

Therefore:

```text
O(n)
```

auxiliary stack space is required.

---

# 23. Iterative vs Recursive

| Characteristic | Iterative | Recursive |
|---|---|---|
| Time | `O(n)` | `O(n)` |
| Extra Space | `O(1)` | `O(n)` |
| Main mechanism | Three pointers | Call stack |
| Main termination | `current == null` | Base case |
| Control flow | `while` | Recursion + `if` |
| Risk of stack overflow | No | Possible for large lists |
| Usually preferred | Yes | Useful to understand recursion |

For this problem, the iterative solution is generally preferable because it achieves:

```text
O(n) time
O(1) extra space
```

---

# 24. Why Not Copy Values Into an Array?

One possible approach would be:

```text
Linked List
    ↓
Array
    ↓
Reverse Array
    ↓
Rebuild Linked List
```

This is unnecessary.

It would require additional memory:

```text
O(n)
```

and would fail to take advantage of the fact that the existing nodes can simply have their links changed.

The iterative pointer solution achieves:

```text
O(1)
```

extra space.

---

# 25. Why Not Swap Node Values?

Another idea might be to keep the links unchanged and swap:

```text
1,2,3,4,5
```

into:

```text
5,4,3,2,1
```

But conceptually, the problem is about reversing the linked list structure.

The better linked-list technique is to manipulate:

```java
node.next
```

references.

This also develops the pointer manipulation skills needed for more advanced linked-list problems.

---

# 26. Common Mistakes

## Mistake 1: Reversing Before Saving `next`

Wrong order:

```java
current.next = previous;
ListNode next = current.next;
```

After the first statement, `current.next` no longer points to the original next node.

The rest of the list may be lost.

Correct order:

```java
ListNode next = current.next;
current.next = previous;
```

---

## Mistake 2: Moving `current` Using `current.next`

After reversing:

```java
current.next = previous;
```

this would be wrong:

```java
current = current.next;
```

because `current.next` now points **backward**.

Instead use the saved reference:

```java
current = next;
```

---

## Mistake 3: Returning `current`

At loop termination:

```text
current = null
```

Therefore:

```java
return current;
```

would incorrectly return an empty list.

Return:

```java
return previous;
```

---

## Mistake 4: Forgetting `head.next = null` in Recursion

Without:

```java
head.next = null;
```

the recursive approach can create cycles between nodes.

---

# 27. Interview Explanation

I use three pointers: `previous`, `current`, and `next`.

`current` points to the node being processed, `previous` points to the already-reversed portion of the list, and `next` temporarily stores the next unprocessed node.

For every node, I first save `current.next` so I don't lose access to the rest of the list. I then reverse the current link by assigning `current.next = previous`. After that, I move `previous` to `current` and move `current` to the saved `next` node.

I use a `while` loop because traversal continues while `current` points to an unprocessed node. When `current` becomes `null`, `previous` points to the new head.

The iterative algorithm visits every node once, giving `O(n)` time complexity, and it uses only three references, giving `O(1)` extra space.

The recursive solution also runs in `O(n)` time, but requires `O(n)` call-stack space.

---

# 28. What I Learned

- Linked-list problems are primarily about manipulating references rather than array indexes.
- Reversing a linked list means reversing each node's `next` pointer.
- Always save a pointer before overwriting it if it is the only reference to unprocessed data.
- The three important references are:
  - `previous`
  - `current`
  - `next`
- The iterative sequence is:

```text
SAVE
 ↓
REVERSE
 ↓
ADVANCE PREVIOUS
 ↓
ADVANCE CURRENT
```

- `while` is appropriate because termination depends on pointer state.
- `for` is less natural because linked lists are not normally traversed using indexes.
- No `if` is required inside the iterative loop because every node receives the same processing.
- Recursion requires a base case.
- The recursive solution reverses the smaller list first and then reconnects the current node.
- `head.next.next = head` reverses the link in the recursive solution.
- `head.next = null` prevents a cycle.
- Both approaches take `O(n)` time.
- Iteration uses `O(1)` extra space.
- Recursion uses `O(n)` call-stack space.

---

# Pattern Recognition

When I see a linked-list problem involving:

```text
reverse
reconnect
reorder
remove
insert
```

I should think about:

```text
What does each pointer represent?

What reference am I about to overwrite?

Do I need to save the next node first?

Which pointer should move?

What condition tells me traversal is finished?
```

For linked-list reversal specifically, remember:

```text
previous = null
current = head

while current != null:

    next = current.next
    current.next = previous

    previous = current
    current = next

return previous
```

This is the core iterative linked-list reversal pattern.