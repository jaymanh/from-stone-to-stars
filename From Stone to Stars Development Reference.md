# From Stone to Stars

## Minecraft Overhaul Mod Development Reference

*Build | Automate | Discover | Advance | Reach the stars*

This living reference defines the intended player fantasy, progression structure, systems language, and open design questions for **From Stone to Stars**. Update it as features are prototyped, tested, and scoped. Ages 0 and 1 cite the numbers that ship in 0.8.0. Later ages leave recipe counts for a balancing pass.

> **Implementation status (2026-10-27):** Age 0 and Age 1 are playable in 0.8.0. Age 0 is the settlement. Age 1 is the bronze workshop: tools, forge and bellows, crucibles, bronze bucket and chest, water wheel, shafts, gears, trip hammer, powered quern, a rotation-powered bronze hopper, crushed-ore doubling, and the `age1` advancements. Age 2 is iron and steel. Rusty mud, iron forged from heated ingots, the steel alloy, heated steel, steel sheets, steel blades, and steel saw blades exist. The first steel machine is the sawmill, a formed multiblock that turns logs into planks on the water wheel. The planned factory, an ironworks that crushes and smelts and a drill that bores eight blocks, is not built. There is no `age2` advancement tree yet. Ages after Age 2 describe intended gameplay.

## Contents

