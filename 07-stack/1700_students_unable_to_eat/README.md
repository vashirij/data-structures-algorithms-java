# 1700. Number of Students Unable to Eat Lunch

**Difficulty:** Easy  
**Topics:** Array, Queue, Stack, Simulation, Counting  
**Status:** In Progress  
**LeetCode:** https://leetcode.com/problems/number-of-students-unable-to-eat-lunch/

---

## Original Question

The school cafeteria offers two types of sandwiches:

```text
0 = circular
1 = square
```

Students stand in a queue, and each student prefers either type `0` or type `1`.

Sandwiches are arranged in a stack where:

```text
sandwiches[0] = top sandwich
```

At each step:

- If the student at the front wants the top sandwich, the student takes it and leaves.
- Otherwise, the student moves to the back of the queue.
- The process stops when none of the remaining students wants the sandwich currently on top.

Return the number of students who are unable to eat.

---

# Example 1

```text
Input:

students   = [1,1,0,0]
sandwiches = [0,1,0,1]

Output:

0
```

All students eventually receive a sandwich.

---

# Example 2

```text
Input:

students   = [1,1,1,0,0,1]
sandwiches = [1,0,0,0,1,1]

Output:

3
```

Three students remain when the top sandwich is `0`, but all three remaining students want `1`.

---

# 1. Inputs

We receive two arrays:

```java
int[] students
int[] sandwiches
```

## `students`

Represents student preferences.

```text
students[i] = 0
```

means the student wants a circular sandwich.

```text
students[i] = 1
```

means the student wants a square sandwich.

Example:

```text
students = [1,1,0,0]
```

means:

```text
Student 1 → square
Student 2 → square
Student 3 → circular
Student 4 → circular
```

---

# 2. Sandwiches

```text
sandwiches[i]
```

represents the sandwich type.

Importantly:

```text
sandwiches[0]
```

is the **top of the sandwich stack**.

Example:

```text
sandwiches = [0,1,0,1]
```

Processing order:

```text
0 → 1 → 0 → 1
↑
top
```

---

# 3. Output

Return:

```text
number of students unable to eat
```

Method:

```java
int countStudents(int[] students, int[] sandwiches)
```

---

# 4. First Approach — Literal Simulation

The problem describes:

```text
Students = Queue

Sandwiches = Stack
```

Suppose:

```text
students   = [1,1,0,0]
sandwiches = [0,1,0,1]
```

Initially:

```text
Student Queue

FRONT
 ↓
1 → 1 → 0 → 0
```

Top sandwich:

```text
0
```

The first student wants:

```text
1
```

but the sandwich is:

```text
0
```

So the student moves to the back:

```text
1 → 0 → 0 → 1
```

The next student also wants `1`, so:

```text
0 → 0 → 1 → 1
```

Now the first student wants:

```text
0
```

which matches the top sandwich.

The student eats and leaves.

---

# 5. Do We Need to Simulate Everything?

Before implementing the queue directly, ask:

> Does the exact order of the students ultimately determine whether the current sandwich can be eaten?

Suppose the current sandwich is:

```text
0
```

If **at least one remaining student wants `0`**, that student will eventually reach the front.

Students who want `1` may rotate to the back, but eventually the student who wants `0` reaches the front.

Therefore the important information is not necessarily:

```text
exact student order
```

Instead, we mainly need:

```text
How many students want 0?

How many students want 1?
```

---

# 6. Key Observation

There are only two student preferences:

```text
0
1
```

Therefore we can count them.

Use:

```java
int count0 = 0;
int count1 = 0;
```

For:

```text
students = [1,1,0,0]
```

we get:

```text
count0 = 2
count1 = 2
```

Now process sandwiches from top to bottom.

---

# 7. Why Counting Works

Suppose the current sandwich is:

```text
0
```

and:

```text
count0 > 0
```

This means at least one student wants that sandwich.

Even if that student is currently at the back:

```text
1 → 1 → 0
```

the students who do not want `0` will move behind them.

Eventually:

```text
0
```

reaches the front.

Therefore one student will eat the sandwich.

So we can simply:

```java
count0--;
```

without actually simulating every rotation.

---

# 8. When Does the Process Stop?

Suppose the top sandwich is:

```text
0
```

but:

```text
count0 == 0
```

That means:

```text
NO remaining student wants sandwich 0.
```

