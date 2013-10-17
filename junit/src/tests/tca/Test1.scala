package tests.tca

import ca.uwaterloo.scalacg.annotation.target

class Test1 {
  @target("Test1.callee") def callee(): Int = {
    5
  }

  def main(args: Array[String]) = {
    "Test1.callee";
    new Test1
  }.callee()
}
