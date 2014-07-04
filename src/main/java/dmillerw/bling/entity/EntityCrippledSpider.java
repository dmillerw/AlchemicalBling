package dmillerw.bling.entity;

import dmillerw.bling.AlchemicalBling;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dmillerw
 */
public class EntityCrippledSpider extends EntitySpider {

	public static final int LEG_STATE_START = 17;

	public static final double MOVEMENT_SPEED = 0.800000011920929D;

	public static void replace(EntitySpider spider, EntityPlayer player) {
		EntityCrippledSpider crippledSpider = new EntityCrippledSpider(spider.worldObj);

		crippledSpider.copyDataFrom(spider, true);
		crippledSpider.copyLocationAndAnglesFrom(spider);

		spider.setDead();

		crippledSpider.worldObj.spawnEntityInWorld(crippledSpider);

		crippledSpider.interact(player);
	}

	public EntityCrippledSpider(World world) {
		super(world);
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataWatcher.addObject(LEG_STATE_START,     (byte)1);
		this.dataWatcher.addObject(LEG_STATE_START + 1, (byte)1);
		this.dataWatcher.addObject(LEG_STATE_START + 2, (byte)1);
		this.dataWatcher.addObject(LEG_STATE_START + 3, (byte)1);
		this.dataWatcher.addObject(LEG_STATE_START + 4, (byte)1);
		this.dataWatcher.addObject(LEG_STATE_START + 5, (byte)1);
		this.dataWatcher.addObject(LEG_STATE_START + 6, (byte)1);
		this.dataWatcher.addObject(LEG_STATE_START + 7, (byte)1);
	}

	@Override
	protected boolean interact(EntityPlayer player) {
		if (!worldObj.isRemote) {
			if (player.getHeldItem() != null && player.getHeldItem().getItem() == Items.shears) {
				List<Integer> legs = new ArrayList<Integer>();
				for (int i=0; i<8; i++) {
					if (dataWatcher.getWatchableObjectByte(LEG_STATE_START + i) != 0) {
						legs.add(i);
					}
				}

				if (legs.size() > 0) {
					int leg = LEG_STATE_START + legs.get(rand.nextInt(legs.size()));
					if (dataWatcher.getWatchableObjectByte(leg) != 0) {
						player.getHeldItem().damageItem(1, player);
						dataWatcher.updateObject(leg, (byte)0);

						dropItem(AlchemicalBling.spiderLeg, 1);

						getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(MOVEMENT_SPEED * ((float)(legs.size() - 1) / (float)8));

						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);

		for (int i=0; i<8; i++) {
			dataWatcher.updateObject(LEG_STATE_START + 1, nbt.getByte("leg" + i));
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);

		for (int i=0; i<8; i++) {
			nbt.setByte("leg" + i, dataWatcher.getWatchableObjectByte(LEG_STATE_START + i));
		}
	}
}
