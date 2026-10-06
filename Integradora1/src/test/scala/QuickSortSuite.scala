import munit.FunSuite

/**
 * Unit tests for the classic quick sort with a 2-way partition (problem 2).
 */
class QuickSortSuite extends FunSuite {

  val qs = new QuickSort

  test("append keeps the order of both lists") {
    assertEquals(qs.append(List(1, 2, 3), List(4, 5)), List(1, 2, 3, 4, 5))
    assertEquals(qs.append(Nil, List(4, 5)), List(4, 5))
    assertEquals(qs.append(List(1, 2), Nil), List(1, 2))
  }

  test("partition2 splits in <= pivot and > pivot") {
    val (lessOrEqual, greater) = qs.partition2(List(2, 8, 1, 5), 5, Nil, Nil)
    assertEquals(lessOrEqual, List(5, 1, 2))
    assertEquals(greater, List(8))
  }

  test("quickSort2 sorts a list with repeated values") {
    assertEquals(qs.quickSort2(List(4, 4, 1, 9, 4, 1, 4, 4)), List(1, 1, 4, 4, 4, 4, 4, 9))
  }

  test("quickSort2 with an empty list and with one element") {
    assertEquals(qs.quickSort2(Nil), Nil)
    assertEquals(qs.quickSort2(List(3)), List(3))
  }

  test("quickSort2 with a list in reverse order") {
    assertEquals(qs.quickSort2(List(5, 4, 3, 2, 1)), List(1, 2, 3, 4, 5))
  }
}

