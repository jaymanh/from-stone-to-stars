# From Stone to Stars

## Minecraft Overhaul Mod Development Reference

*Build | Automate | Discover | Advance | Reach the stars*

This living reference defines the intended player fantasy, progression structure, systems language, and open design questions for **From Stone to Stars**. Update it as features are prototyped, tested, and scoped. Values, recipes, and exact item counts are intentionally left for balancing passes.

> **Implementation status (2026-09-25):** Age 0 is playable as a settlement chapter. Age 1 begins with bronze (zinc + copper); zinc ore and bronze tool items exist in code under temporary `tin` / `brass` registry names pending rename. Sections after Age 0 describe design direction, not shipped gameplay.

## Contents

1. [Core Vision and Design Principles](#core-vision-and-design-principles)
2. [Progression Loop and Player Transformation](#progression-loop-and-player-transformation)
3. [Age Progression](#age-progression)
    - [Age 0 Primitive and Stone](#age-0-primitive-and-stone)
4. [Technology Disciplines and Research](#technology-disciplines-and-research)
5. [Automation Progression](#automation-progression)
6. [Factories Logistics and Infrastructure](#factories-logistics-and-infrastructure)
7. [Power Generation](#power-generation)
8. [Production Chain Examples](#production-chain-examples)
9. [Civilisation Project and Endgame](#civilisation-project-and-endgame)
10. [Optional Mod Integrations](#optional-mod-integrations)
11. [Development Guardrails and Future Questions](#development-guardrails-and-future-questions)

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
| 0 | Primitive and Stone | Survival and settlement | Pebbles, flint tools, hideworking, bone-hafted tools, fire, charcoal, clay bricks, thatch, snares | Tanning rack, snares, fish traps, charcoal pile, brick drying pit | Establish a permanent settlement |
| 1 | Zinc and Bronze | Early metallurgy | Zinc, copper, bronze, crucibles, furnaces, bellows, bronze tools, axles, gears | Water wheel, shaft, gearbox, mechanical hammer | Create dependable metalworking |
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

The opening should feel grounded. The player learns that environment, tools, fire, food, and storage are systems. Automation is deliberately modest: passive processing and simple mechanical assistance. The full Age 0 spec is below.

### Age 0 Primitive and Stone

This is the first playable age and the tutorial for the whole mod: **look at the world, gather what it already dropped, bind it into tools, then make a camp that works while you are elsewhere.** Wood and stone are materials, not starter tool tiers. Vanilla wooden and stone tools are removed.

Exact recipe counts, timings, and durability numbers are left for a later balance pass.

Registry note: zinc ore / raw zinc / zinc ingot and bronze tools currently use temporary `tin_*` and `brass_*` IDs in code. Player-facing text and this document use **zinc** and **bronze**. Rename the registries when convenient; bronze smelting and tool recipes are Age 1 work.

#### What Age 0 does today

| System | Implementation |
| --- | --- |
| Surface gathering | Pebbles and stick piles generate in the overworld. Grass, ferns, and bushes can yield plant fibre. Hammerstone breaks gravel for flint. |
| Early materials and tools | Pebbles compact into cobblestone and split back out. Flint knife, flint axe, flint spear, hammerstone, bow-and-drill, rope (from string/fibre), leather skin, and the full bone tool set (including placed bone saw) have recipes. |
| Hideworking | Mob leather drops are replaced with raw leather. Tanning rack (4 slots): raw leather becomes vanilla leather after five minutes; rain resets progress. Empty-hand collects finished leather; sneak collects all. Knife + leather → strips. |
| Wood work | Logs require a flint-or-better axe for drops. Axe strips logs and drops bark. Strip again to hew an unfinished work stump; lash with a leather strip to finish. Work stump is 3×3 crafting and wears like an anvil. Bone saw placed on a log returns itself and four planks after four interactions. |
| Manual food processing | Mortar and pestle: four empty-hand grinds convert wheat → flour (also bone → bone meal). Flour + leather skin (water) → dough → bread (smelting). |
| Fire and clay | Charcoal pile (kindling + log) → charcoal block (~1 min). Brick drying pit (kindling + charcoal blocks + wet clay bricks) → fired bricks (~5 min). Lit with bow-and-drill (also accepts flint and steel / fire charge if present). |
| Smelting station | Basic smelter: clay blast furnace using blasting recipes at half tick rate. Crafted from bricks + charcoal block. Ore smelting into usable metal is Age 1. |
| Passive food | Snare (plant or meat bait) and fish trap (water + bait). Work while the player is away; nearby players scare game/fish off. |
| Shelter and building | Hide bed (leather + wheat); wool bed recipes removed. Thatch and wattle-and-daub for camp building. Beds need a roof when raining. |
| Early storage | Vanilla **shelves** remain available. Chest and barrel recipes are removed. |
| Water | Bucket recipe removed. Leather skin carries water (no source placement). Clay vessels are the next Age 0 addition. |
| Zinc discovery | Zinc ore (overworld + deepslate) generates. Bone pick can mine zinc and copper ore. Raw zinc is the Age 0 → Age 1 discovery beat; bronze ingots open Age 1. |
| Player feedback | Age 0 advancement tree (`age0/*`) tracks settlement progress and hints next steps. Separate recipe-unlock advancements remain. Jade/WTHIT and JEI cover implemented processes. |

#### Vanilla gates (live)

| Vanilla feature | Treatment |
| --- | --- |
| Wooden / stone / copper tool recipes and wooden/stone tool loot | Removed. |
| Plank recipes (overworld woods, bamboo, crimson, warped) | Removed; bone saw supplies planks. |
| Crafting table recipe | Removed; work stump replaces it. |
| Furnace, bucket, flint and steel, chest, barrel | Recipes removed. |
| Wool beds, boats / chest boats, bamboo rafts | Recipes removed. |
| Bread, bone meal, leather craft recipes | Removed or replaced by mod processes. |
| Vanilla brick craft recipe | Removed; bricks come from firing clay bricks. |
| Leather mob loot | Replaced with raw leather. |
| Log drops without an axe | Blocked via `needs_flint_tool` (fist yields nothing). |
| Toolsmith level-1 stone-tool trades | Softened via villager trade tag override. |

Keep vanilla dirt, gravel, clay, bones, sticks-from-leaves, campfires (once fire exists), shelves, and the overworld unless a later pass needs otherwise.

#### Tool philosophy (as shipped)

**Hands.** Pick up pebbles, clay, fibre, sticks, flint from gravel. Dig dirt and gravel slowly. Cannot take log drops without an axe.

**Flint is an edge.** Cuts, scrapes, pierces. Not a mining tier.

| Flint tool | Job |
| --- | --- |
| Flint knife | Cut fibre and hides into strips; harvest; weak melee |
| Flint axe | Strip bark, early wood work; can take log drops |
| Flint spear | Hunting; reach / thrown |

**Bone is a hafted head.** Head + shaft (stick) + binding (leather strips). Sharpened bone is the head intermediate where used.

| Bone tool | Job | Must not do |
| --- | --- | --- |
| Bone axe | Main woodcutting; stump hewing | Instant plank factory |
| Bone shovel | Clay, gravel, sand, dirt | |
| Bone hoe | Farmland | |
| Bone pick | Soft stone, zinc ore, copper ore | Iron and harder (gated) |
| Bone sword | Camp defence / hunting | |
| Bone saw | Manual plank splitting (4 clicks) | |

**Bronze** (Age 1) sits above bone: mines gold-tier blocks; still blocked from iron/diamond tiers.

**Wood is lumber, not a tool tier. Stone is masonry, not a tool tier.**

Repair tags are populated (`flint_repair_item`, `bone_repair_item`, `brass_repair_item` → rename with bronze).

#### Opening loop

```text
Look down -> gather pebbles, flint, fibre, sticks
    -> flint knife
    -> hunt / fibre cordage
    -> raw leather + bone
    -> tan on rack -> strips
    -> haft bone tools
    -> fell wood, work stump, dig clay, fire
    -> traps, hide bed, thatch camp
    -> charcoal, fire bricks, basic smelter
    -> find zinc -> Age 1 bronze
```

#### Processing systems (live)

**Cordage.** Plant fibre → string → rope. Leather strips are the strong binding.

**Hideworking.**

```text
Kill -> raw leather -> tanning rack -> leather -> knife into strips
                                      \-> leather skin, hide bed, leather armour
```

Rain resets drying; roof the rack. Thatch helps.

**Fire.** Bow-and-drill lights kindling structures. Charcoal comes from the charcoal pile, not a furnace recipe. Campfires cook food once lit.

**Clay.** Wet clay bricks → drying pit or basic smelter/blasting → brick items. Pottery vessels are next; bricks already unlock the basic smelter.

**Wood.** Sticks from worldgen / leaves. Logs need an axe. Planks via bone saw. Prefer thatch and wattle over plank boxes for early bases.

**Stone.** Pebbles → cobble. Bone pick is slow on purpose. Zinc and copper are the metal-age ores bone can reach; iron stays locked.

#### Food, water, and farming

- Hunt, gather, snare, and fish trap for food. Campfire roast for meat.
- Leather skin for water. No moving source blocks until metal buckets (Age 1+).
- Bone hoe for farmland. Mortar and pestle for flour → dough → bread.
- Shelves for early item storage.

#### Shelter

Thatch, wattle-and-daub, cobble from pebbles, hide bed under a roof. Spawn-point settlement is the emotional beat of the age.

#### Automation in Age 0

Modest on purpose. Each removes a chore the player learned by hand.

| Automation | Replaces | How it plays |
| --- | --- | --- |
| Tanning rack | Carrying raw hides until “done” | Placed, rain-sensitive, four slots |
| Snare | Active hunting every meal | Bait, wait, collect |
| Fish trap | Standing in a river | Place in water, bait, wait, collect |
| Charcoal pile / brick drying pit | Furnace shortcuts | Built procedure + time; bow-and-drill to start |
| Mortar and pestle | Instant flour | Manual grind clicks |

The water wheel is **not** Age 0. Rotational power is the Age 1 gift.

#### Proof of Settlement

Age 0 ends when the camp is obviously a settlement. Surfaced by the `age0` advancement tree:

1. Gather and flint edge
2. Tan a hide and haft a bone axe
3. Work stump, fire (charcoal), fired clay / smelter
4. Set a trap; sleep (hide bed)
5. **Proof of Settlement** goal advancement
6. **Zinc Gleam** — mine raw zinc; bronze opens Age 1

#### Discipline mapping for Age 0

| Discipline | Age 0 expression |
| --- | --- |
| Engineering | Work stump, bone saw, snares, mortar |
| Energy | Fire, human labour |
| Chemistry | Hide drying, charcoal, clay firing |
| Biology | Hunting, fibre, plots, traps |
| Civil engineering | Pebble masonry, thatch, wattle, water-siting |
| Science | Watching drying and kiln timings |
| Computing / aerospace | Absent |

#### Near-term Age 0 follow-ups

- Clay vessels / pottery (storage and better water carry; no source placement)
- Finish zinc/bronze registry rename and Age 1 bronze recipes (zinc + copper → bronze ingot; bronze tools)

#### Design rules for this age

- If a feature exists only to be a weaker wooden pickaxe, cut it.
- If the player can skip hideworking and still haft tools, strips have no economy. Keep the binding cost.
- If the bone pick mines iron, Age 1 is optional. It must not.
- If a furnace can be crafted, the drying pit is flavour. Keep the furnace locked.
- If planks are cheap, every camp becomes a vanilla house. Keep splitting slow and thatch cheaper.
- Tanning racks and traps must be worth building.
- Age 0 automation waits. Age 1 automation turns. Do not put shafts and gears here.

#### Zinc and Bronze

Metallurgy introduces conversion: ore becomes a controllable material. **Bronze ingots** (zinc + copper) are the first Age 1 product. Rotational power is the first moment where the player builds a connected system rather than operating an isolated station.

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
| Settlement (Age 0) | Start fire; fire clay bricks; finish a tanning-rack process; set a snare or fish trap; sleep in a hide bed; haft a bone axe; build a work stump. |
| Bronze (Age 1) | Mine zinc; smelt a bronze ingot (zinc + copper); replace at least one bone tool with bronze. |
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
| Zinc | Bronze alloy with copper | Later alloys, galvanising, batteries, advanced materials |
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

## Optional Mod Integrations

All player-facing companion mods are **optional**. The game must launch and Age 0 must be playable with none of them installed. Integrations load through Fabric entrypoints or plugin JSON so their classes are never touched unless that mod is present.

**Policy**

- Do not hard-depend on content mods (AE2, Tech Reborn, recipe viewers, overlay mods).
- Compile against APIs only (`compileOnly`). Never jar-in-jar GUI or overlay mods.
- Fabric has no Forge Energy. **Team Reborn Energy** is the Fabric FE equivalent and *is* jar-in-jar'd (tiny MIT API) so electrical machines can speak a shared lookup. AE2 and other energy *content* mods remain optional; they attach when installed.
- Heat, shaft rotation, and pressure stay first-party lookups. Do not force them through TR Energy.
- EMI has no Fabric 26.2 artifact yet. JEI is the live recipe-viewer plugin. Add the `emi` entrypoint when EMI ports.

| Mod | Mod ID | Role | Load mechanism | Status |
| --- | --- | --- | --- | --- |
| Mod Menu | `modmenu` | Config button in the mod list | `modmenu` entrypoint | Live. Button is a no-op unless YACL is also installed. |
| YetAnotherConfigLib | `yet_another_config_lib_v3` | Config screen | Called from Mod Menu factory | Live. Dev runtime includes YACL so the screen can be tested. |
| JEI | `jei` | Recipe viewer | `jei_mod_plugin` entrypoint | Live. Categories for tanning, charcoal pile, brick drying, manual bone sawing, grinding, and world interactions (axe bark, work-stump hewing). Info pages cover the related items. |
| EMI | `emi` | Recipe viewer (preferred long-term on Fabric) | `emi` entrypoint, not registered yet | **Blocked:** no 26.2 Maven artifact. Placeholder: `FstsEmiPlugin`. |
| Jade | `jade` | Look-at overlay | `jade` entrypoint | Live. Pebbles show waterlogged state. Tanning racks show hide %. Lit kindling shows charcoal-pile burn. Placed bone saws show stroke progress. |
| WTHIT | `wthit` | Look-at overlay | `wthit_plugins.json` | Live. Same overlay lines as Jade. |
| Team Reborn Energy | `team_reborn_energy` | Fabric energy API (FE analogue) | Included in this mod's jar | Live API. Register `EnergyStorage.SIDED` when the first electrical block entity exists. |
| Applied Energistics 2 | `ae2` | Energy consumer / later storage network | Detected via `CompatMods.AE2`; no compile dependency | Optional. Uses TR Energy acceptors. No AE2-specific code until we have machines. |

**Config** (`config/from-stone-to-stars.json`, works without YACL)

| Key | Default | Notes |
| --- | --- | --- |
| `generatePebbles` | `true` | Overworld pebble patches. Requires a game restart. Existing chunks are unchanged. |
| `debugLogging` | `false` | Exposed in the config screen and persisted, but not yet consumed by gameplay code. |

**When adding a new system, also update**

1. Recipe viewers (JEI now, EMI when ported) for any non-crafting process.
2. Jade and WTHIT for machine state the player cannot see from the model (drying %, kiln heat, energy, fluid).
3. `EnergyStorage.SIDED` (and later heat / rotation lookups) for any block with a power face.
4. This table, so optional support does not drift from the code.

**Versions** (bump in `gradle.properties` when those mods update)

Mod Menu 20.0.2, YACL 3.9.6+26.2-fabric, JEI 30.32.0.221, Jade 26.2.11+fabric, WTHIT 20.0.0 (needs Bad Packets 0.12.2), Team Reborn Energy 5.0.0. Dev runs load these via `localRuntime`; do not also copy them into `run/mods`.

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
| Vanilla integration | Implemented: wooden/stone/copper tool recipes, plank/boat/bed/chest/barrel/furnace/bucket/flint-and-steel gates, leather loot replacement, log drops need an axe, hide bed, shelves as early storage. Remaining bypass watchlist: shields, bows, village beds, other container crafts. |
| Research interface | How are milestones surfaced: journal, tech tree, achievements, lab console, or a combination? |
| Failure and safety | How punishing should overpressure, radiation, heat, and power overloads be? What warning systems make failure fair? |
| Multiplayer | Are discoveries shared globally, per player, per team, or configurable? How do large networks perform on servers? |
| Scale and pacing | How long should each age last? Which ages may merge or split after prototype testing? |
| Compatibility | Optional integrations committed: Mod Menu, YACL, JEI, Jade, WTHIT, TR Energy (jar-in-jar API), AE2 detect-only. EMI waits on a 26.2 port. Remaining: Create-like rotation, other space mods, worldgen partners. |
| Accessibility | How can the UI explain complex networks without hiding the satisfaction of learning them? |
| Performance | What simulation limits, chunk-loading expectations, and visualisation tools are necessary for large factories? |

### Working Notes

Add dated decisions, prototype findings, balance targets, and deferred ideas here or in linked feature specifications. When changing a core principle, record why it changed and what systems it affects.

**2026-09-14 — Age 0 spec.** Early Primitive and Stone draft from pebble / flint / bone / hide prototypes. Superseded by later audits.

**2026-09-14 — Optional integrations.** Wired Mod Menu + YACL config, JEI plugin stub, Jade/WTHIT pebble overlay, and jar-in-jar Team Reborn Energy as the Fabric FE API. AE2 is detected only. EMI is documented but not registered until a 26.2 artifact exists. All remain `suggests`, not `depends`.

**2026-09-18 — Companion mods catch up.** JEI categories for tanning, charcoal pile, bone sawing, and world interactions. Jade/WTHIT progress overlays. Mod Menu 20.0.2.

**2026-09-20 — Prototype audit.** Documented gathering, hide, fire, mortar, bone saw, and basic smelter loops separately from long-term wishlist.

**2026-09-25 — Age 0 settlement pass.** Doc aligned to live content: work stump, hide bed, snares, fish trap, thatch, wattle, flint spear, shelves as early storage, Age 0 advancement tree, zinc/bronze naming (code still `tin`/`brass`). Clay vessels next. Bronze ingots are the Age 1 door. Removed obsolete planned systems (knapping surface, scraper, quern, raft, native copper, fire-setting, animal hitch) from the Age 0 spec.

