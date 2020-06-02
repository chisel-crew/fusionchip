package fusion

import java.nio.file.{ Files, Paths }

import Chisel.Module
import firrtl.AnnotationSeq
import firrtl.options.TargetDirAnnotation
import freechips.rocketchip.diplomacy.LazyModule
import freechips.rocketchip.stage.{ ConfigsAnnotation, TopModuleAnnotation }
import freechips.rocketchip.system.{ RocketChipStage }

object Emit extends App {

  val dest = System.getProperty("user.dir") + "/testbuild"
  val path = Paths.get(dest)
  if (!Files.exists(path))
    Files.createDirectory(path)

  val entity = "soc" // {"soc", "core", "vlog"}

  private def runAnnotations(cfg: String, top: String) = Seq(
    new TargetDirAnnotation(dest),
    new TopModuleAnnotation(Class.forName(top)),
    new ConfigsAnnotation(Seq(cfg))
  )

  def emitEntity(item: String) = item match {
    case "soc" =>
      new RocketChipStage().run(runAnnotations("fusion.FusionConfig", "fusion.FusionSystem"))

    case "core" =>
      new RocketChipStage()
        .run(runAnnotations("freechips.rocketchip.system.DefaultConfig", "freechips.rocketchip.system.TestHarness"))

    case "vlog" =>
      println("Running vlog")
      new RocketChipStage()
      val cfg       = new FusionConfig()
      lazy val ldut = LazyModule(new FusionSystem()(cfg))
      Module(ldut.module)

      println(">>>>>>>>>>")
      //stage.emitVerilog(
      //  dut,
      //  Array.empty[String],
      //  // Seq.empty[AnnotationSeq],
      //  stage.run(runAnnotations("fusion.FusionConfig", "fusion.FusionSystem"))
      //  // EEE.emit("/testbuild", Seq(chisel3.stage.ChiselGeneratorAnnotation(() => dut)))
      //)

    case _ => new RuntimeException("Invalid entity provided")
  }

  emitEntity(entity)
}

object EEE {
  private lazy val stage = new chisel3.stage.ChiselStage

  def emit(path: String, ann: AnnotationSeq) = stage.execute((Array("-td", path, "-X", "verilog")), ann)
}
