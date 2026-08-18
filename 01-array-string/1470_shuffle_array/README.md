# 1470. Shuffle the Array

**Difficulty:** Easy
**Topics:** Array
**Status:** Solved

## Original Question

Given the array `nums` consisting of `2n` elements in the form:

```text
[x1, x2, ..., xn, y1, y2, ..., yn]
```

Return the array in the form:

```text
[x1, y1, x2, y2, ..., xn, yn]
```

## Example 1

```text
Input: nums = [2,5,1,3,4,7], n = 3
Output: [2,3,5,4,1,7]
```

Explanation:

```text
x1 = 2
x2 = 5
x3 = 1

y1 = 3
y2 = 4
y3 = 7
```

The shuffled result is:

```text
[2,3,5,4,1,7]
```

## Example 2

```text
Input: nums = [1,2,3,4,4,3,2,1], n = 4
Output: [1,4,2,3,3,2,4,1]
```

## Example 3

```text
Input: nums = [1,1,2,2], n = 2
Output: [1,2,1,2]
```

## Constraints

* `1 <= n <= 500`
* `nums.length == 2 * n`
* `1 <= nums[i] <= 10^3`

---

# My Problem Analysis

## Input

The function receives:

* `nums`: an integer array containing `2n` elements.
* `n`: the number of elements in each half of the array.

The array has the structure:

```text
[x1, x2, ..., xn, y1, y2, ..., yn]
```

For example:

```text
nums = [2,5,1,3,4,7]
n = 3
```

The array can be divided into:

```text
First half:
[2,5,1]

Second half:
[3,4,7]
```

---

# Output

The function must return a new array where elements from the two halves are interleaved.

Instead of:

```text
[x1, x2, x3, y1, y2, y3]
```

the result should be:

```text
[x1, y1, x2, y2, x3, y3]
```

For:

```text
nums = [2,5,1,3,4,7]
```

the result is:

```text
[2,3,5,4,1,7]
```

---

# Key Observation

The array contains two groups of equal size.

The first group begins at:

```text
index 0
```

and the second group begins at:

```text
index n
```

Therefore, for every index `i` from `0` to `n - 1`:

```text
x = nums[i]
y = nums[i + n]
```

For example:

```text
nums = [2,5,1,3,4,7]
n = 3
```

| `i` | `nums[i]` | `nums[i+n]` |
| --: | --------: | ----------: |
|   0 |         2 |           3 |
|   1 |         5 |           4 |
|   2 |         1 |           7 |

This gives the pairs:

```text
[2,3]
[5,4]
[1,7]
```

Combining them produces:

```text
[2,3,5,4,1,7]
```

---

# Index Pattern

The main challenge is determining where each pair belongs in the result array.

For each `i`:

```text
x goes to index 2 * i
y goes to index 2 * i + 1
```

For example:

| `i` | x destination | y destination |
| --: | ------------: | ------------: |
|   0 |             0 |             1 |
|   1 |             2 |             3 |
|   2 |             4 |             5 |
|   3 |             6 |             7 |

Therefore:

```java
result[2 * i] = nums[i];
result[2 * i + 1] = nums[i + n];
```

---

# Algorithm

1. Create a result array with the same length as `nums`.
2. Loop from `i = 0` to `i < n`.
3. Read the corresponding `x` value from `nums[i]`.
4. Read the corresponding `y` value from `nums[i + n]`.
5. Store `x` at index `2 * i`.
6. Store `y` at index `2 * i + 1`.
7. Continue until all `n` pairs have been processed.
8. Return the result array.

---

# Control-Flow Reasoning

## Why Use a `for` Loop?

The number of iterations is known.

There are exactly:

```text
n
```

pairs to process.

The loop starts at:

```text
i = 0
```

and ends after:

```text
i = n - 1
```

Therefore:

```java
for (int i = 0; i < n; i++)
```

is a natural choice.

A `while` loop could also work, but it would require manually initializing and incrementing `i`.

The `for` loop clearly expresses:

> Process each of the `n` pairs exactly once.

---

# Why No `if` Statement?

There is no condition that determines whether an element should be processed.

Every iteration must always:

```text
take one x value
take one y value
place both into the result
```

Therefore, no `if` or `if/else` statement is necessary.

The same operations occur on every iteration.

---

# Why Use `nums[i + n]`?

The first `n` elements belong to the `x` group:

```text
nums[0]
nums[1]
...
nums[n - 1]
```

The second half begins exactly at index:

```text
n
```

Therefore:

