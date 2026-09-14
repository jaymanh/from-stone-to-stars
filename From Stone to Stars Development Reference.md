# From Stone to Stars

## Minecraft Overhaul Mod Development Reference

*Build | Automate | Discover | Advance | Reach the stars*

This living reference defines the intended player fantasy, progression structure, systems language, and open design questions for **From Stone to Stars**. Update it as features are prototyped, tested, and scoped. Values, recipes, and exact item counts are intentionally left for balancing passes.

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
| 0 | Primitive and Stone | Survival and settlement | Pebbles, knapping, flint knife, hideworking, cordage, bone-hafted tools, fire, pottery, thatch, quern | Drying racks, snares, pit kiln, player then animal quern | Establish a permanent settlement |
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

The opening should feel grounded. The player learns that environment, tools, fire, food, and storage are systems. Automation is deliberately modest: passive processing and simple mechanical assistance. The full Age 0 spec is below.

### Age 0 Primitive and Stone

This is the first playable age and the tutorial for the whole mod: **look at the world, gather what it already dropped, bind it into tools, then make a camp that works while you are elsewhere.** Wood and stone are materials, not starter tool tiers. Vanilla wooden and stone tools are removed.

Exact recipe counts, timings, and durability numbers are left for a later balance pass.

#### Prototype spine already in the mod

Build Age 0 outward from content that already exists. Do not invent a parallel starter kit.

| Existing content | Role in Age 0 |
| --- | --- |
| Pebble block (land and water worldgen, cobble compact/split recipes) | First material. Stone construction without a pickaxe. Teaches "the ground is the chest." |
| Flint knife (wooden harvest tier, sword-like use) | First crafted tool. Cutting, skinning, cordage, plant harvest. Not a pickaxe and not a full tool set. |
| Bone sword, axe, pickaxe, shovel, hoe (stone harvest tier today) | First *hafted* toolkit. Replaces vanilla stone tools for survival work. Must **not** mine copper or iron; metal ore is Age 1. |
| Raw leather | Hide drop. Unprocessed. Replaces vanilla leather as a mob drop. |
| Leather strips | Binding. Required to haft bone tools. Cut from processed hide with the flint knife. |
| Pebble pickup / cobble advancements | Pattern for the rest of Age 0: finding a material unlocks the next process, not a tech-tree GUI. |

Repair tags already exist (`flint_repair_item`, `bone_repair_item`). Flint repairs flint. Bone repairs bone.

#### Player fantasy

You wake with empty hands. Logs will not fall to fists. There is no wooden pickaxe. Pebbles lie on beaches, riverbeds, and fields. Grass holds fibre. Animals hold hide and bone. Clay sits in the bank. Fire is made, not crafted from iron.

The first hour is stooping, knapping, cutting, and binding. The first night is a fire and a hide. The first week is a camp whose racks, snares, and pots keep working while you sleep. The age ends when that camp is a settlement, and when fire has been aimed at rock hard enough to suggest metal.

#### Vanilla removals

Disable or replace these so the opening cannot be skipped with the vanilla 30-second tool rush.

| Vanilla feature | Age 0 treatment |
| --- | --- |
| Wooden tool recipes and loot | Removed. Flint is the sharp hand tool. |
| Stone tool recipes and loot | Removed. Bone-hafted tools cover the jobs. |
| Punching logs for wood | Logs do not harvest by hand. Leaves, dead bushes, and knife-work give sticks. Logs require a bone axe. |
| 1 log = 4 planks | Too industrial. Split logs at a work stump with an axe and a pebble wedge. Planks are labour. |
| Crafting table as first station | Gated. 2x2 in the inventory remains for knapping, cordage, and the knife. 3x3 waits on a **work stump**. |
| Furnace | Gated behind pottery. Cook on a campfire. Fire clay in a pit kiln. |
| Cow/horse/llama leather drops | Drop **raw leather** (hides). Finished leather is processed. |
| Iron bucket | Gated to Age 1. Age 0 water is a waterskin or a clay vessel. Source blocks are not moved. |
| Flint and steel | Gated. Fire starts with a fire plough or bow-drill (time, hunger, dry tinder). |
| Wool bed | Gated. Sleep on straw and hides. |
| Chest | Optional delay. Early storage is baskets, hide bags, and clay pots. |
| Boat | Replaced by a lashed raft (logs + rope), slower and fragile. |
| Wooden hoe / instant farmland | Farming waits on the bone hoe and a prepared plot. |
| Villager stone-tool trades | Strip wooden and stone tools from trades so they cannot refund the skip. |

