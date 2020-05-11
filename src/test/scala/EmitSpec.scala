package firtest

import Helper._
import chisel3.stage.ChiselGeneratorAnnotation
import freechips.rocketchip.groundtest.TestHarness
import freechips.rocketchip.system.TinyConfig
import fusion.emiter._

import zio.test.Assertion._
import zio.test._

object EmiterSpec extends DefaultRunnableSpec {
  def spec = suite("Emiter Spec")(
    suite("Emitter Suite")(
      test("Emit FIRRTL High") {
        emiter.emit(firHome, circuit, High)
        assert(true)(isTrue)
      },
      test("Emit Verilog") {
        emiter.emit(firHome, circuit, Verilog)
        assert(true)(isTrue)
      }
    )
  )
}

object Helper {
  val emiter: Emiter = new Emiter() {}

  val cfg = new TinyConfig()

  val circuit: Seq[ChiselGeneratorAnnotation] = Seq(
    chisel3.stage.ChiselGeneratorAnnotation(() => new TestHarness()(cfg))
  )

  val firHome = "out/"

}
