package vg.civcraft.mc.civmodcore.api;

import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

/**
 * Class of static APIs for Spawn Eggs.
 * */
public final class SpawnEggAPI {
	
	private static final BiMap<Material, EntityType> spawnEggs = ImmutableBiMap.<Material, EntityType>builder().
			put(Material.BAT_SPAWN_EGG, EntityType.BAT).
			put(Material.BLAZE_SPAWN_EGG, EntityType.BLAZE).
			put(Material.CAT_SPAWN_EGG, EntityType.CAT).
			put(Material.CAVE_SPIDER_SPAWN_EGG, EntityType.CAVE_SPIDER).
			put(Material.CHICKEN_SPAWN_EGG, EntityType.CHICKEN).
			put(Material.COD_SPAWN_EGG, EntityType.COD).
			put(Material.COW_SPAWN_EGG, EntityType.COW).
			put(Material.CREEPER_SPAWN_EGG, EntityType.CREEPER).
			put(Material.DOLPHIN_SPAWN_EGG, EntityType.DOLPHIN).
			put(Material.DONKEY_SPAWN_EGG, EntityType.DONKEY).
			put(Material.DROWNED_SPAWN_EGG, EntityType.DROWNED).
			put(Material.ELDER_GUARDIAN_SPAWN_EGG, EntityType.ELDER_GUARDIAN).
			put(Material.ENDERMAN_SPAWN_EGG, EntityType.ENDERMAN).
			put(Material.ENDERMITE_SPAWN_EGG, EntityType.ENDERMITE).
			put(Material.EVOKER_SPAWN_EGG, EntityType.EVOKER).
			put(Material.FOX_SPAWN_EGG, EntityType.FOX).
			put(Material.GHAST_SPAWN_EGG, EntityType.GHAST).
			put(Material.GUARDIAN_SPAWN_EGG, EntityType.GUARDIAN).
			put(Material.HORSE_SPAWN_EGG, EntityType.HORSE).
			put(Material.HUSK_SPAWN_EGG, EntityType.HUSK).
			put(Material.LLAMA_SPAWN_EGG, EntityType.LLAMA).
			put(Material.MAGMA_CUBE_SPAWN_EGG, EntityType.MAGMA_CUBE).
			put(Material.MOOSHROOM_SPAWN_EGG, EntityType.MUSHROOM_COW).
			put(Material.MULE_SPAWN_EGG, EntityType.MULE).
			put(Material.OCELOT_SPAWN_EGG, EntityType.OCELOT).
			put(Material.PANDA_SPAWN_EGG, EntityType.PANDA).
			put(Material.PARROT_SPAWN_EGG, EntityType.PARROT).
			put(Material.PHANTOM_SPAWN_EGG, EntityType.PHANTOM).
			put(Material.PIG_SPAWN_EGG, EntityType.PIG).
			put(Material.PILLAGER_SPAWN_EGG, EntityType.PILLAGER).
			put(Material.POLAR_BEAR_SPAWN_EGG, EntityType.POLAR_BEAR).
			put(Material.PUFFERFISH_SPAWN_EGG, EntityType.PUFFERFISH).
			put(Material.RABBIT_SPAWN_EGG, EntityType.RABBIT).
			put(Material.RAVAGER_SPAWN_EGG, EntityType.RAVAGER).
			put(Material.SALMON_SPAWN_EGG, EntityType.SALMON).
			put(Material.SHEEP_SPAWN_EGG, EntityType.SHEEP).
			put(Material.SHULKER_SPAWN_EGG, EntityType.SHULKER).
			put(Material.SILVERFISH_SPAWN_EGG, EntityType.SILVERFISH).
			put(Material.SKELETON_HORSE_SPAWN_EGG, EntityType.SKELETON_HORSE).
			put(Material.SKELETON_SPAWN_EGG, EntityType.SKELETON).
			put(Material.SLIME_SPAWN_EGG, EntityType.SLIME).
			put(Material.SPIDER_SPAWN_EGG, EntityType.SPIDER).
			put(Material.SQUID_SPAWN_EGG, EntityType.SQUID).
			put(Material.STRAY_SPAWN_EGG, EntityType.STRAY).
			put(Material.TRADER_LLAMA_SPAWN_EGG, EntityType.TRADER_LLAMA).
			put(Material.TROPICAL_FISH_SPAWN_EGG, EntityType.TROPICAL_FISH).
			put(Material.TURTLE_SPAWN_EGG, EntityType.TURTLE).
			put(Material.VEX_SPAWN_EGG, EntityType.VEX).
			put(Material.VILLAGER_SPAWN_EGG, EntityType.VILLAGER).
			put(Material.VINDICATOR_SPAWN_EGG, EntityType.VINDICATOR).
			put(Material.WANDERING_TRADER_SPAWN_EGG, EntityType.WANDERING_TRADER).
			put(Material.WITCH_SPAWN_EGG, EntityType.WITCH).
			put(Material.WITHER_SKELETON_SPAWN_EGG, EntityType.WITHER_SKELETON).
			put(Material.WOLF_SPAWN_EGG, EntityType.WOLF).
			put(Material.ZOMBIE_HORSE_SPAWN_EGG, EntityType.ZOMBIE_HORSE).
			put(Material.HOGLIN_SPAWN_EGG, EntityType.HOGLIN).
			put(Material.PIGLIN_SPAWN_EGG, EntityType.PIGLIN).
			put(Material.STRIDER_SPAWN_EGG, EntityType.STRIDER).
			put(Material.ZOGLIN_SPAWN_EGG, EntityType.ZOGLIN).
			put(Material.ZOMBIE_SPAWN_EGG, EntityType.ZOMBIE).
			put(Material.ZOMBIFIED_PIGLIN_SPAWN_EGG, EntityType.ZOMBIFIED_PIGLIN).
			put(Material.ZOMBIE_VILLAGER_SPAWN_EGG, EntityType.ZOMBIE_VILLAGER).
			build();

	private SpawnEggAPI() { } // Make the class effectively static

	/**
	 * Tests if a material is that of a spawn egg.
	 *
	 * @param material The material to test.
	 * @return Returns true if the material is that of a spawn egg.
	 * */
	public static boolean isSpawnEgg(Material material) {
		if (material == null) {
			return false;
		}
		return spawnEggs.containsKey(material);
	}

	/**
	 * Gets the spawned entity type for a spawn egg.
	 *
	 * @param material The material, must be a spawn egg otherwise it's a guaranteed null.
	 * @return Returns the entity type that will be spawned from the spawn egg, or null.
	 * */
	@Nullable
	public static EntityType getEntityType(Material material) {
		if (material == null) {
			return null;
		}
		return spawnEggs.get(material);
	}

	/**
	 * Gets the spawn egg material from an entity type.
	 *
	 * @param entityType The type of entity to match to the spawn egg.
	 * @return Returns a spawn egg material, or null.
	 */
	@Nullable
	public static Material getSpawnEgg(EntityType entityType) {
		if (entityType == null) {
			return null;
		}
		return spawnEggs.inverse().get(entityType);
	}

}