Moving students around cannot solve the problem.

For example:

```text
students remaining:

1 → 1 → 1
```

Top sandwich:

```text
0
```

Rotating gives:

```text
1 → 1 → 1
```

again.

No student can eat.

Therefore the process stops.

---

# 9. Number of Students Remaining

At any point:

```text
remaining students
=
count0 + count1
```

Therefore, if we cannot serve the current sandwich:

```java
return count0 + count1;
```

---

# 10. Step 1 — Count Student Preferences

Initialize:

```java
int count0 = 0;
int count1 = 0;
```

Traverse:

```java
for (int student : students) {

    if (student == 0) {
        count0++;
    } else {
        count1++;
    }
}
```

---

# 11. Why Use a `for` Loop?

We need to examine every student exactly once.

The number of students is already known:

```text
students.length
```

Therefore a `for` loop is appropriate.

We could use:

```java
while (...)
```

but then we would need to manually maintain an index.

The enhanced `for` loop clearly expresses:

> Process every student in the array.

---

# 12. Why Use `if / else`?

Each student has exactly one of two preferences:

```text
0
OR
1
```

Therefore:

```java
if (student == 0) {
    count0++;
} else {
    count1++;
}
```

is appropriate.

The two conditions are mutually exclusive.

A student cannot simultaneously prefer:

```text
0 AND 1
```

---

# 13. Step 2 — Process Sandwiches

Now process each sandwich from top to bottom.

```java
for (int sandwich : sandwiches)
```

For each sandwich, determine whether a remaining student wants it.

---

# 14. Processing Sandwich Type 0

If:

```text
sandwich == 0
```

check:

```text
count0
```

If:

```java
count0 == 0
```

nobody wants it.

Return:

```java
return count0 + count1;
```

Otherwise:

```java
count0--;
```

because one student who prefers `0` eats.

---

# 15. Processing Sandwich Type 1

If:

```text
sandwich == 1
```

check:

```text
count1
```

If:

```java
count1 == 0
```

nobody wants it.

Return:

```java
return count0 + count1;
```

Otherwise:

```java
count1--;
```

---

# 16. Pseudocode

```text
count0 = 0
count1 = 0


FOR every student

    IF student prefers 0
        count0++

    ELSE
        count1++


FOR every sandwich

    IF sandwich is 0

        IF count0 == 0
            return count0 + count1

        count0--


    ELSE

        IF count1 == 0
            return count0 + count1

        count1--


return 0
```

---

# 17. Java Solution

```java
class Solution {

    public int countStudents(int[] students, int[] sandwiches) {

        int count0 = 0;
        int count1 = 0;

        // Count student preferences
        for (int student : students) {

            if (student == 0) {
                count0++;
            } else {
                count1++;
            }
        }

        // Process sandwiches from top to bottom
        for (int sandwich : sandwiches) {

            if (sandwich == 0) {

                // Nobody remaining wants type 0
                if (count0 == 0) {
                    return count0 + count1;
                }

                count0--;

            } else {

                // Nobody remaining wants type 1
                if (count1 == 0) {
                    return count0 + count1;
                }

                count1--;
            }
        }

        // Every student received a sandwich
        return 0;
    }
}
```

---

# 18. Manual Walkthrough — Example 1

Input:

```text
students   = [1,1,0,0]
sandwiches = [0,1,0,1]
```

Count students:

```text
count0 = 2
count1 = 2
```

---

## Sandwich 1

```text
sandwich = 0
```

Check:

```text
count0 = 2
```

Someone wants it.

After eating:

```text
count0 = 1
count1 = 2
```

Remaining students:

```text
3
```

---

## Sandwich 2

```text
sandwich = 1
```

Check:

```text
count1 = 2
```

Someone wants it.

After:

```text
count0 = 1
count1 = 1
```

---

## Sandwich 3

```text
sandwich = 0
```

Someone wants it.

After:

```text
count0 = 0
count1 = 1
```

---

## Sandwich 4

```text
sandwich = 1
```

Someone wants it.

After:

```text
count0 = 0
count1 = 0
```

Every student ate.

Return:

```text
0
```

---

# 19. Manual Walkthrough — Example 2

Input:

```text
students   = [1,1,1,0,0,1]
sandwiches = [1,0,0,0,1,1]
```

Count preferences:

```text
count0 = 2
count1 = 4
```

---

## Sandwich 1