```text
y1 = nums[n]
y2 = nums[n + 1]
y3 = nums[n + 2]
```

In general:

```text
yi = nums[i + n]
```

when `i` uses zero-based indexing.

---

# Why Use `2 * i`?

The output alternates:

```text
x, y, x, y, x, y
```

All `x` values occupy even indexes:

```text
0, 2, 4, 6, ...
```

These indexes can be represented as:

```text
2 * i
```

All `y` values occupy odd indexes:

```text
1, 3, 5, 7, ...
```

These indexes can be represented as:

```text
2 * i + 1
```

This gives a direct mathematical mapping from the input pair to its output positions.

---

# Java Solution

```java
class Solution {
    public int[] shuffle(int[] nums, int n) {

        int[] result = new int[nums.length];

        for (int i = 0; i < n; i++) {
            result[2 * i] = nums[i];
            result[2 * i + 1] = nums[i + n];
        }

        return result;
    }
}
```

---

# Manual Walkthrough

Given:

```text
nums = [2,5,1,3,4,7]
n = 3
```

Create:

```text
result = [0,0,0,0,0,0]
```

## Iteration 1

```text
i = 0
```

Read:

```text
nums[0] = 2
nums[3] = 3
```

Write:

```text
result[0] = 2
result[1] = 3
```

Result:

```text
[2,3,0,0,0,0]
```

---

## Iteration 2

```text
i = 1
```

Read:

```text
nums[1] = 5
nums[4] = 4
```

Write:

```text
result[2] = 5
result[3] = 4
```

Result:

```text
[2,3,5,4,0,0]
```

---

## Iteration 3

```text
i = 2
```

Read:

```text
nums[2] = 1
nums[5] = 7
```

Write:

```text
result[4] = 1
result[5] = 7
```

Final result:

```text
[2,3,5,4,1,7]
```

---

# Complexity Analysis

## Time Complexity

```text
O(n)
```

The loop executes exactly `n` times.

Each iteration performs constant-time operations:

* Two array reads.
* Two array writes.

Therefore the total time complexity is:

```text
O(n)
```

Since `nums.length = 2n`, this can also be described as linear time relative to the size of the input array.

---

# Space Complexity

```text
O(n)
```

A new array of size:

```text
2n
```

is created.

Therefore, the extra space grows linearly with the size of the input.

---

# Alternative Approach

Because the problem does not require an in-place solution, creating a new result array is the most straightforward approach.

An advanced alternative could attempt to encode multiple values into the original array and perform the shuffle in-place.

However, that approach:

* Is harder to understand.
* Requires more arithmetic.
* Is unnecessary for the given requirements.
* Provides little benefit for an interview unless specifically asked for an `O(1)` extra-space solution.

The new-array approach is clearer and easier to maintain.

---

# Edge Cases

## Smallest Input

```text
nums = [1,2]
n = 1
```

The two halves are:

```text
x = [1]
y = [2]
```

Expected:

```text
[1,2]
```

---

## Duplicate Values

```text
nums = [1,1,2,2]
n = 2
```

First half:

```text
[1,1]
```

Second half:

```text
[2,2]
```

Expected:

```text
[1,2,1,2]
```

Duplicates do not require special handling.

---

## Same Values Across Both Halves

```text
nums = [4,4,4,4]
n = 2
```

Expected:

```text
[4,4,4,4]
```

The algorithm still performs the same index mapping.

---

# Interview Explanation

The array contains two equal halves. The first half contains the `x` values, while the second half contains the corresponding `y` values.

For each index `i` from `0` to `n - 1`, `nums[i]` gives me the current `x` value and `nums[i + n]` gives me the matching `y` value.

In the result array, `x` values belong at even indexes, so I place each `x` at `2 * i`. The corresponding `y` belongs immediately after it at `2 * i + 1`.

I use a `for` loop because there are exactly `n` pairs to process, and every iteration performs the same operations, so no conditional statement is required.

The algorithm runs in `O(n)` time and uses `O(n)` additional space.

---

# What I Learned

* Look for mathematical relationships between input and output indexes.
* An array can sometimes be understood as multiple logical sections.
* `nums[i + n]` provides a simple way to access the corresponding element in the second half.
* Even output indexes can be generated using `2 * i`.
* Odd output indexes can be generated using `2 * i + 1`.
* A `for` loop is appropriate when the number of required iterations is known.
* Not every array problem needs an `if` statement.
* Sometimes creating a new array is the clearest solution when the problem does not require in-place modification.
