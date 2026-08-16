# 88. Merge Sorted Array

**Difficulty:** Easy
**Topics:** Array, Two Pointers
**Status:** Solved

## Original Question

You are given two integer arrays `nums1` and `nums2`, sorted in **non-decreasing order**, and two integers `m` and `n`, representing the number of elements in `nums1` and `nums2` respectively.

**Merge** `nums1` and `nums2` into a single array sorted in **non-decreasing order**.

The final sorted array should not be returned by the function, but instead be stored inside the array `nums1`. To accommodate this, `nums1` has a length of `m + n`, where the first `m` elements denote the elements that should be merged, and the last `n` elements are set to `0` and should be ignored. `nums2` has a length of `n`.

### Example 1

```text
Input: nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
Output: [1,2,2,3,5,6]
```

### Example 2

```text
Input: nums1 = [1], m = 1, nums2 = [], n = 0
Output: [1]
```

### Example 3

```text
Input: nums1 = [0], m = 0, nums2 = [1], n = 1
Output: [1]
```

## Constraints

* `nums1.length == m + n`
* `nums2.length == n`
* `0 <= m, n <= 200`
* `1 <= m + n <= 200`
* `-10^9 <= nums1[i], nums2[j] <= 10^9`

## Follow Up

Can you come up with an algorithm that runs in `O(m + n)` time?

---

# My Problem Analysis

## Input

The function receives:

* `nums1`: first sorted integer array.
* `nums2`: second sorted integer array.
* `m`: number of valid elements currently stored in `nums1`.
* `n`: number of elements in `nums2`.

The last `n` positions of `nums1` are reserved for the final merged array.

Example:

```text
nums1 = [1, 2, 3, 0, 0, 0]
m = 3

nums2 = [2, 5, 6]
n = 3
```

Only:

```text
[1, 2, 3]
```

are valid values from `nums1`.

---

# Output

The final merged array must:

* Be stored directly inside `nums1`.
* Contain all `m + n` elements.
* Be sorted in non-decreasing order.
* Not require returning another array.

Expected result:

```text
[1, 2, 2, 3, 5, 6]
```

---

# Key Observation

Both arrays are already sorted.

Also, `nums1` contains unused space at the end.

If I merge from the beginning of `nums1`, I could overwrite values that have not yet been processed.

For example:

```text
nums1 = [1, 2, 3, 0, 0, 0]
nums2 = [2, 5, 6]
```

The unused space is at the right side of `nums1`.

Therefore, instead of merging from left to right, I can merge from **right to left**.

This allows me to place the largest remaining element directly into the final available position.

---

# Pointer Design

I use three pointers:

```java
int a = m - 1;
int b = n - 1;
int c = m + n - 1;
```

### Pointer `a`

Points to the last unprocessed valid element in `nums1`.

```text
nums1 = [1,2,3,0,0,0]
             ↑
             a
```

### Pointer `b`

Points to the last unprocessed element in `nums2`.

```text
nums2 = [2,5,6]
             ↑
             b
```

### Pointer `c`

Points to the next position where the largest value should be stored in `nums1`.

```text
nums1 = [1,2,3,0,0,0]
                   ↑
                   c
```

---

# Algorithm

1. Set `a` to the last valid element in `nums1`.
2. Set `b` to the last element in `nums2`.
3. Set `c` to the last available position in `nums1`.
4. Compare `nums1[a]` and `nums2[b]`.
5. Store the larger value at `nums1[c]`.
6. Decrease the pointer belonging to the array whose value was selected.
7. Decrease `c`.
8. Continue while both arrays still contain unprocessed elements.
9. If elements remain in `nums2`, copy them into `nums1`.

---

# Control-Flow Reasoning

## Why Use a `while` Loop?

The main loop is:

```java
while (a >= 0 && b >= 0)
```

A `while` loop is appropriate because I do not know in advance which array will run out of elements first.

The loop should continue based on the state of the two pointers rather than a fixed number of iterations.

A `for` loop could be used, but it would make the pointer-exhaustion logic less direct.

The `while` loop expresses the intention clearly:

> Continue comparing while both arrays still contain unprocessed elements.

---

## Why Use `&&`?

```java
while (a >= 0 && b >= 0)
```

I can compare:

```java
nums1[a]
```

with:

```java
nums2[b]
```

only while both indexes are valid.

If either becomes `-1`, that array has no remaining elements to compare.

Using `||` would allow the loop to continue when one pointer was already invalid and could cause an invalid array access.

---

## Why Use `if / else`?

```java
if (nums1[a] > nums2[b]) {
    nums1[c] = nums1[a];
    a--;
} else {
    nums1[c] = nums2[b];
    b--;
}
```

At every iteration, exactly one value must be placed into `nums1[c]`.

There are two mutually exclusive choices:

* The value from `nums1` is larger.
* The value from `nums2` is larger or equal.

Because only one branch should execute, `if / else` is more appropriate than two independent `if` statements.

---

## Why Do the Pointers Decrease?

