# 1472. Design Browser History

**Difficulty:** Medium
**Topics:** Array, Linked List, Stack, Design
**Status:** Solved
**LeetCode:** https://leetcode.com/problems/design-browser-history/

---

## Original Question

Design a browser history system for one tab.

The browser starts at a `homepage`. It must support:

* `BrowserHistory(String homepage)` — initialize the browser at the homepage.
* `visit(String url)` — visit a new URL and clear all forward history.
* `back(int steps)` — move back by at most `steps`.
* `forward(int steps)` — move forward by at most `steps`.

Return the current URL after `back()` or `forward()`.

---

## Example

```text
BrowserHistory browserHistory = new BrowserHistory("leetcode.com");

browserHistory.visit("google.com");
browserHistory.visit("facebook.com");
browserHistory.visit("youtube.com");

browserHistory.back(1);       
// facebook.com

browserHistory.back(1);       
// google.com

browserHistory.forward(1);    
// facebook.com

browserHistory.visit("linkedin.com");

browserHistory.forward(2);    
// linkedin.com

browserHistory.back(2);       
// google.com

browserHistory.back(7);       
// leetcode.com
```

---

# 1. Inputs

This problem is different from a normal algorithm problem because we are designing a class whose state changes over time.

The inputs come through different methods.

### Constructor

```java
BrowserHistory(String homepage)
```

Example:

```text
homepage = "leetcode.com"
```

---

### Visit

```java
visit(String url)
```

Example:

```text
url = "google.com"
```

---

### Back

```java
back(int steps)
```

Example:

```text
steps = 2
```

---

### Forward

```java
forward(int steps)
```

Example:

```text
steps = 3
```

---

# 2. Outputs

`visit()` returns nothing.

```java
void visit(String url)
```

`back()` returns the current URL after moving backward.

```java
String back(int steps)
```

`forward()` returns the current URL after moving forward.

```java
String forward(int steps)
```

---

# 3. Required Operations

We need to support four operations:

```text
initialize
visit
back
forward
```

The browser must remember:

```text
previous pages
current page
forward pages
```

For example:

```text
leetcode.com
     ↓
google.com
     ↓
facebook.com
     ↓
youtube.com
```

If the current page is:

```text
facebook.com
```

then:

```text
Back history:
leetcode.com → google.com

Current:
facebook.com

Forward history:
youtube.com
```

---

# 4. Key Observation

Browser history behaves like a sequence of URLs with a pointer indicating the current position.

For example:

```text
0              1             2               3
leetcode.com → google.com → facebook.com → youtube.com
                                              ↑
                                           current
```

If we call:

```text
back(2)
```

the pointer moves left:

```text
0              1             2               3
leetcode.com → google.com → facebook.com → youtube.com
                ↑
             current
```

If we then call:

```text
forward(1)
```

the pointer moves right:

```text
0              1             2               3
leetcode.com → google.com → facebook.com → youtube.com
                              ↑
                           current
```

This suggests storing browser history in a structure that allows us to move backward and forward through positions.

---

# 5. Important Rule — Visiting Clears Forward History

This is the most important behavior in the problem.

Suppose:

```text
leetcode → google → facebook → youtube
                              ↑
                           current
```

After moving back:

```text
leetcode → google → facebook → youtube
                    ↑
                 current
```

`youtube` is now forward history.

If we visit:

```text
linkedin
```

the browser becomes:

```text
leetcode → google → facebook → linkedin
                              ↑
                           current
```

The old forward history:

```text
youtube
```

must disappear.

Therefore `visit()` must overwrite or discard everything after the current position.

---

# 6. Data Structure Choice

One clean approach is to use:

```text
ArrayList<String>
```

and maintain two integer positions:

```text
current
last
```

### `current`

Index of the page currently being displayed.

### `last`

Index of the furthest valid page in browser history.

Example:

```text
history =
[
  leetcode.com,
  google.com,
  facebook.com,
  youtube.com
]

current = 2
last    = 3
```

This means:

```text
facebook.com = current page
youtube.com  = valid forward history
```

---

# 7. Why Use an ArrayList?

We need:

```text
indexed access
move backward
move forward
append or replace history
```

An `ArrayList` provides direct access by index.

For example:

```java
history.get(current)
```

can return the current URL efficiently.

This also makes moving backward and forward simple because we can change an integer index instead of physically moving nodes.

---

# 8. Constructor

When the browser starts:

```java
BrowserHistory(String homepage)
```

we create the history and add the homepage.

Example:

```text
history = ["leetcode.com"]

current = 0
last = 0
```

The homepage is both:

```text
first page
current page
furthest valid page
```

---

# 9. `visit()` Operation

Suppose:

```text
history =
[leetcode, google, facebook, youtube]

current = 2
last = 3
```

