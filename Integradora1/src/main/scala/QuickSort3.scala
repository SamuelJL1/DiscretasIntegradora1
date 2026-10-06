import scala.annotation.tailrec

 /* This class contains the improved quick sort, which replaces the 2-way partition of
 * QuickSort with a 3-way partition (< x, = x and > x), so lists with many repeated
 * values are sorted much faster. It is written in a pure functional style (immutable
 * lists, recursion and pattern matching) and the pivot is the head of the list.
 */
final class QuickSort3 {

  /** Classic quick sort, used here for its auxiliary list functions (append). */
  private val base = new QuickSort

  /**
   * 3-way partition: splits a list in three parts, the elements less than the
   * pivot, the elements equal to the pivot and the elements greater than the pivot.
   * The order inside each part is not kept (it does not matter for sorting).
   *
   * @param list    the elements to classify
   * @param pivot   the value used to compare
   * @param less    accumulator with the elements < pivot (start with Nil)
   * @param equal   accumulator with the elements = pivot
   * @param greater accumulator with the elements > pivot (start with Nil)
   * @return the triple (elements < pivot, elements = pivot, elements > pivot)
   */
  @tailrec
  def partition3(list: List[Int], pivot: Int, less: List[Int], equal: List[Int], greater: List[Int]): (List[Int], List[Int], List[Int]) =
    list match {
      case Nil => (less, equal, greater)
      case head :: tail =>
        if head < pivot then partition3(tail, pivot, head :: less, equal, greater)
        else if head == pivot then partition3(tail, pivot, less, head :: equal, greater)
        else partition3(tail, pivot, less, equal, head :: greater)
    }

  /**
   * Quick sort with a 3-way partition. This is the main solution of problem 2.
   * The head of the list is the pivot. The tail is split in (< pivot), (= pivot)
   * and (> pivot), where the equal part starts with the pivot itself. Only the
   * first and the last part are sorted again; the middle part is already sorted
   * because all its elements are equal.
   *
   * @param list the list to sort
   * @return the list sorted in increasing order
   */
  def quickSort3(list: List[Int]): List[Int] = list match {
    case Nil => Nil
    case _ :: Nil => list
    case pivot :: tail =>
      val (less, equal, greater) = partition3(tail, pivot, Nil, List(pivot), Nil)
      base.append(quickSort3(less), base.append(equal, quickSort3(greater)))
  }
}
