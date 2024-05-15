fun gcd(a: Int, b: Int): Int {
  var a = a
  var b = b
  while (b != 0) {
        val temp = b
        b = a % b
        a = temp
    }
    return a
}

fun main() {
	val (str1, str2) = readLine()!!.split(" ")
  var a = str1.toInt()
  var b = str2.toInt()
  println(gcd(a, b))
}
