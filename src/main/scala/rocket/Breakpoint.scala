// See LICENSE.SiFive for license details.

package freechips.rocketchip.rocket

import Chisel.ImplicitConversions._
import chisel3._
import chisel3.util.{Cat}
import freechips.rocketchip.config.Parameters
import freechips.rocketchip.tile.{CoreModule, CoreBundle, HasCoreParameters}
import freechips.rocketchip.util._

class BPControl(implicit p: Parameters) extends CoreBundle()(p) {
  val ttype = UInt(4.W)
  val dmode = Bool()
  val maskmax = UInt(6.W)
  val reserved = UInt((xLen - (if (coreParams.useBPWatch) 26 else 24)).W)
  val action = UInt((if (coreParams.useBPWatch) 3 else 1).W)
  val chain = Bool()
  val zero = UInt(2.W)
  val tmatch = UInt(2.W)
  val m = Bool()
  val h = Bool()
  val s = Bool()
  val u = Bool()
  val x = Bool()
  val w = Bool()
  val r = Bool()

  def tType = 2
  def maskMax = 4
  def enabled(mstatus: MStatus) = !mstatus.debug && Cat(m, h, s, u)(mstatus.prv)
}

class BP(implicit p: Parameters) extends CoreBundle()(p) {
  val control = new BPControl
  val address = UInt(vaddrBits.W)

  def mask(dummy: Int = 0) =
    (0 until control.maskMax-1).scanLeft(control.tmatch(0))((m, i) => m && address(i)).asUInt

  def pow2AddressMatch(x: UInt) =
    (~x | mask()) === (~address | mask())

  def rangeAddressMatch(x: UInt) =
    (x >= address) ^ control.tmatch(0)

  def addressMatch(x: UInt) =
    Mux(control.tmatch(1), rangeAddressMatch(x), pow2AddressMatch(x))
}

class BPWatch (val n: Int) extends Bundle() {
  val valid = Vec(n, Bool())
  val rvalid = Vec(n, Bool())
  val wvalid = Vec(n, Bool())
  val ivalid = Vec(n, Bool())
  val action = UInt(3.W)
}

class BreakpointUnit(n: Int)(implicit val p: Parameters) extends Module with HasCoreParameters {
  val io = IO(new Bundle {
    val status = Input(new MStatus())
    val bp = Input(Vec(n, new BP))
    val pc = Input(UInt(vaddrBits.W))
    val ea = Input(UInt(vaddrBits.W))
    val xcpt_if  = Output(Bool())
    val xcpt_ld  = Output(Bool())
    val xcpt_st  = Output(Bool())
    val debug_if = Output(Bool())
    val debug_ld = Output(Bool())
    val debug_st = Output(Bool())
    val bpwatch  = Output(Vec(n, new BPWatch(1)))
  })

  io.xcpt_if := false
  io.xcpt_ld := false
  io.xcpt_st := false
  io.debug_if := false
  io.debug_ld := false
  io.debug_st := false

  (io.bpwatch zip io.bp).foldLeft((true.B, true.B, true.B)) { case ((ri, wi, xi), (bpw, bp)) =>
    val en = bp.control.enabled(io.status)
    val r = en && bp.control.r && bp.addressMatch(io.ea)
    val w = en && bp.control.w && bp.addressMatch(io.ea)
    val x = en && bp.control.x && bp.addressMatch(io.pc)
    val end = !bp.control.chain
    val action = bp.control.action

    bpw.action := action
    bpw.valid(0) := false.B
    bpw.rvalid(0) := false.B
    bpw.wvalid(0) := false.B
    bpw.ivalid(0) := false.B

    when (end && r && ri) { io.xcpt_ld := (action === 0.U); io.debug_ld := (action === 1.U); bpw.valid(0) := true.B; bpw.rvalid(0) := true.B }
    when (end && w && wi) { io.xcpt_st := (action === 0.U); io.debug_st := (action === 1.U); bpw.valid(0) := true.B; bpw.wvalid(0) := true.B }
    when (end && x && xi) { io.xcpt_if := (action === 0.U); io.debug_if := (action === 1.U); bpw.valid(0) := true.B; bpw.ivalid(0) := true.B }

    (end || r, end || w, end || x)
  }
}
