package fusion

import java.nio.file.{ Files, Paths }

import firrtl.options.TargetDirAnnotation
import firrtl.AnnotationSeq
import freechips.rocketchip.stage.{ ConfigsAnnotation, TopModuleAnnotation }
import freechips.rocketchip.system.{ RocketChipStage }
import freechips.rocketchip.diplomacy.LazyModule
import Chisel.Module

object Emit extends App {

  val dest = System.getProperty("user.dir") + "/testbuild"
  val path = Paths.get(dest)
  if (!Files.exists(path))
    Files.createDirectory(path)

  val entity = "vlog" // {"soc", "core"}

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
      val stage = new RocketChipStage()
      val cfg   = new FusionConfig()
      val ldut  = LazyModule(new FusionSystem()(cfg))
      val dut   = Module(ldut.module)

      println(">>>>>>>>>>")
      stage.emitVerilog(
        dut,
        Array.empty[String],
        // Seq.empty[AnnotationSeq],
        stage.run(runAnnotations("fusion.FusionConfig", "fusion.FusionSystem"))
        // EEE.emit("/testbuild", Seq(chisel3.stage.ChiselGeneratorAnnotation(() => dut)))
      )

    case _ => new RuntimeException("Invalid entity provided")
  }

  emitEntity("vlog")
}

object EEE {
  private lazy val stage = new chisel3.stage.ChiselStage

  def emit(path: String, ann: AnnotationSeq) = stage.execute((Array("-td", path, "-X", "verilog")), ann)
}
