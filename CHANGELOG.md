With what started as a simple hotfix for a skill issue in 1.4.0, this update is filled with fixes and changes of serious scale!

## ➕ Changes
Loot tables, item data and item models have all changed to allow you to collect each growth stage of spore blossom separately! Breaking a spore blossom will conserve its age in item form. As anticipated, you can still grow it to a later stage by placing it on moss or fertilizing it with bone meal.

In-line with these changes, the appearances of spore blossoms while in the inventory, being held and being worn in the head slot have been slightly tweaked to improve visibility.

In order to expand your building capabilities especially now all growth stages are obtainable, I have made the decision to adjust the spore particle behavior in accordance with the spore blossom block state. Some specifics:
- The concentration of particles is now directly proportional to the age of the spore blossom. Previously, juvenile spore blossoms emitted a dense but small cloud.
- The range is now always a 31 x 11 x 31 box centered on the spore blossom and aligned to the base. The only thing that will change in accordance with the spore blossom's state is the concentration of particles.

I hope this allows you to make more mindful decisions about the vibes you're trying to invoke when using spore blossoms in builds. I'm definitely open to feedback though! Pop me an issue report on [GitHub][github-issues] if you want me to see your idea!

## 🐛 Fixes
- Spore blossoms can now actually be crafted into pink dye in a crafting table.
- Spore blossoms are no longer held too low in the first-person view setting.
- Spore blossoms held in invisible item frames no longer clip into the surface of the block the item frame is placed on.
- Spore blossoms now play sounds when growing.
- Spore blossoms now correctly grow on moss blocks (instead of growing on anything except moss).
  - The blocks affecting this behavior are now defined in the new `florum-sporum:block/spore_blossom_can_grow_on` tag instead of being hard-coded.
- Milling and cutting recipes no longer print log errors when loading a world lacking either of those mods.
- The mod will no longer spit out log confirmations

[github-issues]: https://github.com/axialeaa/FlorumSporum/issues