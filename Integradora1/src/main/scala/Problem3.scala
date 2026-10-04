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


}