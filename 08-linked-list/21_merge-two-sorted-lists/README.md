# 21. Merge Two Sorted Lists

**Difficulty:** Easy  
**Topics:** Linked List, Recursion  
**Status:** Solved  
**LeetCode:** https://leetcode.com/problems/merge-two-sorted-lists/

---

## Problem

You are given the heads of two sorted linked lists `list1` and `list2`.

Merge the two lists into one **sorted** list.

The list should be made by splicing together the existing nodes of the first two lists.

Return the head of the merged linked list.

---

## Example 1

```text
Input:
list1 = [1,2,4]
list2 = [1,3,4]

Output:
[1,1,2,3,4,4]
```

Visual representation:

```text
list1:
1 → 2 → 4 → null

list2:
1 → 3 → 4 → null

Merged:
1 → 1 → 2 → 3 → 4 → 4 → null
```

---

## Example 2

```text
Input:
list1 = []

list2 = []

Output:
[]
```

---

## Example 3

```text
Input:
list1 = []

list2 = [0]

Output:
[0]
```

---

# 1. Inputs

The inputs are:

```text
list1
list2
```

Both are references to the heads of singly linked lists.

Example:

```text
list1
 ↓
 1 → 2 → 4 → null


list2
 ↓
 1 → 3 → 4 → null
```

Each node contains:

```java
class ListNode {
    int val;
    ListNode next;
}
```

Both lists are already sorted in **non-decreasing order**.

That means:

```text
current value <= next value
```

For example:

```text
1 → 2 → 4
```

is sorted.

---

# 2. Output

Return the head of one merged sorted linked list.

For:

```text
list1 = 1 → 2 → 4

list2 = 1 → 3 → 4
```

the output should be:

```text
1 → 1 → 2 → 3 → 4 → 4 → null
↑
head
```

---

# 3. Required Operation

We need to repeatedly compare the current nodes from both lists.

For example:

```text
list1
 ↓
 1 → 2 → 4

list2
 ↓
 1 → 3 → 4
```

Compare:

```text
list1.val = 1
list2.val = 1
```

Choose the smaller value and connect that node to the merged list.

Then move forward only in the list whose node was selected.

Repeat until one list becomes empty.

---

# 4. Important Observation

Because both lists are already sorted, we never need to search the entire list for the smallest value.

We only need to compare:

```java
list1.val
```

with:

```java
list2.val
```

The smaller current value must be the next value in the merged list.

This is the same general idea used when merging two sorted arrays.

---

# 5. Main Challenge

We need to build the merged linked list without losing track of its beginning.

Suppose we keep adding nodes:

```text
1 → 1 → 2 → 3 ...
```

We need one pointer that always remembers:

```text
the beginning of the list
```

and another pointer that remembers:

```text
where to attach the next node
```

This leads to the **dummy node + current pointer** pattern.

---

# 6. Dummy Node

Create a temporary node:

```java
ListNode dummy = new ListNode(0);
```

Initially:

```text
dummy
 ↓
 0 → null
```

The value `0` is not part of the actual answer.

It simply gives us a fixed starting point.

We also create:

```java
ListNode current = dummy;
```

So initially:

```text
dummy
 ↓
 0 → null
 ↑
current
```

`current` represents the end of the merged list.

---

# 7. Why Use a Dummy Node?

Without a dummy node, we would need special logic to determine which node becomes the first node of the merged list.

We might need something like:

```text
if merged list is empty
    initialize head
else
    attach node
```

That adds unnecessary branching.

The dummy node gives us a node that already exists before merging begins.

Therefore every selected node can be attached using the same operation:

```java
current.next = selectedNode;
```

At the end, the actual merged list starts at:

```java
dummy.next
```

---

# 8. Pointers

We use three important references:

```text
list1
list2
current
```

### `list1`

Points to the current unprocessed node in the first list.

### `list2`

Points to the current unprocessed node in the second list.

### `current`

Points to the last node currently added to the merged list.

The dummy node remembers the beginning.

---

# 9. Manual Test Case

Use:

```text
list1 = [1,2,4]
list2 = [1,3,4]
```

Initially:

```text
list1
 ↓
 1 → 2 → 4


list2
 ↓
 1 → 3 → 4


dummy/current
 ↓
 0
```

---

## Comparison 1

Compare:

```text
list1.val = 1
list2.val = 1
```

If we use:

```java
if (list1.val <= list2.val)
```

we select the node from `list1`.

Connect it:

```java
current.next = list1;
```

Merged list:

```text
dummy
 ↓
 0 → 1
```

Move `list1`:

```java
list1 = list1.next;
```

Now:

```text
list1
 ↓
 2 → 4

list2
 ↓
 1 → 3 → 4
```

