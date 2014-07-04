package dmillerw.bling.helper;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author dmillerw
 */
public class EventHelper {

	public static enum Type {
		FML, FORGE, BOTH
	}

	public static void batchRegister(Type type, Side side, Object ... objects) {
		for (Object obj : objects) {
			register(type, side, obj);
		}
	}

	public static void register(Type type, Object instance) {
		register(type, null, instance);
	}

	public static void register(Type type, Side side, Object instance) {
		if (side == null || side == FMLCommonHandler.instance().getEffectiveSide()) {
			if (type == Type.FML || type == Type.BOTH) {
				FMLCommonHandler.instance().bus().register(instance);
			}

			if (type == Type.FORGE || type == Type.BOTH) {
				MinecraftForge.EVENT_BUS.register(instance);
			}
		}
	}
}