Current page:

```text
facebook
```

Now:

```java
visit("linkedin")
```

The new page should be placed immediately after `current`.

Therefore:

```text
current++
```

Now:

```text
current = 3
```

Then the new page is stored at that position.

The forward history must be cleared.

Therefore:

```text
last = current
```

Now the valid history is:

```text
leetcode → google → facebook → linkedin
```

`youtube` is no longer considered part of the valid history.

---

# 10. Why Maintain `last`?

An `ArrayList` may physically still contain old values after visiting a new URL.

For example, the underlying storage might temporarily contain:

```text
[leetcode, google, facebook, linkedin, something old...]
```

But:

```text
last
```

tells us where valid browser history ends.

Therefore `forward()` must never move beyond:

```text
last
```

---

# 11. `back()` Operation

Suppose:

```text
current = 3
steps = 2
```

We want:

```text
3 - 2 = 1
```

So:

```text
current = 1
```

But we must never move before index `0`.

Therefore:

```java
current = Math.max(0, current - steps);
```

Then return:

```java
history.get(current);
```

---

# 12. Why `Math.max()`?

Suppose:

```text
current = 1
steps = 7
```

Naively:

```text
1 - 7 = -6
```

But index `-6` is invalid.

The earliest browser page is:

```text
index 0
```

Therefore:

```java
Math.max(0, current - steps)
```

guarantees:

```text
current >= 0
```

---

# 13. `forward()` Operation

Suppose:

```text
current = 1
steps = 2
last = 3
```

Then:

```text
current + steps = 3
```

which is valid.

But if:

```text
current = 2
steps = 10
last = 3
```

we cannot move to:

```text
12
```

The furthest valid position is:

```text
last = 3
```

Therefore:

```java
current = Math.min(last, current + steps);
```

Then return:

```java
history.get(current);
```

---

# 14. Why `Math.min()`?

The browser may have fewer forward pages than the requested number of steps.

For example:

```text
current = facebook
forward history = [youtube]
steps = 5
```

We can move only one page.

Therefore the upper boundary is:

```text
last
```

and:

```java
Math.min(last, current + steps)
```

prevents moving beyond valid history.

---

# 15. Pseudocode

```text
Constructor(homepage):

    create history

    add homepage

    current = 0

    last = 0


visit(url):

    current = current + 1

    place url at current position

    last = current


back(steps):

    current =
        maximum of:
            0
            current - steps

    return history[current]


forward(steps):

    current =
        minimum of:
            last
            current + steps

    return history[current]
```

---

# 16. Java Solution

```java
import java.util.ArrayList;
import java.util.List;

class BrowserHistory {

    private List<String> history;

    private int current;

    private int last;


    public BrowserHistory(String homepage) {

        history = new ArrayList<>();

        history.add(homepage);

        current = 0;

        last = 0;
    }


    public void visit(String url) {

        current++;

        if (current < history.size()) {
            history.set(current, url);
        } else {
            history.add(url);
        }

        last = current;
    }


    public String back(int steps) {

        current =
            Math.max(
                0,
                current - steps
            );

        return history.get(current);
    }


    public String forward(int steps) {

        current =
            Math.min(
                last,
                current + steps
            );

        return history.get(current);
    }
}
```

---

# 17. Why Use `if / else` in `visit()`?

Inside:

```java
visit(url)
```

we have two possible storage situations.

### Case 1

The position already exists in the `ArrayList`.

Then:

```java
history.set(current, url);
```

replaces the old forward-history entry.

### Case 2

The position does not exist.

Then:

```java
history.add(url);
```

adds a new entry.

Exactly one of these actions is required.

Therefore:

```java
if (...) {
    ...
} else {
    ...
}
```

is appropriate.

---

# 18. Why Not Use Two Separate `if` Statements?

The conditions are mutually exclusive.

Either:

```text
current < history.size()
```

or:

```text
current >= history.size()
```

The page cannot simultaneously require both replacement and append.

Therefore `if / else` communicates the decision better.

---

# 19. Why No Loop in `back()`?

A simple implementation could be:

```java
while (steps > 0 && current > 0) {
    current--;
    steps--;
}
```

This works.

However, it performs one operation per step.

The same result can be calculated directly:

```java
current = Math.max(0, current - steps);
```

This avoids unnecessary repetition.

Therefore a loop is not appropriate when simple arithmetic can directly calculate the final position.

---

# 20. Why No Loop in `forward()`?

Similarly, we could write:

```java
while (steps > 0 && current < last) {
    current++;
    steps--;
}
```

But we can directly calculate:

```java
current = Math.min(last, current + steps);
```

The direct arithmetic approach is simpler and constant time.

---

# 21. Manual Walkthrough

Start:

```java
BrowserHistory browserHistory =
    new BrowserHistory("leetcode.com");
```

