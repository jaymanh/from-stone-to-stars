<p align="center">
  <img src="src/main/resources/assets/from-stone-to-stars/icon.png" alt="From Stone to Stars icon" width="96">
</p>

<h1 align="center">From Stone to Stars</h1>

<p align="center"><em>Build · Automate · Discover · Advance · Reach the stars</em></p>

**From Stone to Stars** is a Fabric overhaul mod for Minecraft about civilisation and technology. You start with pebbles, flint, and bone, and work your way up to an automated, spacefaring industry. The goal isn't a stronger pickaxe. It's replacing your own labour with systems you design.

> **Design motto:** Every age should introduce a new way to automate something that was previously manual.

## Status

| | |
| --- | --- |
| Version | 0.8.0 |
| Minecraft | 26.2 |
| Loader | Fabric Loader 0.19.5+, Fabric API |
| Java | 25 |
| Playable | **Age 0** (Primitive and Stone) and **Age 1** (Zinc and Bronze) |
| In progress | **Age 2** (Iron and Steel). The route to iron is playable. The ironworks, drill, and steel parts are not built yet. |

The mod is in active development. Balance, recipes, and save data may change between versions.

## What's in the mod

### Age 0: Primitive and Stone

The first age is a survival settlement. Wood and stone are materials, not tool tiers, so vanilla wooden and stone tools are removed.

- **Gather from the ground.** Pebbles and stick piles generate in the overworld. Grass and ferns give plant fibre. A hammerstone breaks gravel into flint.
- **Flint and bone tools.** Flint knife, axe, and spear. Bone tools are made from a head, a stick shaft, and a leather-strip binding. Logs only drop when cut with an axe.
- **Hideworking.** Mobs drop raw leather. A tanning rack dries it into leather, but rain resets the progress. A knife cuts leather into strips.
- **Woodwork.** Strip logs for bark and hew a work stump, which works as a 3×3 crafting table. A placed bone saw splits logs into planks.
- **Fire and clay.** Light kindling with a bow-and-drill. A charcoal pile turns logs into charcoal blocks. A brick drying pit fires clay bricks, jugs, and storage pots.
- **Food and water.** Snares, fish traps, mortar-and-pestle flour, dough, and bread. Water is carried in leather skins and clay jugs.
- **Shelter and storage.** Thatch, wattle and daub, and a hide bed that needs a roof when it rains. Storage is shelves and fragile storage pots.
- **Bronze.** Find zinc ore, pack copper and zinc into a clay crucible, and fire it in the basic smelter.

### Age 1: Zinc and Bronze

The camp grows into a workshop that turns.

- **Bronze gear.** Bronze tools, armour, buckets, and chests. Bronze picks mine gold-tier blocks but stay blocked from iron and diamond tiers.
- **The forge.** A multiblock you assemble in the world: a framed coal pad, hearths, bellows, and a chimney. It heats metal and fires crucibles. Pumping the bellows makes the fire burn longer.
- **Forging.** Heated ingots cool after 60 seconds off the fire. Choose a recipe on the forging stump and strike it with a hammer to make plates, blades, shafts, nails, and crushed ore. Crushed ore smelts into twice as many ingots.
- **Rotational power.** A water wheel in flowing water drives wooden shafts and gears to the trip hammer, powered quern, and bronze hopper. A weak wheel runs one machine and a strong wheel runs two. Machines farther than 8 blocks from the wheel are out of range.

### Age 2 door: Iron and Steel

You can make iron before you can mine iron ore. Dig rusty mud and dry it, forge it into iron specks, and alloy the specks into iron ingots. Iron tools, armour, a hammer, and a knife are forged from those ingots. Steel can already be alloyed, but nothing uses it yet.

### Vanilla changes

Because progression is gated, many vanilla shortcuts are removed or replaced:

- Wooden, stone, and copper tools, planks, the crafting table, furnace, chest, barrel, bucket, flint and steel, shield, boats, and wool beds have their recipes removed. Several come back later through mod recipes. For example, a bronze chest crafts into a vanilla chest.
- Iron ingots, nuggets, blocks, raw iron, iron and chainmail gear are removed from loot.
- Mob explosions don't break blocks, so nobody can blast past a gate.

The [Development Reference](From%20Stone%20to%20Stars%20Development%20Reference.md) has the full list of gates and every number.

## Roadmap

The full plan has twelve ages. Each one adds a new engineering problem and a new automation step.