1. [Core Vision and Design Principles](#core-vision-and-design-principles)
2. [Progression Loop and Player Transformation](#progression-loop-and-player-transformation)
3. [Age Progression](#age-progression)
    - [Age 0 Primitive and Stone](#age-0-primitive-and-stone)
    - [Age 1 Zinc and Bronze](#age-1-zinc-and-bronze)
    - [Age 2 Iron and Steel](#age-2-iron-and-steel)
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
| 1 | Zinc and Bronze | Soft metal and first motion | Bronze tools, forge and bellows, bronze bucket and chest, water wheel, shafts, gears, trip hammer, powered quern, bronze hopper | Water wheel → shaft (max 8) → a yard machine, fed by a slow turning hopper | Turn river power into dependable metalwork |
| 2 | Iron and Steel | Mechanical engineering | Iron tools, steel sheets and blades, sawmill, ironworks, marked-face drill | A strong wheel turns a drill and an ironworks; a weak wheel turns one of them | Run one riverside line that feeds itself |
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

#### Zinc and Bronze

Soft metal and the first connected workshop. Bronze tools and a river mill teach conversion and rotation. The full Age 1 spec is below.

#### Iron and Steel

Iron, steel, and the first line that feeds itself. The material path, steel parts, and the sawmill are already in the mod. The ironworks and the drill are planned below and are not built.

### Age 0 Primitive and Stone

This is the first playable age and the tutorial for the whole mod: **look at the world, gather what it already dropped, bind it into tools, then make a camp that works while you are elsewhere.** Wood and stone are materials, not starter tool tiers. Vanilla wooden and stone tools are removed.

Live counts are cited below. Change them in a balance pass, then update this document.

Zinc ore, raw zinc, zinc ingots, and bronze tools use those names in code. Crucible alloying and zinc discovery close Age 0. Bronze tools, the forge, and the mill are Age 1, and they ship.

#### What Age 0 does today

| System | Implementation |
| --- | --- |
| Surface gathering | Pebbles and stick piles generate in the overworld. Grass, ferns, and bushes can yield plant fibre. Hammerstone breaks gravel for flint. |
| Early materials and tools | Pebbles compact into cobblestone and split back out. Flint knife, flint axe, flint spear, hammerstone, bow-and-drill, rope (from string/fibre), leather skin, clay jug (64 water uses), and the full bone tool set (including placed bone saw) have recipes. |
| Hideworking | Mob leather drops are replaced with raw leather. Tanning rack (4 slots): raw leather becomes vanilla leather after five minutes; rain resets progress. Empty-hand collects finished leather; sneak collects all. Knife + leather → strips. |
| Wood work | Logs require a flint-or-better axe for drops. Axe strips logs and drops bark. Strip again to hew an unfinished work stump; lash with a leather strip to finish. Work stump is 3×3 crafting and wears like an anvil. Bone saw placed on a log returns itself and four planks after four interactions. |
| Manual food processing | Mortar and pestle: four empty-hand grinds convert wheat → flour (also bone → bone meal). Flour + water vessel (leather skin or clay jug) → dough → bread (smelting). |
| Fire and clay | Charcoal pile (kindling + log) → charcoal block (~1 min). Brick drying pit (kindling + charcoal blocks + unfired pottery: bricks, jug, or storage pot) → fired result (~5 min). Lit with bow-and-drill (also accepts flint and steel / fire charge if present). |
| Smelting station | Basic smelter: clay blast furnace using blasting recipes at half tick rate, plus charged-crucible alloying. Crafted from bricks + charcoal block. |
| Bronze | Clay crucible (5 wet clay bricks, U shape, 8 uses). Pack 4 copper + 1 zinc → charged crucible; fire in the basic smelter → 5 bronze. Further alloys are datapack `crucible_alloying` recipes. A bronze-reinforced crucible (32 uses) is the Age 1 vessel for the same recipes. |
| Passive food | Snare (plant or meat bait) and fish trap (water + bait). Work while the player is away; nearby players scare game/fish off. |
| Shelter and building | Hide bed (leather + wheat); wool bed recipes removed. Thatch and wattle-and-daub for camp building. Beds need a roof when raining. |
| Early storage | Vanilla **shelves** remain available. Chest and barrel recipes are removed. Fired **storage pot**: 27-slot barrel inventory; silk touch to pick up; needs support or falls and shatters. |
| Water | Bucket recipe removed. Leather skin (16 uses) and clay jug (64 uses) carry water (no source placement). |
| Zinc discovery | Zinc ore (overworld + deepslate) generates. Bone pick can mine zinc and copper ore. Raw zinc is the Age 0 → Age 1 discovery beat; bronze ingots open Age 1. |
| Player feedback | Age 0 advancement tree (`age0/*`) tracks settlement progress and hints next steps. Separate recipe-unlock advancements remain. Jade/WTHIT and JEI cover implemented processes. |

#### Vanilla gates (live)

| Vanilla feature | Treatment |
| --- | --- |
| Wooden / stone / copper tool recipes and wooden/stone tool loot | Removed. |
| Plank recipes (overworld woods, bamboo, crimson, warped) | Removed; bone saw supplies planks. The Age 2 sawmill is the bulk source. |
| Crafting table recipe | Vanilla recipe removed. The work stump is the Age 0 table. A later craft returns a vanilla table: bronze plate, nails, leather strips, and an unfinished work stump. |
| Furnace, flint and steel, barrel, shield | Recipes removed. |
| Bucket | Vanilla recipe removed. The Age 1 bronze bucket picks up and places water, lava, and powder snow. |
| Chest | Vanilla recipe removed. One bronze chest crafts into one vanilla chest. |
| Found iron | Ingots, nuggets, blocks, raw iron, iron tools and armor, and chainmail are stripped from loot. Block drops and end-city chests stay. |
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
| Bone pick | Soft stone, zinc ore, copper ore | Gold, iron, and harder (gated) |
| Bone sword | Camp defence / hunting | |
| Bone saw | Manual plank splitting (4 clicks) | |

**Bronze** (Age 1) sits above bone: mines gold-tier blocks; still blocked from iron/diamond tiers.

**Wood is lumber, not a tool tier. Stone is masonry, not a tool tier.**

Repair tags are populated (`flint_repair_item`, `bone_repair_item`, `bronze_repair_item`).

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

**Clay.** Wet clay bricks, unfired jug, and unfired storage pot craft from clay balls. Fire them in the drying pit (same layout) or by blasting. Bricks unlock the basic smelter; the jug holds 64 water uses; the storage pot is a fragile 27-slot barrel (silk touch to move; needs support).

**Wood.** Sticks from worldgen / leaves. Logs need an axe. Planks via bone saw. Prefer thatch and wattle over plank boxes for early bases.

**Stone.** Pebbles → cobble. Bone pick is slow on purpose. Zinc and copper are the metal-age ores bone can reach; iron stays locked.

#### Food, water, and farming

- Hunt, gather, snare, and fish trap for food. Campfire roast for meat.
- Leather skin (16) or clay jug (64) for water. The bronze bucket, in Age 1, is what places a source.
- Bone hoe for farmland. Mortar and pestle for flour → dough → bread.
- Shelves for early item storage; storage pot for a larger fragile cache.

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

- Settlement UX polish (JEI hints, balance tuning)

#### Bronze from crucibles

Pack an empty clay crucible with metals on a work stump, then fire the charged crucible in a basic smelter, a blast furnace, or a lit forge. Alloys are data-driven (`from-stone-to-stars:crucible_alloying`). First alloy: 4 copper + 1 zinc → 5 bronze, 200 ticks, 0.7 experience. A clay crucible lasts 8 firings. A bronze-reinforced crucible lasts 32 and comes back one use more worn. Making bronze is the Age 0 → Age 1 door. Using it is Age 1.

#### Design rules for this age

- If a feature exists only to be a weaker wooden pickaxe, cut it.
- If the player can skip hideworking and still haft tools, strips have no economy. Keep the binding cost.
- If the bone pick mines iron, Age 1 is optional. It must not.
- If a furnace can be crafted, the drying pit is flavour. Keep the furnace locked.
- If planks are cheap, every camp becomes a vanilla house. Keep splitting slow and thatch cheaper.
- Tanning racks and traps must be worth building.
- Age 0 automation waits. Age 1 automation turns. Do not put shafts and gears here.

### Age 1 Zinc and Bronze

Age 1 is playable in 0.8.0. It is the first **soft-metal** chapter, and the camp grows a **workshop that turns**. The materials are still wood, bronze, clay, rope, and a river. A slow bronze hopper can pass items into a machine. The yard still stops when the chest runs out, the wheel runs dry, or the forge burns down. A line that keeps feeding itself is Age 2.

> **Age motto:** Soft metal in the hand; one turning shaft in the yard.

#### Entry and exit

| Gate | Requirement |
| --- | --- |
| **Enter Age 1** | Obtain a **bronze ingot**. Age 0 teaches zinc mining and the crucible. Alloying 4 copper and 1 zinc into 5 bronze is the door. |
| **Prove Bronze** | The `age1` advancements: haft a bronze tool, spin a water wheel, finish a powered grind or forging, and pump a lit forge. **Bronze Yard** asks for the tool, the finished process, and the bellows together. |
| **Exit toward Age 2** | **Iron Stain.** Dig rusty mud. That path yields iron before any iron ore can be mined. Bronze tools are blocked from iron-tier and diamond-tier blocks. |

#### What ships today

| System | Live behaviour |
| --- | --- |
| Bronze tools | Sword, pickaxe, axe, shovel, hoe, spear, and hammer. Durability 280, mining speed 5.5, attack bonus 2, enchantability 15. The pick mines gold-tier blocks (`needs_bronze_tool`: gold ore, gold block, raw gold block). It is incorrect for `#minecraft:needs_iron_tool` and `#minecraft:needs_diamond_tool`. Repair tag: `bronze_repair_item`. Helmet, chestplate, leggings, and boots exist and sit outside the `bronze_tools` tag. |
| Hammers and gravel | Hammerstone, bronze hammer, and iron hammer break gravel into flint and spend 1 durability. Tag: `gravel_hammers`. |
| Forge | A formed multiblock. Framed coal pad, 1×1 to 5×5. Hearths on the centre coal, sharing one inventory. One inward bellows bank. A framed chimney at least 2 tall. Hearths run blasting recipes and fire charged crucibles. A larger hearth cluster runs hotter. Pump the bellows on a lit forge to stretch the burn. A fresh forge is already at full burn, so the first pump can add 0 ticks and still counts for **Hotter Fire**. |
| Forging stump | Crafted from four bronze and a work stump. Choose a forging recipe, then strike with a bronze or iron hammer. An empty input keeps that recipe, so the next matching stack starts it again. Heated metal keeps cooling on the stump. The trip hammer sits on top and strikes the selected recipe. |
| Heated metal | Blast bronze, copper, or iron into a heated ingot. In the smelter or forge it stays hot. Off the fire it cools in 60 seconds (`COOL_TICKS` = 20×60) back into the plain ingot. |
| Bronze parts | Forging, 4 hits each. 3 heated bronze → 2 tool blades. 4 heated bronze → 2 plates. 2 heated bronze → 4 shafts. 1 heated bronze → 4 nails. Heated copper: 1 → 4 nails, 2 → 4 shafts, also 4 hits. |
| Crucibles | Clay lasts 8 firings. Bronze-reinforced lasts 32 and is returned one use more worn. Same pack-and-fire alloys, still packed by hand. |
| Water | Bronze bucket, from 3 bronze ingots. Picks up and places water, lava, and powder snow, fills from cauldrons, milks, and scoops mobs the way a vanilla bucket does. Clay jug and leather skin stay the early vessels. |
| Storage | Bronze chest: 27 slots. Two of them join, and they stay separate from vanilla chests. One bronze chest crafts into one vanilla chest. Shelves and the storage pot remain. |
| Crafting table | The vanilla recipe stays removed. A bronze-fitted table returns from a bronze plate, nails, leather strips, and an unfinished work stump. |
| Crushed ore | On the forging stump: 1 raw iron, copper, gold, or zinc, and 16 hits, become 2 crushed pieces. Smelting or blasting each piece into one ingot is their only use. The hit count is why the trip hammer is worth building. |
| Rotational power | Water wheel, wooden shaft, rotation gear, trip hammer, powered quern, bronze hopper. Numbers below. |

#### Rotational power

The age’s verb is **connect**.

```text
Flowing water -> water wheel -> shaft (-> gear) -> machine
```

| Piece | Live behaviour |
| --- | --- |
| Water wheel | Reads flowing water every second, on itself and its six neighbours. No flow leaves it idle. Any flow is weak, capacity 1. Two or more flowing sides, or a combined fluid amount of 12 or more, is strong, capacity 2. A lone wheel with flow is already spinning. |
| Wooden shaft | Carries rotation along its axis. A machine farther than 8 blocks from the wheel is out of range. Shafts stay in place. |
| Gear | Turns the shaft onto another face. Gears pass the full turn. |
| Machine | Powered quern, trip hammer, and bronze hopper. Each costs 1. A weak wheel runs one machine. A strong wheel runs two. A third overloads the network and the yard stops. |
| Drive face | Quern and trip hammer take the shaft on the back, opposite the way the block faces. The hopper takes a shaft on any face except the top and the spout. A gear can still meet the hopper on any face. |

Jade and WTHIT show the rotation status: idle, spinning, no water flow, overloaded, too far from the wheel, or disconnected.

The yard is a mill beside a river. Belts, item pipes, multi-chunk shafts, and an ore vein that becomes ingots on its own wait for a later age.

#### Machines and the hopper

| Machine | What it does | Hopper faces |
| --- | --- | --- |
| Trip hammer | One hit every 15 ticks on the forging stump under it, about four hits in three seconds. Finishing a recipe grants **Under Load**. | The stump takes input from the sides and output from the bottom. The top stays closed for the hammer. |
| Powered quern | One grind every 8 ticks while the shaft turns. Same grind recipes as the mortar. | Input from the top or a side. Output from the bottom. |
| Bronze hopper | Five bronze ingots and a bronze chest. Moves one item every 16 ticks, half a vanilla hopper, and only while it is spinning. Costs 1 capacity. | The spout faces down or sideways. Every slot is open on every face. |

A vanilla hopper can use the quern and stump faces above. The vanilla hopper recipe stays in the game. The bronze hopper is the one that has to be turned.

A loaded chest can empty into the quern or the stump while the wheel turns. The yard still needs the player to fill that chest, and it still cannot turn a vein into ingots. That leap is Age 2.

#### Metallurgy loop

```text
Mine copper + zinc (a bronze pick reaches both, and gold)
  -> pack a crucible on the work stump
  -> fire it in the basic smelter or the lit forge
  -> bronze ingots
  -> heat, forge, and haft, or cast the bucket, chest, hopper, and machine parts
```

```text
Raw ore -> 16 hits on the stump or under the trip hammer -> 2 crushed ore -> smelt each into an ingot
```

Broken bronze does not yet recast in a crucible. Repair is the live loop. Recast is still open.

#### Food, water, and camp

- The powered quern takes the click-grind off bread. Farms stay manual.
- The bronze bucket moves source water. The clay jug remains the high-capacity early vessel, and it still cannot place a source.
- Snares and fish traps stay the food machines. Age 1 adds no animal industry.
- Thatch and wattle are still the camp. Bronze speeds the work. There is no hinge block, and no saw beyond the bone saw.

#### Automation ladder

| Age 0 | Age 1 (ships) | Age 2 (next) |
| --- | --- | --- |
| Passive wait: rack, trap, pit | A wheel, a shaft of at most 8, and a machine the player supplies | A step that refills itself |
| Isolated stations | A yard of one or two machines | A line: dig, crush, smelt, store |
| Human and weather power | Water power, plus a bellows on the forge | Iron machines on that same rotation lesson |

#### Proof of Bronze

Live `age1` tree. Every node parents to **First Bronze** except **Under Load**, which parents to **Wheel and Water**. First Bronze parents to Age 0 **Zinc Gleam**. The forge and the mill are parallel, because the bellows and the wheel can both be built before the first bronze ingot.

```text
age0/zinc_gleam
  age1/first_bronze                 inventory: bronze ingot
    age1/hafted_metal               inventory: #from-stone-to-stars:bronze_tools
    age1/wheel_and_water            granted when a water wheel's status is SPINNING
      age1/under_load               granted when a quern finishes a grind or a trip hammer finishes a forging
    age1/hotter_fire                granted when the player pumps a lit, formed forge
    age1/bronze_yard                goal: bronze tool + finished powered process + bellows pump
    age1/iron_stain                 goal: rusty mud, dried rusty mud, iron specks, raw iron, iron ore, or deepslate iron ore
```

`inventory_changed` starts listening when the parent completes. A bronze tool or a lump of rusty mud already in inventory counts the next time that stack changes. A wheel that is already spinning is granted on the next network update, which happens while it turns. Grants use `AgeAdvancements.grantNearby`: same dimension, within 16 blocks.

#### Discipline mapping for Age 1

| Discipline | Age 1 expression |
| --- | --- |
| Engineering | Shafts, gears, trip hammer, quern, bronze hopper |
| Energy | Water wheel; charcoal and the bellows |
| Chemistry | Crucible alloys, forge heat, crushed ore |
| Biology | Quern → bread at settlement scale |
| Civil engineering | Siting the mill on a river; the bronze chest |
| Science | Rotation status on Jade / WTHIT |
| Computing / aerospace | Absent |

#### Design rules for this age

- If bronze mines iron, Age 2 is optional. It must not.
- If a shaft can cross a whole base, Age 2 has nothing new to teach about reach. The cap is 8 blocks. Past that the machine is out of range. Too many machines overload the wheel. Shafts stay put, and gears pass the full turn.
- A hopper may load the quern or the stump. A vein that becomes ingots while the player is away is Age 2.
- If the water wheel spins in a still pool, siting is fake. It reads real flowing water.
- The drying pit keeps its clay, charcoal, and pottery jobs. The forge is for metal and blasting recipes.
- Tanning, snares, and pits stay worth building.
- Age 1 automation **turns**. Age 2 automation **feeds itself**.

#### Zinc and Bronze (summary)

**Bronze ingots** open the age. **Rotational power** is the first connected system. The yard is a mill beside a river. The factory arrives with iron.

### Age 2 Iron and Steel

Age 2 is the next chapter. Its theme is iron and steel, and its new verb is a line that feeds itself. The iron door ships in 0.8.0. Steel parts and the sawmill have been added since. The ironworks, the drill, and the `age2` advancements are the plan. They are not in the mod yet.

> **Age motto:** The yard learned to turn. The ironworks has to keep feeding itself.

Age 1 already spent the wheel's budget: capacity 1 or 2, each machine costs 1, and a machine farther than 8 blocks from the wheel is out of range. Age 2 does not raise those numbers. A weak stream runs the ironworks **or** the drill. A strong stream runs both, when the drill drops straight into the ironworks. That pair is the factory. A third spinning machine stalls it.

#### Entry and exit

| Gate | Requirement |
| --- | --- |
| **Enter Age 2** | **Iron Stain**, already an Age 1 goal. Dig rusty mud with a shovel. Dried rusty mud, iron specks, raw iron, iron ore, and deepslate iron ore count for the same advancement. |
| **First iron** | The live path below. Playable now. It is the door. |
| **Prove Iron** | Planned `age2` tree: an iron ingot, an iron pick, iron ore actually mined, a steel ingot, a spinning ironworks that finishes a smelt, and a spinning drill that breaks a block. |
| **Exit toward steam** | That proof. Steam owns boilers, pressure, pipes, and pumps. The first steel ingot belongs here, and it is spent on the machines. |

#### What already exists

This is the iron and steel a player can make today. It is a hand workshop that uses the Age 1 machines, plus the sawmill.

| Step | Live behaviour |
| --- | --- |
| Rusty mud | Generates as surface disks and as ore. A shovel digs it. This is the iron the player can reach before an iron pick exists. |
| Dry it | Blast rusty mud for 100 ticks into dried rusty mud. Dried mud is mined with a pick. |
| Specks | Forging stump: 1 dried rusty mud, 8 hits → 1 iron specks. |
| Iron ingots | Crucible: 6 iron specks → 2 iron ingots (200 ticks, 0.7 experience). |
| Found iron | Loot that would hand out ingots, nuggets, blocks, raw iron, iron tools, iron armor, or chainmail is removed. Block drops and end-city chests stay, so a chest cannot skip the stain. |
| Heat and forge | Blast an iron ingot into heated iron. It cools in 60 seconds off the fire. Forging: 3 heated iron, 4 hits → 2 tool heads. 4 heated iron, 4 hits → 2 plates. |
| Iron tools | Vanilla sword, pick, axe, shovel, hoe, and spear craft from an iron tool head, a nail, and a stick. The iron pick mines `#minecraft:needs_iron_tool`, including iron ore. |
| Iron armor | Vanilla helmet, chestplate, leggings, and boots craft from iron plates, leather strips, and nails. |
| Iron hammer and knife | Mod tools, from a tool head, a nail, and a stick. The hammer strikes the forging stump and flints gravel. The knife is a short blade. |
| Crushed iron | Once raw iron exists: 1 raw iron, 16 hits → 2 crushed iron, each smelted into an ingot. Copper, gold, and zinc follow the same pattern. |
| Vanilla blast furnace | Crafted from iron ingots around a basic smelter, on smooth stone. It is a hotter furnace the player can build. It is not the ironworks named in the age table. |
| Steel | Crucible: 6 iron ingots + 1 item from `#minecraft:coals` → 2 steel ingots (200 ticks, 0.7 experience). |
| Steel parts | Blast a steel ingot for 100 ticks into heated steel (same 60-second cool-down). Forging: 2 heated steel, 8 hits → 4 steel sheets. Shapeless craft: a hammer + 1 steel sheet → 1 steel blade. Four blades in a 2×2 → 1 steel saw blades block. |
| Crafting hammers | Hammerstone, bronze hammer, and iron hammer (`crafting_hammers`) return from a craft 1 durability more worn. A hammer on its last point is used up. |
| Sawmill | A formed multiblock and the first steel machine. Details below. |

There is no `age2` advancement tree yet. The tree in **Proof of Iron** is the one to write.

#### Sawmill

The sawmill shipped ahead of the ironworks. It is the steel saw from the earlier plan, built as a formed multiblock rather than one block. It replaces the bone saw's four clicks per log with a bed of rollers on the wheel.

| Piece | Recipe | Role |
| --- | --- | --- |
| Saw arbor | Planks, 1 steel ingot, 2 wooden shafts, 1 rotation gear | Controller. Turned from the back by a shaft. Holds the shared log and plank stores. |
| Steel saw blades | 4 steel blades | The blade wall. Each column is one lane. |
| Wooden rollers | 3 wooden shafts | The bed. Each roller is one storage slot. |
| Log intake | Planks around wooden rollers | Input end. All intakes share one log store. |
| Plank outfeed | Planks around wooden rollers | Output end. All outfeeds share one plank store. |

```text
side view (logs travel ->)         front view of the blade wall
         F  F  F  F                       F F F F F
         F  S                             F S S S F
I  R  R  S  R  R  O                       A S S S F
```

| Rule | Live behaviour |
| --- | --- |
| Blade wall | The arbor faces into steel saw blades 1–3 wide and 2–3 high, all facing along the line. |
| Frame | The arbor's column above or below it, the far side of the blades, and a beam across the top. Any log, plank, cobblestone, or stone bricks (`sawmill_frame`). |
| Bed | At the bottom of each blade column, 1–4 full rows of wooden rollers run out both ways along the line. |
| Ends | One end is full rows of log intakes, the other full rows of plank outfeeds. Up to 2 intake rows per infeed roller row and 4 outfeed rows per outfeed roller row. |
| Storage | Each infeed roller or intake holds 16 logs. Each outfeed roller or outfeed holds a stack of planks. |
| Forming | It assembles itself when the arbor turns, retrying every 2 seconds. Clicking the arbor shows its status or why it won't form. |
| Cutting | Each lane cuts one log into 6 planks per cycle. The cycle is 40 ticks at 2 blades high and 34 at 3. It stops when the arbor stops or the plank store has no room. |
| Loading | Logs go into any intake by hand or by hopper. Logs dropped on the infeed rollers are pulled in every 10 ticks. Hoppers can pull planks from any outfeed. Empty hand on an outfeed takes all planks. |
| Wheel cost | 1 per lane once formed (`SAW_ARBOR_COST_PER_LANE`). A weak wheel runs a 1-lane mill. A strong wheel runs a 2-lane mill, or a 1-lane mill and one other machine. A 3-lane mill overloads any single wheel. |

The ports animate logs and planks moving along the bed. JEI has a sawmill category and info pages for each piece. Jade and WTHIT do not have a sawmill-specific overlay yet.

#### Steel

Steel is the machine metal. It is not a new mining tier. Iron stays the tool that mines iron ore. A steel pick that opens diamond would skip the rest of the age table, so this age forges no steel tools and no steel armor.

The crucible recipe stays: 6 iron ingots + 1 `#minecraft:coals` → 2 steel ingots. Heated steel forges on the stump into **steel sheets**, and a hammer cuts a sheet into a **steel blade**. Sheets fill the role the earlier plan called plates. Blades currently go only into saw blades. A **steel rod** does not exist yet. Either add it for the drill, or build the drill from sheets and blades. Sheets, blades, and the saw arbor's ingot are the live uses of steel. The ironworks and the drill should be made from the same parts.

The ironworks can also run that same alloy when it is fed iron ingots and coal, so a finished line can make the parts for the next machine. The crucible recipe remains the hand way.

#### Wheel budget

Nothing in this age changes `RotationConstants`. Wooden shafts still stop at 8 blocks. Gears still pass the full turn. A machine still costs 1. A weak wheel still runs one machine, a strong wheel two.

| Machine | Cost | Role in the line |
| --- | --- | --- |
| Powered quern, trip hammer, bronze hopper | 1 each | Age 1. Still the yard. |
| Ironworks | 1 for the whole line | Crush, then smelt. |
| Drill | 1 | Bore a face and drop the pieces. |
| Vanilla hopper | 0 | Moves items and does not ask the wheel. |
| Sawmill (ships) | 1 per lane | Competes for a slot. Not part of the proof. |

The intended strong-wheel line is a gear, then two backs: the drill, and the ironworks under it. The drill's output is the block below it, which is the ironworks input. No bronze hopper sits between them. A bronze hopper in that gap would be a third machine, and the wheel would stall. A vanilla hopper under the ironworks takes the ingots away for free.

A weak wheel runs the ironworks alone. The player fills it, or a vanilla hopper does. The drill waits for a stronger stream or a second wheel.

#### Ironworks

This is the age's blast furnace. The vanilla blast furnace stays the hand station: iron ingots around a basic smelter, on smooth stone, loaded by the player. The forge and the bellows stay the hand smithy. The ironworks is the one that keeps going.

Shape, when it is built: three blocks in a row, formed in the world the way the forge is formed, and smaller on purpose.

```text
shaft -> [drive][crusher][hearth]
                items drop in from above the crusher
                ingots leave under the hearth
```

| Face | Rule |
| --- | --- |
| Back of the drive | Shaft enters here, opposite the way the line faces. Same rule as the trip hammer. |
| Top and sides of the crusher | Input. Raw ore, crushed ore, or iron ingots plus coal for steel. A player, a vanilla hopper, or the drill above. |
| Bottom of the hearth | Output. Ingot or steel. A vanilla hopper can pull. |
| Anywhere else | Closed. |

While the shaft spins and the hearth has charcoal or coal, the crusher runs the same forging recipes the stump uses, including 1 raw ore → 2 crushed pieces at 16 hits. The hearth then smelts those crushed pieces into ingots. The player does not move the pile between the two. An empty input keeps the last recipe, same rule as the forging stump, so the next stack restarts it.

The line carries its own draft. The player does not pump it. Hand bellows remain how the forge stays hot.

It stops when the shaft stops, the fuel is gone, or the output slot is full. It does not pull from chests on its own. Something has to put ore on the crusher: the player, a hopper, or the drill.

Fuel amounts and the crush-then-smelt timing wait for a balance pass. The first pass should be slower than standing at a trip hammer and a blast furnace yourself, so the machine wins by being left alone, not by being faster than hands.

#### Drill

One block. Steel sheets, an iron pick head, and either a new steel rod or steel blades, counts in the balance pass. The shaft enters the back. It bites the block it faces.

| Rule | Plan |
| --- | --- |
| What it breaks | Blocks an iron pick can break. It stops on diamond-tier blocks, air, and fluids. |
| How far | Up to 8 blocks forward, then it stops until the player moves it. The 8 matches the shaft cap. It is a bore, not a quarry. |
| How fast | Slower than a player swinging an iron pick. The pick stays the better tool in the hand. |
| Where the drops go | Into the inventory under the drill. That block should be the ironworks crusher. If it is not an inventory, the drops fall on the ground. |
| When it is idle | Shaft stopped, the 8 blocks are used up, or the next block is too hard. |

Rusty mud stays the first iron, dug with a shovel before any pick exists. The drill does not gather muddy disks. It is for the vein the iron pick has already proved the player can take.

#### What the old machine list means

An earlier pass of this age named pistons, springs, bearings, chains, pumps, and saws beside the drill. They are parts and later problems, not a second mod's worth of blocks.

| Name | In this plan |
| --- | --- |
| Steel | Sheets and blades ship. A rod is still open. The machines are made of them. |
| Blast furnace | The ironworks hearth. The vanilla blast furnace stays. |
| Piston, spring | Crafting pieces inside the drill head. Not a rotation piston, and not a replacement for the vanilla piston. |
| Bearing | The drill's mount on the shaft. Gears still turn corners. |
| Chain | No longer shaft. Wooden shafts stay capped at 8. |
| Pump | Steam. The ironworks carries its own draft. |
| Drill | The 8-block bore above. |
| Saw | Ships as the sawmill multiblock, ahead of the line. Logs into planks, far faster than the bone saw, 1 wheel slot per lane. Not part of Proof of Iron. The bone saw stays the early plank tool. |

#### The line

```text
flowing water -> wheel -> shaft (max 8) -> gear
    -> drill, dropping down
    -> ironworks, crushing then smelting
    -> vanilla hopper -> chest
```

A chest the player filled, a hopper, and a trip hammer is Age 1. This line still mines after that chest is empty, for as long as the face holds and the wheel turns. When the bore ends, the player moves the drill. That walk is the rest of the labour.

#### Proof of Iron

Planned tree. Not written into `data/` yet. Nodes under **First Iron** are parallel, except the goal, which wants all three proofs. The pick and the mill are not one forced order: steel can be alloyed before the first drill exists.

```text
age1/iron_stain
  age2/first_iron            inventory: iron ingot
    age2/hafted_iron         inventory: minecraft:iron_pickaxe
    age2/the_vein            granted when the player mines iron ore or deepslate iron ore
    age2/steel               inventory: steel ingot
    age2/ironworks           granted when a spinning ironworks finishes a smelt
    age2/bore                granted when a spinning drill breaks a block
    age2/proof_of_iron       goal: steel + ironworks finished + drill has bored
```

`inventory_changed` starts listening when the parent completes, same as Age 1. A stack already in inventory counts the next time it changes. Grants for the ironworks and the drill use `AgeAdvancements.grantNearby`: same dimension, within 16 blocks.

#### Implementation order

1. ~~Heated steel and steel parts on the forging stump.~~ Done: heated steel, sheets, blades, saw blades. A steel rod is still open.
2. Ironworks: the three-block line, one machine cost, hopper faces, crush then smelt, its own draft. **Next.**
3. Drill: 8-block bore, drops into the block below, iron-pick rules.
4. The `age2` advancements above.
5. ~~A steel saw.~~ Done early as the sawmill multiblock.

#### Design rules for this age

- If a village chest can hand the player an iron pick, rusty mud is optional. Keep found iron stripped.
- If steel is a second ingot with nothing to craft, it is not an age. Sheets and blades are the job. The machines are built out of them.
- If the sawmill makes planks cheap before the player has steel, every camp becomes a vanilla house. Keep it behind steel.
- If the "factory" is one hopper into the Age 1 quern, Age 1 already shipped. Age 2 removes the carry from crusher to furnace, and then the swing at the vein.
- The vanilla blast furnace and the forge stay. The ironworks earns its place by crushing and smelting in one spinning line.
- Shafts stay capped at 8. Wheel capacity stays 1 or 2. A part that raises either number has stolen Age 2's constraint.
- If the drill eats a chunk and walks itself to the next one, the player is a spectator. Eight blocks, then a walk.
- If steel mines diamond, later ages lose their door. Iron tier is the ceiling here.
- Pumps, pipes, pressure, and belts wait. This age is a riverside line.
- Rusty mud, the hand forge, the bellows, and the trip hammer stay worth using. A single ore and a repair are still hand work.

#### Discipline mapping for Age 2

| Discipline | Age 2 expression |
| --- | --- |
| Engineering | Steel sheets and blades, the sawmill, the drill head, the three-block ironworks |
| Energy | The same water wheel, now asked to run two machines when the stream is strong |
| Chemistry | Specks to iron, iron and coal to steel, crushed ore to ingots inside the line |
| Biology | Unchanged from Age 1 |
| Civil engineering | Fitting the drill on top of the ironworks inside eight blocks of the wheel |
| Science | Seeing which of the two machines stalled, and why the bore stopped |
| Computing / aerospace | Absent |

#### Iron and Steel (summary)

The stain, the crucible, and the forging stump already make iron, and an iron pick already reaches iron ore. Steel already forges into sheets and blades, and the sawmill is the first steel machine on the wheel. The plan spends steel next on an ironworks and a drill. A strong wheel turns both. A weak wheel turns one. The player still walks the drill forward when the eight blocks are gone.

### Steam

Steam makes engineering visible: boilers, pressure, pipes, fuel, and safety all matter. Failure should be consequential enough to demand attention without being punishingly destructive.

### Electricity and Electronics

Electricity retains steam as useful upstream infrastructure. Electronics adds decisions rather than merely faster machines: sensing, thresholds, timers, and programmable control.

### Automation through Stellar

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
| Bronze (Age 1) | Alloy a bronze ingot; haft a bronze tool; spin a water wheel in flowing water; finish a powered quern grind or a trip-hammer forging; pump the bellows on a lit forge. |
| Iron / mechanical line (Age 2) | Alloy an iron ingot from the stain; haft an iron pick and mine iron ore; alloy steel; run a spinning ironworks through a smelt; bore a block with a spinning drill. The ingot, the pick, steel parts, and the sawmill are playable. The ironworks, the drill, and the `age2` tree are planned, not built. |
| Steam | Build a steam engine; turn steam into mechanical power; run a multi-machine line on that power. The first steel ingot belongs to Age 2. |
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
| Early (Age 0–1) | Shelves, storage pots, bronze chests; a rotation-powered bronze hopper | Where do I keep materials, and how do I pass one stack into the mill? |
| Mechanical (Age 2) | Sawmill intakes and outfeeds share one store each and take hoppers. Planned: the drill drops into the ironworks; a vanilla hopper takes the ingots. No belts. | How do I fit two machines on one strong wheel, inside eight blocks of water? |
| Industrial | Item pipes and pumps | How do I route materials through a larger footprint? |
| Electronic | Addressed logistics network | How does demand choose the destination? |
| Advanced | Drones and mobile routing | How do I serve flexible or remote sites? |
| Space | Orbital logistics | How do I move cargo between ground and orbit? |
| Stellar | Interplanetary logistics | How do I coordinate distant production and travel times? |

## Power Generation

Power systems should be a series of choices, not a strict replacement ladder. Older sources remain useful for local, cheap, renewable, or dependable roles even when higher-density sources exist.

| Source | Strength | Constraint or tradeoff | Likely stage |
| --- | --- | --- | --- |
| Human and animal | Accessible and thematic | Low throughput; manual handling | Primitive (Age 0) |
| Water | Renewable and steady at suitable sites | Terrain-dependent; short mechanical distribution | Bronze (Age 1) to Iron |
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
flowing water -> wheel -> shaft (max 8) -> gear
    -> drill, dropping down
    -> ironworks (crush, then smelt)
    -> vanilla hopper -> chest
```

This is the Age 2 plan, not a recipe. A weak wheel runs the drill or the ironworks. A strong wheel runs both when the drill sits on the ironworks. The Age 1 yard is the wheel, the shaft, and the trip hammer, and that part ships. Belts are a later age.

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
| JEI | `jei` | Recipe viewer | `jei_mod_plugin` entrypoint | Live. Categories for tanning, charcoal pile, brick drying, forge assembly, manual bone sawing, grinding, crucible alloying, forging, sawmill, and world interactions (axe bark, work-stump hewing). Info pages cover the related blocks, including the quern, trip hammer, forging stump, bronze hopper, and every sawmill piece. |
| EMI | `emi` | Recipe viewer (preferred long-term on Fabric) | `emi` entrypoint, not registered yet | **Blocked:** no 26.2 Maven artifact. Placeholder: `FstsEmiPlugin`. |
| Jade | `jade` | Look-at overlay | `jade` entrypoint | Live. Pebbles show waterlogged state. Tanning racks show hide %. Lit kindling shows charcoal-pile burn. Placed bone saws show stroke progress. Snares and fish traps show bait. Forge and other multiblocks show assembly. Rotation blocks show spinning, stalled, or overloaded. |
| WTHIT | `wthit` | Look-at overlay | `wthit_plugins.json` | Live. Pebbles, tanning, charcoal piles, bone saws, snares, and rotation match Jade. Forge assembly is Jade only. |
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
| Vanilla integration | Implemented: wooden/stone/copper tool recipes, plank/boat/bed/chest/barrel/furnace/bucket/flint-and-steel/shield gates, leather loot replacement, log drops need an axe, hide bed, shelves as early storage. A bronze-fitted crafting table and a chest made from a bronze chest come back in Age 1. Found iron loot is stripped except block drops and end-city chests. Remaining bypass watchlist: bows, village beds, other container crafts. |
| Research interface | How are milestones surfaced: journal, tech tree, achievements, lab console, or a combination? |
| Failure and safety | How punishing should overpressure, radiation, heat, and power overloads be? What warning systems make failure fair? |
| Multiplayer | Are discoveries shared globally, per player, per team, or configurable? How do large networks perform on servers? |
| Scale and pacing | How long should each age last? Which ages may merge or split after prototype testing? |
| Compatibility | Optional integrations committed: Mod Menu, YACL, JEI, Jade, WTHIT, TR Energy (jar-in-jar API), AE2 detect-only. EMI waits on a 26.2 port. Shaft rotation is first-party. Remaining: other space mods, worldgen partners. |
| Accessibility | How can the UI explain complex networks without hiding the satisfaction of learning them? |
| Performance | What simulation limits, chunk-loading expectations, and visualisation tools are necessary for large factories? |

### Working Notes

Add dated decisions, prototype findings, balance targets, and deferred ideas here or in linked feature specifications. When changing a core principle, record why it changed and what systems it affects.

**2026-09-14 — Age 0 spec.** Early Primitive and Stone draft from pebble / flint / bone / hide prototypes. Superseded by later audits.

**2026-09-14 — Optional integrations.** Wired Mod Menu + YACL config, JEI plugin stub, Jade/WTHIT pebble overlay, and jar-in-jar Team Reborn Energy as the Fabric FE API. AE2 is detected only. EMI is documented but not registered until a 26.2 artifact exists. All remain `suggests`, not `depends`.

**2026-09-18 — Companion mods catch up.** JEI categories for tanning, charcoal pile, bone sawing, and world interactions. Jade/WTHIT progress overlays. Mod Menu 20.0.2.

**2026-09-20 — Prototype audit.** Documented gathering, hide, fire, mortar, bone saw, and basic smelter loops separately from long-term wishlist.

**2026-09-25 — Age 0 settlement pass.** Doc aligned to live content: work stump, hide bed, snares, fish trap, thatch, wattle, flint spear, shelves as early storage, Age 0 advancement tree, zinc/bronze naming (registries were still `tin`/`brass` on this date). Clay vessels next. Bronze ingots are the Age 1 door. Removed obsolete planned systems (knapping surface, scraper, quern, raft, native copper, fire-setting, animal hitch) from the Age 0 spec.

**2026-09-26 — Age 1 bronze design.** Full Age 1 section: soft metal + light automation (water wheel, short shafts, hand-fed trip hammer / quern, bellows). Explicitly not a factory age — Age 2 owns self-feeding lines. Proof milestones and power/logistics tables updated to match.

**2026-10-12 — Age 1 ships; Age 2 is iron and steel.** Doc aligned to 0.8.0. Age 1 is the bronze yard: forge, bellows, crucibles (8 and 32 uses), bronze tools, bucket, and chest, water wheel (capacity 1 or 2), shafts to 8 blocks, gears that pass the full turn, trip hammer (15 ticks a hit), powered quern (8 ticks a grind), bronze hopper (16 ticks a move, one machine cost). Crushed ore doubles a raw ore at 16 hits. The `age1` advancements parent off First Bronze, with the mill and the forge in parallel. Age 2's door ships: rusty mud to specks to iron, heated-iron tool heads and plates, vanilla iron tools and armor, iron knife and hammer, and a steel alloy nothing crafts with yet. Shafts stay put. Overload and the 8-block cap are the limits. Found iron loot stays stripped so the stain remains the door.

**2026-10-12 — Age 2 plan.** Steel is machine metal: heat it, forge plates and rods, build an ironworks and a drill. No steel tools, no diamond mining. The ironworks is a three-block line (drive, crusher, hearth), one machine cost, crush-then-smelt, its own draft, input top and sides, output bottom. The drill bores 8 blocks, iron-pick rules, and drops into the block below. A strong wheel runs both when the drill sits on the ironworks. A bronze hopper between them would stall the wheel. Vanilla hoppers do not cost rotation. Pumps, pipes, belts, longer shafts, and a higher wheel capacity wait. Proof of Iron is planned and not in the datapack. Saw is optional and later.

**2026-10-27 — Steel parts and the sawmill.** Heated steel (100-tick blast, 60-second cool-down) forges at 2 ingots and 8 hits into 4 steel sheets. A hammer cuts a sheet into a steel blade, and four blades make a steel saw blades block. Sheets take the place of the planned plates. No rod exists yet, so the drill recipe is open. Hammers in `crafting_hammers` now come back from crafts 1 durability more worn. The saw shipped ahead of the ironworks as a formed sawmill: a saw arbor, a blade wall 1–3 wide and 2–3 high framed in logs, planks, cobblestone, or stone bricks, 1–4 rows of wooden rollers each way, and log intakes and plank outfeeds at the ends. One log becomes 6 planks per lane per cycle (40 ticks, 34 at 3 high). It costs 1 rotation per lane, so wheel capacity still caps it at 2 lanes, and `RotationConstants` did not raise any limit. JEI covers it. Jade and WTHIT do not yet. The ironworks is next.