Move `current`:

```java
current = current.next;
```

---

## Comparison 2

Compare:

```text
2 vs 1
```

`1` is smaller.

Select `list2`.

Merged:

```text
0 → 1 → 1
```

Move:

```text
list2 → 3
```

---

## Comparison 3

Compare:

```text
2 vs 3
```

`2` is smaller.

Merged:

```text
0 → 1 → 1 → 2
```

Move:

```text
list1 → 4
```

---

## Comparison 4

Compare:

```text
4 vs 3
```

`3` is smaller.

Merged:

```text
0 → 1 → 1 → 2 → 3
```

Move:

```text
list2 → 4
```

---

## Comparison 5

Compare:

```text
4 vs 4
```

Choose one of the `4`s.

Merged:

```text
0 → 1 → 1 → 2 → 3 → 4
```

One list now reaches:

```text
null
```

The comparison loop stops.

---

# 10. What Happens When One List Finishes?

Suppose:

```text
list1 = null

list2 = 4 → null
```

Do we need to continue comparing?

No.

There is nothing left in `list1`.

Also, `list2` is already sorted.

Therefore the entire remaining portion can simply be attached:

```java
current.next = list2;
```

Result:

```text
0 → 1 → 1 → 2 → 3 → 4 → 4
```

The actual answer starts after the dummy node:

```java
return dummy.next;
```

Result:

```text
1 → 1 → 2 → 3 → 4 → 4
```

---

# 11. Pseudocode

```text
create dummy node

current = dummy

while list1 is not null AND list2 is not null

    compare list1.val and list2.val

    if list1.val <= list2.val

        connect current.next to list1

        move list1 to list1.next

    else

        connect current.next to list2

        move list2 to list2.next

    move current to current.next


if list1 still has nodes

    connect current.next to list1

else

    connect current.next to list2


return dummy.next
```

---

# 12. Verify the Pseudocode

Input:

```text
list1 = [1,2,4]
list2 = [1,3,4]
```

Selections:

```text
Compare 1 and 1 → choose 1
Compare 2 and 1 → choose 1
Compare 2 and 3 → choose 2
Compare 4 and 3 → choose 3
Compare 4 and 4 → choose 4
```

One list finishes.

Attach remaining:

```text
4
```

Result:

```text
[1,1,2,3,4,4]
```

Correct.

---

# 13. Control Flow

## Why Use `while`?

The main loop is:

```java
while (list1 != null && list2 != null)
```

We use `while` because we do not know beforehand which list will become empty first.

The number of times each pointer moves depends on the values being compared.

For example:

```text
list1 = [1,2,3]
list2 = [100]
```

`list1` may move several times before `list2` moves.

But with:

```text
list1 = [100]
list2 = [1,2,3]
```

the opposite happens.

Therefore the algorithm is controlled by pointer state:

> Continue while both lists still contain unprocessed nodes.

This makes `while` more appropriate than `for`.

---

# 14. Why Not Use a `for` Loop?

A `for` loop is best when the traversal range is predictable.

For example:

```java
for (int i = 0; i < nums.length; i++)
```

means:

```text
start at 0
stop at nums.length
move by 1
```

Here, however, we have two independent pointers:

```text
list1
list2
```

and only one moves during each comparison.

Which one moves depends on the data.

Therefore:

```java
while (list1 != null && list2 != null)
```

expresses the algorithm more naturally.

---

# 15. Why Use `if / else`?

Inside the loop:

```java
if (list1.val <= list2.val) {
    ...
} else {
    ...
}
```

We must choose exactly one of two nodes.

Either:

```text
take list1
```

or:

```text
take list2
```

One of those actions must happen during every iteration.

Therefore `if / else` is appropriate.

---

# 16. Why Not Just `if`?

Suppose we only wrote:

```java
if (list1.val <= list2.val) {
    current.next = list1;
}
```

What happens when:

```text
list1.val > list2.val
```

?

We still need to select `list2`.

Therefore the false condition requires another action.

That is why `else` is necessary.

---

# 17. Why Not Two Separate `if` Statements?

We could try:

```java
if (list1.val <= list2.val) {
    ...
}

if (list2.val < list1.val) {
    ...
}
```

But these are not independent operations.

We want exactly **one** node selected during each iteration.

Also, the first branch changes one of the pointers. The second `if` could then evaluate using modified state.

Using:

```java
if (...) {
    ...
} else {
    ...
}
```

guarantees exactly one branch executes.

---

# 18. Pointer Movement

This is one of the most important parts of the algorithm.

If we choose:

```text
list1
```

we move only:

```java
list1 = list1.next;
```

We do **not** move `list2`.

