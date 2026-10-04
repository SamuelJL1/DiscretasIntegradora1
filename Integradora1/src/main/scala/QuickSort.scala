//Samuel Jimenez Lasso
//Matius Montealegre
//Jorge Garcia Leon

import scala.annotation.tailrec

class QuickSort {

  def invert(lst: List[Int], result: List[Int]): List[Int] = lst match {
    case Nil => result
    case head :: tail => invert(tail, head :: result)
  }

  def menores(inputList: List[Int], p: Int): List[Int]  =
    @tailrec
    def aux(remaining: List[Int], result: List[Int]): List[Int] = remaining match {
      case Nil => result
      case head :: tail =>
        if head < p then
          aux(tail, head :: result)
        else
          aux(tail, result)
    }
    invert(aux(inputList, Nil),Nil)


  def mayores(inputList: List[Int], p: Int): List[Int] =
    @tailrec
    def aux(remaining: List[Int], result: List[Int]): List[Int] = remaining match {
      case Nil => result
      case head :: tail =>
        if head > p then
          aux(tail, head :: result)
        else
          aux(tail, result)
    }
    invert(aux(inputList, Nil), Nil)

  def appendTR(inputL1: List[Int], inputL2: List[Int]): List[Int] =
    inputL1:::inputL2


  def separar(inputList: List[Int], p: Int, menoresAcc: List[Int], igualesAcc: List[Int], mayoresAcc: List[Int]): (List[Int], List[Int], List[Int]) =
    inputList match {
      case Nil => (menoresAcc.reverse, igualesAcc.reverse, mayoresAcc.reverse)
      case head :: tail =>
        if (head < p)
          separar(tail, p, head :: menoresAcc, igualesAcc, mayoresAcc)
        else if (head == p)
          separar(tail, p, menoresAcc, head :: igualesAcc, mayoresAcc)
        else
          separar(tail, p, menoresAcc, igualesAcc, head :: mayoresAcc)
    }


  def quickSort(inputList: List[Int]): List[Int] = inputList match {
    case Nil => Nil
    case head :: Nil => List(head)
    case head :: tail =>
      val (menoresList, igualesList, mayoresList) = separar(tail, head, Nil, List(head), Nil)
      quickSort(menoresList) ::: igualesList ::: quickSort(mayoresList)
  }


}
