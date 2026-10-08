import scala.annotation.tailrec

class Problem3 {

  /**
   * Returns one coordinate of a point.
   * @param point the point, represented as List(x, y)
   * @param coord 0 for the x-coordinate, 1 for the y-coordinate
   * @return the requested coordinate
   */
  def coordOf(point: List[Int], coord: Int): Int = point match {
    case x :: y :: Nil => if (coord == 0) x else y
  }

  /**
   * Merges two lists already sorted by a coordinate into one sorted list in O(|left| + |right|).
   * @param left list sorted by coord
   * @param right list sorted by coord
   * @param coord 0 to compare by x, 1 to compare by y
   * @return a single list sorted by coord
   */
  def mergeByCoord(left: List[List[Int]], right: List[List[Int]], coord: Int): List[List[Int]] = {

    @tailrec
    def mergeAcc(l: List[List[Int]], r: List[List[Int]], acc: List[List[Int]]): List[List[Int]] =
      (l, r) match {
        case (Nil, _) => acc.reverse ::: r
        case (_, Nil) => acc.reverse ::: l
        case (lHead :: lTail, rHead :: rTail) =>
          if (coordOf(lHead, coord) <= coordOf(rHead, coord))
            mergeAcc(lTail, r, lHead :: acc)
          else
            mergeAcc(l, rTail, rHead :: acc)
      }

    mergeAcc(left, right, Nil)
  }

  /**
   * Merge sort of points by a coordinate. O(n log n) in every case, with
   * recursion depth O(log n).
   * @param points the points to sort
   * @param coord 0 to sort by x, 1 to sort by y
   * @return the points sorted in ascending order by coord
   */
  def mergeSortBy(points: List[List[Int]], coord: Int): List[List[Int]] = points match {
    case Nil => Nil
    case _ :: Nil => points
    case _ =>
      val (firstHalf, secondHalf) = auxPartInTwoParts(points, points.length / 2)
      mergeByCoord(mergeSortBy(firstHalf, coord), mergeSortBy(secondHalf, coord), coord)
  }

  /**
   * Sorts a list of points by their x-coordinate (merge sort).
   * @param inputList the list of points to sort, each represented as List(x, y)
   * @return a new list of points sorted in ascending order by x-coordinate
   */
  def sortByX(inputList: List[List[Int]]): List[List[Int]] = mergeSortBy(inputList, 0)

  /**
   * Sorts a list of points by their y-coordinate (merge sort).
   * @param inputList the list of points to sort, each represented as List(x, y)
   * @return a new list of points sorted in ascending order by y-coordinate
   */
  def sortByY(inputList: List[List[Int]]): List[List[Int]] = mergeSortBy(inputList, 1)

  /**
   * Computes the minimum Euclidean distance between any pair of points in a list
   * sorted by y-coordinate. Each pivot is compared only with the points that follow
   * it while their y-difference is smaller than the best distance found so far,
   * so the work per pivot is constant and the whole scan is O(k).
   * Used both as the base case and to scan the strip.
   * @param inputList the points to check, sorted by y, each represented as List(x, y)
   * @param initialBest the best distance known before scanning (Double.MaxValue if none)
   * @return the smallest distance found, or initialBest if no pair improves it
   */
  def auxEuristic(inputList: List[List[Int]]): Double = {

    /**
     * Compares a fixed pivot with the points that follow it, stopping as soon as
     * the y-difference is >= best (the remaining points cannot improve best).
     * @param pivotPoint the fixed point to compare against, as List(x, y)
     * @param remaining the points after the pivot, sorted by y
     * @param best the smallest distance found so far
     * @return the smallest distance between the pivot and the remaining points, or best
     */
    @tailrec
    def closestToPivot(pivotPoint: List[Int], remaining: List[List[Int]], best: Double): Double = {
      remaining match {
        case Nil => best
        case head :: tail =>
          (pivotPoint, head) match {
            case (x1 :: y1 :: Nil, x2 :: y2 :: Nil) =>
              if ((y2 - y1).toDouble >= best) best
              else {
                val dx = (x1 - x2).toDouble
                val dy = (y1 - y2).toDouble
                val dist = Math.sqrt(dx * dx + dy * dy)
                closestToPivot(pivotPoint, tail, if (dist < best) dist else best)
              }
          }
      }
    }

    /**
     * Uses each point in turn as a pivot against the points that follow it.
     * @param points the remaining points still to be used as a pivot
     * @param best the smallest distance found so far across all pivots checked
     * @return the smallest distance found
     */
    @tailrec
    def bruteForce(points: List[List[Int]], best: Double): Double = {
      points match {
        case Nil => best
        case _ :: Nil => best
        case head :: tail => bruteForce(tail, closestToPivot(head, tail, best))
      }
    }

    bruteForce(inputList, Double.MaxValue)
  }