Why?

Because the current `list2` node has not been added yet.

Similarly, if we choose `list2`:

```java
list2 = list2.next;
```

and `list1` stays where it is.

After attaching either node, we always move:

```java
current = current.next;
```

because the end of our merged list has advanced.

---

# 19. Why Does the Main Loop Use `&&`?

We use:

```java
while (list1 != null && list2 != null)
```

not:

```java
while (list1 != null || list2 != null)
```

The comparison requires:

```java
list1.val
```

and:

```java
list2.val
```

Therefore **both nodes must exist**.

If one is `null`, we cannot safely compare both values.

Once either list becomes empty, comparisons are unnecessary anyway because the remaining list is already sorted.

So `&&` is the correct condition.

---

# 20. Java Solution

```java
class Solution {

    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {

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

        if (list1 != null) {
            current.next = list1;
        } else {
            current.next = list2;
        }

        return dummy.next;
    }
}
```

---

# 21. Simplifying the Remaining Nodes

After the loop, we know:

```text
list1 is null
OR
list2 is null
```

Therefore this:

```java
if (list1 != null) {
    current.next = list1;
} else {
    current.next = list2;
}
```

can also be written as:

```java
current.next = (list1 != null) ? list1 : list2;
```

For learning purposes, the explicit `if/else` version may be easier to reason about.

---

# 22. Why Don't We Need Another Loop?

Suppose after the main loop:

```text
list1 = null

list2 = 4 → 5 → 7 → null
```

We could write:

```text
while list2 is not null
    attach each node individually
```

But that is unnecessary.

The nodes are already connected:

```text
4 → 5 → 7 → null
```

So we can connect the entire remaining chain with one pointer assignment:

```java
current.next = list2;
```

This is more efficient and simpler.

---

# 23. Edge Cases

## Edge Case 1: Both Lists Empty

```text
list1 = null
list2 = null
```

The `while` condition:

```java
list1 != null && list2 != null
```

is false immediately.

Then:

```java
current.next = list2;
```

sets:

```text
dummy.next = null
```

Return:

```java
dummy.next
```

which is:

```text
null
```

Correct:

```text
[]
```

---

## Edge Case 2: First List Empty

```text
list1 = null

list2 = 0 → null
```

The loop does not execute.

Attach:

```java
current.next = list2;
```

Return:

```text
0 → null
```

Correct.

---

## Edge Case 3: Second List Empty

```text
list1 = 1 → 2 → null

list2 = null
```

The loop does not execute.

Attach:

```java
current.next = list1;
```

Return:

```text
1 → 2 → null
```

Correct.

---

## Edge Case 4: Equal Values

```text
list1 = 1 → 2

list2 = 1 → 3
```

Using:

```java
list1.val <= list2.val
```

allows us to select `list1` when the values are equal.

Either equal-valued node could come first while still preserving sorted order.

---

## Edge Case 5: One List Completely Smaller

```text
list1 = [1,2,3]
list2 = [10,20,30]
```

We repeatedly select:

```text
1
2
3
```

Then `list1` becomes empty.

We attach:

```text
10 → 20 → 30
```

Result:

```text
1 → 2 → 3 → 10 → 20 → 30
```

---

# 24. Complexity Analysis

Let:

```text
m = number of nodes in list1
n = number of nodes in list2
```

## Time Complexity

```text
O(m + n)
```

Each node can be processed at most once.

We never restart from the beginning of either list.

Therefore:

```text
O(m + n)
```

---

## Space Complexity

```text
O(1)
```

The iterative solution only creates a constant number of references:

```text
dummy
current
list1
list2
```

We reuse the existing nodes rather than creating a completely new linked list.

Therefore auxiliary space is:

```text
O(1)
```

---

# 25. Why Not Copy Values Into an Array?

Another possible approach would be:

```text
list1 → array
list2 → array
sort
create new linked list
```

This would introduce unnecessary work and additional memory.

It also ignores the important statement that the lists should be merged by **splicing together the existing nodes**.

Since both lists are already sorted, sorting again is unnecessary.

---

# 26. Why Not Compare Every Node Against Every Other Node?

A brute-force approach might repeatedly search both lists for the next smallest value.

That wastes the sorted property.

Because each list is already sorted, the smallest remaining value must always be at one of the two current heads:

```text
list1.val
```

or:

```text
list2.val
```

Therefore only one comparison is needed at each step.

---

# 27. Relationship to Merge Sorted Array

This problem follows the same fundamental pattern as merging sorted arrays:

```text
Pointer A → first sorted sequence
Pointer B → second sorted sequence

Compare
   ↓
Choose smaller
   ↓
Move chosen pointer
   ↓
Repeat
```

