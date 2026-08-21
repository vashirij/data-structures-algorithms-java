# 707. Design Linked List

**Difficulty:** Medium  
**Topics:** Linked List, Design  
**Status:** In Progress  
**LeetCode:** https://leetcode.com/problems/design-linked-list/

---

## Original Question

Design your own linked list implementation.

You may use either:

- Singly Linked List
- Doubly Linked List

For this solution, we will use a **singly linked list with a dummy head node**.

Implement the `MyLinkedList` class:

```java
MyLinkedList()
int get(int index)
void addAtHead(int val)
void addAtTail(int val)
void addAtIndex(int index, int val)
void deleteAtIndex(int index)
```

---

# Example

```text
Input:

["MyLinkedList",
 "addAtHead",
 "addAtTail",
 "addAtIndex",
 "get",
 "deleteAtIndex",
 "get"]

[[],
 [1],
 [3],
 [1,2],
 [1],
 [1],
 [1]]
```

Operations:

```java
MyLinkedList myLinkedList = new MyLinkedList();

myLinkedList.addAtHead(1);

myLinkedList.addAtTail(3);

myLinkedList.addAtIndex(1, 2);
```

List becomes:

```text
1 → 2 → 3
```

Then:

```java
myLinkedList.get(1);
```

returns:

```text
2
```

Delete:

```java
myLinkedList.deleteAtIndex(1);
```

List becomes:

```text
1 → 3
```

Then:

```java
myLinkedList.get(1);
```

returns:

```text
3
```

---

# 1. What Are We Designing?

Unlike a normal LeetCode problem where we receive an input and calculate one output, this is a **design problem**.

Our object must maintain state across many operations.

We need to build:

```text
MyLinkedList
     │
     ├── get()
     ├── addAtHead()
     ├── addAtTail()
     ├── addAtIndex()
     └── deleteAtIndex()
```

The linked list itself must remember its nodes between method calls.

---

# 2. Node Structure

A singly linked-list node needs two pieces of information:

```text
value
next pointer
```

Java:

```java
private class Node {

    int val;
    Node next;

    Node(int val) {
        this.val = val;
    }
}
```

Conceptually:

```text
┌─────────┬─────────┐
│   val   │  next   │
└─────────┴─────────┘
```

Example:

```text
1 → 2 → 3 → null
```

Each node knows:

```text
its own value
+
where the next node is
```

It does **not** know where the previous node is.

---

# 3. What State Do We Need?

Our linked list needs two important pieces of state:

```java
private Node dummy;
private int size;
```

### `dummy`

A permanent node placed before the real first node.

### `size`

Number of actual elements in the linked list.

For:

```text
1 → 2 → 3
```

we internally store:

```text
dummy → 1 → 2 → 3 → null
```

and:

```text
size = 3
```

The dummy node does **not** count toward `size`.

---

# 4. Why Keep `size`?

Without `size`, checking whether an index is valid would require traversing the list.

Instead, we can immediately check:

```java
if (index < 0 || index >= size)
```

for operations such as:

```text
get
delete
```

For insertion, valid positions are:

```text
0 through size
```

because:

```text
index == size
```

means:

> append the new node to the end.

---

# 5. Why Use a Dummy Head?

Without a dummy node:

```text
head
 ↓
 1 → 2 → 3
```

inserting at index `0` changes `head`.

Deleting index `0` also changes `head`.

That creates special cases.

Instead, use:

```text
dummy → 1 → 2 → 3
```

Now every real node has a node before it.

For example, inserting before `1` becomes:

```text
dummy → NEW → 1 → 2 → 3
```

Deleting `1` becomes:

```text
dummy ─────→ 2 → 3
```

The same pointer logic works at the beginning, middle, and end.

---

# 6. Constructor

Initially, the list is empty.

```java
public MyLinkedList() {

    dummy = new Node(0);
    size = 0;
}
```

State:

```text
dummy → null

size = 0
```

The value `0` stored in the dummy node is irrelevant.

The dummy node is only a structural helper.

---

# 7. `get(index)`

Requirement:

> Return the value of the node at `index`.

If the index is invalid:

```text
return -1
```

Valid indexes are:

```text
0 <= index < size
```

Therefore:

```java
if (index < 0 || index >= size) {
    return -1;
}
```

---

# 8. Traversing to an Index

Suppose:

```text
dummy → 10 → 20 → 30 → 40
```

We want:

```java
get(2)
```

Expected:

```text
30
```

Start:

```java
Node current = dummy.next;
```

So:

```text
current
 ↓
10 → 20 → 30 → 40
```

Move `index` times.

For index `2`:

