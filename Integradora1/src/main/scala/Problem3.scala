import scala.annotation.tailrec

class Problem3 {

  def sortByX(inputList: List[List[Int]]): List[List[Int]] = {

    @tailrec
    def partitionByX(inputList: List[List[Int]], pivotX: Int, lessAcc: List[List[Int]], equalAcc: List[List[Int]], greaterAcc: List[List[Int]]): (List[List[Int]], List[List[Int]], List[List[Int]]) = {
      inputList match {
        case Nil =>
          (lessAcc.reverse, equalAcc.reverse, greaterAcc.reverse)
        case head :: tail =>
          head match {
            case x :: y :: Nil =>
              if (x == pivotX)
                partitionByX(tail, pivotX, lessAcc, head :: equalAcc, greaterAcc)
              else if (x < pivotX)
                partitionByX(tail, pivotX, head :: lessAcc, equalAcc, greaterAcc)
              else
                partitionByX(tail, pivotX, lessAcc, equalAcc, head :: greaterAcc)
          }
      }
    }


    inputList match {
      case Nil => Nil
      case head :: Nil => List(head)
      case head :: tail =>
        head match {
          case pivotX :: _ :: Nil =>
            val (lessList, equalList, greaterList) = partitionByX(tail, pivotX, Nil, List(head), Nil)
            sortByX(lessList) ::: equalList ::: sortByX(greaterList)
        }
    }
  }

  def auxEuristic(inputList: List[List[Int]]): Double = {

    @tailrec
    def closestToPivot(pivotPoint: List[Int], remaining: List[List[Int]], best: Double): Double = {
      remaining match {
        case Nil => best
        case head :: tail =>
          pivotPoint match {
            case x1 :: y1 :: Nil =>
              head match {
                case x2 :: y2 :: Nil =>
                  val dist = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
                  val newBest = if (dist < best) dist else best
                  closestToPivot(pivotPoint, tail, newBest)
              }
          }
      }
    }

    @tailrec
    def bruteForce(points: List[List[Int]], best: Double): Double = {
      points match {
        case Nil => best
        case _ :: Nil => best
        case head :: tail =>
          val distFromHead = closestToPivot(head, tail, Double.MaxValue)
          val newBest = if (distFromHead < best) distFromHead else best
          bruteForce(tail, newBest)
      }
    }

    inputList match {
      case Nil => Double.MaxValue
      case _ =>
        bruteForce(inputList, Double.MaxValue)
    }
  }

  def closestPair(points: List[List[Int]]): Double = {
    val sorted = sortByX(points)

    sorted match {
      case p if p.length <= 3 =>
        auxEuristic(p)
      case _ =>
        val mid = sorted.length / 2
        val (s1, s2) = auxPartInTwoParts(sorted, mid)
        val d1 = closestPair(s1)
        val d2 = closestPair(s2)
        val d = if (d1 < d2) d1 else d2
        s2 match {
          case head :: _ =>
            head match {
              case x :: y :: Nil =>
                val strip = auxBuildStrip(sorted, x, d)
                val sortedByY = sortByY(strip)
                val d0 = auxEuristic(sortedByY)
                math.min(d, d0)
            }
  

        }
    }
  }

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

  def sortByY(inputList: List[List[Int]]): List[List[Int]] = {

    @tailrec
    def partitionByY(inputList: List[List[Int]], pivotY: Int, lessAcc: List[List[Int]], equalAcc: List[List[Int]], greaterAcc: List[List[Int]]): (List[List[Int]], List[List[Int]], List[List[Int]]) = {
      inputList match {
        case Nil =>
          (lessAcc.reverse, equalAcc.reverse, greaterAcc.reverse)
        case head :: tail =>
          head match {
            case _ :: y :: Nil => // Extraemos únicamente 'y'
              if (y == pivotY)
                partitionByY(tail, pivotY, lessAcc, head :: equalAcc, greaterAcc)
              else if (y < pivotY)
                partitionByY(tail, pivotY, head :: lessAcc, equalAcc, greaterAcc)
              else
                partitionByY(tail, pivotY, lessAcc, equalAcc, head :: greaterAcc)
          }
      }
    }

    inputList match {
      case Nil => Nil
      case head :: Nil => List(head)
      case head :: tail =>
        head match {
          case _ :: pivotY :: Nil => // Tomamos la coordenada Y de la cabeza como pivote
            val (lessList, equalList, greaterList) = partitionByY(tail, pivotY, Nil, List(head), Nil)
            sortByY(lessList) ::: equalList ::: sortByY(greaterList)
        }
    }
  }


}



