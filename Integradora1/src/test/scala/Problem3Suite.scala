import munit.FunSuite

class Problem3Suite extends FunSuite {

  val p3 = new Problem3

  // Para comparar Doubles conviene un margen de tolerancia (redondeo de punto flotante)
  val delta = 0.0001

  test("sortByX ordena puntos por coordenada x") {
    val input = List(List(5, 0), List(1, 0), List(9, 0), List(3, 0))
    val expected = List(List(1, 0), List(3, 0), List(5, 0), List(9, 0))
    assertEquals(p3.sortByX(input), expected)
  }

  test("sortByY ordena puntos por coordenada y") {
    val input = List(List(0, 5), List(0, 1), List(0, 9), List(0, 3))
    val expected = List(List(0, 1), List(0, 3), List(0, 5), List(0, 9))
    assertEquals(p3.sortByY(input), expected)
  }

  test("auxEuristic con dos puntos (ejemplo 1 del enunciado)") {
    val input = List(List(0, 0), List(3, 4))
    assertEqualsDouble(p3.auxEuristic(input), 5.0, delta)
  }

  test("auxEuristic con tres puntos (ejemplo 2 del enunciado)") {
    val input = List(List(0, 0), List(3, 4), List(1, 1))
    assertEqualsDouble(p3.auxEuristic(input), 1.4142, delta)
  }


  test("closestPair con dos puntos (ejemplo 1 del enunciado)") {
    val input = List(List(0, 0), List(3, 4))
    assertEqualsDouble(p3.auxClosestPair(input), 5.0, delta)
  }

  test("closestPair con tres puntos (ejemplo 2 del enunciado)") {
    val input = List(List(0, 0), List(3, 4), List(1, 1))
    assertEqualsDouble(p3.auxClosestPair(input), 1.4142, delta)
  }

  test("closestPair con puntos repetidos da distancia 0") {
    val input = List(List(2, 2), List(2, 2), List(9, 9))
    assertEqualsDouble(p3.auxClosestPair(input), 0.0, delta)
  }

  test("closestPair detecta el par más cercano cuando está en la franja (cruzado entre mitades)") {
    // Puntos lejanos a los lados, pero dos puntos muy cerca justo en el medio
    val input = List(
      List(0, 0), List(1, 50), List(2, -50),
      List(49, 0), List(51, 0),   // <- este par (49,0)-(51,0) es el más cercano, cruza la línea media
      List(100, 50), List(101, -50), List(102, 0)
    )
    assertEqualsDouble(p3.auxClosestPair(input), 2.0, delta)
  }

  test("closestPair con un conjunto más grande (6 puntos)") {
    val input = List(List(0, 0), List(1, 1), List(3, 4), List(5, 5), List(6, 7), List(9, 9))
    assertEqualsDouble(p3.auxClosestPair(input), 1.4142, delta)
  }

}