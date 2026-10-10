import scala.annotation.tailrec

/* This class contains two versions of quick sort written in a pure functional style
* (immutable lists, recursion and pattern matching). In both versions the pivot is
  * the head of the list:
  *
*  - quickSort2: the classic version that uses a 2-way partition (<= x and > x).
*  - quickSort3: the improved version that replaces the 2-way partition with a
*    3-way partition (< x, = x and > x), so lists with many repeated values are
  *    sorted much faster.
*/
final class QuickSort {

  /**
   * Moves the elements of a list, one by one, to the front of an accumulator.
   * Because of that, the elements end up in reverse order on top of acc.
   * Example: reverseOnto(List(1, 2, 3), List(9)) = List(3, 2, 1, 9).
   *
   * @param list the list whose elements are moved
   * @param acc  the list that receives the elements
   * @return the reverse of list followed by acc
   */
  @tailrec
  def reverse(list: List[Int], acc: List[Int]): List[Int] = list match {
    case Nil => acc
    case head :: tail => reverse(tail, head :: acc)
  }

  /**
   * Concatenates two lists keeping the order of both of them.
   * It reverses the first list and then moves it back on top of the second one.
   *
   * @param first  the list that goes at the beginning
   * @param second the list that goes at the end
   * @return a list with the elements of first followed by the elements of second
   */
  def append(first: List[Int], second: List[Int]): List[Int] =
    reverse(reverse(first, Nil), second)
/**
 * 2-way partition: splits a list in the elements that are less than or equal
 * to the pivot and the elements that are greater than the pivot.
 * The order inside each part is not kept (it does not matter for sorting).
 *
 * @param list        the elements to classify (without the pivot)
 * @param pivot       the value used to compare
 * @param lessOrEqual accumulator with the elements <= pivot (start with Nil)
 * @param greater     accumulator with the elements > pivot (start with Nil)
 * @return the pair (elements <= pivot, elements > pivot)
 */
@tailrec
def partition2(list: List[Int], pivot: Int, lessOrEqual: List[Int], greater: List[Int]): (List[Int], List[Int]) =
  list match {
    case Nil => (lessOrEqual, greater)
    case head :: tail =>
      if head <= pivot then partition2(tail, pivot, head :: lessOrEqual, greater)
      else partition2(tail, pivot, lessOrEqual, head :: greater)
  }

/**
 * Quick sort with a 2-way partition. The head of the list is the pivot, the
 * tail is split in (<= pivot) and (> pivot), both parts are sorted and the
 * pivot is placed between them.
 *
 * @param list the list to sort
 * @return the list sorted in increasing order
 */
def quickSort2(list: List[Int]): List[Int] = list match {
  case Nil => Nil
  case _ :: Nil => list
  case pivot :: tail =>
    val (lessOrEqual, greater) = partition2(tail, pivot, Nil, Nil)
    append(quickSort2(lessOrEqual), pivot :: quickSort2(greater))
}
}