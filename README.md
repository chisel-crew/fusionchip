# Fusionchip

This project implements a High-end embedded chip, based on Berkeley technologies. It relies on [Chisel](https://github.com/freechipsproject/chisel3), [FIRRTL](https://github.com/freechipsproject/firrtl) and  [Rocketchip](https://github.com/chipsalliance/rocket-chip).

This is meant for learning, teaching and experimentation. The main goals are to simplify SoC implementation with Chisel, innovate with the latest tech and build new chips.

This also serves as a good reference platform for Chisel IP designers, system architects, students and instructors.

## Installation

1. Install Java 8 or Java 11 Development Kit
```shell
    sudo apt install openjdk-8-jdk
    or
    sudo apt install openjdk-11-jdk
```
2. Install [sbt](https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html)

3. Download and build Chisel3 and Firrtl
```shell
    git clone https://github.com/freechipsproject/chisel3
    git clone https://github.com/freechipsproject/firrtl

    cd chisel3
    sbt publishLocal

    cd firrtl
    sbt publishLocal
```

4. Download and compile the Fusion chip:
```shell
    git clone https://github.com/Neurodyne/fusionchip
    sbt
    compile
```

Congratulations! If you can compile the Fusion chip, that means all went fine and you're ready to build your next great SoC with Chisel.

Have a lot of Fun!

## Resources 
1. Chisel and FIRRTL [Official Web](https://www.chisel-lang.org/)
2. Chisel [Gitter](https://gitter.im/freechipsproject/chisel3)
3. FIRRTL [Gitter](https://gitter.im/freechipsproject/firrtl)
4. Neurodyne Chisel Chips [Gitter](https://gitter.im/goneurodyne/chisel_chips)


## Community designs
Here I'll add all related IP cores, SoC and system designs, implemented in Chisel

1. [Rocketchip](https://github.com/chipsalliance/rocket-chip)
2. [BOOM](https://github.com/riscv-boom/riscv-boom)
3. [DMA IP](https://github.com/antmicro/fastvdma)