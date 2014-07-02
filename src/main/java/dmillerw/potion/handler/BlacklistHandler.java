package dmillerw.potion.handler;

import com.google.common.collect.Sets;
import net.minecraft.potion.Potion;

import java.util.Set;

/**
 * @author dmillerw
 */
public class BlacklistHandler {

	private static Set<Integer> blacklist = Sets.newHashSet();

	public static boolean canUse(Potion potion) {
		return canUse(potion.getId());
	}

	public static boolean canUse(int id) {
		return !blacklist.contains(id);
	}

	public static void blacklist(Potion potion) {
		blacklist(potion.getId());
	}

	public static void blacklist(int id) {
		blacklist.add(id);
	}
}