  /**
   * Finds the minimum distance between any two points (n >= 2).
   * Sorts by x only once, then delegates to the recursive function.
   *
   * @param points the list of points, each represented as List(x, y)
   * @return the smallest distance between any two points
   */
  def auxClosestPair(points: List[List[Int]]): Double =
    val (d, _) = closestRec(sortByX(points))
    d

  /**
   * Recursive step of the closest pair algorithm. It returns the points sorted by y
   * together with the distance, so the strip never has to be sorted again: the two
   * halves sorted by y are merged in O(n).
   *
   * @param sorted the points already sorted by x-coordinate
   * @return (smallest distance between two points in sorted, the same points sorted by y)
   */
  def closestRec(sorted: List[List[Int]]): (Double, List[List[Int]]) =
    sorted match {
      case p if p.length <= 3 =>
        (auxEuristic(p), sortByY(p))
      case _ =>
        val mid = sorted.length / 2
        val (s1, s2) = auxPartInTwoParts(sorted, mid)
        s2 match {
          case (midX :: _ :: Nil) :: _ =>
            val (d1, s1ByY) = closestRec(s1)
            val (d2, s2ByY) = closestRec(s2)
            val d = if (d1 < d2) d1 else d2
            val sortedByY = mergeByCoord(s1ByY, s2ByY, 1)
            val strip = auxBuildStrip(sortedByY, midX, d)
            (math.min(d, auxEuristic(strip)), sortedByY)
        }
    }

  /**
   * Splits a list of points into two halves of approximately equal size.
   * @param inputList the list of points to split
   * @param mid the index marking the boundary between the first and second half
   * @return a tuple (firstHalf, secondHalf) preserving the original order within each half
   */
  def auxPartInTwoParts(inputList: List[List[Int]], mid: Int): (List[List[Int]], List[List[Int]]) = {

    @tailrec
    def partInTwoParts(inputList: List[List[Int]], mid: Int, count: Int, s1: List[List[Int]], s2: List[List[Int]]): (List[List[Int]], List[List[Int]]) = inputList match {
      case Nil => (s1.reverse, s2.reverse)
      case head :: tail =>
        if (count <= mid) then
          partInTwoParts(tail, mid, count + 1, head :: s1, s2)
        else partInTwoParts(tail, mid, count + 1, s1, head :: s2)

    }

    partInTwoParts(inputList, mid, 1, Nil, Nil)
  }

  /**
   * Filters the points whose x-coordinate lies within distance d of the
   * dividing line, forming the "strip" of candidate points that may form
   * a closer pair across the left/right halves.
   * @param points the full sorted list of points to filter
   * @param midX the x-coordinate of the dividing line between the two halves
   * @param d the current minimum distance found so far
   * @return the points whose x-coordinate is within d of midX
   */
  def auxBuildStrip(points: List[List[Int]], midX: Int, d: Double): List[List[Int]] = {
    @tailrec
    def buildStrip(points: List[List[Int]], midX: Int, d: Double, acc: List[List[Int]]): List[List[Int]] = {
      points match {
        case Nil => acc.reverse
        case head :: tail =>
          head match {
            case x :: _ :: Nil =>
              if ((x - midX).abs < d)
                buildStrip(tail, midX, d, head :: acc)
              else
                buildStrip(tail, midX, d, acc)
          }
      }
    }

    buildStrip(points, midX, d, Nil)

  }
}