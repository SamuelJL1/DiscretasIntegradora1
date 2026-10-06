# Problema 2 — Improving Quick Sort

La solución está en `src/main/scala/QuickSort.scala`. La función principal es `quickSort3`, que usa la cabeza de la lista como pivote y la partición de 3 vías `partition3` (`< x`, `= x`, `> x`). También está `quickSort2` con la partición clásica de 2 vías para poder comparar.

```scala
@tailrec
def partition3(list: List[Int], pivot: Int, less: List[Int], equal: List[Int], greater: List[Int]): (List[Int], List[Int], List[Int]) =
  list match {
    case Nil => (less, equal, greater)
    case head :: tail =>
      if head < pivot then partition3(tail, pivot, head :: less, equal, greater)
      else if head == pivot then partition3(tail, pivot, less, head :: equal, greater)
      else partition3(tail, pivot, less, equal, head :: greater)
  }

def quickSort3(list: List[Int]): List[Int] = list match {
  case Nil => Nil
  case _ :: Nil => list
  case pivot :: tail =>
    val (less, equal, greater) = partition3(tail, pivot, Nil, List(pivot), Nil)
    append(quickSort3(less), append(equal, quickSort3(greater)))
}
```

## 1. Diseño de pruebas

Las pruebas están en `src/test/scala/QuickSortSuite.scala`.

| Función | Escenario | Entrada | Salida esperada |
|---|---|---|---|
| `partition3` | elementos menores, iguales y mayores | `[2,8,1,3,5]`, pivote 5, `equal = [5]` | `([3,1,2], [5,5], [8])` |
| `partition3` | todos iguales al pivote | `[4,4]`, pivote 4, `equal = [4]` | `(Nil, [4,4,4], Nil)` |
| `quickSort3` | ejemplos del enunciado | `[2,3,9,2,2]` / `[4,4,1,9,4,1,4,4]` | `[2,2,2,3,9]` / `[1,1,4,4,4,4,4,9]` |
| `quickSort3` | lista vacía y un elemento | `Nil` / `[42]` | `Nil` / `[42]` |
| `quickSort3` | ordenada y en orden inverso | `[1,2,3,4,5]` / `[5,4,3,2,1]` | `[1,2,3,4,5]` |
| `quickSort3` | negativos y repetidos | `[0,-3,7,-3,0,2,-10]` | `[-10,-3,-3,0,0,2,7]` |
| `quickSort3` | muchos repetidos (lista grande) | 100 000 elementos con 3 valores distintos; 200 000 iguales | lista ordenada con los mismos elementos |
| `quickSort3` vs `quickSort2` | mismo resultado | 5 000 elementos mezclados | las dos dan la misma lista ordenada |

Las particiones devuelven cada parte al revés del orden de entrada porque los acumuladores agregan por la cabeza. Eso no afecta el resultado final.
