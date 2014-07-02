package dmillerw.bling.entity;

import dmillerw.bling.handler.BlacklistHandler;
import dmillerw.bling.helper.BaublePotionHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class EntityBaublePotion extends EntityItem {

	public EntityBaublePotion(World world) {
		super(world);
	}

	public EntityBaublePotion(World world, double posX, double posY, double posZ) {
		super(world, posX, posY, posZ);
	}

	public EntityBaublePotion(World world, double posX, double posY, double posZ, ItemStack stack) {
		super(world, posX, posY, posZ, stack);
	}

	public EntityBaublePotion(EntityItem item) {
		this(item.worldObj, item.posX, item.posY, item.posZ, item.getEntityItem());
		this.delayBeforeCanPickup = 10;
		this.motionX = item.motionX;
		this.motionY = item.motionY;
		this.motionZ = item.motionZ;
	}

	public void onPotionSplash(PotionEffect effect) {
		if (BlacklistHandler.canUse(effect.getPotionID())) {
			ItemStack stack = getEntityItem();
			BaublePotionHelper.addPotionEffectToItem(stack, effect.getPotionID(), effect.getAmplifier());
		}
	}
}