```text
start:
10

move 1:
20

move 2:
30
```

Then:

```java
return current.val;
```

---

# 9. Why Use a `for` Loop in `get()`?

We know exactly how many times we need to move:

```text
index times
```

Therefore:

```java
for (int i = 0; i < index; i++)
```

is appropriate.

The number of iterations is known before traversal begins.

This is different from situations where we continue until some changing condition occurs.

---

# 10. `get()` Pseudocode

```text
get(index):

    if index is invalid
        return -1

    current = first real node

    move current index times

    return current value
```

---

# 11. `get()` Java

```java
public int get(int index) {

    if (index < 0 || index >= size) {
        return -1;
    }

    Node current = dummy.next;

    for (int i = 0; i < index; i++) {
        current = current.next;
    }

    return current.val;
}
```

---

# 12. `addAtHead(val)`

Requirement:

> Insert a new node before the current first node.

Suppose:

```text
dummy → 2 → 3
```

Add:

```java
addAtHead(1)
```

We want:

```text
dummy → 1 → 2 → 3
```

This is equivalent to:

```java
addAtIndex(0, val);
```

Therefore:

```java
public void addAtHead(int val) {
    addAtIndex(0, val);
}
```

---

# 13. Why Reuse `addAtIndex()`?

We could write separate pointer manipulation code for:

```text
addAtHead
```

but inserting at the head is simply:

```text
insert at index 0
```

Reusing:

```java
addAtIndex(0, val)
```

avoids duplicate logic.

This makes the implementation more modular.

---

# 14. `addAtTail(val)`

Requirement:

> Add the new node after the current last node.

Suppose:

```text
1 → 2 → 3
```

and:

```text
size = 3
```

The valid insertion positions are:

```text
0  1  2  3
```

Index `3` is immediately after the last existing node.

Therefore adding to the tail is:

```java
addAtIndex(size, val);
```

Implementation:

```java
public void addAtTail(int val) {
    addAtIndex(size, val);
}
```

---

# 15. `addAtIndex(index, val)`

This is the central insertion operation.

Requirement:

> Insert a new node before the node currently at `index`.

Suppose:

```text
1 → 3
```

Call:

```java
addAtIndex(1, 2);
```

We want:

```text
1 → 2 → 3
```

---

# 16. Valid Insertion Index

For a list of size:

```text
3
```

valid existing node indexes are:

```text
0
1
2
```

But insertion also allows:

```text
index = 3
```

because that means append.

Therefore valid insertion indexes are:

```text
0 <= index <= size
```

If:

```text
index > size
```

do nothing.

---

# 17. The Key Insertion Idea

To insert a node at index `index`, we don't actually need to first find the node **at** `index`.

We need to find the node **before** it.

Example:

```text
dummy → 1 → 3
```

Insert:

```text
index = 1
value = 2
```

We need:

```text
dummy → 1 → 2 → 3
            ↑
           new
```

The important node is:

```text
1
```

because its `next` pointer must change.

So find:

```text
previous node
```

rather than:

```text
target node
```

---

# 18. Why Start From `dummy`?

Suppose:

```java
addAtIndex(0, 5);
```

We need to insert before the first real node.

If we start at:

```text
dummy
```

then the node before index `0` already exists:

```text
dummy → first
```

This eliminates special handling for inserting at the head.

---

# 19. Traversing for Insertion

Start:

```java
Node previous = dummy;
```

Move:

```text
index times
```

Example:

```text
dummy → 1 → 3
```

For:

```text
index = 1
```

start:

```text
previous = dummy
```

Move once:

```text
previous = 1
```

Now:

```text
previous.next = 3
```

which is exactly where the new node belongs.

---

# 20. Pointer Order for Insertion

This order is extremely important.

Create:

```java
Node newNode = new Node(val);
```

Current:

```text
previous → nextNode
```

We want:

```text
previous → newNode → nextNode
```

First:

```java
newNode.next = previous.next;
```

Now the new node remembers the remainder of the list.

Then:

```java
previous.next = newNode;
```

Final:

```text
previous → newNode → nextNode
```

---

# 21. Why This Pointer Order Matters

Suppose:

```text
1 → 3
```

and we want:

```text
1 → 2 → 3
```

If we first do:

```java
previous.next = newNode;
```

then:

```text
1 → 2
```

and we may lose our easy reference to:

```text
3
```

Instead:

```java
newNode.next = previous.next;
```

first saves the connection:

```text
2 → 3
```

Then:

```java
previous.next = newNode;
```

creates:

```text
1 → 2 → 3
```

General linked-list rule:

```text
SAVE CONNECTION
      ↓
CHANGE CONNECTION
```