State:

```text
history = [leetcode.com]

current = 0
last = 0
```

---

## Visit google.com

```java
visit("google.com")
```

Increment:

```text
current = 1
```

Add:

```text
history =
[leetcode.com, google.com]
```

Update:

```text
last = 1
```

---

## Visit facebook.com

```text
history =
[leetcode.com, google.com, facebook.com]

current = 2
last = 2
```

---

## Visit youtube.com

```text
history =
[
leetcode.com,
google.com,
facebook.com,
youtube.com
]

current = 3
last = 3
```

---

## back(1)

Calculate:

```text
current =
max(0, 3 - 1)

= 2
```

Return:

```text
facebook.com
```

---

## back(1)

```text
current =
max(0, 2 - 1)

= 1
```

Return:

```text
google.com
```

---

## forward(1)

```text
current =
min(3, 1 + 1)

= 2
```

Return:

```text
facebook.com
```

---

# 22. Visiting After Going Back

Current state:

```text
leetcode
   ↓
google
   ↓
facebook
   ↓
youtube
```

Current:

```text
facebook
```

Now:

```java
visit("linkedin.com");
```

Increment:

```text
current = 3
```

Replace index `3`:

```text
youtube.com
```

with:

```text
linkedin.com
```

History becomes:

```text
[
leetcode.com,
google.com,
facebook.com,
linkedin.com
]
```

Then:

```text
last = 3
```

The forward history containing `youtube.com` has effectively been cleared.

---

# 23. forward(2) After New Visit

Now:

```text
current = 3
last = 3
```

Call:

```java
forward(2)
```

Calculate:

```text
current =
min(3, 3 + 2)

= 3
```

So we remain at:

```text
linkedin.com
```

This correctly demonstrates that the previous forward history was cleared.

---

# 24. back(2)

From:

```text
linkedin.com
```

current:

```text
3
```

Call:

```java
back(2)
```

Calculate:

```text
current =
max(0, 3 - 2)

= 1
```

Return:

```text
google.com
```

---

# 25. back(7)

Current:

```text
1
```

Call:

```java
back(7)
```

Naively:

```text
1 - 7 = -6
```

But:

```java
Math.max(0, -6)
```

returns:

```text
0
```

Therefore the browser stops at:

```text
leetcode.com
```

---

# 26. Complexity Analysis

## Constructor

```text
Time: O(1)
Space: O(1) initially
```

---

## `visit()`

`ArrayList.add()` and `set()` are constant time in the normal/amortized case.

```text
Time: O(1) amortized
```

---

## `back()`

Only arithmetic and indexed access are performed.

```text
Time: O(1)
```

---

## `forward()`

Only arithmetic and indexed access are performed.

```text
Time: O(1)
```

---

## Overall Space

If `n` pages are visited:

```text
Space: O(n)
```

because browser history must remember visited URLs.

---

# 27. Alternative Approach — Two Stacks

Another natural solution uses:

```text
backStack
forwardStack
```

The idea is:

```text
backStack
    ↓
pages behind current

forwardStack
    ↓
pages ahead of current
```

When visiting:

```text
push current page into backStack
clear forwardStack
current = new URL
```

When going back:

```text
move pages from backStack
to forwardStack
```

When going forward:

```text
move pages from forwardStack
to backStack
```

This models actual browser behavior very clearly.

---

# 28. Two-Stack Java Approach

```java
import java.util.Stack;

class BrowserHistory {

    private Stack<String> backStack;
    private Stack<String> forwardStack;
    private String current;


    public BrowserHistory(String homepage) {

        backStack = new Stack<>();

        forwardStack = new Stack<>();

        current = homepage;
    }


    public void visit(String url) {

        backStack.push(current);

        current = url;

        forwardStack.clear();
    }


    public String back(int steps) {

        while (
            steps > 0 &&
            !backStack.isEmpty()
        ) {

            forwardStack.push(current);

            current = backStack.pop();

            steps--;
        }

        return current;
    }


    public String forward(int steps) {

        while (
            steps > 0 &&
            !forwardStack.isEmpty()
        ) {

            backStack.push(current);

            current = forwardStack.pop();

            steps--;
        }

        return current;
    }
}
```

---

# 29. Why `while` Is Appropriate in the Two-Stack Version

In the two-stack version:

```java
while (steps > 0 && !backStack.isEmpty())
```

is appropriate because movement happens one history entry at a time.

We continue while:

```text
steps remain
AND
history remains
```

The termination therefore depends on changing state.

This is a natural `while` use case.

---

# 30. ArrayList vs Two Stacks

