package dmillerw.bling.recipe;

import dmillerw.bling.AlchemicalBling;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class RecipeSilver implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inventory, World world) {
		int foundSilverBottle = 0;
		boolean foundOther = false;

		for (int i=0; i<inventory.getSizeInventory(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);

			if (stack != null) {
				if (stack.getItem() == AlchemicalBling.bottleSilver) {
					foundSilverBottle++;
				} else {
					foundOther = true;
				}
			}
		}

		return foundSilverBottle > 0 && !foundOther;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory) {
		int foundSilverBottle = 0;

		for (int i=0; i<inventory.getSizeInventory(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);

			if (stack != null) {
				if (stack.getItem() == AlchemicalBling.bottleSilver) {
					foundSilverBottle++;
				}
			}
		}

		return foundSilverBottle > 0 ? new ItemStack(AlchemicalBling.ingotSilver, foundSilverBottle, 0) : null;
	}

	@Override
	public int getRecipeSize() {
		return 9;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(AlchemicalBling.ingotSilver);
	}
}
