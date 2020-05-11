package fusion

import freechips.rocketchip.system.Generator

object MainGen {
  val circ = Generator.main(Array("out/", "Fusion", "FusionSystem", "FusionConfig"))
}
