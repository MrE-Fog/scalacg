package tests.tca

import ca.uwaterloo.scalacg.annotation.target
import ca.uwaterloo.scalacg.annotation.invocations


/**
 * A test case that represents the use of infix notation in the phantm benchmark.
 */
object Infix {

  def main(args: Array[String]) {
    val sum = { "One.+"; One } + { "Number.+"; Two } + Three + Four
    println(sum.v)
  }

  object One extends Number(1) {
    @target("One.+")
    @invocations("21: <unannotated> tests.tca.Infix.Number: <init>(v: Int)", "21: <unannotated> tests.tca.Infix.Number: v()")
    override def +(n: Number): Number = {
      new Number(v + 10)
    }
  }
  object Two extends Number(2) {
  }
  object Three extends Number(3) {
  }
  object Four extends Number(4) {
  }

  class Number(val v: Int) {
    @invocations("35: <unannotated> tests.tca.Infix.Number: <init>(v: Int)", "35: <unannotated> tests.tca.Infix.Number: v()")
    @target("Number.+") 
    def +(n: Number): Number = {
      new Number(v + n.v)
    }
  }
}