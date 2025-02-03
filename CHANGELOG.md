There are many fixes for bone meal in this update! A couple of simple oversights made the whole feature almost useless, which hopefully now should be resolved. 🌸

# ➕ Additions
`es_ES` translations have been added this update. Thanks, [Flatkat]!

# 🔧 Changes
In visual news, spore blossoms will once again sit higher up in inventory and hotbar slots. Previously they were somewhat hiding behind the item stack counts so I'm hoping this will improve visibility, especially in distinguishing the different states from one another.

Additionally, spore blossoms no longer exclusively generate naturally beneath moss blocks. In fact, the generation location changes have been entirely reverted to vanilla. This is based on the fact that, anecdotally, spore blossoms seemed overly rare and when I found them they were almost always fully grown (no doubt as a result of having spent a long time in this particular survival world). I'm hoping that by making this change, the spore blossom variation will be more "captured in time" so to speak. If you have feedback, be sure to write a feature suggestion on the [issues page][issues]!

The pink dye recipe output quantities have been rebalanced to fit better with the logic that the juvenile spore blossom states can be crafted just as easily as fully-grown ones. They have been brought down to match most small flowers, with the exception of a very slight boost for the supported [Create][create] milling and crushing recipes.

# 🐛 Fixes
- Spore blossoms should now correctly work with manual fertilization, fixing the following sub-issues:
  - They did not display "happy villager" particles.
  - They did not play the "crinkling bone meal" sound.
  - They did not consume an item from the bone meal item stack.
  - They did not emit the `ITEM_INTERACT_FINISH` game event.
- Spore blossoms should now correctly emit sculk sensor vibrations when recoiling and unfurling. 

[Flatkat]: https://github.com/flatkat
[issues]: https://github.com/axialeaa/FlorumSporum/issues
[create]: https://github.com/Fabricators-of-Create/Create