The difference is how we access the next value.

### Array

```java
i++;
```

### Linked List

```java
list1 = list1.next;
```

The underlying merge strategy is the same.

---

# 28. Recursive Solution

The problem can also be solved recursively.

The basic idea is:

```text
Compare list1.val and list2.val.

Choose the smaller node.

Then recursively merge everything that remains.
```

Base cases:

```java
if (list1 == null) {
    return list2;
}

if (list2 == null) {
    return list1;
}
```

Recursive comparison:

```java
if (list1.val <= list2.val) {

    list1.next = mergeTwoLists(list1.next, list2);
    return list1;

} else {

    list2.next = mergeTwoLists(list1, list2.next);
    return list2;
}
```

Complete recursive solution:

```java
class Solution {

    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {

        if (list1 == null) {
            return list2;
        }

        if (list2 == null) {
            return list1;
        }

        if (list1.val <= list2.val) {

            list1.next = mergeTwoLists(list1.next, list2);
            return list1;

        } else {

            list2.next = mergeTwoLists(list1, list2.next);
            return list2;
        }
    }
}
```

---

# 29. Iterative vs Recursive

| Characteristic | Iterative | Recursive |
|---|---|---|
| Time | `O(m+n)` | `O(m+n)` |
| Extra Space | `O(1)` | `O(m+n)` call stack |
| Main control flow | `while` | recursion |
| Uses dummy node | Yes | No |
| Stack overflow risk | No | Possible |
| Recommended first | Yes | After iterative |

The iterative solution is generally preferable because it achieves:

```text
Time:  O(m+n)
Space: O(1)
```

---

# 30. Common Mistakes

## Mistake 1: Moving Both List Pointers

Wrong:

```java
list1 = list1.next;
list2 = list2.next;
```

after every comparison.

Only the list whose node was selected should advance.

---

## Mistake 2: Forgetting to Move `current`

After:

```java
current.next = list1;
```

or:

```java
current.next = list2;
```

we must execute:

```java
current = current.next;
```

Otherwise `current` remains behind.

---

## Mistake 3: Using `||` in the Main Loop

Wrong:

```java
while (list1 != null || list2 != null)
```

Inside the loop we compare both values.

If one list is `null`, accessing:

```java
list1.val
```

or:

```java
list2.val
```

could cause a `NullPointerException`.

Use:

```java
while (list1 != null && list2 != null)
```

---

## Mistake 4: Returning `dummy`

Wrong:

```java
return dummy;
```

The dummy node is not part of the answer.

Return:

```java
return dummy.next;
```

---

## Mistake 5: Forgetting Remaining Nodes

When one list becomes empty, the other list may still contain nodes.

Those nodes must be attached:

```java
if (list1 != null) {
    current.next = list1;
} else {
    current.next = list2;
}
```

---

# 31. Interview Explanation

I use a dummy node to simplify construction of the merged list and a `current` pointer to track its end.

While both lists contain nodes, I compare their current values. I attach the smaller node to `current.next`, advance only the pointer belonging to the selected list, and then advance `current`.

I use a `while` loop because I don't know which list will become empty first; termination depends on the state of the two list pointers.

I use `if/else` because exactly one of the two current nodes must be selected during each iteration.

Once one list becomes empty, I attach the entire remainder of the other list because it is already sorted.

Finally, I return `dummy.next`, since the dummy node itself is not part of the result.

The algorithm runs in `O(m+n)` time and uses `O(1)` auxiliary space.

---

# 32. What I Learned

- Take advantage of the fact that both input lists are already sorted.
- When merging sorted sequences, compare only the current elements.
- Move only the pointer whose element was selected.
- A dummy node simplifies linked-list construction.
- `current` tracks where the next selected node should be attached.
- Use `while` when termination depends on pointer state.
- Use `if/else` when exactly one of two mutually exclusive actions must occur.
- `&&` is necessary because both nodes must exist before their values can be compared.
- Once one sorted list is exhausted, the entire remaining list can be attached directly.
- Existing nodes can be reused instead of creating new nodes.
- The iterative solution achieves `O(m+n)` time and `O(1)` auxiliary space.

---

# Pattern Recognition

When I see:

```text
two sorted linked lists
merge
sorted output
```

I should think:

```text
Two pointers
     ↓
Compare current nodes
     ↓
Choose smaller
     ↓
Attach node
     ↓
Advance chosen list
     ↓
Advance merged-list pointer
     ↓
Repeat while both exist
     ↓
Attach remainder
```

Core pattern:

```java
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

current.next = (list1 != null) ? list1 : list2;

return dummy.next;
```

This **dummy node + tail pointer + two input pointers** pattern is important for many linked-list problems.