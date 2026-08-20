# 4. Median of Two Sorted Arrays

**Difficulty:** Hard
**Topics:** Array, Binary Search, Divide and Conquer
**Status:** In Progress
**LeetCode:** https://leetcode.com/problems/median-of-two-sorted-arrays/

---

## Original Question

Given two sorted arrays `nums1` and `nums2` of size `m` and `n` respectively, return the **median** of the two sorted arrays.

The overall run time complexity should be:

```text
O(log(m + n))
```

---

## Example 1

```text
Input:
nums1 = [1,3]
nums2 = [2]

Output:
2.00000
```

Merged conceptually:

```text
[1,2,3]
```

The middle element is:

```text
2
```

Therefore:

```text
median = 2
```

---

## Example 2

```text
Input:
nums1 = [1,2]
nums2 = [3,4]

Output:
2.50000
```

Merged conceptually:

```text
[1,2,3,4]
```

There are an even number of elements, so the median is the average of the two middle values:

```text
(2 + 3) / 2
= 2.5
```

---

## Constraints

* `nums1.length == m`
* `nums2.length == n`
* `0 <= m <= 1000`
* `0 <= n <= 1000`
* `1 <= m + n <= 2000`
* `-10^6 <= nums1[i], nums2[i] <= 10^6`

---

# 1. Inputs

The function receives two sorted integer arrays:

```text
nums1
nums2
```

Example:

```text
nums1 = [1,3]
nums2 = [2]
```

Both arrays are sorted in non-decreasing order.

That sorted property is extremely important because the required complexity is:

```text
O(log(m+n))
```

---

# 2. Output

Return one value:

```text
median
```

as a `double`.

The median depends on the total number of elements.

Let:

```text
total = m + n
```

If `total` is odd:

```text
median = middle element
```

If `total` is even:

```text
median = average of two middle elements
```

---

# 3. First Simple Idea

A straightforward solution would be:

```text
merge nums1 and nums2
        ↓
create one sorted array
        ↓
find the middle element(s)
```

For example:

```text
nums1 = [1,3]
nums2 = [2]
```

Merged:

```text
[1,2,3]
```

Median:

```text
2
```

This is easy to understand.

However, merging requires:

```text
O(m+n)
```

time.

The problem specifically asks for:

```text
O(log(m+n))
```

Therefore, a normal merge is not enough for the optimal solution.

---

# 4. Key Constraint

The most important clue is:

> Both arrays are already sorted, and the required complexity is logarithmic.

Whenever I see:

```text
sorted data
+
O(log n)
```

I should immediately consider:

```text
Binary Search
```

But this problem is not asking whether a particular value exists.

Instead, binary search must help us find the correct **partition** between the left and right halves of the combined data.

---

# 5. What Is the Median Really Asking For?

Consider:

```text
nums1 = [1,3]
nums2 = [2]
```

Conceptually merged:

```text
[1,2 | 3]
```

The median divides the combined sorted values into:

```text
left half
right half
```

For an odd number of elements, the left half may contain one extra element.

For an even number:

```text
[1,2 | 3,4]
```

both halves contain the same number of elements.

So instead of actually merging the arrays, we can ask:

> Can I partition both arrays so that all values on the left are less than or equal to all values on the right?

If yes, then the median can be calculated directly from the values around that partition.

---

# 6. Partition Idea

Suppose we have:

```text
nums1 = [1,3]
nums2 = [2]
```

Imagine choosing a partition in each array:

```text
nums1 = [1 | 3]

nums2 = [2 |]
```

Combined left side:

```text
[1,2]
```

Combined right side:

```text
[3]
```

The partition is valid if:

```text
largest value on left
<=
smallest value on right
```

The values around the partition are what matter.

---

# 7. Why Binary Search One Array?

Suppose we choose a partition position in `nums1`.

Once that position is known, the partition position in `nums2` can be calculated automatically because the total number of elements required on the left side is fixed.

This means we only need to search one array.

For efficiency, binary search should be performed on the **smaller array**.

Why?

Because the complexity becomes:

```text
O(log(min(m,n)))
```

which is even stronger than the required:

```text
O(log(m+n))
```

---

# 8. Ensure `nums1` Is the Smaller Array

Before binary search, check:

```java
if (nums1.length > nums2.length)
```

If true, swap the roles:

```java
return findMedianSortedArrays(nums2, nums1);
```

This ensures:

