* This class contains two versions of quick sort written in a pure functional style
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
  def reverseOnto(list: List[Int], acc: List[Int]): List[Int] = list match {
    case Nil => acc
    case head :: tail => reverseOnto(tail, head :: acc)
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
    reverseOnto(reverseOnto(first, Nil), second)
}
