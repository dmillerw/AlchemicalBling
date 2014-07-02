package dmillerw.bling.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

/**
 * @author dmillerw
 */
public class EntitySplashPotion extends EntityPotion {

	public EntitySplashPotion(World world) {
		super(world);
	}

	public EntitySplashPotion(World world, EntityLivingBase entity, int id) {
		super(world, entity, id);
	}

	public EntitySplashPotion(World world, EntityLivingBase entity, ItemStack stack) {
		super(world, entity, stack);
	}

	public EntitySplashPotion(World world, double posX, double posY, double posZ, int id) {
		super(world, posX, posY, posZ, id);
	}

	public EntitySplashPotion(World world, double posX, double posY, double posZ, ItemStack stack) {
		super(world, posX, posY, posZ, stack);
	}

	public EntitySplashPotion(EntityPotion potion) {
		this(potion.worldObj, potion.getThrower(), potion.getPotionDamage());
		this.motionX = potion.motionX;
		this.motionY = potion.motionY;
		this.motionZ = potion.motionZ;
	}

	@Override
	protected void onImpact(MovingObjectPosition mob) {
		if (!this.worldObj.isRemote) {
			List effects = Items.potionitem.getEffects(getPotionDamage());

			if (effects != null && !effects.isEmpty()) {
				AxisAlignedBB aabb = this.boundingBox.expand(4.0D, 2.0D, 4.0D);
				List items = this.worldObj.getEntitiesWithinAABB(EntityPotionAmulet.class, aabb);

				if (items != null && !items.isEmpty()) {
					Iterator iterator = items.iterator();
					while (iterator.hasNext()) {
						EntityPotionAmulet amulet = (EntityPotionAmulet) iterator.next();
						amulet.onPotionSplash((PotionEffect) effects.get(0)); //TODO Multiple effects
					}
				}
			}
		}

		super.onImpact(mob);
	}
}