Keep vanilla dirt, gravel, clay, bones, sticks-from-leaves, campfires (once fire exists), and the overworld as-is unless a later pass needs otherwise.

#### Tool philosophy

Do not clone the vanilla five-tool set at every material. Materials have jobs.

**Hands.** Pick up pebbles, clay, fibre, sticks, flint from gravel. Dig dirt and gravel slowly. Cannot fell trees or mine stone.

**Flint is an edge.** Small, sharp, held or short-hafted. It cuts, scrapes, and pierces. It is not a mining tier.

| Flint tool | Job |
| --- | --- |
| Flint knife (exists) | Cut fibre, meat, hides into strips; harvest some plants; weak melee |
| Flint handaxe / chopper | Strip bark, baton sticks off a log, crude butchery. Still cannot fell a tree for logs. |
| Flint scraper | Required station tool for hideworking |
| Flint spear | Hunting. Reach and a thrown option. Better at killing than the knife, worse at everything else |
| Flint flake / blade | Intermediate knapping output, not a hotbar tool |

**Bone is a hafted head.** A bone tool is always three parts: **head + shaft + binding**. That assembly is the seed of every later machine (parts, not lumps). Leather strips are the binding. Sticks are the shaft. Bone (or antler, later) is the head. A flint edge may be lashed on for the axe and spear.

| Bone tool (exists) | Job | Must not do |
| --- | --- | --- |
| Bone axe | First real woodcutting. Logs, then splitting | Instant plank factory |
| Bone shovel | Clay, gravel, sand, dirt at a useful speed | |
| Bone hoe | Turns a prepared plot into farmland | |
| Bone pick | Soft stone, cobble, coal-adjacent rock, packed pebbles | Copper, iron, gold, diamond, redstone, lapis as metal-age gates |
| Bone sword | Camp defence and hunting upgrade | |

Harvest tags: give bone its own `incorrect_for_bone_tool` tag. Do **not** keep vanilla `incorrect_for_stone_tool`, or a bone pickaxe will open copper and iron and skip Age 1.

When a hafted tool breaks, consider returning the stick. Binding is consumed. That makes strips a steady hide-economy sink.

**Wood is lumber, not a tool tier.** **Stone is masonry, not a tool tier.** No wooden pick, no stone pick, no "next material, same five tools."

#### Opening loop

```text
Look down -> gather pebbles, flint, fibre, sticks
    -> knap a flint knife
    -> cut cordage and hunt
    -> raw hide + bone
    -> dry and cut strips
    -> haft bone tools
    -> fell wood, dig clay, build fire
    -> dry, trap, pot, grind
    -> settlement
    -> fire against rock / native copper -> Age 1
```

**First minutes.** Pebbles are visible on the ground (already generated). Grass with a knife, or even by hand, yields plant fibre. Two pebbles knapped together (or pebble on flint) yield flakes. Knife = flake + stick + fibre or a strip if the player already has hide.

**First hunt.** Knife or spear. Drops: raw hide, bone, meat. Meat cooks on a campfire, not in a furnace.

**First binding.** Hide is scraped (flint scraper or knife) and dried on a rack. Dried hide cuts into leather strips. Strips haft the bone set. This is the first *process*, not the first *craft*.

**First wood.** Bone axe. Until then the player is a stick-and-thatch person.

**First stone building.** Nine pebbles already compact to cobble. Age 0 houses can be cobble, thatch, or wattle-and-daub without ever mining a stone block.

#### Processing systems

Each system has a manual step, then a modest automated step. That is the Age 0 version of the design motto.

##### Knapping

Pebble and flint are worked against each other, not in a shapeless lump-to-tool craft. Prefer a ground action or a dedicated knapping surface (a placed cobble or a work stump) so it feels like striking stone. Failures can consume a pebble or yield a useless chip; chips stay useful as fire striker tinder or quern grit later.

