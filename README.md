# Welcome to the Fusion Chip project !

This project implements a full-featured high-end embedded multicore `RISC-V` CPU in `Chisel`

[Chisel](https://www.chisel-lang.org/) is the next generation Hardware Construction Languge Embedded in [Scala](https://www.scala-lang.org/)

This project is a fork from Berkeley/SiFive [RocketChip](https://github.com/chipsalliance/rocket-chip) and is supposed for teaching and experimentation purposes.

This will serve as a setup example for other projects and will help newcomers to evaluate `RISC-V` and  `Chisel` as core technologies for your next project

## Prerequesties 

1. Descent machine with at least 4 CPU Cores, SSD and a minimum 8GB memory. 16GB DDR4 RAM is recommended

2. `Ubuntu 18.10` or higher. Other OS might work, but won't be supported

## Installation

1. Install Java 11
```bash
  sudo apt-get install openjdk-11-jre openjdk-11-jdk
```

2. Install the Scala Build Tool (sbt) from [here](https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html) 

3. Install Verilator and GTKWave 
```bash
  sudo apt install build-essential verilator gtkwave
```

4. Install Device Tree Compiler
```bash
  sudo apt install device-tree-compiler
```

5. Optional - Install a `Visual Studio Code IDE` for a better code experience from [here](https://code.visualstudio.com/download)

## Starting up 

1. Clone a hello world project from Git: 
```bash
  git clone git@github.com:Neurodyne/fusionchip.git
```

2. Launch the `sbt` shell : `> sbt`

3. Compile the project : `sbt> compile`

4. Emit the whole SoC for the current hardware configuration : `sbt> runMain fusion.Emit`

5. Observe generated outputs in the `testbuild` folder

## Tech support 
1. Open issues in this repo
2. Visit `Chisel` and `FIRRTL` [website](https://www.chisel-lang.org/)
3. Join us on [Gitter](https://gitter.im/freechipsproject/chisel3)
