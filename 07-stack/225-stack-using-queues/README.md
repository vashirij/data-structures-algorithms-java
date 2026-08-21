# 225. Implement Stack using Queues

**Difficulty:** Easy  
**Topics:** Stack, Queue, Design  
**Status:** In Progress  
**LeetCode:** https://leetcode.com/problems/implement-stack-using-queues/

---

## Original Question

Implement a **Last-In-First-Out (LIFO) stack** using only standard queue operations.

Implement the `MyStack` class:

```java
void push(int x)
int pop()
int top()
boolean empty()
```

Allowed queue operations:

- Add to the back
- Remove from the front
- Peek at the front
- Get the size
- Check whether the queue is empty

---

# 1. Understand the Difference

The main challenge is that a **stack and queue remove elements in opposite orders**.

## Stack — LIFO

**Last In, First Out**

```text
push(1)
push(2)
push(3)

TOP
 ↓
 3
 2
 1
```

Calling:

```text
pop()
```

returns:

```text
3
```

---

## Queue — FIFO

**First In, First Out**

```text
FRONT           BACK
  ↓               ↓
[ 1 ] → [ 2 ] → [ 3 ]
```

Calling:

```text
poll()
```

returns:

```text
1
```

But our stack needs to return:

```text
3
```

So the question becomes:

> How can we make a FIFO queue behave like a LIFO stack?

---

# 2. Inputs

The class receives input through its methods.

### Push

```java
push(int x)
```

Example:

```text
x = 5
```

---

### Pop

```java
pop()
```

No input.

It removes and returns the top element.

---

### Top

```java
top()
```

No input.

It returns the top element without removing it.

---

### Empty

```java
empty()
```

No input.

Returns:

```text
true
```

or:

```text
false
```

---

# 3. Outputs

| Operation | Output |
|---|---|
| `push(x)` | nothing |
| `pop()` | removed top value |
| `top()` | current top value |
| `empty()` | boolean |

---

# 4. Operations Required

We need to simulate:

```text
STACK
   ↓
push
pop
top
empty
```

using:

```text
QUEUE
   ↓
offer
poll
peek
size
isEmpty
```

We cannot use stack-specific operations.

---

# 5. Key Observation

Suppose we push:

```text
1
2
```

A normal queue becomes:

```text
FRONT       BACK
  ↓           ↓
[1]   →     [2]
```

But our stack needs:

```text
top = 2
```

The problem is that:

```text
queue.peek() = 1
```

while:

```text
stack.top() = 2
```

We need to change the queue's order.

---

# 6. Approach 1 — Two Queues

We can maintain:

```java
Queue<Integer> queue1;
Queue<Integer> queue2;
```

The idea is to keep the newest element at the **front** of the active queue.

Then:

```text
queue front = stack top
```

This makes:

```text
pop
top
```

easy.

---

# 7. Desired Queue Order

After:

```java
push(1);
push(2);
push(3);
```

instead of:

```text
1 → 2 → 3
```

we want:

```text
FRONT
  ↓
3 → 2 → 1
```

Now:

```java
queue.peek()
```

returns:

```text
3
```

which is exactly the top of the simulated stack.

---

# 8. `push(x)`

Suppose:

```text
queue1:

FRONT
  ↓
2 → 1
```

Now:

```java
push(3)
```

We need:

```text
3 → 2 → 1
```

But a queue only lets us add to the **back**.

So first put `3` into the empty second queue:

```text
queue2:

3
```

Then move everything from `queue1` into `queue2`:

```text
queue2:

3 → 2 → 1
```

Finally swap the queue references.

Now:

```text
queue1:

3 → 2 → 1
```

and `queue2` is empty.

---

# 9. Why Use a `while` Loop in `push()`?

We need to move elements until:

```text
queue1 is empty
```

The number of iterations depends on the current number of stored elements.

Therefore:

```java
while (!queue1.isEmpty())
```

