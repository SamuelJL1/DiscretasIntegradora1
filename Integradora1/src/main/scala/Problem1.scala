class Problem1 {
  def numberOfInversions(n: Int, lst: List[Int]): Int =
    val (p, inversions) = aux(n, lst)
    inversions
    
    
  def aux(n: Int, lst: List[Int]): (List[Int], Int) =
    if n <= 1 then
      (lst, 0)
    else
      val (left, right) = split(lst, n / 2)
      val (sortedLeft, invLeft) = aux(left.length, left)
      val (sortedRight, invRight) = aux(right.length, right)
      merge(sortedLeft, sortedRight, invLeft + invRight)



  def split(lst: List[Int], n: Int): (List[Int], List[Int]) =
    if n == 0 then
      (Nil, lst)
    else
      lst match {
        case Nil => (Nil, Nil)
        case head :: tail =>
          val (left, right) = split(tail, n - 1)
          (head :: left, right)
      }

  def merge(left: List[Int], right: List[Int], counter: Int): (List[Int], Int) =
    (left, right) match {
      case (Nil, n) => (right, counter)
      case (p, Nil) => (left, counter)
      case (h1 :: t1, h2 :: t2) =>
        if h1 <= h2 then
          val (list, count) = merge(t1, right, counter)
          (h1 :: list, count)
        else
          val (list, count) = merge(left, t2, counter + left.length)
          (h2 :: list, count)
    }
}
