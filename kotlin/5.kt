import java.util.LinkedList

class MaxStack<T : Comparable<T>>() {
  var size : Int = 0
  private val maxs = LinkedList<Pair<T, Int>>()
  
  fun push(v : T) {
    size += 1
    if (maxs.size == 0 || maxs.last.first <= v) {
      maxs.addLast(Pair(v, size))
    }
  }
  
  fun pop() {
    if (size > 0) {
      if (maxs.size > 0 && maxs.last.second == size) {
        maxs.removeLast()
      }
      size -= 1
    }
  }
  
  fun max() : T? {
    if (size == 0) 
      return null
    return maxs.last.first
  }
}


fun main() {
  val n = readLine()!!.toInt()
  
  val stack = MaxStack<Int>()
  repeat(n) {
    val command = readLine()!!.split(" ")
    when (command[0]) {
        "push" -> {
            val v = command[1].toInt()
            stack.push(v)
        }

        "pop" -> {
            stack.pop()
        }

        "max" -> println(stack.max())
    }
  }
}