The algorithm works from right to left.

Therefore:

```java
a--;
b--;
c--;
```

move toward the beginning of the arrays.

The selected source pointer decreases because that element has now been processed.

`c` decreases after every insertion because one final position has been filled.

---

# Why Is a Second `while` Loop Needed?

After the main loop finishes, `nums2` may still contain unprocessed values.

```java
while (b >= 0) {
    nums1[c] = nums2[b];
    b--;
    c--;
}
```

For example:

```text
nums1 = [0]
m = 0

nums2 = [1]
n = 1
```

Initially:

```text
a = -1
b = 0
c = 0
```

The first loop cannot execute because:

```text
a >= 0
```

is false.

However, `nums2[0]` still needs to be copied into `nums1`.

The second loop handles this case.

---

# Why Is There No `while (a >= 0)`?

If `nums2` runs out first, the remaining values from `nums1` are already in the correct locations.

For example:

```text
nums1 = [1,2,3,0]
nums2 = [4]
```

After placing `4`:

```text
[1,2,3,4]
```

The remaining:

```text
[1,2,3]
```

do not need to be moved.

Therefore, no additional loop for `a` is necessary.

---

# Java Solution

```java
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {

        int a = m - 1;
        int b = n - 1;
        int c = m + n - 1;

        while (a >= 0 && b >= 0) {

            if (nums1[a] > nums2[b]) {
                nums1[c] = nums1[a];
                a--;
            } else {
                nums1[c] = nums2[b];
                b--;
            }

            c--;
        }

        while (b >= 0) {
            nums1[c] = nums2[b];
            b--;
            c--;
        }
    }
}
```

---

# Walkthrough

Given:

```text
nums1 = [1,2,3,0,0,0]
nums2 = [2,5,6]

a = 2
b = 2
c = 5
```

### Step 1

Compare:

```text
nums1[a] = 3
nums2[b] = 6
```

`6` is larger.

```text
nums1 = [1,2,3,0,0,6]
```

Move:

```text
b = 1
c = 4
```

### Step 2

Compare:

```text
3 and 5
```

Place `5`:

```text
nums1 = [1,2,3,0,5,6]
```

### Step 3

Compare:

```text
3 and 2
```

Place `3`:

```text
nums1 = [1,2,3,3,5,6]
```

### Step 4

Compare:

```text
2 and 2
```

Place the value from `nums2`:

```text
nums1 = [1,2,2,3,5,6]
```

The final result is:

```text
[1,2,2,3,5,6]
```

---

# Complexity Analysis

## Time Complexity

```text
O(m + n)
```

Each element from both arrays is processed at most once.

There is no additional sorting operation.

## Space Complexity

```text
O(1)
```

Only three pointer variables are required:

```text
a
b
c
```

The algorithm modifies `nums1` directly and does not create another array proportional to the input size.

---

# Alternative Approach

Another solution would be:

1. Copy all values from `nums2` into the unused portion of `nums1`.
2. Sort the entire `nums1` array.

For example:

```java
for (int i = 0; i < n; i++) {
    nums1[m + i] = nums2[i];
}

Arrays.sort(nums1);
```

This is simpler, but sorting takes approximately:

```text
O((m + n) log(m + n))
```

The two-pointer solution is better because it takes advantage of the fact that both arrays are already sorted and achieves:

```text
O(m + n)
```

---

# Edge Cases

### `nums2` is empty

```text
nums1 = [1]
nums2 = []
```

Result:

```text
[1]
```

### `nums1` contains no valid elements

```text
nums1 = [0]
nums2 = [1]
```

Result:

```text
[1]
```

### All `nums2` values are smaller

```text
nums1 = [4,5,6,0,0,0]
nums2 = [1,2,3]
```

Result:

```text
[1,2,3,4,5,6]
```

### Duplicate values

```text
nums1 = [1,2,2,0,0,0]
nums2 = [2,2,3]
```

Result:

```text
[1,2,2,2,2,3]
```

---

# Interview Explanation

Both input arrays are already sorted, and `nums1` contains enough unused space at the end to store the complete result.

If I merged from left to right, I could overwrite unprocessed values in `nums1`. Therefore, I merge from right to left.

I use three pointers: one for the last valid value in `nums1`, one for the last value in `nums2`, and one for the final available position in `nums1`.

At each step, I compare the two largest remaining values and place the larger one into the destination position. I then move the corresponding source pointer and the destination pointer to the left.

If `nums2` still contains elements after the main comparison loop, I copy them into `nums1`.

This gives `O(m + n)` time complexity and `O(1)` extra space.

---

# What I Learned

* Arrays use zero-based indexing.
* A pointer variable stores an **index**, while `nums[index]` accesses the actual value.
* When unused space is at the end of an array, processing from right to left can prevent overwriting data.
* Two-pointer problems often require careful reasoning about when each pointer should move.
* A loop may finish while one data source still contains unprocessed elements.
* Always ask what remains after the main loop terminates.
* Existing sorted order can often be used to avoid unnecessary sorting.