| No. | Age | Theme | Signature automation | Status |
| --- | --- | --- | --- | --- |
| 0 | Primitive and Stone | Survival and settlement | Tanning rack, snares, fish traps, charcoal pile, brick drying pit | ✅ Playable |
| 1 | Zinc and Bronze | Soft metal and first motion | Water wheel → shaft → machine, fed by a turning hopper | ✅ Playable |
| 2 | Iron and Steel | Mechanical engineering | A drill feeding an ironworks on one strong wheel | 🚧 Iron route playable, machines planned |
| 3 | Steam | Industrial Revolution | Steam network feeding multiple machines | Planned |
| 4 | Electricity | Second Industrial Revolution | Electrical grid and powered machinery | Planned |
| 5 | Electronics | Information and control | Condition-based factory control | Planned |
| 6 | Automation | Mass production | Networked production cells and automated assembly | Planned |
| 7 | Modern | High technology | High-throughput specialised factories | Planned |
| 8 | Nuclear | High-density energy | Fuel cycle and managed reactor operations | Planned |
| 9 | Space | Leaving Earth | Automated launch and propellant infrastructure | Planned |
| 10 | Orbital | Industrialising space | Off-world supply chain and zero-gravity manufacture | Planned |
| 11 | Stellar | Spacefaring civilisation | Interplanetary and stellar-scale logistics | Planned |

### Next up: Age 2

- **Steel parts.** Heat steel ingots and forge them into steel plates and rods, the parts for the new machines.
- **Ironworks.** A three-block machine that crushes and then smelts. Ore goes in at the top and ingots come out the bottom. It can also alloy steel.
- **Drill.** Bores into the block face it is marked on and drops what it mines straight into the ironworks below it.
- **The wheel budget stays the same.** A weak wheel runs the ironworks *or* the drill. A strong wheel runs both, as long as the drill feeds the ironworks directly.
- **`age2` advancements.** Iron ingot, iron pick, mined iron ore, steel, a working ironworks, and a working drill.

### Further out

- **Steam** brings boilers, pressure, pipes, and pumps. Engineering becomes visible, and failures have consequences.
- **Electricity and electronics** keep steam useful upstream, then add sensors, logic, and programmable control.
- **The endgame** is a multi-stage *Civilisation Project*. You industrialise the planet, harness nuclear power, build orbital infrastructure, industrialise the solar system, build a stellar energy system, and finally build and launch a starship.

> You began with stone. You built a civilisation. Now build the future.

## Optional integrations

None of these are required. The game runs and Age 0 is playable without any of them.

| Mod | What it adds |
| --- | --- |
| [JEI](https://modrinth.com/mod/jei) | Recipe categories for tanning, charcoal piles, brick drying, forge assembly, bone sawing, grinding, crucible alloying, forging, and world interactions |
| [Jade](https://modrinth.com/mod/jade) / [WTHIT](https://modrinth.com/mod/wthit) | Look-at info for drying progress, bait, burn timers, multiblock assembly, and rotation status |
| [Mod Menu](https://modrinth.com/mod/modmenu) + [YACL](https://modrinth.com/mod/yacl) | In-game config screen |
| Team Reborn Energy | Bundled inside the mod jar as the shared Fabric energy API for future electrical machines |

EMI support is planned for when EMI releases a 26.2 build.

## Configuration

Settings live in `config/from-stone-to-stars.json`. You can edit the file directly, or use the Mod Menu screen if YACL is installed.

| Key | Default | Notes |
| --- | --- | --- |
| `generatePebbles` | `true` | Generates pebble patches in the overworld. Needs a restart, and only affects newly generated chunks. |
| `debugLogging` | `false` | Saved, but nothing uses it yet. |

## Building from source

```shell
git clone https://github.com/jaymanh/from-stone-to-stars.git
cd from-stone-to-stars
./gradlew build          # jar ends up in build/libs/
./gradlew runClient      # dev client, optional integrations included
./gradlew runDatagen     # regenerate tags into src/main/generated
```

You need JDK 25. The dev runs already load the optional integrations through `localRuntime`, so don't also copy them into `run/mods`.

## Contributing

The [Development Reference](From%20Stone%20to%20Stars%20Development%20Reference.md) is the design source of truth. Before adding a feature, check it against the feature checklist there:

- What manual activity does it reduce, replace, or make more interesting?
- Does it create a new engineering decision, or just a bigger number?
- Which existing resources, machines, or infrastructure does it keep relevant?
- Can the player see the system working, and diagnose why it isn't?

When you add a system, also update the JEI categories, the Jade/WTHIT providers, and the Development Reference.

## License

From Stone to Stars is released under the [GNU Lesser General Public License v3.0 only](LICENSE). The LGPL incorporates the [GNU General Public License v3.0](GPL-3.0.txt), which is included with the project.
