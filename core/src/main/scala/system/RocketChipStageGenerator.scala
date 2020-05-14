// See LICENSE.SiFive for license details.

package freechips.rocketchip.system

import chisel3.stage.{ ChiselCli, ChiselStage }
import firrtl.options.PhaseManager.PhaseDependency
import firrtl.options.{ Dependency, Phase, PreservesAll, Shell, StageMain }
import firrtl.stage.FirrtlCli
import freechips.rocketchip.stage.RocketChipCli
import chisel3.RawModule
import firrtl.AnnotationSeq
import chisel3.stage.ChiselGeneratorAnnotation
import firrtl.annotations.DeletedAnnotation
import firrtl.EmittedVerilogCircuitAnnotation

class RocketChipStage extends ChiselStage with PreservesAll[Phase] {

  override val shell = new Shell("rocket-chip") with RocketChipCli with ChiselCli with FirrtlCli
  override val targets: Seq[PhaseDependency] = Seq(
    Dependency[freechips.rocketchip.stage.phases.Checks],
    Dependency[freechips.rocketchip.stage.phases.TransformAnnotations],
    Dependency[freechips.rocketchip.stage.phases.PreElaboration],
    Dependency[chisel3.stage.phases.Checks],
    Dependency[chisel3.stage.phases.Elaborate],
    Dependency[freechips.rocketchip.stage.phases.GenerateROMs],
    Dependency[chisel3.stage.phases.AddImplicitOutputFile],
    Dependency[chisel3.stage.phases.AddImplicitOutputAnnotationFile],
    Dependency[chisel3.stage.phases.MaybeAspectPhase],
    Dependency[chisel3.stage.phases.MaybeFirrtlStage],
    Dependency[chisel3.stage.phases.Emitter],
    Dependency[chisel3.stage.phases.Convert],
    Dependency[freechips.rocketchip.stage.phases.GenerateFirrtlAnnos],
    Dependency[freechips.rocketchip.stage.phases.AddDefaultTests],
    Dependency[freechips.rocketchip.stage.phases.GenerateTestSuiteMakefrags],
    Dependency[freechips.rocketchip.stage.phases.GenerateArtefacts]
  )

  def emitVlog(
    gen: => RawModule,
    args: Array[String] = Array.empty,
    annotations: AnnotationSeq = Seq.empty
  ): String =
    execute(Array("-X", "verilog") ++ args, ChiselGeneratorAnnotation(() => gen) +: annotations).collectFirst {
      case DeletedAnnotation(_, EmittedVerilogCircuitAnnotation(a)) => a
    }.get.value

  // TODO: need a RunPhaseAnnotation to inject phases into ChiselStage
}

object Generator extends StageMain(new RocketChipStage)
