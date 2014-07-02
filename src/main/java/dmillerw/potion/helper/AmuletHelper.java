package dmillerw.potion.helper;

import dmillerw.potion.handler.BlacklistHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

/**
 * @author dmillerw
 */
public class AmuletHelper {

	public static void replacePotionEffect(EntityPlayer player, int id, int amplifier) {
		boolean replace = false;
		for (Object o : player.getActivePotionEffects()) {
			PotionEffect effect = (PotionEffect) o;

			if (effect.getPotionID() == id && !(effect instanceof BaublePotionEffect)) {
				replace = true;
			}
		}

		if (replace) {
			player.removePotionEffect(id);
			addPotionEffect(player, id, amplifier);
		}
	}

	public static void addPotionEffect(EntityPlayer player, int id, int amplifier) {
		if (BlacklistHandler.canUse(id)) {
			if (!player.isPotionActive(id)) {
				player.addPotionEffect(new BaublePotionEffect(id, amplifier));
			} else {
				replacePotionEffect(player, id, amplifier);
			}
		}
	}

	public static void removePotionEffect(EntityPlayer player, int id) {
		player.removePotionEffect(id);
	}
}