Pebble <-> cobble recipes stay. They are the settlement masonry loop, not the tool loop.

##### Cordage and rope

Plant fibre from grass, vines, kelp, and similar. Knife improves the harvest. Fibre twists into cordage (string analogue). Cordage becomes rope, snares, raft lashings, and bow-drill string. Leather strips are the strong binding; cordage is the cheap binding. Both should stay useful into later ages (belts, cable cores, packing).

##### Hideworking

```text
Kill -> raw leather -> scrape with flint -> drying rack -> dried hide -> knife into leather strips
                                                     \-> waterskin, hide bag, hide bed, leather armor
```

Rain should spoil or reset drying hides. That makes roofing the rack a real camp decision and gives thatch a job besides looking primitive.

Vanilla leather, if kept at all, is the finished hide after drying. Otherwise keep `raw_leather` / dried hide / `leather_strips` as the whole chain.

##### Fire

Fire is a procedure. Tinder (fibre, birch bark, dry grass) + spindle + patience. Success lights a campfire. Campfire cooks food and is the heat source for the pit kiln. No iron, no furnace, no blast of instant charcoal.

Charcoal comes from a covered wood pile or the pit kiln, not from a vanilla furnace recipe. Charcoal is the Age 0 fuel that still matters in Age 1 smelting.

##### Clay and pottery

Clay is dug by hand slowly, by bone shovel well. Form wet pots, bricks, and a crucible-shaped vessel by hand. They must dry on the rack, then fire in a **pit kiln**: a hole, the ware, fuel, a dirt cap, and time. Fired pottery is:

- Storage that is mouse-proof and stack-efficient compared to hide bags
- A clay bucket analogue that carries water but does not place source blocks
- Cooking pots (stews, better hunger than dry roasting)
- The first crucible, which is the Age 1 gate item even if it cannot yet melt copper reliably

This is the first thermal process. It teaches heat, fuel, waiting, and that clay is not dirt.

##### Wood

Sticks: leaves, knife, handaxe. Logs: bone axe only. Planks: work stump + axe + pebble wedge. Thatched roofs and wattle walls should be cheaper than plank boxes so early bases look like camps, not oak cubes.

Bark stripped with the handaxe can be tinder or fibre. Keep wood relevant later as charcoal, ash, potash, and construction.

##### Stone

Pebbles are gathered. Cobble is compacted pebbles. Breaking cobble or stone with a bone pick is slow on-purpose; fire-setting is the interesting way to crack a face of rock (build a hot fire against stone, quench or wait, harvest rubble). Fire-setting is also how native copper or malachite can be hinted before metal tools exist.

Do not let the bone pick replace a mining drill. Age 2 owns mechanised mining.

#### Food, water, and farming

- Hunt and gather first. Berries, eggs, meat, fish.
- Campfire roast is inefficient. Drying racks make jerky that lasts. Clay pot stews are the best Age 0 food.
- Waterskin (hide) sips from sources. Clay vessel carries more. No moving the river until copper buckets.
- Bone hoe unlocks a small plot. Seeds from breaking grass can stay, but farmland should feel prepared (cleared, maybe mulched) rather than instant bread-empire.
- Animal husbandry is late Age 0: lead with rope, pen with wattle, then hitch to the quern.

Water-near-camp matters because buckets are gone. That is a settlement decision, not a QoL skip.

#### Shelter

A valid Age 0 house is any of: thatch hut, wattle-and-daub, cobble foundation with hide roof. Wool beds are not the sleep item. Hide bed = dried hides + straw. Spawn-point settlement is the emotional beat of the age.

#### Automation in Age 0

Modest on purpose. Each one removes a chore the player just learned by hand.

