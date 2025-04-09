This update doesn't have very many notable changes for most users, but I have cleaned up the codebase pretty significantly since the previous version. First, an important announcement:

# 🪦 End-of-Life Versions
A little while ago, I stopped supporting versions below 1.20 due to multiple versions becoming more and more difficult to maintain as the game itself updates faster. Well, I plan to do it again. Assuming no further hotfix, Update 1.5.0 will be the final release for the following versions:

1.20.2
1.20.4
1.20.6
These versions don't have very much demand compared to 1.20.1, 1.21.1 and the latest drop, so those will be staying. I'm also keeping 1.21.3 and 1.21.4 to allow the players on those versions some time to migrate to 1.21.5 if they so choose. The versions mentioned will be deprecated unless a hotfix needs to happen before deprecation. Make sure to report any issues you encounter on [GitHub][issues]; the clock is ticking!
***

Now onto the content...

# ➖ Removals
The in-built 32x resource pack has been removed in this update. Simply put, managing built-in resource packs on multi-version projects like this one is freaking annoying, and they somewhat bloat the resource pack menu. Fret not though, I plan to publish a standalone resource pack on my Modrinth profile which you can download and install to upscale the textures of all of my mods at the same time. It will also be handled on a separate GitHub repository which you will all be able to contribute to!

It's not quite ready yet, but I will work on getting it out as soon as I can after updating each of my mods to 1.21.5. Thank you for your patience! ~🌸

# ➕ Additions
`es_MX` translations have been added this update. Thanks, [CerealConJugo]!

# 🔧 Changes
- Spore blossoms will now only recoil when touched by an animate, living entity.
  - This change was made to prevent fully-grown spore blossoms recoiling when being fertilized with bone meal. The items they drop are no longer valid entities.
  - This is subject to change: I'm still messing around!

# 🐛 Fixes
- Spore blossoms should now correctly work with manual fertilization, fixing the following sub-issues:
  - They did not display "happy villager" particles.
  - They did not play the "crinkling bone meal" sound.
  - They did not consume an item from the bone meal item stack.
  - They did not emit the `ITEM_INTERACT_FINISH` game event.
- Spore blossoms should now correctly emit sculk sensor vibrations when recoiling and unfurling. 

[CerealConJugo]: https://github.com/cerealconjugo
[issues]: https://github.com/axialeaa/FlorumSporum/issues
