package dmillerw.bling.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dmillerw.bling.entity.EntityCrippledSpider;
import dmillerw.bling.entity.EntitySplashPotion;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

/**
 * @author dmillerw
 */
public class EntityEventHandler {

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPotion && !(event.entity instanceof EntitySplashPotion)) {
			EntitySplashPotion potion = new EntitySplashPotion((EntityPotion) event.entity);
			event.setCanceled(true);
			event.entity.setDead();
			event.entity.worldObj.spawnEntityInWorld(potion);
		}
	}

	@SubscribeEvent
	public void onEntityInteract(EntityInteractEvent event) {
		if (event.target instanceof EntitySpider && !(event.target instanceof EntityCrippledSpider) && !(event.target instanceof EntityCaveSpider)) {
			if (event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() == Items.shears) {
				EntityCrippledSpider.replace((EntitySpider) event.target, event.entityPlayer);
			}
		}
	}
}
