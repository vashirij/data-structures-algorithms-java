# 1929. Concatenation of Array

**Difficulty:** Easy
**Topics:** Array
**Status:** Solved

## Original Question

Given an integer array `nums` of length `n`, create an array `ans` of length `2n` where:

```text
ans[i] == nums[i]
```

and:

```text
ans[i + n] == nums[i]
```

for:

```text
0 <= i < n
```

The resulting `ans` array is the concatenation of `nums` with itself.

Return `ans`.

---

## Example 1

```text
Input: nums = [1,2,1]
Output: [1,2,1,1,2,1]
```

The result contains two copies of `nums`:

```text
nums = [1,2,1]

ans  = [1,2,1,1,2,1]
        ------- -------
         copy 1  copy 2
```

---

## Example 2

```text
Input: nums = [1,3,2,1]
Output: [1,3,2,1,1,3,2,1]
```

The result is:

```text
[1,3,2,1] + [1,3,2,1]

=

[1,3,2,1,1,3,2,1]
```

---

## Constraints

* `n == nums.length`
* `1 <= n <= 1000`
* `1 <= nums[i] <= 1000`

---

# My Problem Analysis

## Input

The function receives:

```text
nums
```

which is an integer array of length:

```text
n = nums.length
```

For example:

```text
nums = [1,2,1]
```

Therefore:

```text
n = 3
```

---

# Output

I need to return a new integer array called `ans`.

Its length must be twice the length of `nums`.

Therefore:

```text
ans.length = 2 * n
```

For:

```text
nums = [1,2,1]
```

the result must be:

```text
ans = [1,2,1,1,2,1]
```

---

# Key Observation

The output is simply two copies of the original array placed next to each other.

If:

```text
nums = [1,2,1]
```

then:

```text
ans = nums + nums
```

Conceptually:

```text
[1,2,1] + [1,2,1]

       ↓

[1,2,1,1,2,1]
```

Therefore, every element from `nums` needs to be written into **two positions** in `ans`.

---

# Index Relationship

The problem directly gives the important index relationship:

```text
ans[i] = nums[i]
```

and:

```text
ans[i + n] = nums[i]
```

For:

```text
nums = [1,2,1]
n = 3
```

the mappings are:

| `i` | `nums[i]` | First Position | Second Position |
| --: | --------: | -------------: | --------------: |
|   0 |         1 |       `ans[0]` |        `ans[3]` |
|   1 |         2 |       `ans[1]` |        `ans[4]` |
|   2 |         1 |       `ans[2]` |        `ans[5]` |

Therefore:

```text
ans[0] = nums[0]
ans[3] = nums[0]

ans[1] = nums[1]
ans[4] = nums[1]

ans[2] = nums[2]
ans[5] = nums[2]
```

This produces:

```text
[1,2,1,1,2,1]
```

---

# Why `i + n`?

The first copy occupies indexes:

```text
0 through n - 1
```

The second copy must start immediately after the first copy.

The first available position after the first `n` elements is:

```text
n
```

Therefore, if an original element is at:

```text
i
```

its corresponding position in the second half is:

```text
i + n
```

For example, when:

```text
n = 3
```

we get:

```text
i = 0 → i + n = 3
i = 1 → i + n = 4
i = 2 → i + n = 5
```

---

# Algorithm

1. Determine the length of `nums`.
2. Store it in `n`.
3. Create a new array `ans` of length `2 * n`.
4. Traverse `nums` from index `0` through `n - 1`.
5. Copy `nums[i]` into `ans[i]`.
6. Copy the same value into `ans[i + n]`.
7. Repeat for every element.
8. Return `ans`.

Pseudocode:

```text
n = nums.length

create ans of size 2 * n

for every i from 0 to n - 1:

    ans[i] = nums[i]

    ans[i + n] = nums[i]

return ans
```

---

# Control-Flow Reasoning

## Why Use a `for` Loop?

I know exactly how many original elements need to be processed.

There are:

```text
n
```

elements.

Each index from:

```text
0
```

through:

```text
n - 1
```

must be processed exactly once.

Therefore:

```java
for (int i = 0; i < n; i++)
```

is a natural choice.

A `while` loop could also work, but I would need to manually initialize and increment `i`.

The `for` loop clearly represents:

> Process every original array position exactly once.

---

# Why Does `i` Increase Every Iteration?

Each original element must be copied exactly twice.

Once:

```text
nums[i]
```

has been written to both required positions, there is no reason to examine it again.

Therefore:

```text
i++
```

moves to the next original element.

---

# Why No `if` Statement?

There is no condition determining whether an element should be copied.

Every element must always be copied twice.

For every:

```text
nums[i]
```

we always perform:

```java
ans[i] = nums[i];
ans[i + n] = nums[i];
```

There is no:

```text
keep?
remove?
duplicate?
larger?
smaller?
```

decision.

Therefore, no `if` or `if/else` statement is necessary.

---

