package dmillerw.bling.helper;

import dmillerw.bling.handler.BlacklistHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

/**
 * @author dmillerw
 */
public class BaublePotionHelper {

	public static void addPotionEffectToItem(ItemStack stack, int id, int amplifier) {
		NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
		nbt.setInteger("id", id);
		nbt.setInteger("amp", amplifier);
		stack.setTagCompound(nbt);
	}

	public static BaublePotionEffect getPotionEffectFromItem(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("id")) {
			return new BaublePotionEffect(stack.getTagCompound().getInteger("id"), stack.getTagCompound().getInteger("amp"));
		} else {
			return null;
		}
	}

	public static void replacePotionEffect(EntityPlayer player, BaublePotionEffect baubleEffect) {
		boolean replace = false;
		for (Object o : player.getActivePotionEffects()) {
			PotionEffect effect = (PotionEffect) o;

			if (effect.getPotionID() == baubleEffect.getPotionID() && !(effect instanceof BaublePotionEffect)) {
				replace = true;
			}
		}

		if (replace) {
			player.removePotionEffect(baubleEffect.getPotionID());
			addPotionEffect(player, baubleEffect);
		}
	}

	public static void addPotionEffect(EntityPlayer player, BaublePotionEffect effect) {
		if (BlacklistHandler.canUse(effect.getPotionID())) {
			if (!player.isPotionActive(effect.getPotionID())) {
				player.addPotionEffect(effect);
			} else {
				replacePotionEffect(player, effect);
			}
		}
	}

	public static void removePotionEffect(EntityPlayer player, BaublePotionEffect effect) {
		player.removePotionEffect(effect.getPotionID());
	}
}
