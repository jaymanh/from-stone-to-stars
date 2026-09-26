"""Generate bronze-plated leather armor textures from vanilla leather + copper references."""
from pathlib import Path

from PIL import Image

VANILLA = Path(
	r"C:\Users\jayde\.gradle\caches\ng_execute\e1daac7f12035e5334223e37b07ec7ebb22a956a08a810ce3a7f71df162bb744\transformed\assets\minecraft\textures"
)
OUT_ITEM = Path(
	r"F:\Minecraft Coding\from-stone-to-stars\src\main\resources\assets\from-stone-to-stars\textures\item"
)
OUT_HUMAN = Path(
	r"F:\Minecraft Coding\from-stone-to-stars\src\main\resources\assets\from-stone-to-stars\textures\entity\equipment\humanoid"
)
OUT_LEGS = Path(
	r"F:\Minecraft Coding\from-stone-to-stars\src\main\resources\assets\from-stone-to-stars\textures\entity\equipment\humanoid_leggings"
)

BRONZE = [
	(111, 74, 24),
	(159, 103, 27),
	(196, 131, 38),
	(220, 160, 70),
	(255, 186, 112),
	(255, 213, 168),
]
LEATHER = [
	(55, 32, 18),
	(85, 50, 28),
	(115, 70, 38),
	(145, 95, 52),
	(170, 118, 68),
]


def luma(rgb):
	return 0.299 * rgb[0] + 0.587 * rgb[1] + 0.114 * rgb[2]


def map_to_palette(rgb, palette):
	t = luma(rgb)
	idx = int(max(0, min(len(palette) - 1, (t / 255.0) * (len(palette) - 1) + 0.5)))
	return palette[idx]


def recolor(src: Image.Image, palette) -> Image.Image:
	src = src.convert("RGBA")
	out = Image.new("RGBA", src.size, (0, 0, 0, 0))
	for x in range(src.width):
		for y in range(src.height):
			r, g, b, a = src.getpixel((x, y))
			if a < 16:
				continue
			nr, ng, nb = map_to_palette((r, g, b), palette)
			out.putpixel((x, y), (nr, ng, nb, a))
	return out


def hybrid_layer(leather: Image.Image, metal: Image.Image) -> Image.Image:
	"""Mostly leather, with bronze plate patches where copper is bright."""
	leather = leather.convert("RGBA")
	metal = metal.convert("RGBA")
	base = recolor(leather, LEATHER)
	plate = recolor(metal, BRONZE)
	out = base.copy()
	for x in range(leather.width):
		for y in range(leather.height):
			mr, mg, mb, ma = plate.getpixel((x, y))
			if ma < 80:
				continue
			ml = luma((mr, mg, mb))
			# Only stamp brighter copper areas as plates (leave leather straps/edges)
			if ml < 95:
				continue
			lr, lg, lb, la = base.getpixel((x, y))
			if la < 40:
				continue
			# Checker-ish rivet: every other plate pixel slightly darker
			if ((x + y) % 5) == 0:
				out.putpixel((x, y), BRONZE[1] + (255,))
			else:
				t = 0.8
				out.putpixel(
					(x, y),
					(
						int(lr * (1 - t) + mr * t),
						int(lg * (1 - t) + mg * t),
						int(lb * (1 - t) + mb * t),
						255,
					),
				)
	return out


def hybrid_item(leather_path: Path, overlay_path: Path | None, copper_path: Path) -> Image.Image:
	leather = Image.open(leather_path).convert("RGBA")
	base = recolor(leather, LEATHER)
	if overlay_path and overlay_path.exists():
		overlay = Image.open(overlay_path).convert("RGBA")
		for x in range(16):
			for y in range(16):
				r, g, b, a = overlay.getpixel((x, y))
				if a > 40:
					# stitching / rim stays leather-dark
					base.putpixel((x, y), (*map_to_palette((r, g, b), LEATHER[:3]), 255))
	copper = Image.open(copper_path).convert("RGBA")
	plate = recolor(copper, BRONZE)
	out = base.copy()
	for x in range(16):
		for y in range(16):
			cr, cg, cb, ca = plate.getpixel((x, y))
			if ca < 80:
				continue
			if luma((cr, cg, cb)) < 100:
				continue
			lr, lg, lb, la = out.getpixel((x, y))
			if la < 40:
				continue
			# Keep leather border: skip outer rim pixels that are dark leather
			if luma((lr, lg, lb)) < 70 and (x in (0, 15) or y in (0, 15)):
				continue
			t = 0.75
			# rivet dots
			if (x % 4 == 1) and (y % 4 == 1):
				out.putpixel((x, y), (*BRONZE[0], 255))
			else:
				out.putpixel(
					(x, y),
					(
						int(lr * (1 - t) + cr * t),
						int(lg * (1 - t) + cg * t),
						int(lb * (1 - t) + cb * t),
						255,
					),
				)
	return out


def main():
	OUT_ITEM.mkdir(parents=True, exist_ok=True)
	OUT_HUMAN.mkdir(parents=True, exist_ok=True)
	OUT_LEGS.mkdir(parents=True, exist_ok=True)

	human = hybrid_layer(
		Image.open(VANILLA / "entity/equipment/humanoid/leather.png"),
		Image.open(VANILLA / "entity/equipment/humanoid/copper.png"),
	)
	legs = hybrid_layer(
		Image.open(VANILLA / "entity/equipment/humanoid_leggings/leather.png"),
		Image.open(VANILLA / "entity/equipment/humanoid_leggings/copper.png"),
	)
	human.save(OUT_HUMAN / "bronze.png")
	legs.save(OUT_LEGS / "bronze.png")

	pieces = [
		("bronze_helmet", "leather_helmet", "leather_helmet_overlay", "copper_helmet"),
		("bronze_chestplate", "leather_chestplate", "leather_chestplate_overlay", "copper_chestplate"),
		("bronze_leggings", "leather_leggings", "leather_leggings_overlay", "copper_leggings"),
		("bronze_boots", "leather_boots", "leather_boots_overlay", "copper_boots"),
	]
	for out_name, leather, overlay, copper in pieces:
		img = hybrid_item(
			VANILLA / "item" / f"{leather}.png",
			VANILLA / "item" / f"{overlay}.png",
			VANILLA / "item" / f"{copper}.png",
		)
		img.save(OUT_ITEM / f"{out_name}.png")
		print("wrote", out_name)

	print("done")


if __name__ == "__main__":
	main()
