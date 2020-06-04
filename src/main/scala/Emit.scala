package fusion

import java.nio.file.{ Files, Paths }

import firrtl.AnnotationSeq
import firrtl.options.TargetDirAnnotation
import freechips.rocketchip.stage.{ ConfigsAnnotation, TopModuleAnnotation }
import freechips.rocketchip.system.RocketChipStage

object Emit extends App {

  val dest = System.getProperty("user.dir") + "/testbuild"
  val path = Paths.get(dest)
  if (!Files.exists(path))
    Files.createDirectory(path)

  val entity = "vlog" // {"soc", "core", "vlog"}

  private def runAnnotations(cfg: String, top: String) = Seq(
    new TargetDirAnnotation(dest),
    new TopModuleAnnotation(Class.forName(top)),
    new ConfigsAnnotation(Seq(cfg))
  )

  def emitEntity(item: String) = item match {

    case "soc" =>
      println(">>>> Emiting soc")
      new RocketChipStage().run(runAnnotations("fusion.FusionConfig", "fusion.FusionSystem"))

    case "core" =>
      println(">>>> Emiting core")
      new RocketChipStage()
        .run(runAnnotations("freechips.rocketchip.system.TinyConfig", "freechips.rocketchip.system.TestHarness"))

    case "vlog" =>
      println(">>>> Emiting vlog")

      VlogEmiter.emit(
        s"$dest/freechips.rocketchip.system.TinyConfig.fir",
        runAnnotations("freechips.rocketchip.system.TinyConfig", "freechips.rocketchip.system.TestHarness")
      )

    case _ => new RuntimeException("Invalid entity provided")
  }

  emitEntity(entity)
}

object VlogEmiter {
  private val stage = new chisel3.stage.ChiselStage

  def emit(path: String, ann: AnnotationSeq) =
    stage.execute((Array("-X", "verilog", "-i", path)), ann)
}
