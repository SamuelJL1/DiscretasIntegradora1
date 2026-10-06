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
              val newBest =  if (distFromHead < best) distFromHead else best
              bruteForce(tail, newBest)
          }
        }

    inputList match {
      case Nil => Double.MaxValue
      case _ =>
        bruteForce(inputList,Double.MaxValue)
    }
  }
  }