```text
m <= n
```

and keeps the binary-search boundaries valid.

---

# 9. Partition Sizes

Let:

```text
m = nums1.length
n = nums2.length
```

The total number of elements that should be on the left side is:

```text
(m + n + 1) / 2
```

The `+1` helps handle both odd and even totals consistently.

If we choose:

```text
partition1
```

elements from `nums1`, then we need:

```text
partition2
```

elements from `nums2`.

Therefore:

```text
partition2 =
(m + n + 1) / 2 - partition1
```

---

# 10. Values Around the Partition

For `nums1`, we need:

```text
left1
right1
```

For `nums2`, we need:

```text
left2
right2
```

Conceptually:

```text
nums1:
... left1 | right1 ...

nums2:
... left2 | right2 ...
```

A valid partition requires:

```text
left1 <= right2
```

and:

```text
left2 <= right1
```

If both are true, the combined left half is correctly separated from the combined right half.

---

# 11. Boundary Problem

What if the partition is at the beginning of an array?

Example:

```text
| 1 2 3
```

There is no value on the left.

What if the partition is at the end?

```text
1 2 3 |
```

There is no value on the right.

To handle this cleanly, use conceptual sentinel values.

If there is no left value:

```java
Integer.MIN_VALUE
```

If there is no right value:

```java
Integer.MAX_VALUE
```

This allows the same comparison logic to work without many special cases.

---

# 12. Partition Values

For `nums1`:

```java
int left1 =
    partition1 == 0
        ? Integer.MIN_VALUE
        : nums1[partition1 - 1];
```

```java
int right1 =
    partition1 == m
        ? Integer.MAX_VALUE
        : nums1[partition1];
```

For `nums2`:

```java
int left2 =
    partition2 == 0
        ? Integer.MIN_VALUE
        : nums2[partition2 - 1];
```

```java
int right2 =
    partition2 == n
        ? Integer.MAX_VALUE
        : nums2[partition2];
```

---

# 13. Valid Partition Condition

We have found the correct partition when:

```java
left1 <= right2 && left2 <= right1
```

Why?

Because then:

```text
everything on the left
<=
everything on the right
```

which is exactly what the median requires.

---

# 14. Calculating the Median

## Odd Total Length

If:

```text
(m + n) % 2 == 1
```

the left side contains one extra element.

Therefore the median is:

```java
Math.max(left1, left2)
```

because that is the largest value in the combined left half.

---

## Even Total Length

If the total is even, the median lies between:

```text
largest value on the left
```

and:

```text
smallest value on the right
```

Therefore:

```java
(
    Math.max(left1, left2)
    +
    Math.min(right1, right2)
) / 2.0
```

---

# 15. What If the Partition Is Wrong?

There are two ways a partition can fail.

## Case 1: `left1 > right2`

Example conceptually:

```text
nums1 left side has a value that is too large
```

This means we selected too many elements from `nums1`.

Therefore move the partition left.

```java
right = partition1 - 1;
```

---

## Case 2: `left2 > right1`

This means we selected too few elements from `nums1`.

Therefore move the partition right.

```java
left = partition1 + 1;
```

This is exactly where binary search enters the algorithm.

---

# 16. Control-Flow Reasoning

## Why Use `while`?

The binary search uses:

```java
while (left <= right)
```

This is appropriate because we continue searching while a valid search interval exists.

The termination depends on the changing state of:

```text
left
right
```

rather than processing every array element.

Therefore `while` is more natural than `for`.

---

# 17. Why Not Use a `for` Loop?

A normal `for` loop would imply something like:

```text
visit every index
```

But that would move toward:

```text
O(m)
```

or:

```text
O(m+n)
```

behavior.

We do not want to inspect every partition.

Instead, we repeatedly eliminate half of the remaining search space.

That is binary search behavior, so:

```java
while (left <= right)
```

is appropriate.

---

# 18. Why `if / else if / else`?

There are three mutually exclusive situations.

### Valid partition

```java
left1 <= right2 && left2 <= right1
```

### Partition too far right

```java
left1 > right2
```

### Partition too far left

Otherwise:

```text
left2 > right1
```

Only one of these should determine the next action.

Therefore:

```java
if (...) {

} else if (...) {

} else {

}
```

is appropriate.

Separate independent `if` statements would not express the mutually exclusive decision as clearly.

---

# 19. Pseudocode

