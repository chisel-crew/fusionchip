package fusion

import freechips.rocketchip.system.Generator

object MainGen extends App {
  Generator.main(Array("out/", "Fusion", "FusionSystem", "Fusion", "FusionConfig"))
}
