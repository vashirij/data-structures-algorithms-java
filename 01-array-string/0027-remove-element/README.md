# 27. Remove Element

**Difficulty:** Easy
**Topics:** Array, Two Pointers
**Status:** In Progress

## Original Question

Given an integer array `nums` and an integer `val`, remove all occurrences of `val` in `nums` **in-place**. The order of the elements may be changed. Then return the number of elements in `nums` which are not equal to `val`.

Consider the number of elements in `nums` which are not equal to `val` to be `k`. To get accepted:

* Change the array `nums` such that the first `k` elements contain the elements which are not equal to `val`.
* The remaining elements of `nums` are not important.
* Return `k`.

### Example 1

```text
Input: nums = [3,2,2,3], val = 3
Output: 2, nums = [2,2,_,_]
```

The function should return `k = 2`, with the first two elements of `nums` containing `2`.

### Example 2

```text
Input: nums = [0,1,2,2,3,0,4,2], val = 2
Output: 5, nums = [0,1,4,0,3,_,_,_]
```

The first five positions must contain `0`, `0`, `1`, `3`, and `4`. Their order does not matter.

## Constraints

* `0 <= nums.length <= 100`
* `0 <= nums[i] <= 50`
* `0 <= val <= 100`

---

# My Problem Analysis

## Input

The function receives:

* `nums`: an integer array.
* `val`: the value whose occurrences must be removed.

Example:

```text
nums = [3,2,2,3]
val = 3
```

---

# Output

The function must:

1. Modify `nums` **in-place**.
2. Move all elements that are not equal to `val` into the first `k` positions.
3. Return `k`, the number of elements that are not equal to `val`.

For:

```text
nums = [3,2,2,3]
val = 3
```

there are two values that should remain:

```text
2, 2
```

Therefore:

```text
k = 2
```

The important part of the resulting array is:

```text
[2,2,_,_]
 ↑ ↑
 first k elements
```

Values after index `k - 1` do not matter.

---

# Key Observation

A Java array has a fixed size.

Therefore, I do not need to physically delete elements or create a smaller array.

Instead, I need to reorganize the existing array so that all values I want to keep appear at the beginning.

This suggests using two pointers with different responsibilities:

```text
read  → examines every original element
write → identifies where the next valid element should be stored
```

---

# Pointer Design

## Read Pointer

The read pointer examines each element in the original array.

For example:

```text
nums = [3,2,2,3]
        ↑
       read
```

Its job is to answer:

```text
Should nums[read] be kept?
```

If:

```text
nums[read] == val
```

the element should be ignored.

If:

```text
nums[read] != val
```

the element should be kept.

---

## Write Pointer

The write pointer identifies the next position where a value that should be kept can be stored.

Initially:

```text
write = 0
```

Example:

```text
nums = [3,2,2,3]
        ↑
       write
```

The write pointer only moves after a valid element has been placed into the array.

At the end of the algorithm, `write` also represents the number of elements that were kept.

Therefore:

```text
k = write
```

---

# Algorithm

1. Initialize the write pointer to index `0`.
2. Traverse every element in `nums` using a read pointer.
3. Compare the current element with `val`.
4. If the current element is not equal to `val`, keep it.
5. Store the kept value at the current write position.
6. Move the write pointer forward.
7. If the current value equals `val`, do not move the write pointer.
8. Continue until every original array element has been examined.
9. Return the write pointer as `k`.

---

# Control-Flow Reasoning

## Why Traverse Every Element?

Every original value must be checked to determine whether it equals `val`.

For example:

```text
nums = [0,1,2,2,3,0,4,2]
```

There is no safe way to know whether an element should remain without examining it.

Therefore, the read pointer must eventually visit every original array position.

---

## Why Consider a `for` Loop?

The number of original elements that need to be examined is known:

```text
nums.length
```

The read pointer:

* Starts at index `0`.
* Moves one position after every inspection.
* Stops after index `nums.length - 1`.

This makes a `for` loop a natural control-flow structure.

Conceptually:

```text
for every index in nums
    inspect the current value
```

A `while` loop could also perform the traversal, but it would require manually managing the read pointer.

---

## Why Check `nums[read] != val`?

The algorithm is interested in values that should **remain**.

Therefore, the useful condition is:

```text
nums[read] != val
```

When this condition is true, the value should be copied to the write position.

When it is false, the value is one that should be removed and can simply be skipped.

---

## Why Might `if` Be Enough Without `else`?

When:

```text
nums[read] != val
```

an action is required:

```text
keep the value
```

When:

```text
nums[read] == val
```

nothing needs to be written.

The read pointer can simply continue to the next element.

Therefore, there may be no useful operation for an `else` branch.

---

# Pointer Movement

The two pointers should not necessarily move together.

## Read Pointer

The read pointer should move after every element is examined.

That is because every original array element must eventually be checked.

## Write Pointer

The write pointer should move only when an element is kept.

For example:

```text
nums = [3,2,2,3]
val = 3
```

When the read pointer sees:

```text
3
```

the value is rejected.

The write pointer should remain where it is because that position is still available for the next valid value.

When the read pointer reaches:

```text
2
```

the value is accepted.

It should be placed at the write position, and then the write pointer can advance.

---

# Manual Walkthrough

Given:

```text
nums = [0,1,2,2,3,0,4,2]
val = 2
```

| Read Index | Value | Keep? | Write Index Before | Action    |
| ---------: | ----: | ----- | -----------------: | --------- |
|          0 |     0 | Yes   |                  0 | Keep `0`  |
|          1 |     1 | Yes   |                  1 | Keep `1`  |
|          2 |     2 | No    |                  2 | Skip      |
|          3 |     2 | No    |                  2 | Skip      |
|          4 |     3 | Yes   |                  2 | Write `3` |
|          5 |     0 | Yes   |                  3 | Write `0` |
|          6 |     4 | Yes   |                  4 | Write `4` |
|          7 |     2 | No    |                  5 | Skip      |

At the end:

```text
write = 5
```

Therefore:

```text
k = 5
```

Only the first five positions matter.

---

# Edge Cases

## Empty Array

```text
nums = []
val = 3
```

There are no elements to keep.

Expected:

```text
k = 0
```

---

## Every Element Equals `val`

```text
nums = [3,3,3]
val = 3
```

No elements should remain.

Expected:

```text
k = 0
```

---

## No Elements Equal `val`

```text
nums = [1,2,4]
val = 3
```

Every element should remain.

Expected:

```text
k = 3
```

---

## Single Element Removed

```text
nums = [3]
val = 3
```

Expected:

```text
k = 0
```

---

## Single Element Kept

```text
nums = [2]
val = 3
```

Expected:

```text
k = 1
```

---

# Complexity Target

If every element is examined once, the expected time complexity should be:

```text
O(n)
```

where `n` is `nums.length`.

If only pointer/index variables are used and no additional array proportional to the input is created, the expected extra space should be:

```text
O(1)
```

---

# Java Solution

**To be completed after implementing and testing my own solution.**

---

# Alternative Approaches

**To be completed after solving the problem.**

Questions to consider:

* Could elements equal to `val` be replaced using values from the end of the array?
* Would that be useful because the problem says element order may change?
* How would that approach compare with the read/write pointer approach?

---

# Interview Explanation

**To be completed after solving the problem independently.**

The explanation should cover:

* What the two pointers represent.
* Why every original element needs to be inspected.
* Why the write pointer moves only when an element is kept.
* Why the algorithm modifies the array in-place.
* Why the returned write position represents `k`.
* Time and space complexity.

---

# What I Learned

**To be completed after solving the problem.**
