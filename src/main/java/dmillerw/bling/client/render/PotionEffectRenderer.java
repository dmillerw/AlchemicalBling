package dmillerw.bling.client.render;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import dmillerw.bling.helper.BaublePotionEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author dmillerw
 */
public class PotionEffectRenderer {

	private static final ResourceLocation INVENTORY = new ResourceLocation("textures/gui/container/inventory.png");

	private static final String[] GUI_LEFT = new String[] {"guiLeft", "field_147003_i", "i"};
	private static final String[] GUI_TOP = new String[] {"guiTop", "field_147009_r", "r"};

	private static Field guiLeftField;
	private static Field guiTopField;

	private int screenWidth;
	private int screenHeight;

	private int guiLeft;
	private int guiTop;

	private boolean dimensionsSet = false;
	private boolean ignoreNext = false;

	@SubscribeEvent
	public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
		if (!(event.gui instanceof GuiContainer)) {
			return;
		}

		if (ignoreNext) {
			ignoreNext = false;
			return;
		}

		/*
		For whatever reason, GuiContainerCreative fires the InitGuiEvent twice, EXCEPT when the screen
		resizes, so this is all to counter that
		 */

		if (event.gui instanceof GuiContainerCreative) {
			int width = Minecraft.getMinecraft().displayWidth;
			int height = Minecraft.getMinecraft().displayHeight;

			/*
			If this is the first time the creative gui is opened, or the dimensions match, then we
			ignore the next initGui call
			Otherwise, the screen was resized, and we can let it pass through
			 */

			if (!dimensionsSet || (width == screenWidth && height == screenHeight)) {
				ignoreNext = true;
			}
		}

		/*
		Actual setting of the dimensions. Could be done in the creative block
		But meh
		 */

		dimensionsSet = true;
		screenWidth = Minecraft.getMinecraft().displayWidth;
		screenHeight = Minecraft.getMinecraft().displayHeight;

		/*
		End of creative check block
		 */

		if (guiLeftField == null || guiTopField == null) {
			guiLeftField = ReflectionHelper.findField(GuiContainer.class, GUI_LEFT);
			guiTopField = ReflectionHelper.findField(GuiContainer.class, GUI_TOP);
		}

		try {
			guiLeft = guiLeftField.getInt(event.gui);
			guiTop = guiTopField.getInt(event.gui);
		} catch (IllegalAccessException ex) {
			// ERROR
		}
	}

	@SubscribeEvent
	public void onGuiDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
		Minecraft mc = Minecraft.getMinecraft();

		if (!(event.gui instanceof InventoryEffectRenderer)) {
			return;
		}

		int i = this.guiLeft - 124;
		int j = this.guiTop;

		Collection collection = mc.thePlayer.getActivePotionEffects();

		if (!collection.isEmpty()) {

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			int k = 33;

			if (collection.size() > 5) {
				k = 132 / (collection.size() - 1);
			}

			for (Iterator iterator = mc.thePlayer.getActivePotionEffects().iterator(); iterator.hasNext(); j += k) {
				PotionEffect potioneffect = (PotionEffect) iterator.next();

				if (potioneffect instanceof BaublePotionEffect) {
					mc.getTextureManager().bindTexture(INVENTORY);
					Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					event.gui.drawTexturedModalRect(i, j, 0, 166, 140, 32);

					if (potion.hasStatusIcon()) {
						int l = potion.getStatusIconIndex();
						event.gui.drawTexturedModalRect(i + 6, j + 7, l % 8 * 18, 198 + l / 8 * 18, 18, 18);
					}

					String s1 = I18n.format(potion.getName());

					if (potioneffect.getAmplifier() == 1) {
						s1 = s1 + " II";
					} else if (potioneffect.getAmplifier() == 2) {
						s1 = s1 + " III";
					} else if (potioneffect.getAmplifier() == 3) {
						s1 = s1 + " IV";
					}

					mc.fontRenderer.drawStringWithShadow(s1, i + 10 + 18, j + 6, 16777215);
					mc.fontRenderer.drawStringWithShadow("Bauble", i + 10 + 18, j + 6 + 10, 8355711);
				}
			}
		}
	}
}
