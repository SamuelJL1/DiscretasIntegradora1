import munit.FunSuite
import scala.annotation.tailrec

class QuickSort3Suite extends FunSuite {

  val qs3 = new QuickSort3
  val qs = new QuickSort

  /**
   * Checks if a list is sorted in increasing order.
   *
   * @param list the list to check
   * @return true if every element is less than or equal to the next one
   */
  @tailrec
  private def isSorted(list: List[Int]): Boolean = list match {
    case Nil => true
    case _ :: Nil => true
    case first :: second :: tail =>
      if first > second then false
      else isSorted(second :: tail)
  }

  /**
   * Builds a list of a given size with values between 0 and range - 1.
   * The value of position i is (i * 7919) mod range, so the values look mixed.
   *
   * @param size  the number of elements to generate
   * @param range how many different values can appear
   * @param acc   the elements generated so far
   * @return a list with size values between 0 and range - 1
   */
  @tailrec
  private def buildList(size: Int, range: Int, acc: List[Int]): List[Int] =
    if size == 0 then acc
    else buildList(size - 1, range, ((size * 7919) % range) :: acc)

  /**
   * Counts the number of elements of a list.
   *
   * @param list the list to measure
   * @param acc  the number of elements counted so far (start with 0)
   * @return acc plus the number of elements in list
   */
  @tailrec
  private def count(list: List[Int], acc: Int): Int = list match {
    case Nil => acc
    case _ :: tail => count(tail, acc + 1)
  }

  /**
   * Builds a list with the same value repeated a given number of times.
   *
   * @param size  the number of elements
   * @param value the repeated value
   * @param acc   the elements generated so far
   * @return a list with size copies of value
   */
  @tailrec
  private def repeat(size: Int, value: Int, acc: List[Int]): List[Int] =
    if size == 0 then acc
    else repeat(size - 1, value, value :: acc)

  test("partition3 splits in < pivot, = pivot and > pivot") {
    val (less, equal, greater) = qs3.partition3(List(2, 8, 1, 3, 5), 5, Nil, List(5), Nil)
    assertEquals(less, List(3, 1, 2))
    assertEquals(equal, List(5, 5))
    assertEquals(greater, List(8))
  }

  test("partition3 with all the elements equal to the pivot") {
    val (less, equal, greater) = qs3.partition3(List(4, 4), 4, Nil, List(4), Nil)
    assertEquals(less, Nil)
    assertEquals(equal, List(4, 4, 4))
    assertEquals(greater, Nil)
  }

  test("quickSort3 example 1 of the statement") {
    assertEquals(qs3.quickSort3(List(2, 3, 9, 2, 2)), List(2, 2, 2, 3, 9))
  }

  test("quickSort3 example 2 of the statement") {
    assertEquals(qs3.quickSort3(List(4, 4, 1, 9, 4, 1, 4, 4)), List(1, 1, 4, 4, 4, 4, 4, 9))
  }

  test("quickSort3 with an empty list") {
    assertEquals(qs3.quickSort3(Nil), Nil)
  }

  test("quickSort3 with one element") {
    assertEquals(qs3.quickSort3(List(42)), List(42))
  }

  test("quickSort3 with a sorted list") {
    assertEquals(qs3.quickSort3(List(1, 2, 3, 4, 5)), List(1, 2, 3, 4, 5))
  }

  test("quickSort3 with a list in reverse order") {
    assertEquals(qs3.quickSort3(List(5, 4, 3, 2, 1)), List(1, 2, 3, 4, 5))
  }

  test("quickSort3 with negative numbers and repeated values") {
    assertEquals(qs3.quickSort3(List(0, -3, 7, -3, 0, 2, -10)), List(-10, -3, -3, 0, 0, 2, 7))
  }

  test("quickSort3 with all the elements equal") {
    assertEquals(qs3.quickSort3(List(5, 5, 5, 5, 5, 5, 5)), List(5, 5, 5, 5, 5, 5, 5))
  }

  test("quickSort3 with a big list with only 3 different values") {
    val input = buildList(100000, 3, Nil)
    val result = qs3.quickSort3(input)
    assert(isSorted(result))
    assertEquals(count(result, 0), 100000)
  }

  test("quickSort3 with a big list where every element is the same") {
    val result = qs3.quickSort3(repeat(200000, 1, Nil))
    assertEquals(result, repeat(200000, 1, Nil))
  }

  test("quickSort3 gives the same result as quickSort2 on a mixed list") {
    val input = buildList(5000, 1000, Nil)
    val result = qs3.quickSort3(input)
    assert(isSorted(result))
    assertEquals(result, qs.quickSort2(input))
  }
}