| Characteristic          | ArrayList + Index | Two Stacks               |
| ----------------------- | ----------------- | ------------------------ |
| `visit()`               | `O(1)` amortized  | `O(1)`                   |
| `back()`                | `O(1)`            | `O(steps)`               |
| `forward()`             | `O(1)`            | `O(steps)`               |
| Space                   | `O(n)`            | `O(n)`                   |
| Main idea               | Move index        | Move URLs between stacks |
| Simpler movement        | Yes               | More explicit            |
| Models browser behavior | Good              | Very intuitive           |

For this problem, the **ArrayList + current index** solution is particularly efficient because `back()` and `forward()` can calculate their destination directly.

---

# 31. Why Not Use a Linked List?

A doubly linked list could also represent:

```text
previous ← current → next
```

and allow navigation in either direction.

However, moving:

```text
steps
```

positions would still require following links one at a time.

That means:

```text
O(steps)
```

for `back()` and `forward()`.

With indexed storage, the final position can be calculated directly.

---

# 32. Edge Cases

## Cannot Go Back Further

```text
history = [leetcode]

current = 0
```

Call:

```text
back(10)
```

Result:

```text
current = 0
```

Return:

```text
leetcode
```

---

## Cannot Go Forward Further

```text
current = last
```

Call:

```text
forward(10)
```

Result:

```text
current = last
```

No movement occurs.

---

## Visit Immediately From Homepage

```text
leetcode
```

then:

```text
visit google
```

History becomes:

```text
leetcode → google
```

---

## Back Then Visit

```text
A → B → C
        ↑
```

Back:

```text
A → B → C
    ↑
```

Visit:

```text
D
```

Result:

```text
A → B → D
```

`C` is no longer forward history.

---

# 33. Common Mistakes

## Mistake 1: Not Clearing Forward History

After going back and visiting a new page, old forward pages must no longer be accessible.

Incorrect:

```text
A → B → C

back to B

visit D

still allowing C
```

Correct:

```text
A → B → D
```

---

## Mistake 2: Moving Before Index 0

Do not allow:

```text
current < 0
```

Use:

```java
Math.max(0, current - steps)
```

---

## Mistake 3: Moving Beyond Valid Forward History

Do not use:

```java
history.size() - 1
```

as the forward boundary if old forward-history entries remain physically stored.

Use:

```text
last
```

because `last` represents the end of valid browser history.

---

## Mistake 4: Forgetting to Update `last`

Whenever:

```java
visit(url)
```

occurs:

```java
last = current;
```

must update the valid history boundary.

---

## Mistake 5: Physically Removing All Forward History Unnecessarily

You could delete every forward entry from the `ArrayList`.

But this may require multiple removals.

Instead, the `last` pointer can mark those old positions as invalid and allow future visits to overwrite them.

---

# 34. Interview Explanation

I model browser history as an `ArrayList` and maintain two indexes.

`current` identifies the current page, while `last` identifies the furthest valid page in history.

When visiting a new URL, I move `current` forward, overwrite an existing forward-history position or append the URL, and set `last = current`. This effectively clears all previous forward history.

For `back()`, I calculate the new index directly using:

```java
Math.max(0, current - steps)
```

so the browser cannot move before the homepage.

For `forward()`, I use:

```java
Math.min(last, current + steps)
```

so the browser cannot move beyond valid forward history.

This avoids looping through every requested step.

`back()` and `forward()` therefore run in `O(1)` time, while `visit()` is `O(1)` amortized and overall storage is `O(n)`.

---

# 35. What I Learned

* Design problems require maintaining state across multiple method calls.
* Browser history can be modeled as a sequence plus a current position.
* `current` tracks where the user is.
* `last` tracks the end of valid history.
* Visiting a new page after going back must clear forward history.
* Sometimes old data does not need to be physically deleted; a logical boundary can make it invalid.
* Arithmetic can sometimes replace loops.
* `Math.max()` can enforce a lower boundary.
* `Math.min()` can enforce an upper boundary.
* Use `if / else` when exactly one storage action must occur.
* A two-stack solution is also natural, but moving multiple steps takes `O(steps)`.
* The ArrayList/index solution gives constant-time back and forward navigation.

---

# Pattern Recognition

When I see a design problem involving:

```text
history
undo / redo
back / forward
navigation
current position
```

I should consider:

```text
Option 1:
Sequence + index

Option 2:
Two stacks

Option 3:
Doubly linked list
```

For browser history specifically:

```text
History Array
     ↓
current index
     ↓
last valid index
```

Core operations:

```java
// Back
current = Math.max(0, current - steps);

// Forward
current = Math.min(last, current + steps);
```

Core visit rule:

```text
Visit new page
      ↓
Move current
      ↓
Store URL
      ↓
last = current
      ↓
Old forward history becomes invalid
```

The central concept is:

> **Browser history is not only a collection of URLs—it is a collection plus a current position and a valid forward-history boundary.**