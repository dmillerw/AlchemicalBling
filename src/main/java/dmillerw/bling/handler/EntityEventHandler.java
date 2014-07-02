package dmillerw.bling.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dmillerw.bling.entity.EntitySplashPotion;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

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
}