```text
if nums1 is larger than nums2
    swap them

m = nums1.length
n = nums2.length

left = 0
right = m

while left <= right

    partition1 = midpoint of left and right

    partition2 =
        (m + n + 1) / 2 - partition1

    find:
        left1
        right1
        left2
        right2

    if partition is valid

        if total number of elements is odd

            return largest left value

        else

            return average of:
                largest left value
                smallest right value

    else if left1 > right2

        move search left

    else

        move search right
```

---

# 20. Java Solution

```java
class Solution {

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {

        // Always binary search the smaller array
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }

        int m = nums1.length;
        int n = nums2.length;

        int left = 0;
        int right = m;

        while (left <= right) {

            int partition1 = left + (right - left) / 2;

            int partition2 =
                (m + n + 1) / 2 - partition1;

            int left1 =
                partition1 == 0
                    ? Integer.MIN_VALUE
                    : nums1[partition1 - 1];

            int right1 =
                partition1 == m
                    ? Integer.MAX_VALUE
                    : nums1[partition1];

            int left2 =
                partition2 == 0
                    ? Integer.MIN_VALUE
                    : nums2[partition2 - 1];

            int right2 =
                partition2 == n
                    ? Integer.MAX_VALUE
                    : nums2[partition2];

            // Correct partition
            if (left1 <= right2 && left2 <= right1) {

                // Odd total
                if ((m + n) % 2 == 1) {
                    return Math.max(left1, left2);
                }

                // Even total
                return (
                    Math.max(left1, left2)
                    +
                    Math.min(right1, right2)
                ) / 2.0;
            }

            // Too many elements taken from nums1
            else if (left1 > right2) {
                right = partition1 - 1;
            }

            // Too few elements taken from nums1
            else {
                left = partition1 + 1;
            }
        }

        throw new IllegalArgumentException(
            "Input arrays must be sorted."
        );
    }
}
```

---

# 21. Manual Walkthrough — Example 1

Given:

```text
nums1 = [1,3]
nums2 = [2]
```

Since `nums1` is larger, swap them.

Now:

```text
nums1 = [2]
nums2 = [1,3]

m = 1
n = 2
```

Binary-search boundaries:

```text
left = 0
right = 1
```

Choose:

```text
partition1 = 0
```

Then:

```text
partition2 =
(1 + 2 + 1) / 2 - 0

= 2
```

Partitions:

```text
nums1:
| 2

nums2:
1 3 |
```

Boundary values:

```text
left1  = -∞
right1 = 2

left2  = 3
right2 = +∞
```

Check:

```text
left2 <= right1
3 <= 2
```

False.

We need more values from `nums1`.

Move right:

```text
left = partition1 + 1
left = 1
```

---

## Next Partition

```text
partition1 = 1
```

Then:

```text
partition2 = 1
```

Partitions:

```text
nums1:
2 |

nums2:
1 | 3
```

Boundary values:

```text
left1  = 2
right1 = +∞

left2  = 1
right2 = 3
```

Check:

```text
2 <= 3
1 <= +∞
```

Both true.

Correct partition found.

Total number of values:

```text
3
```

Odd.

Therefore:

```text
median =
max(left1, left2)

= max(2,1)

= 2
```

Correct.

---

# 22. Manual Walkthrough — Example 2

Given:

```text
nums1 = [1,2]
nums2 = [3,4]
```

Total:

```text
4
```

Eventually the correct partition gives:

```text
left side:
[1,2]

right side:
[3,4]
```

Largest left value:

```text
2
```

Smallest right value:

```text
3
```

Median:

```text
(2 + 3) / 2.0

= 2.5
```

---

# 23. Edge Cases

## First Array Empty

```text
nums1 = []
nums2 = [1]
```

The partition logic still works because sentinel values handle the empty side.

Expected:

```text
1.0
```

---

## Second Array Empty

```text
nums1 = [2]
nums2 = []
```

The method swaps arrays so that the empty array becomes the binary-search array.

Expected:

```text
2.0
```

---

## Both Arrays Have One Element

```text
nums1 = [1]
nums2 = [2]
```

Combined:

```text
[1,2]
```

Median:

```text
1.5
```

---

## Duplicate Values

```text
nums1 = [1,1]
nums2 = [1,1]
```

Median:

```text
1.0
```

Duplicate values do not require special handling.

---

## Negative Values

```text
nums1 = [-5,-3]
nums2 = [-2,-1]
```

