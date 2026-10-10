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

Las listas de Scala están definidas recursivamente:

- **Paso base:** `Nil` es una lista.
- **Paso recursivo:** si `t` es una lista y `h` es un entero, entonces `h :: t` es una lista.
  Por eso las demostraciones se hacen por inducción estructural sobre las listas: primero se prueba la propiedad para `Nil` y luego se prueba que, si vale para `t`, también vale para `h :: t`.

### Demostración 1: `partition3` clasifica bien los elementos

Sea `P(xs)` la propiedad que afirma que, para cualquier pivote `p` y cualesquiera acumuladores `L`, `E`, `G`, si `partition3(xs, p, L, E, G) = (L', E', G')`, entonces:

- `L'` tiene los elementos de `L` más los elementos de `xs` que son menores que `p`,
- `E'` tiene los elementos de `E` más los elementos de `xs` que son iguales a `p`,
- `G'` tiene los elementos de `G` más los elementos de `xs` que son mayores que `p`.

  **Paso base:** se debe demostrar que `P(Nil)` es verdadera. Por la definición de `partition3`, `partition3(Nil, p, L, E, G) = (L, E, G)`. Como `Nil` no tiene elementos, no hay nada que agregar a `L`, `E` ni `G`, así que `L' = L`, `E' = E` y `G' = G`. Por lo tanto, `P(Nil)` es verdadera.

**Paso recursivo:** se supone que `P(t)` es verdadera para cualesquiera `L`, `E`, `G` (hipótesis inductiva). Se debe demostrar que esto implica que `P(h :: t)` es verdadera para todo entero `h`. Por la definición de `partition3` hay tres casos:

- Si `h < p`: `partition3(h :: t, p, L, E, G) = partition3(t, p, h :: L, E, G)`. Por la hipótesis inductiva (con el acumulador `h :: L`), `L'` tiene los elementos de `h :: L` más los menores que `p` de `t`. Eso es lo mismo que los elementos de `L` más los menores que `p` de `h :: t`, porque `h` es menor que `p`. `E'` y `G'` quedan igual que en la hipótesis, lo cual es correcto porque `h` no es igual ni mayor que `p`.
- Si `h = p`: `partition3(h :: t, p, L, E, G) = partition3(t, p, L, h :: E, G)`. Por la hipótesis inductiva, `E'` tiene los elementos de `E`, más `h`, más los iguales a `p` de `t`, que son justo los iguales a `p` de `h :: t`. `L'` y `G'` no cambian.
- Si `h > p`: `partition3(h :: t, p, L, E, G) = partition3(t, p, L, E, h :: G)`. Es igual al caso anterior, pero `h` entra en `G'`.
  En los tres casos `P(h :: t)` es verdadera, lo cual concluye la demostración. Además, como los tres casos no se cruzan, cada elemento queda en una sola parte y no se pierde ninguno.

### Demostración 2: `quickSort3` ordena la lista

Sea `Q(xs)` la propiedad que afirma que `quickSort3(xs)` es una lista ordenada de forma creciente y que tiene exactamente los mismos elementos que `xs` (con las mismas repeticiones).

**Paso base:** se debe demostrar que `Q(Nil)` es verdadera. Por la definición, `quickSort3(Nil) = Nil`, que está ordenada y tiene los mismos elementos que `Nil`. Por lo tanto, `Q(Nil)` es verdadera. Igual pasa con una lista de un solo elemento: `quickSort3(x :: Nil) = x :: Nil`, que también está ordenada.

**Paso recursivo:** sea `xs = p :: t`, con `t` no vacía. Como `quickSort3` no se llama sobre `t` sino sobre las partes `less` y `greater`, que salen de `t`, se supone que `Q` es verdadera para todas las listas con menos elementos que `p :: t` (hipótesis inductiva). Se debe demostrar que `Q(p :: t)` es verdadera.

Por la definición, `quickSort3(p :: t)` hace `partition3(t, p, Nil, List(p), Nil) = (less, equal, greater)` y devuelve `quickSort3(less) ++ equal ++ quickSort3(greater)`.

1. Por la Demostración 1 (con `L = Nil`, `E = List(p)`, `G = Nil`): `less` tiene los elementos de `t` menores que `p`, `equal` tiene a `p` y los elementos de `t` iguales a `p`, y `greater` tiene los elementos de `t` mayores que `p`.
2. `less` y `greater` solo tienen elementos de `t`, así que tienen menos elementos que `p :: t`. Por la hipótesis inductiva, `quickSort3(less)` está ordenada y tiene los mismos elementos que `less`, y lo mismo pasa con `quickSort3(greater)` y `greater`.
3. El resultado está ordenado: todo lo de `quickSort3(less)` es menor que `p`, todo lo de `equal` es igual a `p` y todo lo de `quickSort3(greater)` es mayor que `p`, y cada parte ya está ordenada.
4. El resultado tiene los mismos elementos que `p :: t`: entre `less`, `equal` y `greater` están todos los elementos de `t` más `p`, sin perder ni repetir ninguno (Demostración 1).
   Por lo tanto `Q(p :: t)` es verdadera, lo cual concluye la demostración.

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