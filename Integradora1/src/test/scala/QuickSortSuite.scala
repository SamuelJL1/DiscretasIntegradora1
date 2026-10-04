import munit.FunSuite

class QuickSortSuite extends FunSuite {

  val qs = new QuickSort

  test("menores devuelve elementos menores al pivote") {
    assertEquals(qs.menores(List(5, 2, 8, 1, 3), 4), List(2, 1, 3))
  }

  test("mayores devuelve elementos mayores al pivote") {
    assertEquals(qs.mayores(List(5, 2, 8, 1, 3), 4), List(5, 8))
  }

  test("appendTR concatena dos listas") {
    assertEquals(qs.appendTR(List(1, 2, 3), List(4, 5)), List(1, 2, 3, 4, 5))
  }


  test("quickSort ordena lista desordenada") {
    assertEquals(qs.quickSort(List(5, 2, 8, 1, 3)), List(1, 2, 3, 5, 8))
  }

  test("quickSort con lista vacía") {
    assertEquals(qs.quickSort(Nil), Nil)
  }

  test("quickSort con lista ya ordenada") {
    assertEquals(qs.quickSort(List(1, 2, 3, 4)), List(1, 2, 3, 4))
  }

  test("quickSort con elementos repetidos") {
    assertEquals(qs.quickSort(List(4, 4, 2, 2, 1)), List(1, 2,2, 4,4))
  }
}
