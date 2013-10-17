package tests.ra

import ca.uwaterloo.scalacg.annotation.target

object ThisType1 {
  trait A {
    @target("A.foo")
    def foo()
    @target("A.bar") def bar() {
      val x: this.type = this;
      { "A.foo"; "B.foo"; "C.foo"; x }.foo(); // can only call B.foo() because that is the only type with a def. of foo() that can have A.bar() as a member
    }
  }

  class B extends A {
    @target("B.foo") def foo() {}
  }

  class C extends A {
    @target("C.foo") def foo() {}
    @target("C.bar") override def bar() {}
  }
  def main(args: Array[String]) {
    { "A.bar"; "C.bar"; (new B)}.bar();
    { "A.bar"; "C.bar"; (new C)}.bar()
  }
} 