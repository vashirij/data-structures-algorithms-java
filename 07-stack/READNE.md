# 155. Min Stack

**Difficulty:** Medium
**Topics:** Stack, Design
**Status:** Solved

## Original Question

Design a stack that supports `push`, `pop`, `top`, and retrieving the minimum element in **constant time**.

Implement the `MinStack` class:

* `MinStack()` initializes the stack object.
* `void push(int val)` pushes `val` onto the stack.
* `void pop()` removes the element on the top of the stack.
* `int top()` gets the top element of the stack.
* `int getMin()` retrieves the minimum element in the stack.

Every operation must run in:

```text
O(1)
```

---

## Example

```text
Input:
["MinStack","push","push","push","getMin","pop","top","getMin"]

[[],[-2],[0],[-3],[],[],[],[]]

Output:
[null,null,null,null,-3,null,0,-2]
```

Operations:

```java
MinStack minStack = new MinStack();

minStack.push(-2);
minStack.push(0);
minStack.push(-3);

minStack.getMin(); // -3

minStack.pop();

minStack.top();    // 0
minStack.getMin(); // -2
```

---

# Constraints

```text
-2^31 <= val <= 2^31 - 1
```

`pop()`, `top()`, and `getMin()` will always be called on non-empty stacks.

At most:

```text
3 * 10^4
```

calls will be made to the operations.

Most importantly:

> Every operation must have O(1) time complexity.

---

# 1. Inputs

Unlike many array problems, this problem does not have one input array.

Instead, we are designing a data structure that receives operations over time.

The main input operation is:

```java
push(int val)
```

For example:

```text
push(-2)
push(0)
push(-3)
```

The stack then contains:

```text
TOP

-3
 0
-2
```

---

# 2. Outputs

Different methods produce different results.

### `push(val)`

Adds an element.

Returns nothing.

### `pop()`

Removes the top element.

Returns nothing.

### `top()`

Returns the top element.

### `getMin()`

Returns the smallest element currently stored in the stack.

---

# 3. Required Operations

The data structure must support:

```text
push
pop
top
getMin
```

with each operation running in:

```text
O(1)
```

A normal stack already gives us:

```text
push → O(1)
pop  → O(1)
top  → O(1)
```

The difficult operation is:

```text
getMin → O(1)
```

---

# 4. Key Constraint

The most important clue is:

> `getMin()` must execute in O(1).

Suppose the stack contains:

```text
TOP

 5
-3
 7
 2
```

A simple approach would scan the entire stack looking for the smallest number.

That would require:

```text
5
↓
-3
↓
7
↓
2
```

and therefore take:

```text
O(n)
```

This violates the requirement.

So the minimum must already be available when `getMin()` is called.

---

# 5. First Idea — Store One `min` Variable

We might try:

```java
int min;
```

Whenever we push a value:

```text
min = smaller of current min and new value
```

Example:

```text
push(-2)

min = -2

push(0)

min = -2

push(-3)

min = -3
```

So far this works.

But now:

```text
pop()
```

removes:

```text
-3
```

What should the minimum become?

```text
-2
```

But if we only stored:

```text
min = -3
```

we have forgotten that the previous minimum was `-2`.

Therefore, storing only one minimum value is not enough.

---

# 6. Key Observation

When values are pushed onto the stack, we need to remember the minimum that existed at each stage.

For:

```text
push(-2)
push(0)
push(-3)
```

the values are:

```text
Value     Minimum so far

 -2           -2
  0           -2
 -3           -3
```

This suggests maintaining **two stacks**.

---

# 7. Data Structure Design

We maintain:

```text
stack
minStack
```

## Main Stack

Stores the actual values.

```text
stack
```

## Minimum Stack

Stores information needed to retrieve the current minimum.

```text
minStack
```

The top of `minStack` always represents the minimum associated with the current state of the main stack.

---

# 8. Manual Example

Start with:

```text
stack    = []
minStack = []
```

## push(-2)

Main stack:

```text
[-2]
```

Current minimum:

```text
-2
```

Minimum stack:

```text
[-2]
```