| Automation | Replaces | How it plays | Later evolution |
| --- | --- | --- | --- |
| Drying rack | Holding hide/clay/meat in inventory and waiting | Placed block, weather-sensitive, several slots | Age 1 smokehouse / heated dryer |
| Snare and deadfall | Active hunting every meal | Bait, wait, collect; cheap cordage + stick + pebble | Age 1 animal pens as the reliable version |
| Fish basket | Standing in a river with a stick | Place in water, wait, collect | Nets, then Age 2 fishing as a food line |
| Pit kiln | There is no vanilla furnace yet | A built procedure, not a GUI machine | Clay furnace, then bloomery / Age 1 |
| Quern (player) | No flour, dyes, crushed clay, or crushed charcoal otherwise | Two cobble stones, right-click labour, hunger cost | Same block, better prime mover |
| Quern (animal) | Player standing at the quern | Hitch a beast; slow, requires feed and a pen | Water wheel in Age 1 turns the same mill |

The water wheel is **not** an Age 0 machine. Age 0 may *notice* current (fish baskets, washing ore dirt, washing pebbles). Rotational power as a network is the Age 1 gift. The animal quern is the proof that "something else can turn it."

#### Proof of Settlement

Age 0 does not end on a single craft. It ends when the camp is obviously a settlement. Surface this with advancements, not a hidden flag.

Suggested proof, all required:

1. Sleep in a hide bed at a fire you started.
2. Store food or water in fired pottery.
3. Run a drying rack to completion (hide, jerky, or clay).
4. Collect from a trap or fish basket you did not babysit.
5. Own a hafted bone tool set (at least axe + one other).
6. Produce charcoal or fired ware in a pit kiln.

**Age 1 gate item:** native copper (surface nugget, like a rare pebble) or malachite stained rock revealed by fire-setting, plus a fired clay crucible. Smelting is Age 1. Finding the green stone is the last Age 0 discovery.

#### Discipline mapping for Age 0

| Discipline | Age 0 expression |
| --- | --- |
| Engineering | Wedges, snares, work stump, quern |
| Energy | Fire, human labour, then animal labour |
| Chemistry | Hide drying, charcoal, pottery firing, ash |
| Biology | Hunting, fibre plants, plots, hitching animals |
| Civil engineering | Pebble masonry, thatch, wattle, siting the camp by water |
| Science | Watching drying time and kiln results; fire-setting as experiment |
| Computing / aerospace | Absent, correctly |

#### Suggested new content

Implement in this order so each slice is playable.

**Slice 1 — Close the existing loop.** Knife recipe (flint + stick + fibre/strips). Bone tool recipes (bone + stick + strips). Raw leather from animals. Strips from hide + knife. Repair tags filled. Wooden and stone tool recipes disabled. Bone pick harvest tag corrected so it cannot mine metal ores.

**Slice 2 — Hands and fibre.** Plant fibre item. Cordage. Sticks from leaves guaranteed enough to play. Log harvest blocked without an axe.

**Slice 3 — Hide camp.** Scraper. Drying rack. Hide bed. Waterskin. Hide bag.

**Slice 4 — Fire and clay.** Fire plough. Pit kiln. Clay pot, clay vessel, clay bricks. Charcoal pile. Furnace recipe still disabled.

**Slice 5 — Passive food.** Snare, deadfall, fish basket. Jerky on the drying rack.

**Slice 6 — Settlement stations.** Work stump (3x3). Pebble wedge + plank splitting. Wattle and thatch blocks. Quern. Animal hitch.

**Slice 7 — Age 1 door.** Native copper worldgen (sparse, pebble-like). Fire-setting on stone. Fired crucible. Advancement "Proof of Settlement."

Optional flavour, only after the slices above: flint spear and handaxe, raft, bark tinder, ash as a later soap/potash input, antler as a rare bone-tier head.

#### Design rules for this age