is appropriate.

The stopping condition is based on the changing state of the queue.

---

# 10. Why Not an `if`?

An `if` executes only once.

For:

```text
queue1 = [3,2,1]
```

we need to move:

```text
3
2
1
```

not just one element.

Therefore:

```java
if (!queue1.isEmpty())
```

would move only one item.

We need repetition, so we use:

```java
while (!queue1.isEmpty())
```

---

# 11. Why Not a `for` Loop?

A `for` loop could work if we saved:

```java
int size = queue1.size();
```

and moved exactly `size` elements.

However, our natural stopping condition is:

```text
continue until queue1 is empty
```

Therefore a `while` loop communicates the intention more directly.

---

# 12. `push()` Pseudocode

```text
push(x):

    add x to queue2

    while queue1 is not empty

        remove front of queue1

        add it to queue2

    swap queue1 and queue2
```

---

# 13. Why Swap the Queues?

After moving:

```text
queue1 = empty

queue2 = new stack order
```

We want `queue1` to remain our primary queue.

Instead of copying everything again, simply swap the references:

```java
Queue<Integer> temp = queue1;

queue1 = queue2;

queue2 = temp;
```

Now:

```text
queue1 = stack contents
queue2 = empty helper queue
```

---

# 14. `pop()`

Because we maintain:

```text
stack top = queue front
```

`pop()` becomes simple:

```java
return queue1.poll();
```

Example:

```text
queue1:

3 → 2 → 1
↑
front
```

Call:

```java
pop();
```

returns:

```text
3
```

Remaining:

```text
2 → 1
```

Exactly like a stack.

---

# 15. Why No Loop in `pop()`?

Because `push()` already did the work of rearranging the queue.

The top element is always at:

```text
queue1 front
```

Therefore only one queue operation is needed:

```java
queue1.poll();
```

---

# 16. `top()`

The top element is at the front of `queue1`.

Therefore:

```java
return queue1.peek();
```

Unlike `poll()`:

```text
peek = read without removing
poll = read and remove
```

---

# 17. `empty()`

We simply ask whether our primary queue contains any elements:

```java
return queue1.isEmpty();
```

---

# 18. Two-Queue Java Solution

```java
import java.util.LinkedList;
import java.util.Queue;

class MyStack {

    private Queue<Integer> queue1;
    private Queue<Integer> queue2;

    public MyStack() {

        queue1 = new LinkedList<>();
        queue2 = new LinkedList<>();
    }

    public void push(int x) {

        // New element enters the empty helper queue.
        queue2.offer(x);

        // Move existing stack elements behind it.
        while (!queue1.isEmpty()) {
            queue2.offer(queue1.poll());
        }

        // Swap queues.
        Queue<Integer> temp = queue1;
        queue1 = queue2;
        queue2 = temp;
    }

    public int pop() {

        return queue1.poll();
    }

    public int top() {

        return queue1.peek();
    }

    public boolean empty() {

        return queue1.isEmpty();
    }
}
```

---

# 19. Manual Walkthrough

Start:

```java
MyStack stack = new MyStack();
```

State:

```text
queue1 = []
queue2 = []
```

---

## push(1)

First:

```text
queue2 = [1]
```

`queue1` is empty, so the `while` loop does not execute.

Swap:

```text
queue1 = [1]
queue2 = []
```

Stack representation:

```text
TOP
 ↓
 1
```

---

# 20. push(2)

Before:

```text
queue1 = [1]
queue2 = []
```

Add `2`:

```text
queue2 = [2]
```

Move everything from `queue1`:

```text
queue1.poll() = 1
```

Then:

```text
queue2 = [2,1]
queue1 = []
```

Swap:

```text
queue1 = [2,1]
queue2 = []
```

Now:

```text
FRONT
  ↓
2 → 1
```

Therefore:

```text
top = 2
```

---

# 21. push(3)

Before:

```text
queue1 = [2,1]
```

Add:

```text
queue2 = [3]
```

Move `2`:

```text
queue2 = [3,2]
```

Move `1`:

```text
queue2 = [3,2,1]
```

Swap:

```text
queue1 = [3,2,1]
queue2 = []
```

This represents:

```text
STACK

TOP
 ↓
 3
 2
 1
```

---

# 22. top()

Current queue:

```text
FRONT
  ↓
3 → 2 → 1
```

Execute:

```java
queue1.peek();
```

Return:

```text
3
```

Queue remains:

```text
3 → 2 → 1
```

---

# 23. pop()

Execute:

```java
queue1.poll();
```

Return:

```text
3
```

Queue becomes:

```text
2 → 1
```

This represents:

```text
TOP
 ↓
 2
 1
```

---

# 24. empty()

Queue:

```text
2 → 1
```

Therefore:

```java
queue1.isEmpty()
```

returns:

```text
false
```

---

# 25. Control Flow Summary

## Constructor

No conditional or loop required.

We simply initialize:

```text
queue1
queue2
```

---

## `push()`

Uses:

```java
while
```

because we must repeatedly move elements until:

```text
queue1 is empty
```

---

## `pop()`

No control flow needed.

```java
queue1.poll();
```

directly performs the required operation.

---

## `top()`

No control flow needed.

```java
queue1.peek();
```

directly accesses the stack top.

---

## `empty()`

No control flow needed.

```java
queue1.isEmpty();
```

already returns the required boolean.

---

# 26. Complexity — Two Queues

Let:

```text
n = number of elements currently in the stack
```

### `push(x)`

We may move all existing elements.

```text
Time: O(n)
```

### `pop()`

```text
Time: O(1)
```

### `top()`

```text
Time: O(1)
```

### `empty()`

```text
Time: O(1)
```

### Space

We store all stack elements in queues.

```text
Space: O(n)
```

---

# 27. Follow-Up — Can We Use One Queue?

Yes.

This is an important optimization.

Instead of using:

```text
queue1
queue2
```

we can use:

```text
one queue
```

The trick is to **rotate the queue after every push**.

---

# 28. One-Queue Idea

Suppose:

```text
queue:

FRONT
  ↓
2 → 1
```

This represents:

```text
stack top = 2
```

Now push:

```text
3
```

Normal queue insertion gives:

```text
2 → 1 → 3
```

But we need:

```text
3 → 2 → 1
```

So after adding `3`, rotate the older elements behind it.

---

# 29. Queue Rotation

After:

```java
queue.offer(3);
```

we have:

```text
2 → 1 → 3
```

Queue size:

```text
3
```

The new element is at the back.

Move the previous:

```text
size - 1
```

elements from front to back.

First:

```text
remove 2
add 2

1 → 3 → 2
```

Second:

```text
remove 1
add 1

3 → 2 → 1
```

Now:

```text
3
```

is at the front.

Exactly what we need.

---

# 30. Why `size - 1`?

After adding the new element:

```text
old elements = size - 1
```

We want to rotate all old elements behind the new one.

We do **not** want to rotate the new element again.

Therefore:

```java
for (int i = 0; i < queue.size() - 1; i++)
```

is the idea.

However, because `queue.size()` stays constant during the rotation, this works safely.

---

# 31. Why Use a `for` Loop Here?

Unlike the two-queue approach, we should **not** rotate until the queue becomes empty.

We know exactly how many rotations are needed:

```text
size - 1
```

Therefore a `for` loop is more appropriate:

```java
for (int i = 0; i < size - 1; i++)
```

This is a good example of choosing control flow based on the problem.

Use:

```text
while
```

when:

```text
repeat until a state condition changes
```

Use:

```text
for
```

when:

```text
repeat a known number of times
```

---

# 32. One-Queue Java Solution

