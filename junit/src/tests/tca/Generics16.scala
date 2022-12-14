package tests.tca

import ca.uwaterloo.scalacg.annotation.reachable
import ca.uwaterloo.scalacg.annotation.target

object Generics16 {

  def main(args: Array[String]): Unit = {
    val v = new SCC[Int];
    { "foo"; v}.foo
  }
  
  class SCC[S] {
    @reachable
    @target("foo")
    def foo = "foo"
  }
}