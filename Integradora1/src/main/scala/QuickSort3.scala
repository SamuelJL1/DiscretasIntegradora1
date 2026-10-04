class QuickSort3 {
  
  def separar3(inputList: List[Int], p: Int, menoresAcc: List[Int], igualesAcc: List[Int], mayoresAcc: List[Int]): (List[Int], List[Int], List[Int]) = inputList match {
    case Nil =>
      (menoresAcc.reverse, igualesAcc.reverse, mayoresAcc.reverse)
    case head :: tail =>
      if (head < p)
        separar3(tail, p, head :: menoresAcc, igualesAcc, mayoresAcc)
      else if (head == p)
        separar3(tail, p, menoresAcc, head :: igualesAcc, mayoresAcc)
      else
        separar3(tail, p, menoresAcc, igualesAcc, head :: mayoresAcc)
  }

  def quickSort3(inputList: List[Int]): List[Int] = inputList match {
    case Nil => Nil
    case head :: Nil => List(head)
    case head :: tail =>
      val (menoresList, igualesList, mayoresList) = separar3(tail, head, Nil, List(head), Nil)
      quickSort3(menoresList) ::: igualesList ::: quickSort3(mayoresList)
  }


}
