package tests.tca

import callgraph.annotation.target
import callgraph.annotation.invocations
import callgraph.annotation.notreachable

object GetterMethod1 {

  class Emitter {

    /**
     * Emit `any`.
     */
    @notreachable
    def emit (any : Any) {
        print (any.toString)
    }

    /**
     * Emit `any` and start a new line.
     */
    @target("emitln") def emitln (any : Any) {
        println (any.toString)
    }

    /**
     * Emit a new line.
     */
    @notreachable
    def emitln() {
        println()
    }

}
  
  trait StdoutEmitter {
     val emitter = new Emitter
  }
  
  object Foo extends StdoutEmitter {
    
    @invocations("44: <unannotated> tests.tca.GetterMethod1.StdoutEmitter: emitter()")
    @target("bar") def bar() {
       {"emitln"; this.emitter}.emitln("foo")
    }
  }
  
  def main(args: Array[String]) {
    {"bar"; Foo}.bar()
  }

}