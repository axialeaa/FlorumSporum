So sorry for the delay folks! I've been busy with life stuff and other projects. This small update probably isn't that interesting for most of you as the majority of changes I made have been to the code structure instead of testable behavior. Before talking about that, I need to make a very important announcement:

# 🪦 End-of-Life Versions
A little while ago, I stopped supporting versions below 1.20 due to multi-version projects becoming more and more difficult to maintain as the game itself updates faster. Well, I plan to do it again. Assuming no further hotfix, Update 1.7.0 will be the final release for the following versions:

- 1.20.2
- 1.20.4
- 1.20.6

These versions don't have very much demand compared to 1.20.1, 1.21.1 and the latest drop, so those will be staying. I'm also keeping 1.21.3 and 1.21.4 to allow the players on those versions some time to migrate to 1.21.5 if they so choose. The versions mentioned will be deprecated unless a hotfix needs to happen before deprecation. Make sure to report any issues you encounter on [GitHub][issues]; the clock is ticking!
***

Now onto the content...

# ➖ Removals
The in-built 32x resource pack has been removed in this update. Simply put, managing built-in resource packs on multi-version projects like this one is freaking annoying, and they somewhat bloat the resource pack menu. Fret not though, I plan to publish a standalone resource pack on my Modrinth profile which you can download and install to upscale the textures of all of my mods at the same time. It will also be handled on a separate GitHub repository which you will all be able to contribute to!

It's not quite ready yet, but I will work on getting it out as soon as I can after updating each of my mods to 1.21.5. Thank you for your patience! ~🌸

# ➕ Additions
`es_MX` translations have been added in this update. Thanks, [CerealConJugo]!

# 🔧 Changes
- Spore particles now perform raycasts using the "outline" algorithm instead of the "collision" one.
  - This change was made to slightly improve performance, but it does minimally affect behavior; you should expect fewer particles in areas with lots of foliage (e.g. long grass, vines, flowers). If this is a make-or-break for you, consider writing an issue report letting me know why you think this should be re-added!

[CerealConJugo]: https://github.com/cerealconjugo
[issues]: https://github.com/axialeaa/FlorumSporum/issues
