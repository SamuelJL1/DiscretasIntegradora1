# 3. Complexity Analysis

## 3.1 Notation

- $n$: size of the list.
- $T(n)$: cost of `quickSort3(list)`.
- $k$: number of distinct values in the list.
- For a call on a list of size $n$, `less`, `equal` and `greater` have sizes $a$, $e$ and $b$, so $a + e + b = n$ (the pivot is inside `equal`, hence $e \ge 1$).
- Each elementary operation (comparison, `::`, pattern matching) costs a constant $c$.

## 3.2 Cost of the Helper Methods

| Method | Description | Cost |
|---|---|---|
| `reverse(list, acc)` | One step per element of `list` (tail-recursive) | $\Theta(m)$ for $m = \lvert list \rvert$ |
| `append(first, second)` | Reverses `first` twice; `second` is not traversed | $\Theta(\lvert first \rvert)$ |
| `partition3` / `partition2` | One step per element, each with at most two comparisons | $\Theta(m)$ for a list of size $m$ |

**Cost of one call of `quickSort3` (without the recursive calls):**

- `partition3(tail, ...)` traverses $n-1$ elements: $\Theta(n)$.
- `append(equal, quickSort3(greater))` costs $\Theta(e)$.
- `append(quickSort3(less), ...)` costs $\Theta(a)$.

Since $a + e \le n$, the work outside the recursion is

$$f(n) = c_1 (n-1) + c_2\, e + c_3\, a = \Theta(n)$$

## 3.3 Recurrence Equation

$$
T(n) =
\begin{cases}
\Theta(1) & \text{if } n \le 1 \\
T(a) + T(b) + \Theta(n) & \text{if } n > 1
\end{cases}
$$

where $a + b = n - e$ and $e \ge 1$.

The cost depends on how the pivot splits the list, so there are several scenarios.

## 3.4 Best Case

There are two situations that minimize the cost.

**a) All elements equal.** Then $a = b = 0$ and $e = n$, so there are no recursive calls on non-empty lists:

$$T(n) = \Theta(n)$$

**b) The pivot splits the list in halves** ($a \approx b \approx n/2$):

$$T(n) = 2T\!\left(\frac{n}{2}\right) + n$$

**Master theorem:** $a = 2$, $b = 2$, $f(n) = n$ and $n^{\log_2 2} = n$. Since $f(n) = \Theta\!\left(n^{\log_b a}\right)$, case 2 applies:

$$T(n) = \Theta(n \log n)$$

**Recursion tree:** there are $\log_2 n$ levels. Level $i$ has $2^i$ subproblems of size $n/2^i$, which cost $n$ in total, hence $T(n) = n\log_2 n$.

## 3.5 Worst Case

The worst case happens when the pivot is always the smallest or the largest element and the elements are distinct. Because the pivot is the head, this is exactly **a sorted list or a reverse-ordered list**. Then one of `less` / `greater` is empty and the other has $n-1$ elements ($e = 1$):

$$T(n) = T(n-1) + T(0) + c\,n$$

The master theorem does not apply (the subproblem size is $n-1$, not $n/b$), so we expand:

$$
T(n) = \sum_{i=1}^{n} c\,i + \Theta(1) = c\,\frac{n(n+1)}{2} + \Theta(1) = \Theta(n^2)
$$

**Recursion tree:** it degenerates into a chain of depth $n$, and level $i$ costs $n - i$. Adding the levels gives $\Theta(n^2)$.

## 3.6 Effect of Repeated Values (3-way vs 2-way)

Every recursive call of `quickSort3` is made on `less` or `greater`, and both discard **all** the elements equal to the pivot. So each level of the recursion removes at least one distinct value, and the depth is at most $k$. The sublists at the same level are disjoint, so each level costs at most $c\,n$:

$$T(n) = O(n \cdot k)$$

In particular, with a fixed number of distinct values $k$ (as in the test of 100,000 elements with 3 values) the cost is $\Theta(n)$.

`quickSort2` does not have this property: elements equal to the pivot always go to `lessOrEqual`. For a list with all elements equal, $a = n-1$ and $b = 0$ in every call:

$$T_{2}(n) = T_{2}(n-1) + c\,n \;\Rightarrow\; T_{2}(n) = \Theta(n^2)$$

For the list of 200,000 equal elements this means about $n^2/2 = 2\times 10^{10}$ steps for `quickSort2` against about $2\times 10^{5}$ for `quickSort3`.

## 3.7 Average Case

Assume distinct elements in random order, so each element is equally likely to be the pivot. Then $a$ takes every value from $0$ to $n-1$ with probability $1/n$ and $b = n-1-a$:

$$T(n) = \frac{1}{n}\sum_{i=0}^{n-1}\big(T(i) + T(n-1-i)\big) + c\,n = \frac{2}{n}\sum_{i=0}^{n-1} T(i) + c\,n$$

Multiplying by $n$ and subtracting the same equation for $n-1$:

$$n\,T(n) = (n+1)\,T(n-1) + c\,(2n-1)$$

Dividing by $n(n+1)$:

$$\frac{T(n)}{n+1} = \frac{T(n-1)}{n} + \frac{c\,(2n-1)}{n(n+1)} \le \frac{T(n-1)}{n} + \frac{2c}{n+1}$$

Telescoping gives $\dfrac{T(n)}{n+1} \le T(0) + 2c\sum_{j=2}^{n+1}\dfrac{1}{j} = O(\log n)$, therefore:

$$T(n) = O(n \log n)$$

(the exact expected number of comparisons is $\approx 2n\ln n \approx 1.39\, n\log_2 n$).

## 3.8 Time Complexity Summary

| Case | Input | Recurrence | Complexity |
|---|---|---|---|
| Best (a) | all elements equal | no recursive calls | $\Theta(n)$ |
| Best (b) | pivot splits in halves | $T(n) = 2T(n/2) + n$ | $\Theta(n \log n)$ |
| Average | random order | $T(n) = \frac{2}{n}\sum T(i) + cn$ | $\Theta(n \log n)$ |
| Worst | sorted or reverse list, distinct elements | $T(n) = T(n-1) + n$ | $\Theta(n^2)$ |
| With $k$ distinct values | any order | depth $\le k$ | $O(n \cdot k)$ |

| Input | `quickSort2` | `quickSort3` |
|---|---|---|
| All elements equal | $\Theta(n^2)$ | $\Theta(n)$ |
| Few distinct values | close to $\Theta(n^2)$ | $\Theta(n)$ |
| Random distinct values | $\Theta(n \log n)$ | $\Theta(n \log n)$ |
| Sorted / reversed distinct | $\Theta(n^2)$ | $\Theta(n^2)$ |

The improvement only changes the behavior with repeated values. For distinct values both versions behave the same.

## 3.9 Space Complexity

- `reverse`, `append` and `partition3` are tail-recursive (`@tailrec` or built from tail-recursive calls), so they use constant stack.
- `quickSort3` is **not** tail-recursive: its stack depth equals the recursion depth, which is $\Theta(\log n)$ in the best and average cases and $\Theta(n)$ in the worst case (a sorted list of 100,000 elements may overflow the stack).
- Heap: the lists `less`, `equal` and `greater` of a call are disjoint subsets of the elements, so the live intermediate lists never exceed $O(n)$ elements in total.

$$\text{Space} = \Theta(n) \text{ (heap)} \;+\; \Theta(\log n) \text{ to } \Theta(n) \text{ (stack)} = \Theta(n)$$
