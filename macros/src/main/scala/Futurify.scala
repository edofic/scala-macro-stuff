import language.experimental.macros
import scala.annotation.MacroAnnotation
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.reflect.macros.AnnotationContext

class Futurify extends MacroAnnotation {
  def transform = macro Futurify.nonBlocking
}

class FuturifyBlocking extends MacroAnnotation {
  def transform = macro Futurify.blocking
}

object Futurify {
  private def impl(c: AnnotationContext, block: Boolean) = {
    import c.universe._

    val ignoreNames =  Seq("<init>")

    val future = typeOf[Future[Any]]

    val fixBody: Tree => Tree = {
      case d@DefDef(mods, name, tparams, vparamss, tpt, rhsRaw) if !(ignoreNames contains name.toString) =>
        val rhs = c.typeCheck(rhsRaw)
        val fixedRhs = if(rhs.tpe <:< future){
          rhs
        } else {
          val re = c.Expr[Any](rhs)
          val exp = if(block){
            reify{Future.successful(re.splice)}
          } else {
            reify{Future(re.splice)(global)}
          }
          exp.tree
        }
        DefDef(mods, name, tparams, vparamss, TypeTree(), fixedRhs)
      case other => other
    }

    c.annottee match {
      case ClassDef(mods, name, tparams, Template(parents, self, body)) =>
        ClassDef(mods, name, tparams, Template(parents, self, body map fixBody))
    }
  }

  def blocking(c: AnnotationContext) = impl(c, true)
  def nonBlocking(c: AnnotationContext) = impl(c, false)
}
