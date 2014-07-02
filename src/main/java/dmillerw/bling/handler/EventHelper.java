package dmillerw.bling.handler;

import net.minecraftforge.common.MinecraftForge;

/**
 * @author dmillerw
 */
public class EventHelper {

	public static void batchRegister(Object ... objects) {
		for (Object obj : objects) {
			MinecraftForge.EVENT_BUS.register(obj);
		}
	}
}
