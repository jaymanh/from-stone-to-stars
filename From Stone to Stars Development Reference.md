# From Stone to Stars

## Minecraft Overhaul Mod Development Reference

*Build | Automate | Discover | Advance | Reach the stars*

This living reference defines the intended player fantasy, progression structure, systems language, and open design questions for **From Stone to Stars**. Update it as features are prototyped, tested, and scoped. Values, recipes, and exact item counts are intentionally left for balancing passes.

## Contents

1. [Core Vision and Design Principles](#core-vision-and-design-principles)
2. [Progression Loop and Player Transformation](#progression-loop-and-player-transformation)
3. [Age Progression](#age-progression)
4. [Technology Disciplines and Research](#technology-disciplines-and-research)
5. [Automation Progression](#automation-progression)
6. [Factories Logistics and Infrastructure](#factories-logistics-and-infrastructure)
7. [Power Generation](#power-generation)
8. [Production Chain Examples](#production-chain-examples)
9. [Civilisation Project and Endgame](#civilisation-project-and-endgame)
10. [Development Guardrails and Future Questions](#development-guardrails-and-future-questions)

## Core Vision and Design Principles

**From Stone to Stars** is a civilisation and technology progression overhaul. The player begins with primitive survival tools and gradually becomes the designer of an automated, spacefaring industrial system. The mod is not primarily about obtaining a stronger pickaxe; it is about replacing personal labour with thoughtfully engineered systems.

> **Design motto:** Every age should introduce a new way to automate something that was previously manual.

| Principle | Development meaning |
| --- | --- |
| Automation is the reward | A new tier should eliminate a meaningful source of repetition, not only increase speed or output. |
| The player is not the factory | Progress from hand work to system design, planning, monitoring, and optimisation. |
| Ages change play style | Each age introduces a new engineering problem, interface, or resource relationship. |
| Old resources evolve | Earlier materials gain new uses rather than becoming throwaway loot. |
| Infrastructure matters | Power, heat, fluids, pressure, data, and transport are gameplay systems, not decorative requirements. |
| Breakthroughs are demonstrated | Advance through proof of capability, not a single arbitrary craft. |

## Progression Loop and Player Transformation

The core loop repeats at every scale. Players discover a bottleneck, research a capability, build infrastructure, automate the bottleneck, and use the new capacity to tackle a more complex problem.

```text
Survive -> Discover -> Research -> Industrialise -> Automate -> Solve the next bottleneck
```

1. **Survive:** Secure basic food, shelter, tools, and materials.
2. **Discover:** Encounter materials, processes, or constraints that point to new capabilities.
3. **Research:** Complete practical requirements that prove a technology is understood.
4. **Industrialise:** Build the machines and infrastructure that make the process scalable.
5. **Automate:** Replace repeated manual action with a reliable system.
6. **Solve the next bottleneck:** Use the new system to pursue the next age.

| Stage | Player role | Typical question |
| --- | --- | --- |
| Early game | Survivor and craftsperson | How do I make this material at all? |
| Mechanical era | Builder and operator | How do I move force and materials through a workshop? |
| Industrial era | Factory engineer | How do I supply and stabilise a production line? |
| Electronic era | Systems designer | How do I coordinate machines based on conditions? |
| Space endgame | Civilisation architect | How do I sustain an off-world industrial network? |

## Age Progression

The proposed structure uses twelve substantial ages. Names may evolve, but every age needs a distinct theme, meaningful engineering constraint, and signature automation step.

| No. | Age | Theme | Example technologies | New automation | Primary goal |
| --- | --- | --- | --- | --- | --- |
| 0 | Primitive and Stone | Survival and settlement | Knapping, flint and bone tools, fire, rope, clay, pottery, hunting, farming, stone construction | Drying racks, simple traps, animal power, water wheels | Establish a permanent settlement |
| 1 | Copper and Bronze | Early metallurgy | Copper, bronze, crucibles, furnaces, bellows, metal tools, axles, gears | Water wheel, shaft, gearbox, mechanical hammer | Create dependable metalworking |
| 2 | Iron and Steel | Mechanical engineering | Iron, steel, blast furnace, pistons, springs, bearings, chains, pumps, drills, saws | Mechanised mining and ore processing | Operate the first automated factory |
| 3 | Steam | Industrial Revolution | Boilers, engines, turbines, pipes, pumps, pressure tanks, locomotives, cranes | Steam network feeding multiple machines | Generate and distribute controlled steam power |
| 4 | Electricity | Second Industrial Revolution | Generators, wire, batteries, motors, transformers, electromagnets, lighting, rail | Electrical grid and powered machinery | Generate, store, and use electricity |
| 5 | Electronics | Information and control | Transistors, boards, logic gates, sensors, timers, computers, controllers, storage | Condition-based factory control | Build a responsive automated process |
| 6 | Automation | Mass production | Robotic arms, advanced belts, routers, assemblers, drones, factory controllers | Networked production cells and automated assembly | Run multi-resource factory systems |
| 7 | Modern | High technology | Advanced alloys, plastics, semiconductors, CPUs, batteries, solar, lasers, robotics | High-throughput specialised factories | Support advanced components at scale |
| 8 | Nuclear | High-density energy | Uranium, enrichment, reactor systems, radiation detection, advanced materials, fusion research | Fuel cycle and managed reactor operations | Harness nuclear energy safely |
| 9 | Space | Leaving Earth | Rocket parts, launch pad, fuel, guidance, engines, satellites, suits, stations | Automated launch and propellant infrastructure | Complete a first orbital mission |
| 10 | Orbital | Industrialising space | Orbital factories, asteroid mining, space elevator, mining drones, solar arrays | Off-world supply chain and zero-gravity manufacture | Establish a self-sustaining orbital industry |
| 11 | Stellar | Spacefaring civilisation | Fusion, antimatter, Dyson swarm technology, AI, planetary engineering, interstellar propulsion | Interplanetary and stellar-scale logistics | Build an interstellar future |

### Age Notes

#### Primitive and Stone

The opening should feel grounded. The player learns that environment, tools, fire, food, and storage are systems. Automation is deliberately modest: passive processing and simple mechanical assistance.

#### Copper and Bronze

Metallurgy introduces conversion: ore becomes a controllable material. Rotational power is the first moment where the player builds a connected system rather than operating an isolated station.

#### Iron and Steel

This is the first major factory leap. Mining, crushing, smelting, and storage can become a visible line, making the mod feel fundamentally different from vanilla.

#### Steam

Steam makes engineering visible: boilers, pressure, pipes, fuel, and safety all matter. Failure should be consequential enough to demand attention without being punishingly destructive.

#### Electricity and Electronics

Electricity retains steam as useful upstream infrastructure. Electronics adds decisions rather than merely faster machines: sensing, thresholds, timers, and programmable control.

#### Automation through Stellar

Later ages expand scope. The player coordinates factories, power sources, mining systems, launches, and off-world industry until the final project represents a civilisation rather than a crafted item.

## Technology Disciplines and Research

The technology tree should branch across disciplines. It must not be a single linear recipe chain; disciplines create strategic choice while still converging around shared infrastructure and age milestones.

| Discipline | Focus | Representative unlocks |
| --- | --- | --- |
| Engineering | Mechanical movement and machines | Gears, drives, pumps, conveyors, drills, machine frames |
| Energy | Generation, conversion, storage, distribution | Water, steam, generators, batteries, solar, nuclear, fusion |
| Chemistry | Materials, fuels, industrial processing | Alloys, refining, plastics, propellants, enrichment |
| Science | Discovery, measurement, research capability | Laboratories, instrumentation, experiments, analysis |
| Computing | Information and automated decision-making | Logic, sensors, controllers, data networks, AI |
| Aerospace | Flight, launch, and off-world operations | Guidance, engines, launch complexes, stations, orbital systems |
| Biology | Food, medicine, agriculture, biotechnology | Crop systems, breeding, processing, life support |
| Civil Engineering | Large-scale infrastructure | Rail, cranes, structural systems, grids, space elevators |

### Proof of Technology Milestones

Research should reward demonstrated competence. A breakthrough is unlocked when the player builds and operates the enabling system, turning progression into an achievement rather than a checklist of arbitrary crafts.

| Target breakthrough | Example proof requirements |
| --- | --- |
| Steam | Produce steel; build a steam engine; generate mechanical power; automate iron processing; demonstrate a functioning multi-machine line. |
| Electricity | Produce copper wire; build a generator; store a threshold of mechanical energy; create an electromagnetic device; deliver electrical power to a machine. |
| Electronics | Manufacture a circuit board; install a sensor; automate a condition-based response; retain operational data or a timing program. |
| Nuclear | Produce processed uranium; enrich fuel; demonstrate containment and cooling; run a stable reactor cycle; store or process waste. |
| Space | Manufacture propulsion and guidance components; establish fuel production; build a launch pad and assembly facility; complete a verified launch. |
| Orbital | Deploy a station module; establish off-world power; deliver materials from an orbital or asteroid source; operate an orbital production step. |

## Automation Progression

Automation should be legible as a sequence of increasingly capable approaches. The same task becomes less hands-on and more systemic over time, which keeps familiar resources and production lines meaningful.

| Level | Player relationship | Iron example |
| --- | --- | --- |
| Manual | Player performs every step | Mine ore, process it, and smelt it by hand. |
| Mechanical | Simple machines assist a process | A driven crusher handles raw ore. |
| Powered | A network distributes energy | Steam or electricity runs the crusher reliably. |
| Automated | Inputs and outputs flow without attention | A miner feeds crushing, smelting, and storage. |
| Autonomous | Systems monitor demand and adapt | A controller starts production when reserves fall below a target. |

### Resource Evolution

Earlier materials must gain new roles as the civilisation grows. This avoids the common progression problem where large portions of the world become irrelevant after one tier.

| Resource | Early role | Later role |
| --- | --- | --- |
| Copper | Bronze alloy and simple metalwork | Wiring, motors, circuitry, advanced electronics, superconducting systems |
| Iron | Tools, structures, and basic machines | Steel, machine frames, rail, industrial parts, aerospace structural components |
| Clay and ceramics | Pottery and storage | Refractories, insulation, chemical vessels, electronics substrates |
| Wood and biomass | Fuel, construction, basic tools | Charcoal, chemicals, composite feedstocks, renewable energy inputs |
| Stone | Shelter and tools | Aggregate, industrial masonry, concrete, heat-resistant construction |

## Factories Logistics and Infrastructure

Factory layout should matter. Machines are part of a connected environment and should expose clear ports or faces for the resources they consume and produce.

| Connection | Gameplay purpose | Examples |
| --- | --- | --- |
| Items | Physical ingredients and products | Ore inputs, components, conveyors, storage, assemblers |
| Power | Energy required for operation | Rotational shafts, electrical cables, batteries, grid transformers |
| Fluids | Liquids and gases used or produced | Water, steam, oils, acids, rocket propellant |
| Heat | Temperature as a process constraint | Kilns, furnaces, chemical processing, reactor cooling |
| Pressure | Containment and flow challenge | Steam lines, pressure tanks, pneumatic systems |
| Data | Signals, monitoring, and control | Sensors, logic networks, programmable controllers, factory dashboards |

### Logistics Progression

| Era | Logistics method | New design question |
| --- | --- | --- |
| Early | Chests and hoppers | Where do I keep materials close to work? |
| Mechanical | Conveyor belts and mechanical transfer | How do I place machines in a physical line? |
| Industrial | Item pipes and pumps | How do I route materials through a larger footprint? |
| Electronic | Addressed logistics network | How does demand choose the destination? |
| Advanced | Drones and mobile routing | How do I serve flexible or remote sites? |
| Space | Orbital logistics | How do I move cargo between ground and orbit? |
| Stellar | Interplanetary logistics | How do I coordinate distant production and travel times? |

## Power Generation

Power systems should be a series of choices, not a strict replacement ladder. Older sources remain useful for local, cheap, renewable, or dependable roles even when higher-density sources exist.

| Source | Strength | Constraint or tradeoff | Likely stage |
| --- | --- | --- | --- |
| Human and animal | Accessible and thematic | Low throughput; manual handling | Primitive |
| Water | Renewable and steady at suitable sites | Terrain-dependent; mechanical distribution limits | Stone to Iron |
| Steam and fossil fuel | Strong industrial scale; teaches networks | Fuel supply, heat, pressure, emissions | Steam to Modern |
| Wind | Renewable and early-friendly | Variable output and site conditions | Electricity onward |
| Solar | Free operating input; excellent off-world potential | Intermittency; storage and surface area | Modern onward |
| Hydroelectric | High sustained output where terrain permits | Site-specific construction challenge | Modern onward |
| Nuclear fission | Very high density and reliable output | Complex fuel cycle, cooling, waste, safety | Nuclear |
| Fusion and stellar collection | Endgame-scale energy | Extreme infrastructure and advanced materials | Stellar |

## Production Chain Examples

These examples illustrate the intended texture of the game. They are not recipe commitments; adjust them for pacing, mod compatibility, and technical feasibility.

### Mechanical Iron Line

```text
Mine -> Mechanical drill -> Conveyor -> Crusher -> Furnace -> Storage
```

Turns a hand-mined resource into a visible, expandable processing line.

```text
Water wheel -> Shaft -> Gearbox -> Mechanical hammer
```

Introduces connected rotational power and the concept of a driven workstation.

### Steam to Electricity Conversion

```text
Coal or biomass -> Boiler -> Steam turbine -> Generator -> Electrical grid -> Factory
```

Makes steam an upstream system for electricity rather than a discarded tier.

```text
Water -> Boiler -> Steam pipes -> Engine network -> Pumps, drills, saws
```

Creates pressure, fuel, and distribution challenges that make factory engineering meaningful.

### Information Driven Factory

```text
Storage -> Item sensor -> Logic controller -> If iron reserve is below target -> Enable iron factory
```

Defines the Electronics age as decision-making, not simply increased processing speed.

```text
Ore processing hub -> Component factories -> Computer factory -> Rocket factory
```

Shows how later manufacturing becomes an interconnected network of specialised lines.

### Nuclear Fuel Cycle

```text
Uranium ore -> Crusher -> Chemical processing -> Concentrate -> Enrichment -> Fuel -> Reactor -> High output energy
```

Nuclear becomes a distinct discipline with material processing, operations, and waste considerations.

### Earth to Orbit

```text
Earth -> Launch facility -> Orbit -> Space station -> Asteroid mining -> Orbital manufacturing
```

Moves the endgame from a one-off rocket craft into an expanding off-world industrial chain.

## Civilisation Project and Endgame

The endgame should be a multi-stage **Civilisation Project** rather than a single final recipe. Each stage asks the player to prove that an entire industrial layer exists and works. The final project makes the mod name literal: the player began with stone and built the capacity to reach the stars.

| Phase | Civilisation project | Completion intent |
| --- | --- | --- |
| I | Industrialise the planet | Maintain automated mining, processing, logistics, and energy infrastructure. |
| II | Harness nuclear energy | Construct and operate a stable nuclear system with a viable fuel cycle and waste solution. |
| III | Establish orbital infrastructure | Build a launch complex and a functioning space station or orbital foothold. |
| IV | Industrialise the solar system | Automate asteroid mining and off-world material movement. |
| V | Build a stellar-scale energy system | Deploy a Dyson swarm or comparable stellar energy infrastructure. |
| VI | Build the Starship | Assemble and launch an interstellar vessel using the civilisation-scale systems built earlier. |

### Final From Stone to Stars Project

The final objective is not merely to defeat a boss or craft an ultimate component. It is to assemble the infrastructure required to leave the solar system: advanced propulsion, navigation, life support, materials, power, launch or departure capacity, and an interplanetary supply network. The completion moment should recognise the player as the architect of a civilisation.

> You began with stone.
>
> You built a civilisation.
>
> Now build the future.

## Development Guardrails and Future Questions

### Feature Evaluation Checklist

- What previously manual activity does this feature reduce, replace, or make more interesting?
- Does it create a new engineering decision, or only a higher numerical tier?
- Which existing resources, machines, or infrastructure does it keep relevant?
- What inputs, outputs, constraints, and feedback does the player need to understand?
- Can the player see the system working and diagnose why it is not working?
- Does the unlock fit a discipline and have a credible Proof of Technology milestone?
- Will the automation feel like a reward while leaving room for meaningful planning?

### Questions for Future Design Passes

| Topic | Questions to resolve |
| --- | --- |
| Vanilla integration | Which vanilla recipes, ores, mobs, dimensions, and progression gates are replaced, retained, or recontextualised? |
| Research interface | How are milestones surfaced: journal, tech tree, achievements, lab console, or a combination? |
| Failure and safety | How punishing should overpressure, radiation, heat, and power overloads be? What warning systems make failure fair? |
| Multiplayer | Are discoveries shared globally, per player, per team, or configurable? How do large networks perform on servers? |
| Scale and pacing | How long should each age last? Which ages may merge or split after prototype testing? |
| Compatibility | Which external tech, world-generation, and space mods should be optional partners rather than duplicated systems? |
| Accessibility | How can the UI explain complex networks without hiding the satisfaction of learning them? |
| Performance | What simulation limits, chunk-loading expectations, and visualisation tools are necessary for large factories? |

### Working Notes

Add dated decisions, prototype findings, balance targets, and deferred ideas here or in linked feature specifications. When changing a core principle, record why it changed and what systems it affects.

