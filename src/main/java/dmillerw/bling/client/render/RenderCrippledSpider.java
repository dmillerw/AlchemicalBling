package dmillerw.bling.client.render;

import dmillerw.bling.client.model.ModelCrippledSpider;
import dmillerw.bling.entity.EntityCrippledSpider;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * @author dmillerw
 */
public class RenderCrippledSpider extends RenderLiving {

	private static final ResourceLocation spiderEyesTextures = new ResourceLocation("textures/entity/spider_eyes.png");
	private static final ResourceLocation spiderTextures = new ResourceLocation("textures/entity/spider/spider.png");

	public RenderCrippledSpider() {
		super(new ModelCrippledSpider(), 1.0F);
		this.setRenderPassModel(new ModelCrippledSpider());
	}

	protected float getDeathMaxRotation(EntityCrippledSpider par1EntityCrippledSpider) {
		return 180.0F;
	}

	protected int shouldRenderPass(EntityCrippledSpider par1EntityCrippledSpider, int par2, float par3) {
		if (par2 != 0) {
			return -1;
		} else {
			this.bindTexture(spiderEyesTextures);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

			if (par1EntityCrippledSpider.isInvisible()) {
				GL11.glDepthMask(false);
			} else {
				GL11.glDepthMask(true);
			}

			char c0 = 61680;
			int j = c0 % 65536;
			int k = c0 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j / 1.0F, (float) k / 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			return 1;
		}
	}

	protected ResourceLocation getEntityTexture(EntityCrippledSpider par1EntityCrippledSpider) {
		return spiderTextures;
	}

	protected float getDeathMaxRotation(EntityLivingBase par1EntityLivingBase) {
		return this.getDeathMaxRotation((EntityCrippledSpider) par1EntityLivingBase);
	}

	protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3) {
		return this.shouldRenderPass((EntityCrippledSpider) par1EntityLivingBase, par2, par3);
	}

	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return this.getEntityTexture((EntityCrippledSpider) par1Entity);
	}
}