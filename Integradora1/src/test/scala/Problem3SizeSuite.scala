import munit.FunSuite
import scala.annotation.tailrec
import scala.concurrent.duration.*

class Problem3SizeSuite extends FunSuite {

  // munit fails a test after 30 seconds by default; the large input may need more
  override val munitTimeout: Duration = Duration(5, "min")

  val problem = new Problem3

  /**
   * Builds n points (i * stepX, i * stepY) for i = 0 until n, in increasing i order.
   * @param i the index being built, starting at n - 1
   * @param stepX the increment of the x-coordinate per index
   * @param stepY the increment of the y-coordinate per index
   * @param acc the points built so far
   * @return the list of n points
   */
  @tailrec
  private def buildPoints(i: Int, stepX: Int, stepY: Int, acc: List[List[Int]]): List[List[Int]] =
    if (i < 0) acc
    else buildPoints(i - 1, stepX, stepY, List(i * stepX, i * stepY) :: acc)


  /**
   * Runs the closest pair algorithm on n points for five patterns whose answer is known.
   * @param n the number of points (n >= 2)
   */
  private def checkSize(n: Int): Unit = {
    val delta = 1e-9
    // one axis only: the answer is the absolute value of the step
    assertEqualsDouble(problem.auxClosestPair(buildPoints(n - 1, 1, 0, Nil)), 1.0, delta)
    assertEqualsDouble(problem.auxClosestPair(buildPoints(n - 1, -1, 0, Nil)), 1.0, delta)
    assertEqualsDouble(problem.auxClosestPair(buildPoints(n - 1, 5, 0, Nil)), 5.0, delta)
    assertEqualsDouble(problem.auxClosestPair(buildPoints(n - 1, -3, 0, Nil)), 3.0, delta)
    assertEqualsDouble(problem.auxClosestPair(buildPoints(n - 1, 0, 1, Nil)), 1.0, delta)
    assertEqualsDouble(problem.auxClosestPair(buildPoints(n - 1, 0, -2, Nil)), 2.0, delta)
    assertEqualsDouble(problem.auxClosestPair(buildPoints(n - 1, 0, 7, Nil)), 7.0, delta)

    // both axes: the answer is sqrt(stepX^2 + stepY^2)
    assertEqualsDouble(problem.auxClosestPair(buildPoints(n - 1, 1, 1, Nil)), math.sqrt(2), delta)
    assertEqualsDouble(problem.auxClosestPair(buildPoints(n - 1, 1, -1, Nil)), math.sqrt(2), delta)
    assertEqualsDouble(problem.auxClosestPair(buildPoints(n - 1, -1, -1, Nil)), math.sqrt(2), delta)
    assertEqualsDouble(problem.auxClosestPair(buildPoints(n - 1, 2, 2, Nil)), math.sqrt(8), delta)
    assertEqualsDouble(problem.auxClosestPair(buildPoints(n - 1, -2, 1, Nil)), math.sqrt(5), delta)
    assertEqualsDouble(problem.auxClosestPair(buildPoints(n - 1, 3, 4, Nil)), 5.0, delta)
    assertEqualsDouble(problem.auxClosestPair(buildPoints(n - 1, 3, -4, Nil)), 5.0, delta)

    // every point repeated
    assertEqualsDouble(problem.auxClosestPair(buildPoints(n - 1, 0, 0, Nil)), 0.0, delta)
  }

  test("toy input (n < 10^2)") {
    checkSize(50)
  }

  test("small input (10^2 <= n < 10^4)") {
    checkSize(5000)
  }

  test("medium input (10^4 <= n < 10^5)") {
    checkSize(50000)
  }

  test("large input (n >= 10^6)") {
    checkSize(1000000)
  }
}