Combined conceptually:

```text
[-5,-3,-2,-1]
```

Median:

```text
(-3 + -2) / 2
= -2.5
```

---

# 24. Complexity Analysis

Let the smaller array contain:

```text
min(m,n)
```

elements.

Binary search runs on that array.

Therefore:

```text
Time:
O(log(min(m,n)))
```

This satisfies the required:

```text
O(log(m+n))
```

Extra space:

```text
O(1)
```

The algorithm only uses a constant number of variables.

---

# 25. Why Not Merge the Arrays?

The simple merge approach would take:

```text
O(m+n)
```

time.

It would work functionally, but it would fail the required optimal complexity.

The entire purpose of this problem is to exploit sorted order using binary search.

---

# 26. Why Not Sort After Combining?

Combining and sorting would take approximately:

```text
O((m+n) log(m+n))
```

This is even slower.

It also ignores the fact that both input arrays are already sorted.

---

# 27. Why Search the Smaller Array?

Binary searching the smaller array guarantees:

```text
O(log(min(m,n)))
```

and avoids invalid partition calculations.

If one array has:

```text
2 elements
```

and the other has:

```text
1000 elements
```

there is no reason to binary search the larger one.

Searching the smaller array gives fewer possible partition positions.

---

# 28. Common Mistakes

## Mistake 1: Actually Merging the Arrays

This produces:

```text
O(m+n)
```

instead of logarithmic time.

---

## Mistake 2: Binary Searching Both Arrays Independently

Only one partition needs to be searched.

Once:

```text
partition1
```

is known:

```text
partition2
```

is determined automatically.

---

## Mistake 3: Forgetting Empty Partition Boundaries

Accessing:

```java
nums1[partition1 - 1]
```

when:

```text
partition1 = 0
```

would cause an invalid index.

Sentinel values prevent this.

---

## Mistake 4: Integer Division for Even Median

Wrong:

```java
(a + b) / 2
```

if both are integers.

Use:

```java
(a + b) / 2.0
```

to ensure a `double` result.

---

## Mistake 5: Wrong Partition Size

The left side should contain:

```text
(m + n + 1) / 2
```

elements.

The `+1` is especially important for odd total lengths.

---

## Mistake 6: Moving Binary Search in the Wrong Direction

If:

```text
left1 > right2
```

the partition in `nums1` is too far right.

Move:

```java
right = partition1 - 1;
```

If:

```text
left2 > right1
```

the partition in `nums1` is too far left.

Move:

```java
left = partition1 + 1;
```

---

# 29. Interview Explanation

Both arrays are sorted, and the required complexity is logarithmic, so merging them directly is too slow.

Instead, I binary search a partition position in the smaller array. Once that partition is chosen, the corresponding partition in the second array is determined so that the combined left side contains half of all elements.

For a valid partition, the largest values on the left must be less than or equal to the smallest values on the right.

Specifically:

```text
left1 <= right2
```

and:

```text
left2 <= right1
```

If the partition is valid, I can calculate the median directly from the boundary values.

If `left1 > right2`, I move the binary search left. Otherwise I move it right.

The algorithm runs in:

```text
O(log(min(m,n)))
```

time and:

```text
O(1)
```

extra space.

---

# 30. What I Learned

* A sorted-input problem with a logarithmic requirement strongly suggests binary search.
* Binary search does not have to search for a specific value; it can search for a valid partition.
* The median can be derived from the boundary between the left and right halves.
* Once one partition is chosen, the other partition can be calculated.
* Searching the smaller array improves efficiency and simplifies boundaries.
* Sentinel values can eliminate complicated edge-case branching.
* `while` is appropriate because binary-search termination depends on changing `left` and `right` boundaries.
* `if / else if / else` is appropriate because the partition can be correct, too far left, or too far right.
* The optimal solution avoids actually merging the arrays.
* This problem is fundamentally about **partitioning**, not merging.

---

# Pattern Recognition

When I see:

```text
two sorted arrays
+
median / kth element
+
O(log n)
```

I should consider:

```text
Binary search on a partition
        ↓
Determine corresponding partition
        ↓
Inspect boundary values
        ↓
Too far left?
Too far right?
Correct?
        ↓
Calculate answer from partition boundaries
```

Core validity condition:

```java
left1 <= right2 && left2 <= right1
```

Core complexity:

```text
O(log(min(m,n)))
```