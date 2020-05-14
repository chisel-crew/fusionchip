package fusion

import java.nio.file.{ Files, Paths }

import firrtl.options.TargetDirAnnotation
import freechips.rocketchip.stage.{ ConfigsAnnotation, TopModuleAnnotation }
import freechips.rocketchip.system.{ RocketChipStage }

object EmitSoC extends App {

  val dest = System.getProperty("user.dir") + "/testbuild"
  val path = Paths.get(dest)
  if (!Files.exists(path))
    Files.createDirectory(path)

  val entity = "soc" // {"soc", "core"}

  def emitEntity(item: String) = item match {
    case "soc" =>
      new RocketChipStage().run(
        Seq(
          new TargetDirAnnotation(dest),
          new TopModuleAnnotation(Class.forName("fusion.FusionSystem")),
          new ConfigsAnnotation(Seq("fusion.FusionConfig"))
        )
      )
    case "core" =>
      new RocketChipStage().run(
        Seq(
          new TargetDirAnnotation(dest),
          new TopModuleAnnotation(Class.forName("freechips.rocketchip.system.TestHarness")),
          new ConfigsAnnotation(Seq("freechips.rocketchip.system.DefaultConfig"))
        )
      )

    case _ => new RuntimeException("Invalid entity provided")
  }
}