# Why Create a New Array?

The required output contains:

```text
2n
```

elements.

But the original `nums` array contains only:

```text
n
```

elements.

Java arrays have fixed sizes.

Therefore, `nums` cannot simply expand from length `n` to length `2n`.

A new array must be created:

```java
int[] ans = new int[2 * n];
```

---

# Java Solution

```java
class Solution {
    public int[] getConcatenation(int[] nums) {

        int n = nums.length;
        int[] ans = new int[2 * n];

        for (int i = 0; i < n; i++) {
            ans[i] = nums[i];
            ans[i + n] = nums[i];
        }

        return ans;
    }
}
```

---

# Manual Walkthrough

Given:

```text
nums = [1,2,1]
```

Determine:

```text
n = 3
```

Create:

```text
ans = [0,0,0,0,0,0]
```

---

## Iteration 1

```text
i = 0
nums[i] = 1
```

Write the first copy:

```text
ans[0] = 1
```

Write the second copy:

```text
ans[0 + 3] = 1
ans[3] = 1
```

Now:

```text
ans = [1,0,0,1,0,0]
```

---

## Iteration 2

```text
i = 1
nums[i] = 2
```

Write:

```text
ans[1] = 2
ans[4] = 2
```

Now:

```text
ans = [1,2,0,1,2,0]
```

---

## Iteration 3

```text
i = 2
nums[i] = 1
```

Write:

```text
ans[2] = 1
ans[5] = 1
```

Now:

```text
ans = [1,2,1,1,2,1]
```

Return:

```text
[1,2,1,1,2,1]
```

---

# Complexity Analysis

## Time Complexity

```text
O(n)
```

The algorithm traverses the original array exactly once.

For every element, it performs two constant-time assignments:

```java
ans[i] = nums[i];
ans[i + n] = nums[i];
```

Therefore:

```text
O(n)
```

time is required.

---

# Space Complexity

```text
O(n)
```

The algorithm creates a new array containing:

```text
2n
```

elements.

In Big-O notation:

```text
O(2n) = O(n)
```

Therefore, the additional space complexity is:

```text
O(n)
```

---

# Edge Cases

## Single Element

```text
nums = [5]
```

Here:

```text
n = 1
```

Expected:

```text
[5,5]
```

---

## Duplicate Elements

```text
nums = [1,1,1]
```

Expected:

```text
[1,1,1,1,1,1]
```

Duplicates do not require any special handling.

---

## Different Values

```text
nums = [1,2,3,4]
```

Expected:

```text
[1,2,3,4,1,2,3,4]
```

---

# Comparison With 1470 — Shuffle the Array

This problem has an interesting relationship with **1470. Shuffle the Array**.

In Shuffle the Array, the input is divided into two halves:

```text
[x1,x2,...,xn,y1,y2,...,yn]
```

and we calculate where each value belongs in the result.

For this problem, the same input element is placed in two different locations:

```text
nums[i]
   ↓
   ├──→ ans[i]
   │
   └──→ ans[i + n]
```

Both problems demonstrate an important array technique:

> Find the mathematical relationship between an input index and its output index.

---

# Alternative Approach — Two Loops

Another possible solution is to use two separate loops.

Conceptually:

```text
First loop:
copy nums into the first half

Second loop:
copy nums into the second half
```

For example:

```java
for (int i = 0; i < n; i++) {
    ans[i] = nums[i];
}

for (int i = 0; i < n; i++) {
    ans[i + n] = nums[i];
}
```

This still has:

```text
Time: O(n)
Space: O(n)
```

However, one loop is simpler because both assignments are directly related to the same `nums[i]`.

---

# Why I Prefer One Loop

Using:

```java
for (int i = 0; i < n; i++) {
    ans[i] = nums[i];
    ans[i + n] = nums[i];
}
```

keeps both operations involving the current element together.

For each `nums[i]`, I immediately place both copies.

This makes the relationship between the indexes easier to see.

---

# Interview Explanation

The output is simply two copies of the original array concatenated together.

I first create a new array of length `2 * nums.length`.

Then I iterate through the original array once.

For every index `i`, the first copy belongs at `ans[i]`, while the second copy belongs exactly `n` positions later at `ans[i + n]`.

I use a `for` loop because every original element must be processed exactly once, and no conditional statement is needed because every element is always copied.

The algorithm runs in `O(n)` time and requires `O(n)` additional space for the output array.

---

# What I Learned

* Always look for the relationship between input and output indexes.
* `i + n` can be used to access the corresponding position in a second array section.
* Java arrays have fixed sizes, so a larger output requires a new array.
* A `for` loop is appropriate when every array element must be processed exactly once.
* Not every problem requires an `if` statement.
* Multiple related assignments can often be performed during the same traversal.
* `O(2n)` simplifies to `O(n)` in Big-O notation.
* Simple array problems are useful for learning index manipulation before moving to more complex two-pointer and sliding-window problems.
