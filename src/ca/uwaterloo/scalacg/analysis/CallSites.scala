package ca.uwaterloo.scalacg.analysis

import ca.uwaterloo.scalacg.config.Global

trait CallSites extends Global {
  import global._

  class AbstractCallSite(receiverTree: Tree, val staticTarget: Symbol) {
    // The type of the receiver.
    lazy val receiver = receiverTree.tpe

    // Is this a constructor call?
    lazy val isConstructorCall = staticTarget.isConstructor

    // Is this a super call?
    lazy val isSuperCall = hasSuperReceiver || hasSuperAccessor

    // The super receiver, if any.
    protected lazy val superReceiver = {
      receiverTree match {
        case s: Super => Some(s)
        case _ => None
      }
    }

    // Does this call site use "super" as a receiver?
    lazy val hasSuperReceiver = superReceiver.isDefined

    // The "this" receiver, if any.
    protected lazy val thisReceiver = {
      receiverTree match {
        case t: This => Some(t.symbol)
        case t => t.tpe match {
          case tpe: ThisType => Some(tpe.sym)
          case _ => None
        }
      }
    }

    // Does this call site use "this" as a receiver?
    lazy val hasThisReceiver = thisReceiver.isDefined

    // Does this call site us super in this fashion super[Z]?
    lazy val hasStaticSuperReference = superReceiver.isDefined && superReceiver.get.mix.nonEmpty

    // Does this call site has a "super" accessor (e.g., super$bar)?
    lazy val hasSuperAccessor = staticTarget.isSuperAccessor

    // Get the name of the static target method.
    lazy val targetName = {
      if (hasSuperAccessor) staticTarget.name drop nme.SUPER_PREFIX_STRING.length
      else staticTarget.name
    }
  }

  class CallSite(receiverTree: Tree, override val staticTarget: Symbol,
    val enclMethod: Symbol, val position: Position, val annotations: Set[String])
    extends AbstractCallSite(receiverTree, staticTarget) {

    // Get the named super (the Z in super[Z]) of this call site, if any
    lazy val staticSuperReference = {
      if (hasStaticSuperReference) {
        val superClass = receiver.baseClasses find (_.name.toTypeName == superReceiver.get.mix)
        if (superClass.isDefined) superClass.get.tpe else NoType
      } else NoType
    }

    // If the receiver is a This, then thisEnclMethod is the method whose implicit
    // this parameter is of the same class as the receiver This. Otherwise, thisEnclMethod
    // is NoSymbol.
    lazy val thisEnclMethod = {
      if (hasThisReceiver) {
        enclMethod.ownerChain.find { sym => sym.isMethod && sym.owner == thisReceiver.get }.getOrElse(NoSymbol)
      } else {
        NoSymbol
      }
    }
  }

  object CallSite {
    def apply(receiverTree: Tree, staticTarget: Symbol) =
      new AbstractCallSite(receiverTree, staticTarget)
    def apply(receiverTree: Tree, staticTarget: Symbol,
      enclMethod: Symbol, position: Position, annotations: Set[String]) =
      new CallSite(receiverTree, staticTarget, enclMethod, position, annotations)
  }
}