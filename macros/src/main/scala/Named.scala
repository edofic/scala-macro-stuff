import language.experimental.macros
import scala.annotation.MacroAnnotation
import scala.reflect.macros.AnnotationContext

class Named extends MacroAnnotation {
  def transform = macro Named.impl
}

object Named {
  def impl(c: AnnotationContext) = {
    import c.universe._
    c.annottee match {
      case ClassDef(mods, name, tparams, Template(parents, self, body)) =>
        val foo = DefDef(NoMods, TermName("name"), Nil, Nil, TypeTree(), Literal(Constant(name.toString)))
        ClassDef(mods, name, tparams, Template(parents, self, body :+ foo))
    }
  }
}
