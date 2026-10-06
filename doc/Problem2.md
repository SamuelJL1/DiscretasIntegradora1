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

## 2. Prueba de correctitud (inducción estructural)

`ms(xs)` es el multiconjunto de los elementos de `xs` y `++` es la concatenación.

### Lema — `partition3`

**P(xs):** para cualesquiera `L, E, G`, si `partition3(xs, p, L, E, G) = (L', E', G')`, entonces `L'` tiene los elementos de `L` más los de `xs` que son `< p`, `E'` los de `E` más los `= p`, y `G'` los de `G` más los `> p`.

- **Caso base (`xs = Nil`):** devuelve `(L, E, G)`. Como `Nil` no tiene elementos, no se agrega nada. Se cumple.
- **Hipótesis inductiva:** `P(t)` se cumple para cualesquiera `L, E, G`.
- **Paso inductivo (`xs = h :: t`):** si `h < p`, la función devuelve `partition3(t, p, h :: L, E, G)`. Por la hipótesis, `L'` tiene los elementos de `h :: L` más los de `t` menores que `p`, que son justo los de `L` más los de `h :: t` menores que `p`. `E'` y `G'` no cambian porque `h` no es `= p` ni `> p`. Los casos `h = p` y `h > p` son iguales, pero con `E` y `G`. ✔
### Teorema — `quickSort3` ordena

**Q(xs):** `quickSort3(xs)` está ordenada de forma creciente y `ms(quickSort3(xs)) = ms(xs)`.

Usamos inducción estructural fuerte: suponemos que `Q` vale para toda lista con menos elementos que `xs`.

- **Casos base:** `quickSort3(Nil) = Nil` y `quickSort3([x]) = [x]`. Las dos están ordenadas y tienen los mismos elementos. ✔
- **Hipótesis inductiva:** `Q(ys)` se cumple para toda lista `ys` con menos elementos que `xs`.
- **Paso inductivo (`xs = p :: t`, con `t` no vacía):**
    1. Por el lema, `partition3(t, p, Nil, List(p), Nil)` da `less` (los elementos de `t` menores que `p`), `equal` (`p` y sus copias) y `greater` (los mayores que `p`).
    2. `less` y `greater` salen de `t`, así que tienen menos elementos que `xs`. Por la hipótesis, `quickSort3(less)` y `quickSort3(greater)` están ordenadas y tienen los mismos elementos que `less` y `greater`.
    3. El resultado es `quickSort3(less) ++ equal ++ quickSort3(greater)` (`append` concatena). Está ordenado porque todo lo de la izquierda es `< p`, la mitad es solo `p` y todo lo de la derecha es `> p`. Además, `ms(less) ∪ ms(equal) ∪ ms(greater) = ms(xs)`. ✔
## 3. Complejidad

`partition3` recorre la lista una vez: `P(n) = P(n-1) + c = Θ(n)`. `append` también es lineal, así que cada llamada de `quickSort3` hace `Θ(n)` de trabajo sin contar la recursión. Si la partición deja `L` elementos en `less` y `G` en `greater`:

```
T(0) = T(1) = c0
T(n) = T(L) + T(G) + c·n
```

**Peor caso.** Como el pivote es la cabeza, el peor caso es una lista ya ordenada (o al revés) sin repetidos. Ahí siempre `L = 0` y `G = n - 1`:

```
T(n) = T(n-1) + c·n = T(n-2) + c·(n-1) + c·n = ... = c0 + c·(2 + 3 + ... + n) = Θ(n²)
```

**Mejor caso.** El pivote siempre queda en la mitad: `T(n) = 2T(n/2) + c·n`. Por el teorema maestro (`a = 2`, `b = 2`, `f(n) = Θ(n^log₂2)`, caso 2), `T(n) = Θ(n log n)`.

**Caso promedio.** La lista viene en orden aleatorio y la cabeza puede ser el k-ésimo menor con probabilidad `1/n`:

```
T(n) = (2/n)·Σ_{k=0}^{n-1} T(k) + c·n
```

Multiplicando por `n` y restando la misma ecuación para `n - 1`, queda `n·T(n) = (n+1)·T(n-1) + c·(2n-1)`. Dividiendo por `n(n+1)` y desenrollando:

```
T(n)/(n+1) ≤ T(1)/2 + 2c·(1/3 + 1/4 + ... + 1/(n+1)) = O(log n)
```

Entonces `T(n) = Θ(n log n)`.

**Efecto de la partición de 3 vías.** Con `k` valores distintos, cada llamada saca **todas** las copias del pivote, así que la recursión es como un quick sort sobre los `k` valores: `T(n) = O(n log k)` en promedio. Si todos son iguales, la primera partición deja `less` y `greater` vacías y `T(n) = c·n = Θ(n)`. Con la partición de 2 vías, las copias del pivote se van todas al mismo lado y con todos iguales queda `T(n) = T(n-1) + c·n = Θ(n²)`.

| Caso | 2 vías | 3 vías |
|---|---|---|
| Orden aleatorio, todos distintos | Θ(n log n) | Θ(n log n) |
| Muchos repetidos (`k` valores distintos) | hasta Θ(n²) | O(n log k) |
| Todos iguales | Θ(n²) | Θ(n) |
| Lista ya ordenada sin repetidos | Θ(n²) | Θ(n²) |

**Espacio.** La profundidad de la recursión es `O(log n)` en promedio y `O(n)` en el peor caso. Las listas ocupan `Θ(n)`.