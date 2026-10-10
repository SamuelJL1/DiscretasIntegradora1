# 1.3 Complexity and Recurrence Equation

## 1.3.1 Notation

- $n$: size of the list.
- $T(n)$: cost of `aux(n, lst)`. Since `numberOfInversions` only unpacks the tuple, $T_{\text{numberOfInversions}}(n) = T(n) + O(1)$.
- Each elementary operation (comparison, `::`, addition, pattern matching) costs a constant $c$.

## 1.3.2 Cost of the Helper Methods

| Method | Description | Cost |
|---|---|---|
| `length` | Traverses the entire list | $\Theta(m)$ for a list of size $m$ |
| `split(lst, k)` | One recursive call per element, up to $k$ steps | $\Theta(k)$; with $k = n/2$ it becomes $\Theta(n)$ |
| `merge` | Each call consumes one element from `left` or `right` | See below |

**`split`:**

$$S(k) = S(k-1) + c, \qquad S(0) = c \;\Rightarrow\; S(k) = \Theta(k)$$

**`merge`:** let $m = |left|$ and $r = |right|$. There are at most $m + r + 1$ calls, that is, $\Theta(n)$. However, the `h1 > h2` branch evaluates `left.length`, which is $\Theta(|left|)$, every time it is executed:

- **Best case** (sorted list): always `h1 <= h2`, so `length` is never called.

$$M(n) = c\,n = \Theta(n)$$

- **Worst case** (reverse-ordered list): there are $n/2$ calls through the `h1 > h2` branch, each with a `length` of $n/2$.

$$M(n) = \frac{n}{2}\cdot\frac{n}{2} + n = \frac{n^2}{4} + n = \Theta(n^2)$$

## 1.3.3 Recurrence Equation of `aux`

Each call with $n > 1$ executes `split` in $O(n)$, two `length` calls in $O(n)$, two recursive calls on the halves, and one `merge`:

$$
T(n) =
\begin{cases}
2\*T\left(\dfrac{n}{2}\right) + n_1 + M(n) & n > 1
\end{cases}
$$

### Best case: $M(n) = \Theta(n)$

$$T(n) = 2T\left(\frac{n}{2}\right) + n$$

**Master theorem:** $a = 2$, $b = 2$, $f(n) = n$ and $n^{\log_2 2} = n$. Since $f(n) = \Theta\!\left(n^{\log_b a}\right)$, case 2 applies:

$$T(n) = \Theta(n \log n)$$

**Recursion tree:** there are $\log_2 n$ levels and each level costs $n$ (level $i$ has $2^i$ subproblems of size $n/2^i$), hence $T(n) = n\log_2 n$.

### Worst case: $M(n) = \Theta(n^2)$

$$T(n) = 2T\!\left(\frac{n}{2}\right) + \frac{n^2}{4} + n$$

**Master theorem:** $a = 2$, $b = 2$, $f(n) = \Theta(n^2)$ and $n^{\log_2 2} = n$. Since $f(n) = \Omega\!\left(n^{1+\varepsilon}\right)$ with $\varepsilon = 1$, case 3 applies. Regularity condition:

$$a\,f(n/b) = 2\left(\frac{n}{2}\right)^2 = \frac{n^2}{2} \le k\,n^2 \quad\text{with } k = \tfrac{1}{2} < 1 \;\checkmark$$

$$T(n) = \Theta(n^2)$$

**By expansion:**

$$
T(n) = \sum_{i=0}^{\log_2 n} 2^i \cdot \frac{(n/2^i)^2}{4}
= \frac{n^2}{4}\sum_{i=0}^{\log_2 n}\frac{1}{2^i}
\le \frac{n^2}{4}\cdot 2 = \frac{n^2}{2}
$$

This is a geometric series, so the root level dominates and $T(n) = \Theta(n^2)$.

## 1.3.4 Time Complexity Summary

| Case | Recurrence | Complexity |
|---|---|---|
| Best (sorted list) | $T(n) = 2T(n/2) + n$ | $\Theta(n \log n)$ |
| Worst (reversed list) | $T(n) = 2T(n/2) + n^2/4 + n$ | $\Theta(n^2)$ |

## 1.3.5 Space Complexity

- `aux` has a recursion depth of $\log_2 n$.
- `split` and `merge` are **not tail-recursive**: the stack can grow up to $O(n)$.
- Intermediate lists take up $O(n)$ per level and are released as the recursion unwinds.

$$\text{Space} = \Theta(n)$$

## 1.3.6 Observation and Optimized Version

Computing `left.length` inside `merge` prevents guaranteeing $O(n \log n)$. If the length of `left` is passed as a parameter and decremented each time an element from the left is consumed (`counter + lenLeft`), then $M(n) = \Theta(n)$ in all cases and:

$$T(n) = 2T\left(\frac{n}{2}\right) + n \;\Rightarrow\; T(n) = \Theta(n \log n)$$

for the best, worst, and average cases.
