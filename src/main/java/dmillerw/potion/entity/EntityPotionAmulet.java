package dmillerw.potion.entity;

import dmillerw.potion.AlchemicalBling;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class EntityPotionAmulet extends EntityItem {

	public EntityPotionAmulet(World world) {
		super(world);
	}

	public EntityPotionAmulet(World world, double posX, double posY, double posZ) {
		super(world, posX, posY, posZ);
	}

	public EntityPotionAmulet(World world, double posX, double posY, double posZ, ItemStack stack) {
		super(world, posX, posY, posZ, stack);
	}

	public EntityPotionAmulet(EntityItem item) {
		this(item.worldObj, item.posX, item.posY, item.posZ, item.getEntityItem());
		this.delayBeforeCanPickup = 100;
		this.motionX = item.motionX;
		this.motionY = item.motionY;
		this.motionZ = item.motionZ;
	}

	public void onPotionSplash(PotionEffect effect) {
		ItemStack stack = new ItemStack(AlchemicalBling.infusedAmulet, 1, effect.getPotionID());
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("amp", effect.getAmplifier());
		stack.setTagCompound(nbt);
		this.setEntityItemStack(stack);
	}
}