```java
import java.util.LinkedList;
import java.util.Queue;

class MyStack {

    private Queue<Integer> queue;

    public MyStack() {

        queue = new LinkedList<>();
    }

    public void push(int x) {

        queue.offer(x);

        int size = queue.size();

        // Rotate all older elements behind x.
        for (int i = 0; i < size - 1; i++) {

            queue.offer(queue.poll());
        }
    }

    public int pop() {

        return queue.poll();
    }

    public int top() {

        return queue.peek();
    }

    public boolean empty() {

        return queue.isEmpty();
    }
}
```

---

# 33. Manual Walkthrough — One Queue

Start:

```text
[]
```

---

## push(1)

Add:

```text
[1]
```

Size:

```text
1
```

Rotations:

```text
size - 1 = 0
```

Final:

```text
[1]
```

---

## push(2)

Before:

```text
[1]
```

Add:

```text
[1,2]
```

Rotate old element:

```text
poll 1
offer 1
```

Result:

```text
[2,1]
```

Stack:

```text
TOP
 ↓
 2
 1
```

---

## push(3)

Before:

```text
[2,1]
```

Add:

```text
[2,1,3]
```

Rotate twice.

First:

```text
[1,3,2]
```

Second:

```text
[3,2,1]
```

Final:

```text
FRONT
  ↓
3 → 2 → 1
```

Correct.

---

# 34. Why Does Rotation Work?

Before inserting `3`, the queue is already organized as:

```text
2 → 1
```

where the front is the stack top.

After adding `3`:

```text
2 → 1 → 3
```

The only problem is that the newest element is at the wrong end.

Rotating all older elements:

```text
2
1
```

behind `3` produces:

```text
3 → 2 → 1
```

The previous stack order remains unchanged behind the new top.

---

# 35. Two Queues vs One Queue

| Operation | Two Queues | One Queue |
|---|---:|---:|
| `push()` | O(n) | O(n) |
| `pop()` | O(1) | O(1) |
| `top()` | O(1) | O(1) |
| `empty()` | O(1) | O(1) |
| Auxiliary queues | 2 | 1 |
| Main technique | Transfer | Rotation |

The one-queue solution is cleaner once the rotation idea is understood.

---

# 36. Alternative Strategy — Expensive `pop()`

There is another valid approach.

Instead of doing the rearrangement during:

```text
push()
```

we could keep the queue in normal order:

```text
1 → 2 → 3
```

Then when:

```text
pop()
```

is requested, move:

```text
1
2
```

out of the way to reach:

```text
3
```

This would give:

```text
push = O(1)
pop  = O(n)
```

Our chosen solution instead gives:

```text
push = O(n)
pop  = O(1)
top  = O(1)
```

---

# 37. Why Choose Expensive `push()`?

A stack conceptually expects its top element to be immediately accessible.

By rearranging during `push()`, we maintain this invariant:

```text
queue front = stack top
```

Then:

```text
pop
top
```

become simple queue operations.

This makes the representation easy to reason about.

---

# 38. Important Invariant

After every operation:

```text
FRONT OF QUEUE
      =
TOP OF STACK
```

Example:

```text
Stack:

TOP
 ↓
 5
 4
 3
 2
 1
```

Queue representation:

```text
FRONT
  ↓
5 → 4 → 3 → 2 → 1
```

As long as this invariant remains true:

```java
pop()
```

can use:

```java
queue.poll();
```

and:

```java
top()
```

can use:

```java
queue.peek();
```

---

# 39. Edge Cases

## One Element

```java
push(1);
```

Queue:

```text
[1]
```

Then:

```java
top()
```

returns:

```text
1
```

and:

```java
pop()
```

returns:

```text
1
```

---

## Empty After Pop

```text
queue = [1]
```

Call:

```java
pop();
```

Queue:

```text
[]
```

Then:

```java
empty();
```

returns:

```text
true
```

---

## Multiple Pushes

```java
push(1);
push(2);
push(3);
push(4);
```