---

# 22. `addAtIndex()` Pseudocode

```text
if index > size
    do nothing

find node before insertion position

create new node

new node points to previous.next

previous points to new node

increase size
```

---

# 23. `addAtIndex()` Java

```java
public void addAtIndex(int index, int val) {

    if (index > size) {
        return;
    }

    Node previous = dummy;

    for (int i = 0; i < index; i++) {
        previous = previous.next;
    }

    Node newNode = new Node(val);

    newNode.next = previous.next;

    previous.next = newNode;

    size++;
}
```

---

# 24. `deleteAtIndex(index)`

Requirement:

> Delete the node at `index` if the index is valid.

Suppose:

```text
1 → 2 → 3
```

Delete:

```text
index = 1
```

We want:

```text
1 → 3
```

Again, the important node is not only the node being deleted.

We need the node **before** it.

---

# 25. Finding the Previous Node

With:

```text
dummy → 1 → 2 → 3
```

delete index:

```text
1
```

We need:

```text
previous = 1
```

because:

```text
previous.next = 2
```

and we want to change that to:

```text
previous.next = 3
```

---

# 26. Deletion Pointer Operation

Current:

```text
previous → target → next
```

We want:

```text
previous ─────────→ next
```

This can be done with:

```java
previous.next = previous.next.next;
```

Example:

```text
Before:

1 → 2 → 3
    ↑
   delete
```

Operation:

```java
previous.next = previous.next.next;
```

Result:

```text
1 ─────→ 3
```

Node `2` is no longer reachable from the list.

---

# 27. Valid Delete Index

Deletion requires an actual node to exist.

Therefore:

```text
0 <= index < size
```

If:

```text
index >= size
```

the index is invalid.

Do nothing.

---

# 28. `deleteAtIndex()` Java

```java
public void deleteAtIndex(int index) {

    if (index < 0 || index >= size) {
        return;
    }

    Node previous = dummy;

    for (int i = 0; i < index; i++) {
        previous = previous.next;
    }

    previous.next = previous.next.next;

    size--;
}
```

---

# 29. Full Java Solution

```java
class MyLinkedList {

    private class Node {

        int val;
        Node next;

        Node(int val) {
            this.val = val;
        }
    }


    private Node dummy;
    private int size;


    public MyLinkedList() {

        dummy = new Node(0);

        size = 0;
    }


    public int get(int index) {

        if (index < 0 || index >= size) {
            return -1;
        }

        Node current = dummy.next;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.val;
    }


    public void addAtHead(int val) {

        addAtIndex(0, val);
    }


    public void addAtTail(int val) {

        addAtIndex(size, val);
    }


    public void addAtIndex(int index, int val) {

        if (index > size) {
            return;
        }

        Node previous = dummy;

        for (int i = 0; i < index; i++) {
            previous = previous.next;
        }

        Node newNode = new Node(val);

        newNode.next = previous.next;

        previous.next = newNode;

        size++;
    }


    public void deleteAtIndex(int index) {

        if (index < 0 || index >= size) {
            return;
        }

        Node previous = dummy;

        for (int i = 0; i < index; i++) {
            previous = previous.next;
        }

        previous.next = previous.next.next;

        size--;
    }
}
```

---

# 30. Manual Walkthrough

Start:

```java
MyLinkedList myLinkedList =
    new MyLinkedList();
```

State:

```text
dummy → null

size = 0
```

---

## addAtHead(1)

Equivalent to:

```java
addAtIndex(0, 1);
```

Start:

```text
dummy → null
```

Create:

```text
newNode = 1
```

Set:

```java
newNode.next = dummy.next;
```

So:

```text
1 → null
```

Then:

```java
dummy.next = newNode;
```

Result:

```text
dummy → 1 → null

size = 1
```

---

# 31. addAtTail(3)

Equivalent to:

```java
addAtIndex(1, 3);
```

Current:

```text
dummy → 1 → null
```

Find previous node:

```text
previous = 1
```

Create:

```text
3
```

Set:

```text
3 → null
```

Then:

```text
1 → 3
```

Result:

```text
dummy → 1 → 3 → null

size = 2
```

---

# 32. addAtIndex(1, 2)

Current:

```text
dummy → 1 → 3
```

Need to insert:

```text
2
```

before index:

```text
1
```

Find previous:

```text
previous = 1
```

Save connection:

```java
newNode.next = previous.next;
```

Result:

```text
2 → 3
```

Connect previous:

```java
previous.next = newNode;
```

Result:

```text
dummy → 1 → 2 → 3
```

Size:

```text
3
```

---

# 33. get(1)

Current:

