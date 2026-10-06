# 1.3 Complejidad y ecuación de recurrencia

## 1.3.1 Notación

- $n$: tamaño de la lista.
- $T(n)$: costo de `aux(n, lst)`. Como `numberOfInversions` solo desempaqueta la tupla, $T_{\text{numberOfInversions}}(n) = T(n) + O(1)$.
- Cada operación elemental (comparación, `::`, suma, pattern matching) cuesta una constante $c$.

## 1.3.2 Costo de los métodos auxiliares

| Método | Descripción | Costo |
|---|---|---|
| `length` | Recorre toda la lista | $\Theta(m)$ para una lista de tamaño $m$ |
| `split(lst, k)` | Una llamada recursiva por elemento, hasta $k$ pasos | $\Theta(k)$; con $k = n/2$ queda $\Theta(n)$ |
| `merge` | Cada llamada consume un elemento de `left` o `right` | Ver abajo |

**`split`:**

$$S(k) = S(k-1) + c, \qquad S(0) = c \;\Rightarrow\; S(k) = \Theta(k)$$

**`merge`:** sean $m = |left|$ y $r = |right|$. Hay a lo sumo $m + r + 1$ llamadas, es decir $\Theta(n)$. Sin embargo, la rama `h1 > h2` evalúa `left.length`, que es $\Theta(|left|)$, cada vez que se ejecuta:

- **Mejor caso** (lista ordenada): siempre `h1 <= h2`, nunca se llama `length`.

$$M(n) = c\,n = \Theta(n)$$

- **Peor caso** (lista en orden inverso): hay $n/2$ llamadas por la rama `h1 > h2`, cada una con un `length` de $n/2$.

$$M(n) = \frac{n}{2}\cdot\frac{n}{2} + n = \frac{n^2}{4} + n = \Theta(n^2)$$

## 1.3.3 Ecuación de recurrencia de `aux`

Cada llamada con $n > 1$ ejecuta `split` $O(n)$, dos `length` $O(n)$, dos llamadas recursivas sobre mitades y un `merge`:

$$
T(n) =
\begin{cases}
2\*T\left(\dfrac{n}{2}\right) + n_1 + M(n) & n > 1
\end{cases}
$$

### Mejor caso: $M(n) = \Theta(n)$

$$T(n) = 2T\left(\frac{n}{2}\right) + n$$

**Teorema maestro:** $a = 2$, $b = 2$, $f(n) = n$ y $n^{\log_2 2} = n$. Como $f(n) = \Theta\!\left(n^{\log_b a}\right)$, aplica el caso 2:

$$T(n) = \Theta(n \log n)$$

**Árbol de recursión:** hay $\log_2 n$ niveles y cada nivel cuesta $n$ (el nivel $i$ tiene $2^i$ subproblemas de tamaño $n/2^i$), luego $T(n) = n\log_2 n$.

### Peor caso: $M(n) = \Theta(n^2)$

$$T(n) = 2T\!\left(\frac{n}{2}\right) + \frac{n^2}{4} + n$$

**Teorema maestro:** $a = 2$, $b = 2$, $f(n) = \Theta(n^2)$ y $n^{\log_2 2} = n$. Como $f(n) = \Omega\!\left(n^{1+\varepsilon}\right)$ con $\varepsilon = 1$, aplica el caso 3. Condición de regularidad:

$$a\,f(n/b) = 2\left(\frac{n}{2}\right)^2 = \frac{n^2}{2} \le k\,n^2 \quad\text{con } k = \tfrac{1}{2} < 1 \;\checkmark$$

$$T(n) = \Theta(n^2)$$

**Por expansión:**

$$
T(n) = \sum_{i=0}^{\log_2 n} 2^i \cdot \frac{(n/2^i)^2}{4}
= \frac{n^2}{4}\sum_{i=0}^{\log_2 n}\frac{1}{2^i}
\le \frac{n^2}{4}\cdot 2 = \frac{n^2}{2}
$$

Es una serie geométrica, así que el nivel raíz domina y $T(n) = \Theta(n^2)$.

## 1.3.4 Resumen de complejidad temporal

| Caso | Recurrencia | Complejidad |
|---|---|---|
| Mejor (lista ordenada) | $T(n) = 2T(n/2) + n$ | $\Theta(n \log n)$ |
| Peor (lista inversa) | $T(n) = 2T(n/2) + n^2/4 + n$ | $\Theta(n^2)$ |

## 1.3.5 Complejidad espacial

- `aux` tiene profundidad de recursión $\log_2 n$.
- `split` y `merge` **no son recursivas de cola**: la pila puede crecer hasta $O(n)$.
- Las listas intermedias ocupan $O(n)$ por nivel y se liberan al subir.

$$\text{Espacio} = \Theta(n)$$

## 1.3.6 Observación y versión optimizada

El cálculo de `left.length` dentro de `merge` impide garantizar $O(n \log n)$. Si se pasa la longitud de `left` como parámetro y se decrementa al consumir un elemento de la izquierda (`counter + lenLeft`), entonces $M(n) = \Theta(n)$ en todos los casos y:

$$T(n) = 2T\left(\frac{n}{2}\right) + n \;\Rightarrow\; T(n) = \Theta(n \log n)$$

para el mejor, el peor y el caso promedio.
