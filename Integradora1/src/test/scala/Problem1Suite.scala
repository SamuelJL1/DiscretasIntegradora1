import munit.FunSuite
import scala.concurrent.duration.*

class Problem1Suite extends FunSuite:
  val problem = new Problem1()
  override val munitTimeout = 2.minutes

  def randomList(n: Int, seed: Int): List[Int] =
    val r = new scala.util.Random(seed)
    List.fill(n)(r.nextInt(2001) - 1000)

  def bruteForce(l: List[Int]): Int =
    val a = l.toArray
    var count = 0
    var i = 0
    while i < a.length do
      var j = i + 1
      while j < a.length do
        if a(i) > a(j) then count += 1
          j += 1
      i += 1
    count

  def swappedPairs(n: Int): List[Int] =
    List.tabulate(n)(i => if i % 2 == 0 then i + 1 else i - 1)

  test("toy (n=50): random list matches brute force") {
    val l = randomList(50, seed = 1)
    assertEquals(problem.numberOfInversions(l.length, l), bruteForce(l))
  }

  test("small (n=5000): random list matches brute force") {
    val l = randomList(5000, seed = 2)
    assertEquals(problem.numberOfInversions(l.length, l), bruteForce(l))
  }

  test("medium (n=50000): sorted list has 0 inversions") {
    val l = List.range(0, 50000)
    assertEquals(problem.numberOfInversions(l.length, l), 0)
  }

  test("medium (n=50000): swapped pairs has n/2 inversions") {
    val n = 50000
    assertEquals(problem.numberOfInversions(n, swappedPairs(n)), n / 2)
  }

  test("large (n=1000000): sorted list has 0 inversions") {
    val n = 1000000
    assertEquals(problem.numberOfInversions(n, List.range(0, n)), 0)
  }

  test("large (n=1000000): swapped pairs has n/2 inversions") {
    val n = 1000000
    assertEquals(problem.numberOfInversions(n, swappedPairs(n)), n / 2)
  }

  test("empty list has 0 inversions") {
    assertEquals(problem.numberOfInversions(0, Nil), 0)
  }


  test("single-element list has 0 inversions") {
    assertEquals(problem.numberOfInversions(1, List(7)), 0)
  }
  
  test("two unsorted elements: 1 inversion") {
    assertEquals(problem.numberOfInversions(2, List(2, 1)), 1)
  }

  test("already sorted list: 0 inversions") {
    assertEquals(problem.numberOfInversions(6, List(1, 2, 3, 4, 5, 6)), 0)
  }

  test("reverse-ordered list: n(n-1)/2 inversions (even length)") {
    val n = 6
    assertEquals(problem.numberOfInversions(n, List(6, 5, 4, 3, 2, 1)), n * (n - 1) / 2)
  }
  

  test("[2,3,9,2,3] has 2 inversions") {
    assertEquals(problem.numberOfInversions(5, List(2, 3, 9, 2, 9)), 2)
  }

  test("[9,7,5,3] has 6 inversions") {
    assertEquals(problem.numberOfInversions(4, List(9, 7, 5, 3)), 6)
  }

  test("[3,1,2] has 2 inversions") {
    assertEquals(problem.numberOfInversions(3, List(3, 1, 2)), 2)
  }

  test("all elements equal: 0 inversions") {
    assertEquals(problem.numberOfInversions(5, List(4, 4, 4, 4, 4)), 0)
  }
  

  test("negative numbers: [0,-1,-2] has 3 inversions") {
    assertEquals(problem.numberOfInversions(3, List(0, -1, -2)), 3)
  }

  test("mixed negatives and positives: [3,-1,2,-5] has 5 inversions") {
    assertEquals(problem.numberOfInversions(4, List(3, -1, 2, -5)), 5)
  }
  
  test("aux and numberOfInversions agree") {
    val lst = List(9, 3, 7, 1, 5)
    assertEquals(problem.aux(lst.length, lst)._2, problem.numberOfInversions(lst.length, lst))
  }

  test("split divides into (first n, rest)") {
    assertEquals(problem.split(List(1, 2, 3, 4, 5), 2), (List(1, 2), List(3, 4, 5)))
  }

  test("split with n = 0 leaves the left side empty") {
    assertEquals(problem.split(List(1, 2, 3), 0), (Nil, List(1, 2, 3)))
  }

  test("split with n = length leaves the right side empty") {
    assertEquals(problem.split(List(1, 2, 3), 3), (List(1, 2, 3), Nil))
  }

  test("split with n greater than the length does not fail") {
    assertEquals(problem.split(List(1, 2), 5), (List(1, 2), Nil))
  }

  test("split on an empty list") {
    assertEquals(problem.split(Nil, 3), (Nil, Nil))
  }
  
  test("merge with empty left returns the right and the untouched counter") {
    assertEquals(problem.merge(Nil, List(1, 2, 3), 5), (List(1, 2, 3), 5))
  }

  test("merge with empty right returns the left and the untouched counter") {
    assertEquals(problem.merge(List(1, 2, 3), Nil, 5), (List(1, 2, 3), 5))
  }

  test("merge of two lists with no crossings: 0 new inversions") {
    assertEquals(problem.merge(List(1, 2, 3), List(4, 5, 6), 0), (List(1, 2, 3, 4, 5, 6), 0))
  }

  test("merge with everything crossed: left entirely greater than right") {
    assertEquals(problem.merge(List(4, 5, 6), List(1, 2, 3), 0), (List(1, 2, 3, 4, 5, 6), 9))
  }

  test("merge accumulates on top of the counter it receives") {
    assertEquals(problem.merge(List(4, 5, 6), List(1, 2, 3), 10)._2, 19)
  }

  test("interleaved merge: [1,3,5] and [2,4,6] produce 3 inversions") {
    assertEquals(problem.merge(List(1, 3, 5), List(2, 4, 6), 0), (List(1, 2, 3, 4, 5, 6), 3))
  }

  test("merge with equal elements: no inversion counted when h1 == h2") {
    assertEquals(problem.merge(List(2, 2), List(2, 2), 0), (List(2, 2, 2, 2), 0))
  }

