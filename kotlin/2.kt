fun flatmatrix_mult(m1: Array<Long>, m2: Array<Long>, mod: Long): Array<Long> {
  return Array<Long>(4){i -> 
    ((m1[2 * (i / 2)] * m2[i % 2]) % mod + (m1[2 * (i / 2) + 1] * m2[2 + i % 2]) % mod) % mod
  }
}

fun binpow_fib(fib: Array<Long>, pow: Long, mod: Long): Array<Long> {
  if (pow == 0L)
    return arrayOf(1, 0, 0, 1)
  var b = binpow_fib(fib, pow / 2, mod)
  b = flatmatrix_mult(b, b, mod)
  if (pow % 2 != 0L)
    b = flatmatrix_mult(b, arrayOf(1, 1, 1, 0), mod)
  return b
}

fun fibbonacci(n: Long, mod: Long): Long {
  val matrix = binpow_fib(arrayOf(1, 0, 0, 1), n-1, mod)
  //println("[" + fib.joinToString(", ") + "]")
  return matrix[0]
}

fun main() {
	val (str1, str2) = readLine()!!.split(" ")
  var n = str1.toLong()
  var m = str2.toLong()
  println(fibbonacci(n, m))
}
