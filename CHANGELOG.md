# ➕ Additions
- Added seven gamerules for the purpose of configuring some of **Florum Sporum**'s server-side behaviors. Consider this the "first pass", and make sure to report any bugs you find!

|                  Registry Identifier                   | Description                                                                                                          | Default Value |   Value Bounds    |
|:------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------|:-------------:|:-----------------:|
|      `florum-sporum:spore_blossom_growth_chance`       | The chance for a spore blossom to advance one growth stage, per random-tick. Set to 0 to disable random-tick growth. |      0.1      |     0.0...1.0     |                                                 
|     `florum-sporum:spore_blossom_unfurl_interval`      | The time between spore blossom unfurling stages.                                                                     |   10 ticks    | 1...2,147,483,647 |                                                                  
|  `florum-sporum:spore_blossom_entity_check_interval`   | The time from when an entity leaves the spore blossom's collision box to when it begins unfurling.                   |   60 ticks    | 1...2,147,483,647 |
|   `florum-sporum:panda_default_max_sneeze_interval`    | The maximum time between passive panda sneezes. Set to 0 to disable.                                                 |  6000 ticks   | 0...2,147,483,647 |
|     `florum-sporum:panda_weak_max_sneeze_interval`     | The maximum time between passive weak panda sneezes. Set to 0 to disable.                                            |   500 ticks   | 0...2,147,483,647 |
| `florum-sporum:panda_spore_shower_max_sneeze_interval` | The maximum time between panda sneezes when in a spore shower. Set to 0 to disable.                                  |   100 ticks   | 0...2,147,483,647 |
|     `florum-sporum:panda_spore_shower_check_depth`     | How close the spore blossom needs to be in order to trigger panda sneezes.                                           |   8 blocks    | 1...2,147,483,647 |

---

# 🔧 Changes
- The codebase now uses official Mojang mappings in preparation (and practice!) for 26.1.