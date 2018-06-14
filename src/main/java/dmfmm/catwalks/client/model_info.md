Model in blockstate: `"modid:resource/path!.postfix!catwalks:catwalk"`
(postfix used for stuff like .obj models. Just use !! if you don't need a postfix)

Models are split into quadrants. Just like a connected texture, these quadrants are stitched together to make the final model.

Due to technical limitations each quadrant must be distinct in the json, meaning rails must be split in half, floors in quarters, etc. But this system has the benefit of having only two model files and a very simple structure.

## Item
(only used for the item, unused for the block)

```
// Resource location: "modid:resource/path/complete.postfix"
//
// 0, 0
// ├─────────────┐
// │╔═══════════╗│
// │║           ║│
// │║           ║│
// │║           ║│
// │║           ║│
// │╚═══════════╝│
// └─────────────┤
//          16, 16
```

## Rail

```
// Resource location: "modid:resource/path/rails.postfix"
//
// -16, -16
// ├─────────────┬─────────────┬─────────────┐ +x
// │             │║           ║│╦           ╦│
// │             │║           ║│║           ║│
// │      0      │║     1     ║│║     2     ║│
// │             │║           ║│║           ║│
// │             │║           ║│║           ║│
// │             │║           ║│╩           ╩│
// ├─────────────┼─────────────┼─────────────┤
// │═════════════│╝           ╚│             │
// │             │             │             │
// │      3      │      4      │             │
// │             │             │             │
// │             │             │             │
// │═════════════│╗           ╔│             │
// ├─────────────┼─────────────┼─────────────┤
// │╠═══════════╣│             │╔═══════════╗│
// │             │             │║           ║│
// │      5      │             │║     6     ║│
// │             │             │║           ║│
// │             │             │║           ║│
// │╠═══════════╣│             │╚═══════════╝│
// └─────────────┴─────────────┴─────────────┤
// +z                                   32, 32
```
Key:

0. Middle
1. Z axis connected rails
2. Z axis end rails
3. X axis connected rails
4. Inner corner rails
5. X axis end rails
6. Outer corner rails

Here's an image showing where each piece is used. 0 should probably be empty for the rail model, as I don't know what you'd want to put there, but is shown in the image as a reference for where that part is used:
![](http://i.imgur.com/IN47S7h.png)
## Floor

```
// Resource location: "modid:resource/path/floor.postfix"
//
// -16, -16
// ├─────────────┬─────────────┬─────────────┐ +x
// │             │║           ║│             │
// │             │║           ║│             │
// │      0      │║     1     ║│             │
// │             │║           ║│             │
// │             │║           ║│             │
// │             │║           ║│             │
// ├─────────────┼─────────────┼─────────────┤
// │═════════════│╝           ╚│             │
// │             │             │             │
// │      1      │      2      │             │
// │             │             │             │
// │             │             │             │
// │═════════════│╗           ╔│             │
// ├─────────────┼─────────────┼─────────────┤
// │             │             │╔═══════════╗│
// │             │             │║           ║│
// │             │             │║     3     ║│
// │             │             │║           ║│
// │             │             │║           ║│
// │             │             │╚═══════════╝│
// └─────────────┴─────────────┴─────────────┤
// +z                                   32, 32
```
Key

0. Middle floor
1. Z axis edged floor
2. X axis edged floor
3. Inner corner floor
4. Outer corner floor

The floor is exactly the same as a CTM texture, so just look that up.
