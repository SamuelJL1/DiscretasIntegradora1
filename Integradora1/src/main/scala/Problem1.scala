import scala.annotation.tailrec
class Problem1 {
  /**
   * Counts the number of inversions in a list. An inversion is a pair of positions
   * (i, j) such that i < j and lst(i) > lst(j), i.e. the number of out-of-order pairs.
   * It relies on a merge sort that counts inversions while sorting, running in O(n log n).
   *
   * @param n   the number of elements in the list
   * @param lst the list whose inversions are counted
   * @return the total number of inversions in the list
   */
  def numberOfInversions(n: Int, lst: List[Int]): Int =
    val (p, inversions) = aux(n, lst)
    inversions
  
  /**
   * Recursive helper of merge sort that sorts the list and counts its inversions at the same time.
   * The list is split in two halves, each half is processed recursively, and the results are
   * combined with {@link #merge}, which adds the inversions found between both halves.
   *
   * @param n   the number of elements in the list
   * @param lst the list to sort and analyze
   * @return a pair with the sorted list and the total number of inversions in the original list
   */
  def aux(n: Int, lst: List[Int]): (List[Int], Int) =
    if n <= 1 then
      (lst, 0)
    else
      val (left, right) = split(lst, n / 2)
      val (sortedLeft, invLeft) = aux(left.length, left)
      val (sortedRight, invRight) = aux(right.length, right)
      merge(sortedLeft, sortedRight, invLeft + invRight)
  
  /**
   * Splits a list into two parts: the first n elements and the remaining ones.
   * If the list has fewer than n elements, the right part is empty and the left part
   * contains the whole list.
   *
   * @param lst the list to split
   * @param n   the number of elements that go into the left part
   * @return a pair (left, right) where left holds the first n elements and right the rest
   */
  def split(lst: List[Int], n: Int): (List[Int], List[Int]) =
    @tailrec
    def loop(rest: List[Int], k: Int, acc: List[Int]): (List[Int], List[Int]) =
      if k == 0 then (acc.reverse, rest)
      else rest match
        case Nil => (acc.reverse, Nil)
        case head :: tail => loop(tail, k - 1, head :: acc)
    loop(lst, n, Nil)

  /**
   * Merges two sorted lists into a single sorted list while counting the inversions between them.
   * Whenever the head of the right list is strictly smaller than the head of the left list,
   * it forms an inversion with every remaining element of the left list, so the counter grows
   * by the length of the left list. Equal elements are not counted as inversions.
   *
   * @param left    a sorted list (the left half)
   * @param right   a sorted list (the right half)
   * @param counter the number of inversions accumulated so far
   * @return a pair with the merged sorted list and the updated inversion counter
   */
  def merge(left: List[Int], right: List[Int], counter: Int): (List[Int], Int) =
    @tailrec
    def loop(l: List[Int], r: List[Int], lenL: Int, count: Int, acc: List[Int]): (List[Int], Int) =
      (l, r) match
        case (Nil, _) => (acc.reverse ::: r, count)
        case (_, Nil) => (acc.reverse ::: l, count)
        case (h1 :: t1, h2 :: t2) =>
          if h1 <= h2 then loop(t1, r, lenL - 1, count, h1 :: acc)
          else loop(l, t2, lenL, count + lenL, h2 :: acc)
    loop(left, right, left.length, counter, Nil)
}