// See LICENSE for license details.
package fusion

import Chisel._

import freechips.rocketchip.config._
import freechips.rocketchip.subsystem._
import freechips.rocketchip.devices.debug._

import sifive.blocks.devices.mockaon._
import sifive.blocks.devices.gpio._
import sifive.blocks.devices.pwm._
import sifive.blocks.devices.spi._
import sifive.blocks.devices.uart._
import sifive.blocks.devices.i2c._

//-------------------------------------------------------------------------
// FusionSystem
//-------------------------------------------------------------------------

class FusionSystem(implicit p: Parameters)
    extends RocketSubsystem
    with HasPeripheryDebug
    with HasPeripheryMockAON
    with HasPeripheryUART
    with HasPeripherySPIFlash
    with HasPeripherySPI
    with HasPeripheryGPIO
    with HasPeripheryPWM
    with HasPeripheryI2C {
  override lazy val module = new FusionSystemModule(this)
}

class FusionSystemModule[+L <: FusionSystem](_outer: L)
    extends RocketSubsystemModuleImp(_outer)
    with HasPeripheryDebugModuleImp
    with HasPeripheryUARTModuleImp
    with HasPeripherySPIModuleImp
    with HasPeripheryGPIOModuleImp
    with HasPeripherySPIFlashModuleImp
    with HasPeripheryMockAONModuleImp
    with HasPeripheryPWMModuleImp
    with HasPeripheryI2CModuleImp {
  // Reset vector is set to the location of the mask rom
  val maskROMParams = p(PeripheryMaskROMKey)
  global_reset_vector := maskROMParams(0).address.U
}
