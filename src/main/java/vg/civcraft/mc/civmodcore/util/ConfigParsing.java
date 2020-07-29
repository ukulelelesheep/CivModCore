package vg.civcraft.mc.civmodcore.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.common.collect.Lists;

import vg.civcraft.mc.civmodcore.areas.EllipseArea;
import vg.civcraft.mc.civmodcore.areas.GlobalYLimitedArea;
import vg.civcraft.mc.civmodcore.areas.IArea;
import vg.civcraft.mc.civmodcore.areas.RectangleArea;
import vg.civcraft.mc.civmodcore.itemHandling.ItemMap;

public class ConfigParsing {

	private static final Logger log = Bukkit.getLogger();

	/**
	 * Creates an itemmap containing all the items listed in the given config
	 * section
	 *
	 * @param config ConfigurationSection to parse the items from
	 * @return The item map created
	 */
	public static ItemMap parseItemMap(ConfigurationSection config) {
		ItemMap result = new ItemMap();
		if (config == null) {
			return result;
		}
		for (String key : config.getKeys(false)) {
			ConfigurationSection current = config.getConfigurationSection(key);
			if (current != null) {
				ItemStack item = config.getItemStack(key);
				result.addItemStack(item);
			}
		}
		return result;
	}
	
	public static int parseTimeAsTicks(String arg) {
		return (int) (parseTime(arg, TimeUnit.MILLISECONDS) / 50L);
	}

	public static long parseTime(String arg, TimeUnit unit) {
		long millis = parseTime(arg);
		return unit.convert(millis, TimeUnit.MILLISECONDS);
	}

	/**
	 * Parses a time value specified in a config. This allows to specify human
	 * readable time values easily, instead of having to specify every amount in
	 * ticks or seconds. The unit of a number specifed by the letter added after it,
	 * for example 5h means 5 hours or 34s means 34 seconds. Possible modifiers are:
	 * t (ticks), s (seconds), m (minutes), h (hours) and d (days)
	 * <p>
	 * Additionally you can combine those amounts in any way you want, for example
	 * you can specify 3h5m43s as 3 hours, 5 minutes and 43 seconds. This doesn't
	 * have to be sorted and may even list the same unit multiple times for
	 * different values, but the values are not allowed to be separated by anything
	 *
	 * @param input Parsed string containing the time format
	 * @return How many milliseconds the given time value is
	 */
	public static long parseTime(String input) {
		input = input.replace(" ", "").replace(",", "").toLowerCase();
		long result = 0;
		try {
			result += Long.parseLong(input);
			return result;
		} catch (NumberFormatException e) {
		}
		while (!input.equals("")) {
			String typeSuffix = getSuffix(input, Character::isLetter);
			input = input.substring(0, input.length() - typeSuffix.length());
			String numberSuffix = getSuffix(input, Character::isDigit);
			input = input.substring(0, input.length() - numberSuffix.length());
			long duration;
			if (numberSuffix.length() == 0) {
				duration = 1;
			} else {
				duration = Long.parseLong(numberSuffix);
			}
			switch (typeSuffix) {
				case "ms":
				case "milli":
				case "millis":
					result += duration;
					break;
				case "s": // seconds
				case "sec":
				case "second":
				case "seconds":
					result += TimeUnit.SECONDS.toMillis(duration);
					break;
				case "m": // minutes
				case "min":
				case "minute":
				case "minutes":
					result += TimeUnit.MINUTES.toMillis(duration);
					break;
				case "h": // hours
				case "hour":
				case "hours":
					result += TimeUnit.HOURS.toMillis(duration);
					break;
				case "d": // days
				case "day":
				case "days":
					result += TimeUnit.DAYS.toMillis(duration);
					break;
				case "w": // weeks
				case "week":
				case "weeks":
					result += TimeUnit.DAYS.toMillis(duration * 7);
					break;
				case "month": // weeks
				case "months":
					result += TimeUnit.DAYS.toMillis(duration * 30);
					break;
				case "y":
				case "year":
				case "years":
					result += TimeUnit.DAYS.toMillis(duration * 365);
					break;
				case "never":
				case "inf":
				case "infinite":
				case "perm":
				case "perma":
				case "forever":
					// 1000 years counts as perma
					result += TimeUnit.DAYS.toMillis(365 * 1000);
				default:
					// just ignore it
			}
		}
		return result;
	}

	private static String getSuffix(String arg, Predicate<Character> selector) {
		StringBuilder number = new StringBuilder();
		for (int i = arg.length() - 1; i >= 0; i--) {
			if (selector.test(arg.charAt(i))) {
				number.insert(0, arg.substring(i, i + 1));
			} else {
				break;
			}
		}
		return number.toString();
	}

