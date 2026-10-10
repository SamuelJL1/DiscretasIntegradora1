Here’s the full translation into English, keeping the structure exactly as it is:

---

# Problem 2 — Improving Quick Sort

The solution is in `src/main/scala/QuickSort.scala`. The main function is `quickSort3`, which uses the head of the list as pivot and the 3‑way partition `partition3` (`< x`, `= x`, `> x`). There is also `quickSort2` with the classic 2‑way partition to be able to compare.

```scala
@tailrec
def partition3(list: List[Int], pivot: Int, less: List[Int], equal: List[Int], greater: List[Int]): (List[Int], List[Int], List[Int]) =
  list match {
    case Nil => (less, equal, greater)
    case head :: tail =>
      if head < pivot then partition3(tail, pivot, head :: less, equal, greater)
      else if head == pivot then partition3(tail, pivot, less, head :: equal, greater)
      else partition3(tail, pivot, less, equal, head :: greater)
  }

def quickSort3(list: List[Int]): List[Int] = list match {
  case Nil => Nil
  case _ :: Nil => list
  case pivot :: tail =>
    val (less, equal, greater) = partition3(tail, pivot, Nil, List(pivot), Nil)
    append(quickSort3(less), append(equal, quickSort3(greater)))
}
```

## 1. Test Design

The tests are in `src/test/scala/QuickSortSuite.scala`.

| Function | Scenario | Input | Expected output |
|---|---|---|---|
| `partition3` | elements less, equal and greater | `[2,8,1,3,5]`, pivot 5, `equal = [5]` | `([3,1,2], [5,5], [8])` |
| `partition3` | all equal to pivot | `[4,4]`, pivot 4, `equal = [4]` | `(Nil, [4,4,4], Nil)` |
| `quickSort3` | examples from the statement | `[2,3,9,2,2]` / `[4,4,1,9,4,1,4,4]` | `[2,2,2,3,9]` / `[1,1,4,4,4,4,4,9]` |
| `quickSort3` | empty list and one element | `Nil` / `[42]` | `Nil` / `[42]` |
| `quickSort3` | sorted and reverse order | `[1,2,3,4,5]` / `[5,4,3,2,1]` | `[1,2,3,4,5]` |
| `quickSort3` | negatives and duplicates | `[0,-3,7,-3,0,2,-10]` | `[-10,-3,-3,0,0,2,7]` |
| `quickSort3` | many duplicates (large list) | 100,000 elements with 3 distinct values; 200,000 equal | sorted list with the same elements |
| `quickSort3` vs `quickSort2` | same result | 5,000 mixed elements | both give the same sorted list |

The partitions return each part reversed compared to the input order because the accumulators add at the head. That does not affect the final result.

## 2. Correctness Proof (Structural Induction)

Scala lists are defined recursively:

- **Base step:** `Nil` is a list.
- **Recursive step:** if `t` is a list and `h` is an integer, then `h :: t` is a list.
  That is why proofs are done by structural induction on lists: first the property is proven for `Nil` and then it is proven that, if it holds for `t`, it also holds for `h :: t`.

### Proof 1: `partition3` classifies elements correctly

Let `P(xs)` be the property that states that, for any pivot `p` and any accumulators `L`, `E`, `G`, if `partition3(xs, p, L, E, G) = (L', E', G')`, then:

- `L'` has the elements of `L` plus the elements of `xs` that are less than `p`,
- `E'` has the elements of `E` plus the elements of `xs` that are equal to `p`,
- `G'` has the elements of `G` plus the elements of `xs` that are greater than `p`.

  **Base step:** we must prove that `P(Nil)` is true. By definition of `partition3`, `partition3(Nil, p, L, E, G) = (L, E, G)`. Since `Nil` has no elements, there is nothing to add to `L`, `E` or `G`, so `L' = L`, `E' = E` and `G' = G`. Therefore, `P(Nil)` is true.

**Recursive step:** suppose that `P(t)` is true for any `L`, `E`, `G` (inductive hypothesis). We must prove that this implies that `P(h :: t)` is true for any integer `h`. By definition of `partition3` there are three cases:

- If `h < p`: `partition3(h :: t, p, L, E, G) = partition3(t, p, h :: L, E, G)`. By the inductive hypothesis (with accumulator `h :: L`), `L'` has the elements of `h :: L` plus the elements of `t` less than `p`. That is the same as the elements of `L` plus the elements of `h :: t` less than `p`, because `h` is less than `p`. `E'` and `G'` remain as in the hypothesis, which is correct because `h` is neither equal nor greater than `p`.
- If `h = p`: `partition3(h :: t, p, L, E, G) = partition3(t, p, L, h :: E, G)`. By the inductive hypothesis, `E'` has the elements of `E`, plus `h`, plus the elements of `t` equal to `p`, which are exactly the elements of `h :: t` equal to `p`. `L'` and `G'` do not change.
- If `h > p`: `partition3(h :: t, p, L, E, G) = partition3(t, p, L, E, h :: G)`. Same as the previous case, but `h` goes into `G'`.
  In all three cases `P(h :: t)` is true, which concludes the proof. Moreover, since the three cases do not overlap, each element ends up in exactly one part and none are lost.

### Proof 2: `quickSort3` sorts the list

Let `Q(xs)` be the property that states that `quickSort3(xs)` is a list sorted in increasing order and that it has exactly the same elements as `xs` (with the same repetitions).

**Base step:** we must prove that `Q(Nil)` is true. By definition, `quickSort3(Nil) = Nil`, which is sorted and has the same elements as `Nil`. Therefore, `Q(Nil)` is true. The same holds for a single‑element list: `quickSort3(x :: Nil) = x :: Nil`, which is also sorted.

**Recursive step:** let `xs = p :: t`, with `t` non‑empty. Since `quickSort3` is not called on `t` but on the parts `less` and `greater`, which come from `t`, we suppose that `Q` is true for all lists with fewer elements than `p :: t` (inductive hypothesis). We must prove that `Q(p :: t)` is true.

By definition, `quickSort3(p :: t)` does `partition3(t, p, Nil, List(p), Nil) = (less, equal, greater)` and returns `quickSort3(less) ++ equal ++ quickSort3(greater)`.

1. By Proof 1 (with `L = Nil`, `E = List(p)`, `G = Nil`): `less` has the elements of `t` less than `p`, `equal` has `p` and the elements of `t` equal to `p`, and `greater` has the elements of `t` greater than `p`.
2. `less` and `greater` only have elements of `t`, so they have fewer elements than `p :: t`. By the inductive hypothesis, `quickSort3(less)` is sorted and has the same elements as `less`, and the same holds for `quickSort3(greater)` and `greater`.
3. The result is sorted: everything in `quickSort3(less)` is less than `p`, everything in `equal` is equal to `p` and everything in `quickSort3(greater)` is greater than `p`, and each part is already sorted.
4. The result has the same elements as `p :: t`: between `less`, `equal` and `greater` are all the elements of `t` plus `p`, without losing or duplicating any (Proof 1).
   Therefore `Q(p :: t)` is true, which concludes the proof.

## 3
