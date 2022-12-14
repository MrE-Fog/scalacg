package tests.tca.matching

import ca.uwaterloo.scalacg.annotation.target
import ca.uwaterloo.scalacg.annotation.invocations

object CaseClassWildcard {

  /**
   * Testing pattern matching on case class: wildcard matching
   */
  @invocations("17: <unannotated> tests.tca.matching.CaseClassWildcard.Lit: <init>(value: Boolean)",
               "19: <unannotated> scala.Any: asInstanceOf([T0])",
               "19: <unannotated> scala.Any: isInstanceOf([T0])", 
               "19: <unannotated> tests.tca.matching.CaseClassWildcard.Var: name()"
               )
  def main(args: Array[String]) {
    val e: Expr = Lit(value = true)
    e match {
      case Var(z) => Console.println(z)
      case _      => {"foo"; this}.foo()
    }
  }

  @target("foo") def foo() {
    Console.println("foo")
  }
  
  abstract class Expr
	case class Lit(value: Boolean) extends Expr
	case class Var(name: String) extends Expr
	case class And(left: Expr, right: Expr) extends Expr
}