- If a feature exists only to be a weaker wooden pickaxe, cut it.
- If the player can skip hideworking and still haft tools, strips have no economy. Keep the binding cost.
- If the bone pick mines copper, Age 1 is optional. It must not.
- If a furnace can be crafted, the pit kiln is flavour. Keep the furnace locked.
- If planks are cheap, every camp becomes a vanilla house. Keep splitting slow and thatch cheaper.
- Drying racks and traps must be worth building, not slower than standing still with the item in hand.
- Age 0 automation waits. Age 1 automation turns. Do not put shafts and gears here.

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
| Settlement (Age 0) | Start fire; fire pottery in a pit kiln; finish a drying-rack process; collect from an unattended trap; sleep in a hide bed; haft a bone axe. |
| Copper (Age 1) | Find native copper or fire-set ore; fire a crucible; smelt a usable metal; replace at least one bone tool with a metal one. |
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
| JEI | `jei` | Recipe viewer | `jei_mod_plugin` entrypoint | Live plugin stub. Add custom recipe categories when pit kiln / knapping / drying exist. |
| EMI | `emi` | Recipe viewer (preferred long-term on Fabric) | `emi` entrypoint, not registered yet | **Blocked:** no 26.2 Maven artifact. Placeholder: `FstsEmiPlugin`. |
| Jade | `jade` | Look-at overlay | `jade` entrypoint | Live. Pebbles show waterlogged state. |
| WTHIT | `wthit` | Look-at overlay | `wthit_plugins.json` | Live. Same pebble line as Jade. |
| Team Reborn Energy | `team_reborn_energy` | Fabric energy API (FE analogue) | Included in this mod's jar | Live API. Register `EnergyStorage.SIDED` when the first electrical block entity exists. |
| Applied Energistics 2 | `ae2` | Energy consumer / later storage network | Detected via `CompatMods.AE2`; no compile dependency | Optional. Uses TR Energy acceptors. No AE2-specific code until we have machines. |

**Config** (`config/from-stone-to-stars.json`, works without YACL)

| Key | Default | Notes |
| --- | --- | --- |
| `generatePebbles` | `true` | Overworld pebble patches. Requires a game restart. Existing chunks are unchanged. |
| `debugLogging` | `false` | Extra diagnostic log lines. |

**When adding a new system, also update**

1. Recipe viewers (JEI now, EMI when ported) for any non-crafting process.
2. Jade and WTHIT for machine state the player cannot see from the model (drying %, kiln heat, energy, fluid).
3. `EnergyStorage.SIDED` (and later heat / rotation lookups) for any block with a power face.
4. This table, so optional support does not drift from the code.

**Versions** (bump in `gradle.properties` when those mods update)

Mod Menu 20.0.1, YACL 3.9.6+26.2-fabric, JEI 30.32.0.221, Jade 26.2.11+fabric, WTHIT 20.0.0, Team Reborn Energy 5.0.0.

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
| Vanilla integration | Age 0 now commits to removing wooden and stone tools, fist-felling, leather drops, the early furnace, iron buckets, and flint-and-steel. Remaining questions: chests, shields, bows, villager restocks, and whether gravel still drops flint vanilla-style. |
| Research interface | How are milestones surfaced: journal, tech tree, achievements, lab console, or a combination? |
| Failure and safety | How punishing should overpressure, radiation, heat, and power overloads be? What warning systems make failure fair? |
| Multiplayer | Are discoveries shared globally, per player, per team, or configurable? How do large networks perform on servers? |
| Scale and pacing | How long should each age last? Which ages may merge or split after prototype testing? |
| Compatibility | Optional integrations committed: Mod Menu, YACL, JEI, Jade, WTHIT, TR Energy (jar-in-jar API), AE2 detect-only. EMI waits on a 26.2 port. Remaining: Create-like rotation, other space mods, worldgen partners. |
| Accessibility | How can the UI explain complex networks without hiding the satisfaction of learning them? |
| Performance | What simulation limits, chunk-loading expectations, and visualisation tools are necessary for large factories? |

### Working Notes

Add dated decisions, prototype findings, balance targets, and deferred ideas here or in linked feature specifications. When changing a core principle, record why it changed and what systems it affects.

**2026-09-14 — Age 0 spec.** Fleshed out Primitive and Stone from current prototypes (pebble, flint knife, bone set, raw leather, leather strips). Wooden and stone tools are out. Flint is an edge, not a tier. Bone is hafted and must not mine metal ores. First automation is drying racks, traps, pit kiln, then player/animal quern. Water wheel stays Age 1. Age 1 door is native copper plus a fired crucible. Implementation slices 1–7 are the build order.

**2026-09-14 — Optional integrations.** Wired Mod Menu + YACL config, JEI plugin stub, Jade/WTHIT pebble overlay, and jar-in-jar Team Reborn Energy as the Fabric FE API. AE2 is detected only. EMI is documented but not registered until a 26.2 artifact exists. All remain `suggests`, not `depends`.

