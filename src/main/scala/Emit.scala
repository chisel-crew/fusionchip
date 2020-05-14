package fusion

import java.nio.file.{ Files, Paths }

import firrtl.options.TargetDirAnnotation
import firrtl.AnnotationSeq
import freechips.rocketchip.stage.{ ConfigsAnnotation, TopModuleAnnotation }
import freechips.rocketchip.system.{ RocketChipStage }
import freechips.rocketchip.diplomacy.LazyModule
import Chisel.Module
import chisel3.stage.ChiselStage

object Emit extends App {

  private val dest = System.getProperty("user.dir") + "/testbuild"
  private val path = Paths.get(dest)
  if (!Files.exists(path))
    Files.createDirectory(path)

  private def runAnnotations(cfg: String, top: String) = Seq(
    new TargetDirAnnotation(dest),
    new TopModuleAnnotation(Class.forName(top)),
    new ConfigsAnnotation(Seq(cfg))
  )
  private val cfg = new FusionConfig()

  private lazy val ldut = LazyModule(new FusionSystem()(cfg))
  private lazy val dut  = Module(ldut.module)

  val mod = new FusionSystem()(cfg)

  def emitEntity(item: String) = item match {
    case "soc" =>
      println("Running soc")

      // val stage = new RocketChipStage()
      val stage = new ChiselStage()
      // .run(runAnnotations("fusion.FusionConfig", "fusion.FusionSystem"))
      // // .andThen(chisel3.stage.ChiselGeneratorAnnotation(() => dut))
      // // .andThen(EEE.emit(Seq(chisel3.stage.ChiselGeneratorAnnotation(() => dut))))
      // .run(EEE.emit(Seq(chisel3.stage.ChiselGeneratorAnnotation(() => dut))))
      stage.run(
        Seq(
          new TargetDirAnnotation(dest),
          new TopModuleAnnotation(Class.forName("fusion.FusionSystem")),
          new ConfigsAnnotation(Seq("fusion.FusionConfig"))
          // EEE.emit(Seq(chisel3.stage.ChiselGeneratorAnnotation(() => dut)))
          // ).andThen(EEE.emit(Seq(chisel3.stage.ChiselGeneratorAnnotation(() => dut))))
        )
      )
    // stage.emitVlog(chisel3.stage.ChiselGeneratorAnnotation(() => dut))
    // stage.emitVlog(dut)

    case "core" =>
      println("Running Core")

      new RocketChipStage()
        .run(runAnnotations("freechips.rocketchip.system.DefaultConfig", "freechips.rocketchip.system.TestHarness"))

    case "vlog" =>
      println("Running vlog")
      // val stage = new RocketChipStage()
      val stage = new ChiselStage()

      // val anno = chisel3.stage.ChiselGeneratorAnnotation(() => new FusionSystem()(cfg))
      // val anno = chisel3.stage.ChiselGeneratorAnnotation(() => mod.module)

      stage.emitVerilog(
        mod.module,
        Array.empty[String],
        // // Seq.empty[AnnotationSeq],
        stage.run(runAnnotations("fusion.FusionConfig", "fusion.FusionSystem"))
        // EEE.emit(Seq(chisel3.stage.ChiselGeneratorAnnotation(() => dut)))
      )

    case _ => new RuntimeException("Invalid entity provided")
  }

  emitEntity("vlog")
}

object EEE {
  private lazy val stage = new chisel3.stage.ChiselStage

  def emit(ann: AnnotationSeq) = stage.execute((Array("-X", "verilog")), ann)
}
