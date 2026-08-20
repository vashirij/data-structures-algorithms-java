# 23. Merge k Sorted Lists

**Difficulty:** Hard
**Topics:** Linked List, Divide and Conquer, Heap (Priority Queue), Merge Sort
**Status:** Solved
**LeetCode:** https://leetcode.com/problems/merge-k-sorted-lists/

---

## Original Question

You are given an array of `k` linked lists `lists`, where each linked list is sorted in ascending order.

Merge all the linked lists into one sorted linked list and return it.

---

## Example 1

```text
Input:
lists = [[1,4,5],[1,3,4],[2,6]]

Output:
[1,1,2,3,4,4,5,6]
```

The linked lists are:

```text
1 → 4 → 5

1 → 3 → 4

2 → 6
```

Merged:

```text
1 → 1 → 2 → 3 → 4 → 4 → 5 → 6
```

---

## Example 2

```text
Input:
lists = []

Output:
[]
```

---

## Example 3

```text
Input:
lists = [[]]

Output:
[]
```

---

# 1. Inputs

The input is:

```text
lists
```

`lists` is an array containing the heads of `k` sorted linked lists.

For example:

```text
lists[0] → 1 → 4 → 5

lists[1] → 1 → 3 → 4

lists[2] → 2 → 6
```

Each individual linked list is already sorted in ascending order.

---

# 2. Output

Return the head of one sorted linked list containing all nodes from all input lists.

For:

```text
1 → 4 → 5

1 → 3 → 4

2 → 6
```

the output is:

```text
1 → 1 → 2 → 3 → 4 → 4 → 5 → 6
```

---

# 3. Important Constraints

```text
k == lists.length
0 <= k <= 10^4
```

Each individual list may contain up to:

```text
500 nodes
```

The total number of nodes across all lists is at most:

```text
10^4
```

Because `k` can be large, repeatedly scanning all lists for the smallest node would be inefficient.

---

# 4. First Observation

Every linked list is already sorted.

Therefore, the smallest remaining value in each list must always be at that list's current head.

For example:

```text
List 1:
1 → 4 → 5
↑
candidate

List 2:
1 → 3 → 4
↑
candidate

List 3:
2 → 6
↑
candidate
```

The next value in the merged list must therefore be the smallest among:

```text
1, 1, 2
```

Once one head is selected, only that linked list needs to move forward.

---

# 5. Simple Brute-Force Idea

One possible approach would be:

1. Look at the current head of every list.
2. Find the smallest value.
3. Add that node to the merged list.
4. Move forward in the selected list.
5. Repeat.

If there are `k` lists and `N` total nodes, scanning all `k` list heads for every node can cost approximately:

```text
O(N * k)
```

This can become expensive when:

```text
k = 10^4
```

We need a better way to repeatedly find the smallest current node.

---

# 6. Key Idea — Min Heap / Priority Queue

A **min heap** is designed to efficiently retrieve the smallest element.

Java's:

```java
PriorityQueue
```

can be used as a min heap.

Instead of repeatedly scanning all `k` list heads, we store the current head of each non-empty list in the heap.

The heap automatically keeps the smallest node accessible at the top.

---

# 7. Why a Priority Queue?

We repeatedly need this operation:

> Give me the smallest current node among all active linked lists.

A priority queue supports:

```text
insert element       → O(log k)
remove smallest      → O(log k)
peek smallest        → O(1)
```

This is much better than checking all `k` lists every time.

---

# 8. Initial Heap

For:

```text
lists = [
    1 → 4 → 5,
    1 → 3 → 4,
    2 → 6
]
```

insert the heads:

```text
1
1
2
```

into the priority queue.

Conceptually:

```text
        1
       / \
      1   2
```

The smallest node can now be removed efficiently.

---

# 9. Dummy Node Pattern

As in **21. Merge Two Sorted Lists**, we use:

```java
ListNode dummy = new ListNode(0);
ListNode current = dummy;
```

The dummy node gives us a fixed starting point.

`current` tracks the end of the merged list.

Initially:

```text
dummy/current
     ↓
     0
```

When the smallest node is removed from the heap:

```java
current.next = smallest;
current = current.next;
```

---

# 10. Important Heap Operation

Suppose we remove:

```text
1 → 4 → 5
```

from the heap.

We add that `1` to the merged list.

But that linked list still contains:

```text
4 → 5
```

The next possible candidate from that list is now:

```text
4
```

Therefore, after removing a node:

```java
ListNode smallest = minHeap.poll();
```

we check:

```java
smallest.next
```

If it exists, add it to the heap:

```java
minHeap.offer(smallest.next);
```

This ensures the heap always contains at most one current candidate from each active list.

---

# 11. Algorithm

1. Create a min heap.
2. Insert the head of every non-empty linked list.
3. Create a dummy node.
4. Set `current = dummy`.
5. While the heap is not empty:

   * Remove the smallest node.
   * Attach it to the merged list.
   * Move `current`.
   * If the removed node has a next node, insert that next node into the heap.
6. When the heap becomes empty, all nodes have been processed.
7. Return `dummy.next`.

---

# 12. Pseudocode

```text
create minHeap

for every list in lists

    if list is not null
        insert list head into minHeap


create dummy node

current = dummy


while minHeap is not empty

    smallest = remove smallest node

    current.next = smallest

    current = current.next

    if smallest.next exists
        insert smallest.next into minHeap


return dummy.next
```

---

# 13. Control-Flow Reasoning

## Why Use a `for` Loop First?

We need to inspect each position in the `lists` array exactly once.

We know the traversal range:

```text
0 through lists.length - 1
```

Therefore:

```java
for (ListNode node : lists)
```

is appropriate.

The purpose is simply:

> Check each list head and add non-empty heads to the heap.

A `while` loop would work, but it would require manually maintaining an array index without providing an advantage.

---

# 14. Why Use `if` During Initialization?

Some lists may be empty.

For example:

```text
lists = [
    null,
    1 → 2,
    null,
    3 → 4
]
```

We must not add:

```text
null
```

to the priority queue.

Therefore:

```java
if (node != null)
```

is appropriate.

There is no useful action for an empty list, so no `else` block is necessary.

---

# 15. Why Use a `while` Loop for the Heap?

The main processing loop is:

```java
while (!minHeap.isEmpty())
```

We do not know beforehand exactly how the heap size will change.

During each iteration:

```text
one node is removed
```

and potentially:

```text
another node is inserted
```

The process should continue until no candidate nodes remain.

Therefore the algorithm is naturally:

> Keep processing while the heap is not empty.

This makes `while` more appropriate than `for`.

---

# 16. Why Not Use a `for` Loop for the Main Merge?

A `for` loop would require us to know exactly how many iterations are necessary.

Although the total number of nodes could theoretically be counted first, doing so would require another traversal and would not express the actual algorithm clearly.

The real termination condition is:

```text
heap is empty
```

Therefore:

```java
while (!minHeap.isEmpty())
```

is clearer.

---

# 17. Why Use `if (smallest.next != null)`?

After selecting a node, we need to determine whether its original linked list still contains another node.

If:

```text
smallest.next != null
```

there is another candidate to add to the heap.

If:

```text
smallest.next == null
```

that linked list is finished.

No action is required in the false case.

Therefore a simple `if` is sufficient.

---

# 18. Java Solution — Priority Queue

```java
import java.util.PriorityQueue;

class Solution {

    public ListNode mergeKLists(ListNode[] lists) {

        PriorityQueue<ListNode> minHeap =
            new PriorityQueue<>(
                (a, b) -> Integer.compare(a.val, b.val)
            );

        // Add the first node of every non-empty list
        for (ListNode node : lists) {

            if (node != null) {
                minHeap.offer(node);
            }
        }

        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        while (!minHeap.isEmpty()) {

            // Get the smallest current node
            ListNode smallest = minHeap.poll();

            // Attach it to the merged list
            current.next = smallest;
            current = current.next;

            // Add the next node from that same list
            if (smallest.next != null) {
                minHeap.offer(smallest.next);
            }
        }

        return dummy.next;
    }
}
```

---

# 19. Manual Walkthrough

Given:

```text
list1 = 1 → 4 → 5
list2 = 1 → 3 → 4
list3 = 2 → 6
```

Initial heap contains:

```text
1, 1, 2
```

Merged list:

```text
dummy → null
```

---

## Iteration 1

Remove smallest:

```text
1
```

Suppose this comes from:

```text
1 → 4 → 5
```

Attach:

```text
dummy → 1
```

Its next node is:

```text
4
```

Add `4` to heap.

Heap now contains conceptually:

```text
1, 2, 4
```

---

## Iteration 2

Remove:

```text
1
```

Attach:

```text
dummy → 1 → 1
```

Its next node:

```text
3
```

Add `3`.

Heap:

```text
2, 3, 4
```

---

## Iteration 3

Remove:

```text
2
```

Merged:

