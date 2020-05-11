package fusion.emiter

import firrtl.{ AnnotationSeq }

// Generator ADT
sealed trait Form
final case object Verilog extends Form
final case object High    extends Form
final case object Low     extends Form

class Emiter() {

  private lazy val stage = new chisel3.stage.ChiselStage

  def emit(path: String, ann: AnnotationSeq, form: Form): Object = form match {
    case Verilog => stage.execute((Array("-td", path, "-X", "verilog")), ann)
    case High    => stage.execute((Array("-td", path, "-X", "high")), ann)
    case Low     => stage.execute((Array("-td", path, "-X", "low")), ann)
    case _       => new Exception("Unsuppored output format")
  }
}