```text
dummy → 1 → 2 → 3
```

Start:

```text
current = 1
```

Move once:

```text
current = 2
```

Return:

```text
2
```

---

# 34. deleteAtIndex(1)

Current:

```text
dummy → 1 → 2 → 3
```

Find node before index `1`:

```text
previous = 1
```

Currently:

```text
previous.next = 2
```

and:

```text
previous.next.next = 3
```

Execute:

```java
previous.next = previous.next.next;
```

Result:

```text
dummy → 1 → 3
```

Update:

```text
size = 2
```

---

# 35. get(1)

Current:

```text
dummy → 1 → 3
```

Move to index `1`.

Return:

```text
3
```

Correct.

---

# 36. Control Flow — Why `if` for Invalid Index?

For:

```java
get(index)
```

we check:

```java
if (index < 0 || index >= size)
```

There are two possible outcomes:

```text
invalid
    ↓
return -1

valid
    ↓
continue normally
```

Because `return` immediately exits the method, an `else` block is unnecessary.

---

# 37. Why `||` for Invalid Index?

An index is invalid when:

```text
index is too small
OR
index is too large
```

Therefore:

```java
index < 0 || index >= size
```

is correct.

Using:

```java
&&
```

would incorrectly require the index to be both negative and too large simultaneously.

---

# 38. Control Flow — Why `for` for Traversal?

For operations such as:

```text
get(index)
addAtIndex(index)
deleteAtIndex(index)
```

we know exactly how many links must be followed.

For example:

```text
move index times
```

Therefore:

```java
for (int i = 0; i < index; i++)
```

clearly expresses the traversal.

A `while` loop would also work:

```java
while (i < index)
```

but would require manually initializing and updating `i`.

Since the number of repetitions is known, `for` is more appropriate.

---

# 39. Why No Loop for `addAtHead()`?

We could manually manipulate pointers.

But:

```text
addAtHead(val)
```

is exactly:

```text
addAtIndex(0, val)
```

Therefore:

```java
addAtIndex(0, val);
```

is simpler and avoids duplicated code.

---

# 40. Why No Separate Traversal for `addAtTail()`?

Likewise:

```text
addAtTail(val)
```

means:

```text
insert at index size
```

Therefore:

```java
addAtIndex(size, val);
```

can reuse the existing insertion logic.

---

# 41. Edge Cases

## Empty List

```text
dummy → null

size = 0
```

Calling:

```java
get(0)
```

returns:

```text
-1
```

---

## Add to Empty List

```java
addAtHead(5);
```

Result:

```text
dummy → 5
```

---

## Delete First Node

Before:

```text
dummy → 1 → 2 → 3
```

Delete:

```java
deleteAtIndex(0);
```

`previous` remains:

```text
dummy
```

Then:

```java
dummy.next = dummy.next.next;
```

Result:

```text
dummy → 2 → 3
```

This demonstrates why the dummy node is useful.

---

## Delete Last Node

Before:

```text
dummy → 1 → 2 → 3
```

Delete:

```text
index = 2
```

Result:

```text
dummy → 1 → 2 → null
```

---

## Insert at `size`

Before:

```text
1 → 2 → 3

size = 3
```

Call:

```java
addAtIndex(3, 4);
```

Result:

```text
1 → 2 → 3 → 4
```

This is equivalent to:

```java
addAtTail(4);
```

---

## Insert Beyond `size`

```text
size = 3
```

Call:

```java
addAtIndex(5, 10);
```

Since:

```text
5 > 3
```

nothing happens.

---

# 42. Complexity Analysis

Let:

```text
n = number of nodes
```

### Constructor

```text
Time:  O(1)
Space: O(1)
```

### `get(index)`

May traverse up to `n` nodes.

```text
Time: O(n)
```

### `addAtHead(val)`

Because it calls:

```text
addAtIndex(0, val)
```

no traversal is required.

```text
Time: O(1)
```

### `addAtTail(val)`

With this singly linked-list implementation, we traverse to the end.

```text
Time: O(n)
```

### `addAtIndex(index, val)`

May traverse up to `n` nodes.

```text
Time: O(n)
```

### `deleteAtIndex(index)`

May traverse up to `n` nodes.

```text
Time: O(n)
```

### Space

Each element requires one node.

```text
Space: O(n)
```

---

# 43. Could `addAtTail()` Be O(1)?

Yes.

We could maintain another pointer:

```java
Node tail;
```

which always points to the final node.

Then:

```text
tail.next = newNode
tail = newNode
```

would make:

```text
addAtTail()
```

an:

```text
O(1)
```

operation.

However, maintaining `tail` adds additional state and requires careful updates when deleting the last node.

