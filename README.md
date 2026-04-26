# AZA — Object-Oriented Circuit Simulator

AZA is a SPICE-like circuit simulator implemented in Java, combining object-oriented circuit modeling, numerical time-domain simulation, and a graphical user interface.

The system parses text-based circuit descriptions, simulates electrical behavior over time, and provides both numerical outputs and visual representations.

---

## ⚡ Overview

AZA reconstructs the full circuit simulation pipeline:

* Parse → SPICE-like input file
* Model → Object-oriented circuit representation
* Solve → Time-stepped numerical simulation
* Visualize → GUI + plots

The project supports **linear, non-linear, and time-dependent circuit elements**, along with interactive visualization tools.

---

## 🧠 Features

### 🔹 Circuit Modeling

* Object-oriented design with a shared `Element` base class
* Explicit modeling of:

  * Nodes (`Nodes.java`)
  * Elements (`Resistor`, `Capacitor`, `Inductor`, `Diodes`, etc.)
  * Sources (`ISource`, `VSource`, dependent sources)

### 🔹 Supported Elements

* Resistor, Capacitor, Inductor
* Independent current & voltage sources (DC / AC)
* Dependent sources (G, F, E, H types)
* Ideal diode (with switching behavior)

### 🔹 Simulation Engine

* Implemented in `Brain.java`
* Time-domain simulation using **Euler discretization**
* Based on:

  * Kirchhoff’s Current Law (KCL)
  * Supernode handling for voltage sources
* Iterative updates of node voltages and element currents

### 🔹 Input System

* Managed by `InputManager.java`
* SPICE-like text format (`input.txt`, `inputTest.txt`)
* Supports:

  * Units (k, m, u, n, …)
  * Time-dependent expressions
  * Comments and structured commands

### 🔹 Output

* Saved in `output.txt`
* Includes:

  * Node voltages over time
  * Element voltage, current, and power

### 🔹 Graphical Interface (JavaFX)

* Controllers:

  * `Controller.java`
  * `ControllerMainPage.java`
  * `ControllerHomePage.java`
* FXML-based UI (`sampleMainPage.fxml`, etc.)
* Features:

  * Circuit visualization (`Diagram.java`)
  * Interactive controls (Run, Draw, Load, Edit)
  * Plotting of voltage / current / power over time

---

## 🏗️ Project Structure

```id="aza-structure"
.
├── src
│   └── main
│       ├── java
│       │   ├── Brain.java              # Core simulation engine
│       │   ├── Element.java            # Base class for all elements
│       │   ├── Resistor.java
│       │   ├── Capacitor.java
│       │   ├── Inductor.java
│       │   ├── Diodes.java
│       │   ├── Source.java             # Base class for sources
│       │   ├── ISource.java
│       │   ├── VSource.java
│       │   ├── Nodes.java              # Node representation
│       │   ├── InputManager.java       # Input parsing
│       │   ├── Diagram.java            # Circuit drawing
│       │   ├── Controller*.java        # GUI controllers
│       │   └── Main.java               # Entry point
│       └── resources
│           ├── *.fxml                 # GUI layouts
│           ├── *.css                  # Styling
│           └── *.png                  # UI assets
├── elements
│   └── *.jpg                          # Visual representations of components
├── loadingImgs
│   └── (loading animation assets)
├── input.txt                          # Example input
├── output.txt                         # Simulation output
└── pom.xml                            # Maven configuration
```

---

## 🔢 Simulation Method

* Continuous circuit equations are discretized over time:

  `t → t + Δt`

* At each time step:

  * Apply **KCL at nodes**
  * Update node voltages iteratively
  * Update element currents using discrete equations

* Convergence occurs when:

  **Σ I(node) ≈ 0**

---

## ▶️ How to Run

### 1. Build

```id="aza-build"
mvn clean install
```

### 2. Run

```id="aza-run"
mvn exec:java -Dexec.mainClass="Main"
```

### 3. Usage

* Load a circuit file (`input.txt`)
* Click **RUN** to simulate
* Click **DRAW** to visualize
* View results:

  * `output.txt`
  * GUI plots

---

## 🖼️ GUI

* Circuit is rendered on a fixed grid layout
* Elements are visualized using assets from `/elements`
* Additional features:

  * File editor (load/edit circuits)
  * Interactive plotting (voltage, current, power)

---

## ⚠️ Error Handling

The simulator detects:

* Invalid input syntax
* Floating nodes
* Conflicting sources:

  * Parallel voltage sources
  * Series current sources
* Missing or invalid ground node

---

## 🎯 Motivation

AZA was built to:

* Recreate circuit simulation from first principles
* Connect OOP design with physical system modeling
* Explore numerical solutions of differential systems
* Provide an interactive interface for experimentation

---

## 🚀 Future Work

* Non-linear solvers (Newton-Raphson)
* Improved convergence strategies
* More advanced visualization
* Frequency-domain analysis

---

## 👩‍💻 Authors

* Zahra Sodagar
* Amir Mohammad Marshal Pirgheibi
* Mohammad Amin Panahandeh

---

## 📌 Notes

This project was developed as part of an Object-Oriented Programming course,
with an emphasis on building a complete simulation system from scratch.