```text
1 → 1 → 2
```

Its next node:

```text
6
```

Add `6`.

Heap:

```text
3, 4, 6
```

---

## Iteration 4

Remove:

```text
3
```

Merged:

```text
1 → 1 → 2 → 3
```

Add its next node:

```text
4
```

Heap:

```text
4, 4, 6
```

---

## Remaining Process

Continue removing:

```text
4
4
5
6
```

Final merged list:

```text
1 → 1 → 2 → 3 → 4 → 4 → 5 → 6
```

---

# 20. Why Is the Result Sorted?

At any moment, the heap contains the smallest unprocessed node from every active list.

Because each original list is sorted, no node after a current head can be smaller than that current head.

Therefore the globally smallest remaining node must be one of the nodes currently in the heap.

Removing the heap minimum repeatedly guarantees sorted output.

---

# 21. Complexity Analysis

Let:

```text
N = total number of nodes across all lists
k = number of linked lists
```

The heap contains at most:

```text
k
```

nodes at any time.

For every one of the `N` nodes:

* It may be inserted into the heap once.
* It may be removed from the heap once.

Each heap operation costs:

```text
O(log k)
```

Therefore:

```text
Time Complexity:
O(N log k)
```

---

## Space Complexity

The heap contains at most one node from each list.

Therefore:

```text
O(k)
```

extra space is required.

The merged list reuses the existing nodes.

So:

```text
Auxiliary Space:
O(k)
```

---

# 22. Why Not Put Every Node Into the Heap Immediately?

One alternative would be:

```text
visit every node
put all N nodes into heap
remove all N nodes
```

That would require a heap containing up to:

```text
N
```

nodes.

Space:

```text
O(N)
```

and each heap operation would cost approximately:

```text
O(log N)
```

Instead, we only keep one candidate from each list:

```text
O(k)
```

heap space.

This takes advantage of the fact that every individual linked list is already sorted.

---

# 23. Alternative — Repeatedly Merge Two Lists

Because we already know how to solve:

```text
21. Merge Two Sorted Lists
```

one approach is:

```text
result = null

for every list:
    result = mergeTwoLists(result, list)
```

This works.

However, repeatedly merging into a growing list can result in poor performance.

In the worst case, it can approach:

```text
O(Nk)
```

depending on the list sizes.

---

# 24. Alternative — Divide and Conquer

A stronger alternative is to merge lists in pairs.

For example:

```text
L1   L2   L3   L4
 \   /     \   /
 merge     merge
    \       /
      merge
```

Instead of:

```text
merge L1 with L2
then merge result with L3
then merge result with L4
```

we merge lists in balanced pairs.

This creates approximately:

```text
log k
```

merge levels.

At each level, all `N` nodes are processed once.

Therefore:

```text
Time:
O(N log k)
```

This matches the heap solution asymptotically.

---

# 25. Divide and Conquer Java Solution

```java
class Solution {

    public ListNode mergeKLists(ListNode[] lists) {

        if (lists == null || lists.length == 0) {
            return null;
        }

        int interval = 1;

        while (interval < lists.length) {

            for (
                int i = 0;
                i + interval < lists.length;
                i += interval * 2
            ) {

                lists[i] =
                    mergeTwoLists(
                        lists[i],
                        lists[i + interval]
                    );
            }

            interval *= 2;
        }

        return lists[0];
    }


    private ListNode mergeTwoLists(
        ListNode list1,
        ListNode list2
    ) {

        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        while (list1 != null && list2 != null) {

            if (list1.val <= list2.val) {

                current.next = list1;
                list1 = list1.next;

            } else {

                current.next = list2;
                list2 = list2.next;
            }

            current = current.next;
        }

        current.next =
            list1 != null
                ? list1
                : list2;

        return dummy.next;
    }
}
```

---

# 26. Heap vs Divide and Conquer

| Characteristic       | Min Heap                     | Divide & Conquer              |
| -------------------- | ---------------------------- | ----------------------------- |
| Time                 | `O(N log k)`                 | `O(N log k)`                  |
| Extra Space          | `O(k)` heap                  | `O(1)` iterative merging      |
| Main structure       | Priority Queue               | Pairwise merge                |
| Uses Merge Two Lists | No direct helper required    | Yes                           |
| Easy to reason about | Yes                          | Moderate                      |
| Main idea            | Always select global minimum | Merge lists in balanced pairs |

Both are excellent solutions.

For interviews, the min-heap approach is often the most intuitive when the problem says:

> Merge `k` sorted sequences.

