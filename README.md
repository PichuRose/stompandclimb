Stomp & Climb is an addon for Pehkui. It offers a multitude of items, including rings to change your own height, collars to change others' height, and even socks and hard hats to protect others or yourself from your (now) deadlier steps. It also allows you to climb by holding various sticky items, like slime balls or string. You need to be 1/5th of your size or smaller! You can also create potions for the effects. Be wary of rusted copper, you might get an infection or a curse. Wax your copper rings!

I also have included a mod of a friend of mine, known as HyperCollider. This mod optimizes collision enough that you can become size 32 or even 64 if you so wish to push it. This is much higher than the norm of size 8 being feasible with Pehkui. Even with all of this, I would advise changing the config.json of your Pehkui mod.

Config I used to take the screenshot of me being 1024x:  
<div class="spoiler">{
	"minimumCameraDepth": 3.051850947599719E-5,
	"keepAllScalesOnRespawn": true,
	"scalesKeptOnRespawn": [],
	"accurateNetherPortals": true,
	"enableCommands": true,
	"enableDebugCommands": false,
	"scaledFallDamage": true,
	"scaledMotion": true,
	"scaledReach": true,
	"scaledAttack": true,
	"scaledDefense": true,
	"scaledHealth": true,
	"scaledItemDrops": true,
	"scaledProjectiles": true,
	"scaledExplosions": true,
	"base.minimum": 1.2621774483536189E-29,
	"base.maximum": 3.4028234663852886E38,
	"width.minimum": 1.2621774483536189E-29,
	"width.maximum": 3.4028234663852886E38,
	"height.minimum": 1.2621774483536189E-29,
	"height.maximum": 3.4028234663852886E38,
	"eye_height.minimum": 1.2621774483536189E-29,
	"eye_height.maximum": 64,
	"hitbox_width.minimum": 1.2621774483536189E-29,
	"hitbox_width.maximum": 32,
	"hitbox_height.minimum": 1.2621774483536189E-29,
	"hitbox_height.maximum": 32,
	"interaction_box_width.minimum": 1.401298464324817E-45,
	"interaction_box_width.maximum": 3.4028234663852886E38,
	"interaction_box_height.minimum": 1.401298464324817E-45,
	"interaction_box_height.maximum": 3.4028234663852886E38,
	"model_width.minimum": 1.401298464324817E-45,
	"model_width.maximum": 3.4028234663852886E38,
	"model_height.minimum": 1.401298464324817E-45,
	"model_height.maximum": 3.4028234663852886E38,
	"third_person.minimum": 1.401298464324817E-45,
	"third_person.maximum": 96,
	"motion.minimum": 1.401298464324817E-45,
	"motion.maximum": 32,
	"falling.minimum": 1.401298464324817E-45,
	"falling.maximum": 3.4028234663852886E38,
	"step_height.minimum": 1.401298464324817E-45,
	"step_height.maximum": 3.4028234663852886E38,
	"view_bobbing.minimum": 1.401298464324817E-45,
	"view_bobbing.maximum": 32,
	"visibility.minimum": 1.401298464324817E-45,
	"visibility.maximum": 3.4028234663852886E38,
	"jump_height.minimum": 1.401298464324817E-45,
	"jump_height.maximum": 3.4028234663852886E38,
	"flight.minimum": 1.401298464324817E-45,
	"flight.maximum": 3.4028234663852886E38,
	"reach.minimum": 1.401298464324817E-45,
	"reach.maximum": 64,
	"block_reach.minimum": 1.401298464324817E-45,
	"block_reach.maximum": 3.4028234663852886E38,
	"entity_reach.minimum": 1.401298464324817E-45,
	"entity_reach.maximum": 3.4028234663852886E38,
	"mining_speed.minimum": 1.401298464324817E-45,
	"mining_speed.maximum": 3.4028234663852886E38,
	"attack_speed.minimum": 1.401298464324817E-45,
	"attack_speed.maximum": 3.4028234663852886E38,
	"knockback.minimum": 1.401298464324817E-45,
	"knockback.maximum": 3.4028234663852886E38,
	"attack.minimum": 1.401298464324817E-45,
	"attack.maximum": 3.4028234663852886E38,
	"defense.minimum": 1.401298464324817E-45,
	"defense.maximum": 3.4028234663852886E38,
	"health.minimum": 1.401298464324817E-45,
	"health.maximum": 3.4028234663852886E38,
	"drops.minimum": 1.401298464324817E-45,
	"drops.maximum": 3.4028234663852886E38,
	"held_item.minimum": 1.401298464324817E-45,
	"held_item.maximum": 3.4028234663852886E38,
	"projectiles.minimum": 1.401298464324817E-45,
	"projectiles.maximum": 3.4028234663852886E38,
	"explosions.minimum": 1.401298464324817E-45,
	"explosions.maximum": 3.4028234663852886E38
}</div>

Also, if you are using Lithium, look at the hypercollider config. It lists what you need to put into the Lithium config, and if you do not do it, the mod will repeatedly crash.
<div class="spoiler"># with this config, you can disable individual mixins from hypercollider
# for compat with lithium it is recommended to disable the fluid collision and specialize cube mixins
#mixin.entity.FastFluids=false
#mixin.voxel.special.SpecializeCube=false
# in the lithium config, it is recommended to disable its collision optimizations
# paste "mixin.entity.collisions.movement=false" into lithium.properties to do that</div>

Also, I plan on releasing a 1.20.1 forge version. But, I will only keep versions updated as I see fit.

If you want to join my server, please go here:
https://discord.gg/nqE5efx4Ag
