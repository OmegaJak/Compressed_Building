package com.omegajak.compressedbuilding.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
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
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
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
	    
	    GL11.glDisable(GL11.GL_BLEND);
	   
	    //Render the inner block
		
		tessellator.startDrawingQuads();
		
		ItemStack tempItem = new ItemStack(((item.getItemDamage() >>> 8) > 0) ? ((Item)Item.getItemById(item.getItemDamage() >>> 8)) : Item.getItemById(4), 1, 0xFF & item.getItemDamage());

//		icon = tempItem.getItem().getIconFromDamage(0xFF & item.getItemDamage());
		Block tempBlock = Block.getBlockById((item.getItemDamage() >>> 8) > 0 ? item.getItemDamage() >>> 8 : 4);
		

		
		switch (type) {
			case INVENTORY:
				GL11.glTranslatef(0.0F, 0.01F, 0.0F);
				break;
			default:
				GL11.glTranslatef(0.01F, 0.01F, 0.01F);
		}
		
		GL11.glScalef(0.98F, 0.98F, 0.98F);//so that the inner block doesn't clip with the outer mesh thing		
				
		// xpos face
		icon = tempBlock.getIcon(5, 0xFF & item.getItemDamage());//change the texture being used
	    tessellator.setNormal(1.0F, 0.0F, 0.0F);
	    tessellator.addVertexWithUV(1.0, 0.33, 0.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(12));
	    tessellator.addVertexWithUV(1.0, 0.66, 0.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(4));
	    tessellator.addVertexWithUV(1.0, 0.66, 1.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(4));
	    tessellator.addVertexWithUV(1.0, 0.33, 1.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(12));
	    
	    // xneg face
	    icon = tempBlock.getIcon(4, 0xFF & item.getItemDamage());
	    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
	    tessellator.addVertexWithUV(0.0, 0.33, 1.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(12));
	    tessellator.addVertexWithUV(0.0, 0.66, 1.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(4)); 
	    tessellator.addVertexWithUV(0.0, 0.66, 0.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(4));  
	    tessellator.addVertexWithUV(0.0, 0.33, 0.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(12)); 

	    // zneg face
	    icon = tempBlock.getIcon(2, 0xFF & item.getItemDamage());
	    tessellator.setNormal(0.0F, 0.0F, -1.0F);
	    tessellator.addVertexWithUV(0.0, 0.33, 0.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(12));
	    tessellator.addVertexWithUV(0.0, 0.66, 0.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(4)); 
	    tessellator.addVertexWithUV(1.0, 0.66, 0.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(4));  
	    tessellator.addVertexWithUV(1.0, 0.33, 0.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(12)); 

	    // zpos face
	    icon = tempBlock.getIcon(3, 0xFF & item.getItemDamage());
	    tessellator.setNormal(0.0F, 0.0F, -1.0F);
	    tessellator.addVertexWithUV(1.0, 0.33, 1.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(12));
	    tessellator.addVertexWithUV(1.0, 0.66, 1.0, (double)icon.getInterpolatedU(16), (double)icon.getInterpolatedV(4)); 
	    tessellator.addVertexWithUV(0.0, 0.66, 1.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(4));  
	    tessellator.addVertexWithUV(0.0, 0.33, 1.0, (double)icon.getInterpolatedU(0), (double)icon.getInterpolatedV(12)); 
	    
	    if (tempBlock.equals(Blocks.grass)) {
			//this is for rendering the proper color, adapted from EntityDiggingFX
			int j = Block.getBlockById(item.getItemDamage() >>> 8).getRenderColor(0);//the getRenderColor parameter doesn't seem to matter
			tessellator.setColorOpaque_F((float)(j >> 16 & 255) / 255.0F, (float)(j >> 8 & 255) / 255.0F, (float)(j & 255) / 255.0F);
	    }
	    
	    // ypos face
	    icon = tempBlock.getIcon(1, 0xFF & item.getItemDamage());
	    tessellator.setNormal(0.0F, 1.0F, 0.0F);
	    tessellator.addVertexWithUV(1.0, 0.66, 1.0, (double)icon.getMaxU(), (double)icon.getMaxV());
	    tessellator.addVertexWithUV(1.0, 0.66, 0.0, (double)icon.getMaxU(), (double)icon.getMinV());
	    tessellator.addVertexWithUV(0.0, 0.66, 0.0, (double)icon.getMinU(), (double)icon.getMinV());
	    tessellator.addVertexWithUV(0.0, 0.66, 1.0, (double)icon.getMinU(), (double)icon.getMaxV());

	    // yneg face 
	    icon = tempBlock.getIcon(0, 0xFF & item.getItemDamage());
	    tessellator.setNormal(0.0F, -1.0F, 0.0F);
	    tessellator.addVertexWithUV(0.0, 0.33, 1.0, (double)icon.getMaxU(), (double)icon.getMaxV());
	    tessellator.addVertexWithUV(0.0, 0.33, 0.0, (double)icon.getMaxU(), (double)icon.getMinV());
	    tessellator.addVertexWithUV(1.0, 0.33, 0.0, (double)icon.getMinU(), (double)icon.getMinV());
	    tessellator.addVertexWithUV(1.0, 0.33, 1.0, (double)icon.getMinU(), (double)icon.getMaxV());

	    tessellator.draw();
	    
	}

}
