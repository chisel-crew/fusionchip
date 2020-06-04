package fusion

import java.nio.file.{ Files, Paths }

import firrtl.AnnotationSeq
import firrtl.options.TargetDirAnnotation
import freechips.rocketchip.stage.{ ConfigsAnnotation, TopModuleAnnotation }
import freechips.rocketchip.system.{ RocketChipStage, TestHarness, TinyConfig }

object Emit extends App {

  val dest = System.getProperty("user.dir") + "/testbuild"
  val path = Paths.get(dest)
  if (!Files.exists(path))
    Files.createDirectory(path)

  val entity = "core" // {"soc", "core", "vlog"}

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
        .run(runAnnotations("freechips.rocketchip.system.TinyConfig", "freechips.rocketchip.system.TestHarness"))

    case "vlog" =>
      println("Running vlog")

      // val cfg  = new TinyConfig()
      // val ldut = LazyModule(new FusionSystem()(cfg))
      // val dut  = Module(ldut)

      val cfg = new TinyConfig()
      val circuit = Seq(
        chisel3.stage.ChiselGeneratorAnnotation(() => new TestHarness()(cfg))
      )

      ChiselEmiter.emit("testbuild", circuit)

    case _ => new RuntimeException("Invalid entity provided")
  }

  emitEntity(entity)
}

object ChiselEmiter {
  private lazy val stage = new chisel3.stage.ChiselStage

  def emit(path: String, ann: AnnotationSeq) = stage.execute((Array("-td", path, "-X", "verilog")), ann)
}