---

# 27. Edge Cases

## No Lists

```text
lists = []
```

Heap starts empty.

The main loop never executes.

Return:

```text
null
```

Correct:

```text
[]
```

---

## One Empty List

```text
lists = [[]]
```

The only list head is:

```text
null
```

Nothing is inserted into the heap.

Return:

```text
[]
```

---

## One Non-Empty List

```text
lists = [[1,2,3]]
```

Initial heap contains:

```text
1
```

The algorithm processes:

```text
1
2
3
```

Result:

```text
[1,2,3]
```

---

## Lists With Duplicate Values

```text
lists = [
    [1,1,3],
    [1,2,2]
]
```

Result:

```text
[1,1,1,2,2,3]
```

Duplicates require no special handling.

---

## Negative Values

```text
lists = [
    [-5,-2],
    [-4,-1]
]
```

Result:

```text
[-5,-4,-2,-1]
```

---

# 28. Common Mistakes

## Mistake 1: Adding `null` to the Heap

Always check:

```java
if (node != null)
```

before:

```java
minHeap.offer(node);
```

---

## Mistake 2: Forgetting to Add the Next Node

After:

```java
ListNode smallest = minHeap.poll();
```

the selected list may still have nodes.

If:

```java
smallest.next != null
```

it must be inserted into the heap.

Otherwise, the rest of that linked list will never be processed.

---

## Mistake 3: Adding Every Node at the Start

This loses the benefit of using the sorted structure of the individual lists and uses unnecessary memory.

Only add the current head from each active list.

---

## Mistake 4: Forgetting to Move `current`

After:

```java
current.next = smallest;
```

you must execute:

```java
current = current.next;
```

Otherwise new nodes will repeatedly overwrite the same connection.

---

## Mistake 5: Wrong Priority Queue Ordering

A min heap should prioritize smaller values.

Use:

```java
(a, b) -> Integer.compare(a.val, b.val)
```

rather than subtraction such as:

```java
a.val - b.val
```

Using `Integer.compare` avoids potential integer-overflow problems in more general situations.

---

# 29. Interview Explanation

Each linked list is individually sorted, so the smallest remaining element from each list is always at its current head.

I maintain those current heads in a min heap.

Initially, I insert the head of every non-empty list.

Then, while the heap is not empty, I remove the smallest node, attach it to the merged list, and if that node has a next node, I insert the next node into the heap.

This ensures the heap always contains at most one candidate from each active linked list.

I use a `for` loop to initialize the heap because I know I need to inspect each list exactly once.

I use a `while` loop for the merge because the process continues until the heap becomes empty.

The heap has at most `k` elements, and every one of the `N` nodes is inserted and removed at most once.

Therefore the time complexity is:

```text
O(N log k)
```

and the auxiliary space complexity is:

```text
O(k)
```

---

# 30. What I Learned

* Multiple sorted sequences often suggest a min heap.
* In a sorted linked list, the current head is the smallest unprocessed value in that list.
* A priority queue can efficiently choose the smallest candidate across multiple lists.
* Keep only one candidate per active list instead of putting every node into the heap.
* After selecting a node, insert its next node as the new candidate from that list.
* A dummy node simplifies construction of the merged linked list.
* `for` is appropriate for the known initialization traversal.
* `while` is appropriate when processing continues until a data structure becomes empty.
* A simple `if` is enough when an action is only required for non-null nodes.
* Min-heap merging gives `O(N log k)` time.
* Divide-and-conquer merging also gives `O(N log k)` time.
* Problems involving `k` sorted inputs often have better solutions than repeatedly scanning all `k` sources.

---

# Pattern Recognition

When I see:

```text
k sorted lists / arrays / streams
+
repeatedly need smallest element
```

I should consider:

```text
Min Heap / Priority Queue
```

Pattern:

```text
Insert first candidate from each source
            ↓
Remove global minimum
            ↓
Add it to result
            ↓
Insert next candidate from same source
            ↓
Repeat until heap empty
```

Core implementation:

```java
PriorityQueue<ListNode> minHeap =
    new PriorityQueue<>(
        (a, b) -> Integer.compare(a.val, b.val)
    );

for (ListNode node : lists) {
    if (node != null) {
        minHeap.offer(node);
    }
}

while (!minHeap.isEmpty()) {

    ListNode smallest = minHeap.poll();

    current.next = smallest;
    current = current.next;

    if (smallest.next != null) {
        minHeap.offer(smallest.next);
    }
}
```

This is the **k-way merge pattern**.