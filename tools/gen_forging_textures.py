from PIL import Image
from pathlib import Path

OUT = Path(r"F:\Minecraft Coding\from-stone-to-stars\src\main\resources\assets\from-stone-to-stars\textures\item")
bronze = Image.open(OUT / "bronze_ingot.png").convert("RGBA")
copper = Image.open(OUT / "_ref_copper_ingot.png").convert("RGBA")


def sample_palette(img):
	colors = {}
	for x in range(img.width):
		for y in range(img.height):
			p = img.getpixel((x, y))
			if p[3] < 128:
				continue
			colors[p[:3]] = colors.get(p[:3], 0) + 1
	ranked = sorted(colors.items(), key=lambda kv: -kv[1])
	return [c for c, _ in ranked]


def roles(palette):
	by_luma = sorted(palette, key=lambda c: 0.299 * c[0] + 0.587 * c[1] + 0.114 * c[2])
	return {
		"dark": by_luma[0],
		"shadow": by_luma[max(0, len(by_luma) // 4)],
		"mid": by_luma[len(by_luma) // 2],
		"light": by_luma[min(len(by_luma) - 1, 3 * len(by_luma) // 4)],
		"hi": by_luma[-1],
	}


bp = sample_palette(bronze)
cp = sample_palette(copper)
br = roles(bp)
cr = roles(cp)
print("bronze roles", br)
print("copper roles", cr)

# Bronze-tinted heat (more yellow/gold) vs copper-tinted (more pink/red)
HEAT_BRONZE = {
	"dark": (100, 35, 8),
	"shadow": (170, 55, 12),
	"mid": (230, 110, 25),
	"light": (255, 185, 55),
	"hi": (255, 245, 190),
}
HEAT_COPPER = {
	"dark": (95, 18, 22),
	"shadow": (175, 35, 30),
	"mid": (230, 75, 45),
	"light": (255, 140, 95),
	"hi": (255, 230, 200),
}


def new_img():
	return Image.new("RGBA", (16, 16), (0, 0, 0, 0))


def put(img, x, y, rgb, a=255):
	if 0 <= x < 16 and 0 <= y < 16:
		img.putpixel((x, y), (*rgb, a))


def heated_from(src, heat):
	out = new_img()
	lums = []
	for x in range(16):
		for y in range(16):
			p = src.getpixel((x, y))
			if p[3] >= 128:
				lums.append(0.299 * p[0] + 0.587 * p[1] + 0.114 * p[2])
	lo, hi = min(lums), max(lums)
	span = max(1.0, hi - lo)
	for x in range(16):
		for y in range(16):
			p = src.getpixel((x, y))
			if p[3] < 128:
				continue
			t = (0.299 * p[0] + 0.587 * p[1] + 0.114 * p[2] - lo) / span
			if t > 0.75:
				c = heat["hi"]
			elif t > 0.55:
				c = heat["light"]
			elif t > 0.35:
				c = heat["mid"]
			elif t > 0.18:
				c = heat["shadow"]
			else:
				c = heat["dark"]
			out.putpixel((x, y), (*c, 255))
	for x, y in [(5, 5), (7, 4), (9, 5), (6, 6), (8, 5)]:
		if out.getpixel((x, y))[3] > 0:
			out.putpixel((x, y), (*heat["hi"], 255))
	return out


def draw_nail(r):
	img = new_img()
	for x in range(6, 10):
		put(img, x, 3, r["hi"])
		put(img, x, 4, r["light"])
	put(img, 5, 4, r["mid"])
	put(img, 10, 4, r["mid"])
	for y in range(5, 13):
		put(img, 7, y, r["mid"])
		put(img, 8, y, r["light"] if y % 2 == 0 else r["mid"])
		put(img, 6, y, r["shadow"])
		put(img, 9, y, r["shadow"])
	put(img, 7, 13, r["dark"])
	put(img, 8, 13, r["shadow"])
	put(img, 7, 14, r["dark"])
	return img


def draw_shaft(r):
	img = new_img()
	for y in range(2, 14):
		x = 6
		put(img, x, y, r["shadow"])
		put(img, x + 1, y, r["mid"])
		put(img, x + 2, y, r["light"])
		put(img, x + 3, y, r["mid"])
		put(img, x + 4, y, r["shadow"])
	for x in range(6, 11):
		put(img, x, 1, r["hi"])
		put(img, x, 14, r["dark"])
	return img


def draw_blade(r):
	img = new_img()
	for y in range(11, 15):
		put(img, 4, y, r["shadow"])
		put(img, 5, y, r["mid"])
		put(img, 6, y, r["light"])
	rows = {
		10: range(4, 9),
		9: range(4, 11),
		8: range(4, 12),
		7: range(5, 13),
		6: range(5, 13),
		5: range(6, 13),
		4: range(7, 13),
		3: range(8, 12),
		2: range(9, 11),
	}
	for y, xs in rows.items():
		xs = list(xs)
		for i, x in enumerate(xs):
			t = i / max(1, len(xs) - 1)
			if i == 0:
				c = r["shadow"]
			elif i == len(xs) - 1:
				c = r["hi"]
			elif t < 0.35:
				c = r["mid"]
			else:
				c = r["light"]
			put(img, x, y, c)
	return img


def draw_plate(r):
	img = new_img()
	for y in range(3, 13):
		for x in range(3, 13):
			if y == 3 or x == 3:
				c = r["hi"]
			elif y == 12 or x == 12:
				c = r["dark"]
			elif y == 4 or x == 4:
				c = r["light"]
			elif y == 11 or x == 11:
				c = r["shadow"]
			else:
				c = r["mid"] if (x + y) % 3 else r["light"]
			put(img, x, y, c)
	for y in range(6, 10):
		for x in range(6, 10):
			put(img, x, y, r["shadow"] if x == 6 or y == 6 else r["mid"])
	return img


def draw_hammer(r):
	img = new_img()
	wood_d, wood_m, wood_l = (90, 58, 30), (140, 95, 45), (180, 130, 70)
	for y in range(7, 15):
		put(img, 7, y, wood_d)
		put(img, 8, y, wood_m)
		put(img, 9, y, wood_l)
	for y in range(3, 8):
		for x in range(4, 13):
			if y == 3:
				c = r["hi"]
			elif y == 7:
				c = r["dark"]
			elif x == 4:
				c = r["shadow"]
			elif x == 12:
				c = r["light"]
			else:
				c = r["mid"]
			put(img, x, y, c)
	for x in range(4, 6):
		for y in range(4, 7):
			put(img, x, y, r["shadow"])
	for x in range(11, 13):
		for y in range(4, 7):
			put(img, x, y, r["hi"])
	return img


heated_from(bronze, HEAT_BRONZE).save(OUT / "heated_bronze_ingot.png")
heated_from(copper, HEAT_COPPER).save(OUT / "heated_copper_ingot.png")
draw_nail(br).save(OUT / "bronze_nail.png")
draw_nail(cr).save(OUT / "copper_nail.png")
draw_shaft(br).save(OUT / "bronze_shaft.png")
draw_shaft(cr).save(OUT / "copper_shaft.png")
draw_blade(br).save(OUT / "bronze_tool_blade.png")
draw_plate(br).save(OUT / "bronze_plate.png")
draw_hammer(br).save(OUT / "bronze_hammer.png")

ref = OUT / "_ref_copper_ingot.png"
if ref.exists():
	ref.unlink()

print("wrote textures")
for name in [
	"heated_bronze_ingot",
	"heated_copper_ingot",
	"bronze_nail",
	"copper_nail",
	"bronze_shaft",
	"copper_shaft",
	"bronze_tool_blade",
	"bronze_plate",
	"bronze_hammer",
]:
	im = Image.open(OUT / f"{name}.png")
	print(name, im.size, "opaque", sum(1 for p in im.getdata() if p[3] > 0))
