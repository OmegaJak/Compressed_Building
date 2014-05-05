package com.omegajak.compressedbuilding.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class RenderItemSquareTemplate implements IItemRenderer{

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type) {
			case ENTITY:
			case EQUIPPED:
			case EQUIPPED_FIRST_PERSON:
			case INVENTORY:
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
	    switch (type) {
	      case ENTITY: {
	        return (helper == ItemRendererHelper.ENTITY_BOBBING ||
	                helper == ItemRendererHelper.ENTITY_ROTATION ||
	                helper == ItemRendererHelper.BLOCK_3D);
	      }
	      case EQUIPPED: {
	        return (helper == ItemRendererHelper.BLOCK_3D || helper == ItemRendererHelper.EQUIPPED_BLOCK);
	      }
	      case EQUIPPED_FIRST_PERSON: {
	        return helper == ItemRendererHelper.EQUIPPED_BLOCK;
	      }
	      case INVENTORY: {
	        return helper == ItemRendererHelper.INVENTORY_BLOCK;
	      }
	      default: {
	        return false;
	      }
	    }
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		
		
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		
		IIcon icon = item.getItem().getIcon(item, 0);
		
		 switch (type) {
	      case EQUIPPED:
	      case EQUIPPED_FIRST_PERSON: {
	        break; // caller expects us to render over [0,0,0] to [1,1,1], no transformation necessary
	      }
	      case INVENTORY: {  // caller expects [-0.5, -0.5, -0.5] to [0.5, 0.5, 0.5]
	        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
	        break;
	      }
	      case ENTITY: {
	        // translate our coordinates and scale so that [0,0,0] to [1,1,1] translates to the [-0.25, -0.25, -0.25] to [0.25, 0.25, 0.25] expected by the caller.
	        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
	        break;
	      }
	      default:
	        break; // never here
	    }
		
		// xpos face
	    tessellator.setNormal(1.0F, 0.0F, 0.0F);
	    tessellator.addVertexWithUV(1.0, 0.33, 0.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(12));
	    tessellator.addVertexWithUV(1.0, 0.66, 0.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(4));
	    tessellator.addVertexWithUV(1.0, 0.66, 1.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(4));
	    tessellator.addVertexWithUV(1.0, 0.33, 1.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(12));

	    // xneg face
	    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
	    tessellator.addVertexWithUV(0.0, 0.33, 1.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(12));
	    tessellator.addVertexWithUV(0.0, 0.66, 1.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(4)); 
	    tessellator.addVertexWithUV(0.0, 0.66, 0.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(4));  
	    tessellator.addVertexWithUV(0.0, 0.33, 0.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(12)); 

	    // zneg face
	    tessellator.setNormal(0.0F, 0.0F, -1.0F);
	    tessellator.addVertexWithUV(0.0, 0.33, 0.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(12));
	    tessellator.addVertexWithUV(0.0, 0.66, 0.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(4)); 
	    tessellator.addVertexWithUV(1.0, 0.66, 0.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(4));  
	    tessellator.addVertexWithUV(1.0, 0.33, 0.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(12)); 

	    // zpos face
	    tessellator.setNormal(0.0F, 0.0F, -1.0F);
	    tessellator.addVertexWithUV(1.0, 0.33, 1.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(12));
	    tessellator.addVertexWithUV(1.0, 0.66, 1.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(4)); 
	    tessellator.addVertexWithUV(0.0, 0.66, 1.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(4));  
	    tessellator.addVertexWithUV(0.0, 0.33, 1.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(12)); 

	    // ypos face
	    tessellator.setNormal(0.0F, 1.0F, 0.0F);
	    tessellator.addVertexWithUV(1.0, 0.66, 1.0, (double)icon.getMaxU(), (double)icon.getMaxV());
	    tessellator.addVertexWithUV(1.0, 0.66, 0.0, (double)icon.getMaxU(), (double)icon.getMinV());
	    tessellator.addVertexWithUV(0.0, 0.66, 0.0, (double)icon.getMinU(), (double)icon.getMinV());
	    tessellator.addVertexWithUV(0.0, 0.66, 1.0, (double)icon.getMinU(), (double)icon.getMaxV());

	    // yneg face 
	    tessellator.setNormal(0.0F, -1.0F, 0.0F);
	    tessellator.addVertexWithUV(0.0, 0.33, 1.0, (double)icon.getMaxU(), (double)icon.getMaxV());
	    tessellator.addVertexWithUV(0.0, 0.33, 0.0, (double)icon.getMaxU(), (double)icon.getMinV());
	    tessellator.addVertexWithUV(1.0, 0.33, 0.0, (double)icon.getMinU(), (double)icon.getMinV());
	    tessellator.addVertexWithUV(1.0, 0.33, 1.0, (double)icon.getMinU(), (double)icon.getMaxV());

	    tessellator.draw();
	    
	   
	    //Render the inner block
		
		tessellator.startDrawingQuads();
		
		ItemStack tempItem = new ItemStack(((item.getItemDamage() >>> 8) > 0) ? ((Item)Item.getItemById(item.getItemDamage() >>> 8)) : Item.getItemById(4), 1, 0xFF & item.getItemDamage());
		icon = tempItem.getItem().getIconFromDamage(0xFF & item.getItemDamage());
		
		switch (type) {
			case INVENTORY:
				GL11.glTranslatef(0.0F, 0.01F, 0.0F);
				break;
			default:
				GL11.glTranslatef(0.01F, 0.01F, 0.01F);
		}
		
		GL11.glScalef(0.98F, 0.98F, 0.98F);
		
		// xpos face
	    tessellator.setNormal(1.0F, 0.0F, 0.0F);
	    tessellator.addVertexWithUV(1.0, 0.33, 0.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(12));
	    tessellator.addVertexWithUV(1.0, 0.66, 0.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(4));
	    tessellator.addVertexWithUV(1.0, 0.66, 1.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(4));
	    tessellator.addVertexWithUV(1.0, 0.33, 1.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(12));

	    // xneg face
	    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
	    tessellator.addVertexWithUV(0.0, 0.33, 1.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(12));
	    tessellator.addVertexWithUV(0.0, 0.66, 1.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(4)); 
	    tessellator.addVertexWithUV(0.0, 0.66, 0.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(4));  
	    tessellator.addVertexWithUV(0.0, 0.33, 0.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(12)); 

	    // zneg face
	    tessellator.setNormal(0.0F, 0.0F, -1.0F);
	    tessellator.addVertexWithUV(0.0, 0.33, 0.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(12));
	    tessellator.addVertexWithUV(0.0, 0.66, 0.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(4)); 
	    tessellator.addVertexWithUV(1.0, 0.66, 0.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(4));  
	    tessellator.addVertexWithUV(1.0, 0.33, 0.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(12)); 

	    // zpos face
	    tessellator.setNormal(0.0F, 0.0F, -1.0F);
	    tessellator.addVertexWithUV(1.0, 0.33, 1.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(12));
	    tessellator.addVertexWithUV(1.0, 0.66, 1.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(4)); 
	    tessellator.addVertexWithUV(0.0, 0.66, 1.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(4));  
	    tessellator.addVertexWithUV(0.0, 0.33, 1.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(12)); 

	    // ypos face
	    tessellator.setNormal(0.0F, 1.0F, 0.0F);
	    tessellator.addVertexWithUV(1.0, 0.66, 1.0, (double)icon.getMaxU(), (double)icon.getMaxV());
	    tessellator.addVertexWithUV(1.0, 0.66, 0.0, (double)icon.getMaxU(), (double)icon.getMinV());
	    tessellator.addVertexWithUV(0.0, 0.66, 0.0, (double)icon.getMinU(), (double)icon.getMinV());
	    tessellator.addVertexWithUV(0.0, 0.66, 1.0, (double)icon.getMinU(), (double)icon.getMaxV());

	    // yneg face 
	    tessellator.setNormal(0.0F, -1.0F, 0.0F);
	    tessellator.addVertexWithUV(0.0, 0.33, 1.0, (double)icon.getMaxU(), (double)icon.getMaxV());
	    tessellator.addVertexWithUV(0.0, 0.33, 0.0, (double)icon.getMaxU(), (double)icon.getMinV());
	    tessellator.addVertexWithUV(1.0, 0.33, 0.0, (double)icon.getMinU(), (double)icon.getMinV());
	    tessellator.addVertexWithUV(1.0, 0.33, 1.0, (double)icon.getMinU(), (double)icon.getMaxV());

	    tessellator.draw();
	    
	}

}