	/**
	 * Parses a potion effect
	 *
	 * @param configurationSection ConfigurationSection to parse the effect from
	 * @return The potion effect parsed
	 */
	public static List<PotionEffect> parsePotionEffects(ConfigurationSection configurationSection) {
		List<PotionEffect> potionEffects = Lists.newArrayList();
		if (configurationSection != null) {
			for (String name : configurationSection.getKeys(false)) {
				ConfigurationSection configEffect = configurationSection.getConfigurationSection(name);
				String type = configEffect.getString("type");
				if (type == null) {
					log.severe("Expected potion type to be specified, but found no \"type\" option at "
							+ configEffect.getCurrentPath());
					continue;
				}
				PotionEffectType effect = PotionEffectType.getByName(type);
				if (effect == null) {
					log.severe("Expected potion type to be specified at " + configEffect.getCurrentPath()
							+ " but found " + type + " which is no valid type");
				}
				int duration = configEffect.getInt("duration", 200);
				int amplifier = configEffect.getInt("amplifier", 0);
				potionEffects.add(new PotionEffect(effect, duration, amplifier));
			}
		}
		return potionEffects;
	}

	public static IArea parseArea(ConfigurationSection config) {
		if (config == null) {
			log.warning("Tried to parse area on null section");
			return null;
		}
		String type = config.getString("type");
		if (type == null) {
			log.warning("Found no area type at " + config.getCurrentPath());
			return null;
		}
		int lowerYBound = config.getInt("lowerYBound", 0);
		int upperYBound = config.getInt("upperYBound", 255);
		String worldName = config.getString("world");
		if (worldName == null) {
			log.warning("Found no world specified for area at " + config.getCurrentPath());
			return null;
		}
		World world = Bukkit.getWorld(worldName);
		if (world == null) {
			log.warning("Found no world with name " + worldName + " as specified at " + config.getCurrentPath());
			return null;
		}
		Location center = null;
		if (config.isConfigurationSection("center")) {
			ConfigurationSection centerSection = config.getConfigurationSection("center");
			int x = centerSection.getInt("x", 0);
			int y = centerSection.getInt("y", 0);
			int z = centerSection.getInt("z", 0);
			if (world != null) {
				center = new Location(world, x, y, z);
			}
		}
		int xSize = config.getInt("xSize", -1);
		int zSize = config.getInt("zSize", -1);
		IArea area = null;
		switch (type) {
		case "GLOBAL":
			area = new GlobalYLimitedArea(lowerYBound, upperYBound, world);
			break;
		case "ELLIPSE":
			if (center == null) {
				log.warning("Found no center for area at " + config.getCurrentPath());
				return null;
			}
			if (xSize == -1) {
				log.warning("Found no xSize for area at " + config.getCurrentPath());
				return null;
			}
			if (zSize == -1) {
				log.warning("Found no zSize for area at " + config.getCurrentPath());
				return null;
			}
			area = new EllipseArea(lowerYBound, upperYBound, center, xSize, zSize);
			break;
		case "RECTANGLE":
			if (center == null) {
				log.warning("Found no center for area at " + config.getCurrentPath());
				return null;
			}
			if (xSize == -1) {
				log.warning("Found no xSize for area at " + config.getCurrentPath());
				return null;
			}
			if (zSize == -1) {
				log.warning("Found no zSize for area at " + config.getCurrentPath());
				return null;
			}
			area = new RectangleArea(lowerYBound, upperYBound, center, xSize, zSize);
			break;
		default:
			log.warning("Invalid area type " + type + " at " + config.getCurrentPath());
		}
		return area;
	}

	/**
	 * Parses a section which contains key-value mappings of a type to another type
	 * 
	 * @param <K>            Key type
	 * @param <V>            Value type
	 * @param parent         Configuration section containing the section with the values
	 * @param identifier     Config identifier of the section containing the entries
	 * @param logger         The logger to write in progress work to
	 * @param keyConverter   Converts strings to type K
	 * @param valueConverter Converts strings to type V
	 * @param mapToUse       The map to place parsed keys and values.
	 */
	public static <K, V> void parseKeyValueMap(ConfigurationSection parent, String identifier, Logger logger,
			Function<String, K> keyConverter, Function<String, V> valueConverter, Map<K, V> mapToUse) {
		if (!parent.isConfigurationSection(identifier)) {
			return;
		}
		ConfigurationSection section = parent.getConfigurationSection(identifier);
		for (String keyString : section.getKeys(false)) {
			if (!section.isString(keyString)) {
				logger.warning(
						"Ignoring invalid " + identifier + " entry " + keyString + " at " + section.getCurrentPath());
				continue;
			}
			K keyinstance;
			try {
				keyinstance = keyConverter.apply(keyString);
			} catch (IllegalArgumentException e) {
				logger.warning("Failed to parse " + identifier + " " + keyString + " at " + section.getCurrentPath()
						+ ": " + e.toString());
				continue;
			}
			V value = valueConverter.apply(section.getString(keyString));
			mapToUse.put(keyinstance, value);
		}
	}

}