For learning the core linked-list operations, the dummy-head + size implementation is simpler.

---

# 44. Singly vs Doubly Linked List

### Singly Linked List

Node:

```text
val
next
```

Structure:

```text
1 → 2 → 3
```

Advantages:

- simpler
- less memory
- good for learning pointer manipulation

Disadvantage:

- cannot directly move backward

---

### Doubly Linked List

Node:

```text
prev
val
next
```

Structure:

```text
null ← 1 ⇄ 2 ⇄ 3 → null
```

Advantages:

- can traverse both directions
- easier deletion when node reference is already known

Disadvantages:

- more pointers to maintain
- more opportunities for pointer errors
- slightly more memory per node

For this implementation, a singly linked list is sufficient.

---

# 45. Common Mistake — Wrong Insertion Order

Wrong idea:

```java
previous.next = newNode;
newNode.next = previous.next;
```

After the first statement:

```text
previous.next
```

already points to `newNode`.

Then:

```java
newNode.next = previous.next;
```

would make:

```text
newNode → newNode
```

creating a self-loop.

Correct order:

```java
newNode.next = previous.next;
previous.next = newNode;
```

Always:

```text
SAVE OLD CONNECTION
        ↓
CREATE NEW CONNECTION
```

---

# 46. Common Mistake — Off-by-One Traversal

For insertion and deletion, we need the node **before** the target index.

Therefore start:

```java
Node previous = dummy;
```

and move:

```text
index times
```

Do not start from:

```java
dummy.next
```

for these operations, because that changes the meaning of the traversal.

---

# 47. Common Mistake — Wrong Validation for Insertion

For:

```text
get
delete
```

valid indexes are:

```text
0 <= index < size
```

But for:

```text
addAtIndex
```

valid indexes include:

```text
index == size
```

because that means append.

Remember:

```text
GET / DELETE:

index < size


INSERT:

index <= size
```

---

# 48. Common Mistake — Forgetting `size`

After successful insertion:

```java
size++;
```

After successful deletion:

```java
size--;
```

If `size` is wrong, future index validation will also become wrong.

---

# 49. Common Mistake — Counting Dummy as a Real Node

For:

```text
dummy → 10 → 20 → 30
```

the indexes are:

```text
10 = index 0
20 = index 1
30 = index 2
```

The dummy node has **no user-visible index**.

It exists only to simplify pointer operations.

---

# 50. Interview Explanation

I implement the structure as a singly linked list with a dummy head and a `size` variable.

The dummy node simplifies insertion and deletion at index `0` because every real node has a predecessor.

For `get`, I validate the index and traverse from the first real node until reaching the requested position.

For insertion, I find the node immediately before the requested index. I first point the new node to the previous node's current `next`, then point the previous node to the new node.

For deletion, I again find the previous node and bypass the target using:

```java
previous.next = previous.next.next;
```

`addAtHead` and `addAtTail` reuse `addAtIndex` because adding at the head is insertion at index `0`, while adding at the tail is insertion at index `size`.

The operations that require traversal are `O(n)`, while adding at the head is `O(1)`. The overall storage is `O(n)`.

---

# 51. What I Learned

- A linked list is built from nodes connected by references.
- A singly linked-list node needs `val` and `next`.
- A dummy node simplifies head insertion and deletion.
- `size` makes index validation easier.
- For insertion and deletion, finding the **previous node** is usually more useful than finding the target node.
- Always save an existing pointer before overwriting it.
- Insertion follows:

```text
new.next = previous.next
previous.next = new
```

- Deletion follows:

```text
previous.next = previous.next.next
```

- `for` loops are useful when the number of pointer movements is known.
- `addAtHead()` can reuse `addAtIndex(0, val)`.
- `addAtTail()` can reuse `addAtIndex(size, val)`.
- Valid insertion indexes differ from valid get/delete indexes.
- Off-by-one errors are one of the biggest risks in linked-list problems.

---

# Pattern Recognition

When I see:

```text
Linked List
+
insert at index
+
delete at index
+
head may change
```

I should consider:

```text
Dummy Node
    +
Size
```

For insertion:

```text
Find PREVIOUS node
        ↓
Create new node
        ↓
new.next = previous.next
        ↓
previous.next = new
        ↓
size++
```

For deletion:

```text
Find PREVIOUS node
        ↓
previous.next = previous.next.next
        ↓
size--
```

The central linked-list rule is:

> **Before changing a pointer, make sure you have preserved any connection you still need.**

Mental model:

```text
VALIDATE → FIND PREVIOUS → SAVE CONNECTION → REWIRE → UPDATE SIZE
```