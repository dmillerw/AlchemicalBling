package dmillerw.bling.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dmillerw.bling.AlchemicalBling;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.brewing.PotionBrewedEvent;

/**
 * @author dmillerw
 */
public class BrewingHandler {

	public static final int GHAST_TEAR = 8192;
	public static final int GLOWSTONE = 32;

	public static ItemStack getIronResult(ItemStack stack) {
		if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("potion")) {
			if (stack.getItemDamage() == GHAST_TEAR || stack.getItemDamage() == GLOWSTONE) {
				NBTTagCompound tag = new NBTTagCompound();
				NBTTagCompound potion = new NBTTagCompound();
				if (stack.getItemDamage() == GHAST_TEAR) {
					potion.setBoolean("ghast", true);
				} else if (stack.getItemDamage() == GLOWSTONE) {
					potion.setBoolean("glow", true);
				}
				tag.setTag("potion", potion);
				stack.setTagCompound(tag);
			}
			stack.setItemDamage(0);
			return stack;
		} else {
			if (stack.getTagCompound().hasKey("potion")) {
				NBTTagCompound potion = stack.getTagCompound().getCompoundTag("potion");

				if (potion.getBoolean("ghast") && stack.getItemDamage() == GLOWSTONE) {
					return new ItemStack(AlchemicalBling.bottleSilver);
				} else if (potion.getBoolean("glow") && stack.getItemDamage() == GHAST_TEAR) {
					return new ItemStack(AlchemicalBling.bottleSilver);
				}
			}

			stack.setItemDamage(0);
			return stack;
		}
	}

	@SubscribeEvent
	public void onPotionBrewed(PotionBrewedEvent event) {
		for (int i = 0; i < event.brewingStacks.length; i++) {
			ItemStack stack = event.brewingStacks[i];
			if (stack != null && stack.getItem() == AlchemicalBling.bottleMoltenIron) {
				event.brewingStacks[i] = getIronResult(stack);
			}
		}
	}
}
