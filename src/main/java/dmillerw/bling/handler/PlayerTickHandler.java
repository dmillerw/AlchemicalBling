package dmillerw.bling.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import dmillerw.bling.AlchemicalBling;
import net.minecraft.item.ItemStack;

/**
 * @author dmillerw
 */
public class PlayerTickHandler {

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		ItemStack boots = event.player.getCurrentArmor(0);

		if (boots != null && boots.getItem() == AlchemicalBling.bootsSpider) {
			if (event.phase == TickEvent.Phase.END) {
				if (event.player.isCollidedHorizontally) {
					event.player.motionY = 0.2D;
				}
			}
		}
	}
}