---

## push(0)

Main stack:

```text
[-2, 0]
```

The minimum is still:

```text
-2
```

So we preserve that minimum information.

---

## push(-3)

Main stack:

```text
[-2, 0, -3]
```

Now:

```text
-3 < -2
```

so the minimum becomes:

```text
-3
```

---

# 9. Why a Second Stack Works

The important idea is that minimum information changes as the stack changes.

The second stack gives us a history of minimum values.

When an element is removed, the corresponding minimum information can also be removed.

Therefore we don't need to scan the remaining elements to rediscover the minimum.

---

# 10. `push()` Reasoning

When a new value arrives, two things must happen:

```text
1. Add the value to the main stack.
2. Determine the minimum for the new stack state.
```

Suppose:

```text
current minimum = -2
new value       = -3
```

The new minimum is:

```text
-3
```

But suppose:

```text
current minimum = -2
new value       = 5
```

The minimum remains:

```text
-2
```

So the minimum information must be updated whenever a value is pushed.

---

# 11. `pop()` Reasoning

A stack follows:

```text
LIFO
```

which means:

```text
Last In, First Out
```

If the latest value is removed, the minimum information associated with that state must also be removed.

This restores the minimum information for the previous state.

This is the key reason a stack works so naturally for storing minimum history.

---

# 12. `top()` Reasoning

The main stack already stores the values in stack order.

Therefore:

```java
stack.peek()
```

returns the current top.

No traversal is required.

Time:

```text
O(1)
```

---

# 13. `getMin()` Reasoning

The minimum information is maintained while values are pushed and popped.

Therefore `getMin()` does not calculate the minimum.

It simply retrieves the minimum already being maintained.

Conceptually:

```java
minStack.peek()
```

Time:

```text
O(1)
```

This distinction is important:

> `getMin()` retrieves the minimum; it does not search for the minimum.

---

# 14. Control-Flow Reasoning

This problem is different from problems such as array traversal.

We do **not** need a `for` loop to search for the minimum.

We do **not** need a `while` loop to search for the minimum.

Doing either would make `getMin()` potentially:

```text
O(n)
```

Instead, we maintain the required information as operations happen.

---

# Why `if` Is Appropriate in `push()`

When pushing a value, we need to determine how it affects the minimum.

Conceptually:

```text
Is this the first value?

or

Is this value smaller than/equal to the current minimum?
```

Those are conditional decisions, so `if` is appropriate.

We are not repeatedly processing elements, so a loop is unnecessary.

---

# Why Not a `for` Loop?

A `for` loop would make sense if we needed to process every element.

For example:

```text
for every element:
    calculate minimum
```

But that would require scanning values.

The problem explicitly requires:

```text
getMin() = O(1)
```

Therefore scanning is not appropriate.

---

# Why Not a `while` Loop?

A `while` loop is appropriate when an operation needs to continue until some changing condition becomes false.

For example:

```text
while stack is not empty
```

But `push`, `pop`, `top`, and `getMin` each perform a fixed number of operations.

There is no repeated process.

Therefore `while` is unnecessary.

---

# Java Implementation

```java
import java.util.Stack;

class MinStack {

    private Stack<Integer> stack;
    private Stack<Integer> minStack;

    public MinStack() {
        stack = new Stack<>();
        minStack = new Stack<>();
    }

    public void push(int val) {

        stack.push(val);

        if (minStack.isEmpty() || val <= minStack.peek()) {
            minStack.push(val);
        }
    }

    public void pop() {

        int removed = stack.pop();

        if (removed == minStack.peek()) {
            minStack.pop();
        }
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return minStack.peek();
    }
}
```

---

# Why Use `<=` Instead of `<`?

This is an important edge case.

Suppose:

```text
push(2)
push(2)
```

Both values are minimum values.

If we only used:

```java
val < minStack.peek()
```

the second `2` would not be stored in `minStack`.

Then:

```text
pop()
```

could remove one `2`, and we might incorrectly lose track of the duplicate minimum.

Using:

```java
val <= minStack.peek()
```

allows duplicate minimum values to be tracked correctly.