Queue becomes:

```text
4 → 3 → 2 → 1
```

Repeated `pop()` produces:

```text
4
3
2
1
```

which is correct LIFO order.

---

# 40. Common Mistake — Normal Queue Order

If we simply write:

```java
queue.offer(x);
```

without rotating:

```text
push(1)
push(2)
push(3)
```

produces:

```text
1 → 2 → 3
```

Then:

```java
pop()
```

returns:

```text
1
```

But a stack should return:

```text
3
```

Therefore queue order must be rearranged.

---

# 41. Common Mistake — Rotating Too Many Times

After adding the new element, rotate:

```text
size - 1
```

times.

Not:

```text
size
```

times.

If you rotate all `size` elements, you return to the original queue order.

Example:

```text
[1,2]
```

Rotate twice:

```text
[2,1]
```

then:

```text
[1,2]
```

We would undo our work.

---

# 42. Common Mistake — Confusing `peek()` and `poll()`

Remember:

```java
queue.peek();
```

returns the front without removing it.

Use for:

```text
top()
```

While:

```java
queue.poll();
```

returns and removes the front.

Use for:

```text
pop()
```

---

# 43. Common Mistake — Using Stack Operations

The problem specifically asks us to simulate a stack using queue operations.

Do not use:

```java
Stack<Integer>
```

or stack-specific behavior from another structure.

The learning objective is understanding how one abstract data structure can simulate another.

---

# 44. Interview Explanation

I use one queue and maintain the invariant that the front of the queue always represents the top of the stack.

When pushing a new value, the queue naturally inserts it at the back. Since a stack requires the newest value at the top, I rotate all previously stored elements from the front to the back.

If the queue contains `n` elements after insertion, I perform `n - 1` rotations. This places the newly inserted element at the front while preserving the relative order of the older elements.

Because the stack top is always at the queue front, `pop()` becomes `poll()`, `top()` becomes `peek()`, and `empty()` becomes `isEmpty()`.

`push()` takes `O(n)` time, while `pop()`, `top()`, and `empty()` are `O(1)`.

---

# 45. What I Learned

- A stack follows **LIFO**.
- A queue follows **FIFO**.
- One data structure can simulate another by changing how elements are organized.
- The key invariant is:

```text
queue front = stack top
```

- A two-queue solution can move old elements behind the newly pushed value.
- A one-queue solution can achieve the same result through rotation.
- `offer()` adds to the queue.
- `poll()` removes from the front.
- `peek()` reads the front without removing it.
- Use a `while` loop when processing until a queue becomes empty.
- Use a `for` loop when the number of required rotations is known.
- `if` is not appropriate when multiple elements must be processed.
- The one-queue solution requires `size - 1` rotations.
- Maintaining a useful invariant can make later operations much simpler.

---

# Pattern Recognition

When I see:

```text
Implement X using Y
```

I should first ask:

```text
What behavior is different?
```

Here:

```text
STACK
LIFO

vs

QUEUE
FIFO
```

Then ask:

```text
What invariant would make the queue behave like a stack?
```

Answer:

```text
QUEUE FRONT = STACK TOP
```

So for every push:

```text
ADD NEW ELEMENT
       ↓
ROTATE OLD ELEMENTS
       ↓
NEW ELEMENT MOVES TO FRONT
       ↓
QUEUE FRONT = STACK TOP
```

For example:

```text
push(1)

[1]


push(2)

[1,2]
   ↓ rotate
[2,1]


push(3)

[2,1,3]
   ↓ rotate old elements
[3,2,1]
```

Then:

```text
top()   → peek()
pop()   → poll()
empty() → isEmpty()
```

The core mental model is:

```text
IDENTIFY BEHAVIOR DIFFERENCE
          ↓
CREATE AN INVARIANT
          ↓
REARRANGE DURING PUSH
          ↓
KEEP TOP AT QUEUE FRONT
```