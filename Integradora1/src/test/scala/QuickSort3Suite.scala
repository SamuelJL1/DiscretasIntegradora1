import munit.FunSuite

class QuickSort3Suite extends FunSuite {

  val qs3 = new QuickSort3

  test("quickSort3 con lista vacía") {
    assertEquals(qs3.quickSort3(Nil), Nil)
  }

  test("quickSort3 con lista de un elemento") {
    assertEquals(qs3.quickSort3(List(42)), List(42))
  }

  test("quickSort3 con lista ya ordenada") {
    assertEquals(qs3.quickSort3(List(1, 2, 3, 4, 5)), List(1, 2, 3, 4, 5))
  }

  test("quickSort3 con lista en orden inverso") {
    assertEquals(qs3.quickSort3(List(5, 4, 3, 2, 1)), List(1, 2, 3, 4, 5))
  }

  test("quickSort3 con elementos repetidos") {
    assertEquals(qs3.quickSort3(List(4, 2, 4, 1, 2)), List(1, 2, 2, 4, 4))
  }

  test("quickSort3 con muchos valores iguales al pivote") {
    assertEquals(qs3.quickSort3(List(5, 5, 5, 5, 5, 5, 5)), List(5, 5, 5, 5, 5, 5, 5))
  }

  test("separar3 divide correctamente respecto al pivote") {
    val (menores, iguales, mayores) = qs3.separar3(List(5, 2, 8, 1, 3, 5), 5, Nil, List(5), Nil)
    assertEquals(menores, List(2, 1, 3))
    assertEquals(iguales, List(5, 5, 5))
    assertEquals(mayores, List(8))
  }
}