---

# Manual Walkthrough

Consider:

```text
push(-2)
push(0)
push(-3)
getMin()
pop()
top()
getMin()
```

Initially:

```text
stack    = []
minStack = []
```

### push(-2)

```text
stack:
[-2]

minStack:
[-2]
```

Minimum:

```text
-2
```

### push(0)

`0` is not smaller than or equal to `-2`.

```text
stack:
[-2, 0]

minStack:
[-2]
```

Minimum remains:

```text
-2
```

### push(-3)

```text
-3 <= -2
```

So:

```text
stack:
[-2, 0, -3]

minStack:
[-2, -3]
```

### getMin()

Return the top of `minStack`:

```text
-3
```

### pop()

Remove:

```text
-3
```

Because `-3` is also the current minimum, remove it from `minStack`.

Now:

```text
stack:
[-2, 0]

minStack:
[-2]
```

### top()

Return:

```text
0
```

### getMin()

Return:

```text
-2
```

This matches the expected output.

---

# Edge Case — Duplicate Minimum

Consider:

```text
push(2)
push(1)
push(1)
```

We need:

```text
stack:
[2,1,1]

minStack:
[2,1,1]
```

Now:

```text
pop()
```

removes one `1`.

The minimum must still be:

```text
1
```

Because another `1` remains.

This is why duplicate minimum values must be handled correctly.

---

# Complexity Analysis

## `push()`

```text
O(1)
```

A constant number of stack operations are performed.

## `pop()`

```text
O(1)
```

A constant number of stack operations are performed.

## `top()`

```text
O(1)
```

The top element is accessed directly.

## `getMin()`

```text
O(1)
```

The minimum is stored at the top of `minStack`.

Therefore:

| Operation  |   Time |
| ---------- | -----: |
| `push()`   | `O(1)` |
| `pop()`    | `O(1)` |
| `top()`    | `O(1)` |
| `getMin()` | `O(1)` |

## Space Complexity

```text
O(n)
```

The main stack stores the values, and the auxiliary minimum stack may also contain up to `n` values.

---

# Why Not Scan the Stack for the Minimum?

An alternative would be:

```text
getMin():

    minimum = first element

    for every element:
        minimum = smaller value

    return minimum
```

This would require:

```text
O(n)
```

time for `getMin()`.

The problem specifically requires:

```text
O(1)
```

Therefore this approach is not acceptable.

---

# Why Not Sort the Values?

Sorting would destroy the normal stack order.

A stack must preserve:

```text
Last In, First Out
```

For example:

```text
push(5)
push(1)
push(3)
```

The top must be:

```text
3
```

Sorting:

```text
[1,3,5]
```

would destroy that relationship.

Sorting also costs more than `O(1)`.

Therefore sorting is inappropriate.

---

# Interview Explanation

A normal stack already supports `push`, `pop`, and `top` in constant time, but finding the minimum by scanning would require `O(n)`.

To make `getMin()` constant time, I maintain a second stack containing minimum values.

Whenever I push a value that is less than or equal to the current minimum, I also push it onto the minimum stack.

When popping, if the removed value equals the current minimum, I pop the minimum stack as well.

This preserves the history of minimum values as the main stack grows and shrinks.

As a result, `getMin()` simply returns the top of the minimum stack.

All four operations run in `O(1)` time, while the additional space is `O(n)`.

---

# What I Learned

* Constraints can determine the data structure required by the solution.
* A normal stack gives `push`, `pop`, and `top` in `O(1)`, but not necessarily `getMin()`.
* If information will be expensive to calculate later, it can sometimes be maintained as the data structure changes.
* An auxiliary data structure can trade additional space for faster operations.
* A second stack can preserve historical state.
* Stack LIFO behavior makes it possible to restore the previous minimum after `pop()`.
* Duplicate minimum values must be handled carefully.
* `<=` is important when tracking duplicate minima.
* Not every problem requires loops.
* Before selecting control flow, determine whether repetition is actually necessary.
* The `O(1)` requirement rules out scanning the stack each time `getMin()` is called.
