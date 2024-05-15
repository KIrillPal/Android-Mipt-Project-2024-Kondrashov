class TreeNode<T : Comparable<T>>(var value: T) {
    var left: TreeNode<T>? = null
    var right: TreeNode<T>? = null

    fun preOrderTraversal() {
      println(value)
      left?.let { node -> node.preOrderTraversal() }
      right?.let { node -> node.preOrderTraversal() }
    }
    
    fun insert(x: T) {
      if (x <= value) 
        if (left != null)
          left!!.insert(x)
        else
          left = TreeNode<T>(x)
        else if (right != null)
          right!!.insert(x)
        else
          right = TreeNode<T>(x)
      
    }
}



fun main() {
  val n = readLine()!!.toInt()
  val root = readLine()!!.toLong()
  var tree = TreeNode<Long>(root)
  
  repeat(n-1) {
    val v = readLine()!!.toLong()
    tree.insert(v)
  }
  
  tree.preOrderTraversal()
}
