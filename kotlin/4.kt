import kotlin.math.*
typealias Line = Pair<Pair<Double, Double>, Pair<Double, Double>>

fun isBetween(x: Double, a: Double, b: Double): Boolean {
  return min(a, b) <= x && x <= max(a, b)
}

fun findIntersection(line1: Line, line2: Line): Pair<Double, Double>? {
  val (p1, p2) = line1
  val (p3, p4) = line2
  val (x1, y1) = p1
  val (x2, y2) = p2
  val (x3, y3) = p3
  val (x4, y4) = p4

  val denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)
  if (denominator == 0.0)
    return null

  val x = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / denominator
  val y = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / denominator
  
  if (isBetween(x, x1, x2) &&  isBetween(x, x3, x4) && isBetween(y, y1, y2) &&  isBetween(y, y3, y4)) {
    return Pair(x, y)
  }
  return null
}


fun parseLine(): Line {
  @Suppress("UNCHECKED_CAST")
  val row = readLine()!!.split(" ")
  val p1 = Pair(row[0].toDouble(), row[1].toDouble())
  val p2 = Pair(row[2].toDouble(), row[3].toDouble())
  return Pair(p1, p2)
}



fun main() {
  val road = parseLine()
  val n = readLine()!!.toInt()
  val rivers = Array<Line>(n) {parseLine()}
  val intersections = Array<Pair<Double, Double>?>(n) {i -> findIntersection(road, rivers[i])}
  /*for (p in intersections) {
    println(p)
  }*/
  val bridge_points = intersections.filterNotNull()
  print(bridge_points.size)
}