```text
sandwich = 1
```

Someone wants it.

```text
count0 = 2
count1 = 3
```

---

## Sandwich 2

```text
sandwich = 0
```

Someone wants it.

```text
count0 = 1
count1 = 3
```

---

## Sandwich 3

```text
sandwich = 0
```

Someone wants it.

```text
count0 = 0
count1 = 3
```

---

## Sandwich 4

Current sandwich:

```text
0
```

But:

```text
count0 = 0
```

No remaining student wants type `0`.

Therefore processing stops.

Remaining students:

```text
count0 + count1

= 0 + 3

= 3
```

Return:

```text
3
```

---

# 20. Why Use Early `return`?

When:

```text
sandwich == 0
```

and:

```text
count0 == 0
```

we already know the final answer.

No future sandwich can be reached because the current top sandwich cannot be removed.

Therefore:

```java
return count0 + count1;
```

is appropriate.

---

# 21. Why Not Use `break`?

We could write:

```java
if (count0 == 0) {
    break;
}
```

but then we would still need code after the loop to calculate and return the answer.

Since the final answer is already known, `return` is clearer.

Remember:

```text
break
```

means:

```text
leave this loop
but continue executing the method
```

while:

```text
return
```

means:

```text
leave the entire method
and return the answer
```

---

# 22. Why Not Use a `while` Loop?

For the counting solution, we process:

```text
students once
sandwiches once
```

Both are known arrays.

Therefore `for` loops are natural.

A `while` loop would be more appropriate for the literal simulation because the number of student rotations is not known beforehand.

---

# 23. Alternative — Queue Simulation

We could solve the problem exactly as described.

Create a queue:

```java
Queue<Integer> queue = new LinkedList<>();
```

Add students:

```text
1 → 1 → 0 → 0
```

Then repeatedly inspect:

```text
front student
vs
current sandwich
```

---

# 24. Queue Simulation Logic

If:

```text
student == sandwich
```

then:

```text
student leaves queue
sandwich index moves forward
```

Otherwise:

```text
student moves from front to back
```

But we need to detect when **nobody wants the current sandwich**.

One method is to count consecutive rejections.

If the number of consecutive rejections equals:

```text
queue.size()
```

then every remaining student has rejected the current sandwich.

The process must stop.

---

# 25. Queue Simulation Pseudocode

```text
put all students into queue

sandwichIndex = 0
rejections = 0


WHILE queue is not empty

    student = remove front


    IF student matches current sandwich

        sandwichIndex++

        rejections = 0


    ELSE

        put student at back

        rejections++


        IF rejections == queue size

            stop


return queue size
```

---

# 26. Why `while` Is Appropriate for Simulation

In the literal simulation, we don't know exactly how many operations will occur.

A student may:

```text
move to back
move to back again later
eventually eat
```

Therefore the process continues until a state condition occurs:

```text
queue becomes empty
```

or:

```text
everyone rejects the current sandwich
```

That is a natural use case for:

```java
while
```

---

# 27. Counting vs Simulation

| Approach | Main Idea | Time | Extra Space |
|---|---|---:|---:|
| Queue Simulation | Follow cafeteria process | More operations | O(n) |
| Counting | Count preferences | O(n) | O(1) |

The simulation follows the problem story closely.

The counting approach recognizes that the exact order of students is unnecessary.

---

# 28. Why Counting Is Better Here

Consider:

```text
students:

1 → 1 → 1 → 0
```

Current sandwich:

```text
0
```

The queue simulation would perform:

```text
1 moves
1 moves
1 moves
0 eats
```

The counting solution already knows:

```text
count0 = 1
```

Therefore someone will eventually eat the sandwich.

We don't need to simulate the three rotations.

---

# 29. Important Invariant

At any point:

```text
count0
=
remaining students who want 0
```

and:

```text
count1
=
remaining students who want 1
```

Therefore:

```text
count0 + count1
=
total remaining students
```

This invariant lets us immediately return the number of students unable to eat.

---

# 30. Complexity Analysis

Let:

```text
n = number of students
```

We first traverse all students:

```text
O(n)
```

Then process at most all sandwiches:

```text
O(n)
```

Therefore:

```text
O(n) + O(n)
=
O(n)
```

### Time Complexity

```text
O(n)
```

We only store:

```text
count0
count1
```

regardless of input size.

