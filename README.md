# Stackable shulkers fix
A simple addon to the [Carpet mod](https://www.curseforge.com/minecraft/mc-mods/carpet) that fixes the 1.20.5+ behaviour of stacked shulker boxes to be the same as in older versions, as well as some optional features.

## Features
> [!NOTE]  
Features marked as `intrusive` are modifying the standard behaviour in their default state. This is done to maintain compatibility with older versions of the mod.

### Fixed hopper transfers
Prevents hoppers from stacking shulker boxes, even if the max count is >1.  
*Setting this rule to `false` enables the functionality of this mod by preventing shulkers from being stacked by hoppers.*

- Name: `hopperShulkerStacking`
- Type: `boolean`
- Default: `false`
- Categories: `shulkerfix`, `bugfix`, `intrusive`
- Since: `1.5`

### Fixed comparator output
Makes stacked shulker boxes count as unstackable items, allowing comparator signal strength >15.

- Name: `overstackedShulkerSignalStrength`
- Type: `boolean`
- Default: `true`
- Categories: `shulkerfix`, `bugfix`, `intrusive`
- Since: `1.5`

### Legacy shulker item merging
Reintroduces the logic for merging shulker box items before 1.20.5.
The implementation allows partial item merging
*(normally the game doesn't merge item stacks if the total amount is more than the max size)*
and resets the entity age, allowing faster merging.
*(Both were bugs in Carpet, but no one complained, so now we're reintroducing the bugs huh)*

- Name: `legacyShulkerItemMerging`
- Type: `boolean`
- Default: `false`
- Categories: `shulkerfix`, `experimental`
- Since: `1.6`

### Hopper collection
Makes hoppers pick up one shulker at a time from an entity stack.
Note that this has a risk of breaking contraptions.

- Name: `hopperCollectSingleShulkers`
- Type: `boolean`
- Default: `false`
- Categories: `shulkerfix`, `feature`, `experimental`
- Since: `1.5`

### Hopper minecart collection
Makes minecarts pick up one shulker at a time from an entity stack.
Note that this has a risk of breaking contraptions.

- Name: `minecartCollectSingleShulkers`
- Type: `boolean`
- Default: `false`
- Categories: `shulkerfix`, `feature`, `experimental`
- Since: `1.5`

### Minecart slowdown
Disables the additional slowdown caused by overstacked minecarts.
The implementation makes shulker boxes count as 64-stackable when calculating the minecart slowdown. However if `overstackedShulkerSignalStrength` is enabled, reading the fill level will still output the overstacked value.  
*Setting this rule to `false` enables the functionality of this mod by removing the additional slowdown.*

- Name: `overstackedMinecartSlowdown`
- Type: `boolean`
- Default: `true`
- Categories: `shulkerfix`, `feature`, `experimental`
- Since: `1.5`

### Client count fix
Fixes [Carpet issue #1899](https://github.com/gnembon/fabric-carpet/issues/1899),
where vanilla clients see stacked shulker boxes as single items inside containers.
- Name: `clientShulkerSync`
- Type: `boolean`
- Default: `false`
- Categories: `shulkerfix`, `bugfix`, `experimental`
- Since: `1.4`