### Extra Space Complexity

```text
O(1)
```

---

# 31. Edge Cases

## Everyone Wants the Same Type

```text
students   = [1,1,1]
sandwiches = [1,1,1]
```

Every student eats.

Return:

```text
0
```

---

## Nobody Wants the First Sandwich

```text
students   = [1,1,1]
sandwiches = [0,1,1]
```

Counts:

```text
count0 = 0
count1 = 3
```

First sandwich:

```text
0
```

No student wants it.

Return:

```text
3
```

immediately.

---

## One Student

```text
students   = [0]
sandwiches = [0]
```

The student eats.

Return:

```text
0
```

---

## One Student Cannot Eat

```text
students   = [1]
sandwiches = [0]
```

No student wants `0`.

Return:

```text
1
```

---

# 32. Common Mistake — Thinking Student Order Must Always Be Simulated

The problem description strongly suggests a queue.

That does not mean a queue is required for the optimal solution.

Always ask:

```text
Does the exact order affect the final result?
```

Here, if someone wants the current sandwich, rotations will eventually bring that person to the front.

Therefore counts are enough.

---

# 33. Common Mistake — Continuing After Count Reaches Zero

Suppose:

```text
count0 = 0
```

and the current sandwich is:

```text
0
```

Do not skip the sandwich and inspect the next one.

Sandwiches are a stack.

The top sandwich must be removed before the next sandwich becomes available.

Therefore the process stops immediately.

---

# 34. Common Mistake — Decreasing the Wrong Count

For:

```text
sandwich = 0
```

decrease:

```java
count0--;
```

For:

```text
sandwich = 1
```

decrease:

```java
count1--;
```

The counts represent **remaining students**, not remaining sandwiches.

A decrement means:

> One student with this preference has successfully eaten and left.

---

# 35. Common Mistake — Returning Remaining Sandwiches

The question asks for:

```text
students unable to eat
```

not:

```text
sandwiches remaining
```

They happen to be equal in this problem because the initial counts are equal and each successful match removes one student and one sandwich.

But conceptually, the value we maintain is:

```text
remaining students
=
count0 + count1
```

---

# 36. Interview Explanation

I first considered simulating the student queue because that directly follows the problem description.

However, there are only two student preferences: `0` and `1`.

If at least one remaining student wants the current sandwich, that student will eventually reach the front because all students who do not want it simply rotate to the back.

Therefore the exact order of students does not matter.

I count how many remaining students prefer `0` and how many prefer `1`.

Then I process sandwiches from index `0` onward.

If the current sandwich is `0` and `count0 > 0`, I decrement `count0`. The same applies to type `1`.

If the required count is zero, nobody remaining can take the top sandwich, so the process stops. I return `count0 + count1`.

The algorithm runs in `O(n)` time and uses `O(1)` extra space.

---

# 37. What I Learned

- Problem descriptions may suggest a data structure without requiring literal simulation.
- Always ask whether the exact order actually matters.
- Queue rotation can sometimes be replaced by frequency counting.
- There are only two states here: `0` and `1`.
- Counting those states is enough to represent the remaining students.
- A `for` loop is appropriate when processing every element of a known array.
- `if / else` is appropriate for mutually exclusive states.
- An early `return` is useful when the final answer becomes known.
- The top sandwich blocks access to every sandwich underneath it.
- If nobody wants the top sandwich, processing must stop.
- Maintaining a useful invariant can eliminate unnecessary simulation.

---

# Pattern Recognition

When I see a problem involving:

```text
Queue
+
Repeated rotation
+
Small number of possible preferences
```

I should ask:

```text
Does exact order matter?
```

If the answer is no, consider:

```text
COUNTING / FREQUENCY ARRAY
```

Instead of:

```text
simulate every movement
```

we can reduce:

```text
[1,1,1,0,0,1]
```

to:

```text
0 → 2 students
1 → 4 students
```

Then:

```text
Current sandwich
       ↓
Does anyone want it?
      / \
    Yes  No
     ↓    ↓
decrement STOP
 count     ↓
     ↓   return remaining
next sandwich
```

The central mental model is:

```text
UNDERSTAND SIMULATION
        ↓
ASK IF ORDER MATTERS
        ↓
REDUCE STATE TO COUNTS
        ↓
PROCESS SANDWICHES
        ↓
STOP WHEN REQUIRED COUNT = 0
```