package dmf444.catwalks.client;

import dmf444.catwalks.block.CableBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.List;

/**
 * File created by mincrmatt12 on 6/12/2018.
 * Originally written for Catwalk.
 * <p>
 * See LICENSE.txt for license information.
 */
public class CableModel implements IBakedModel {
    private IBakedModel cable_top;
    private IBakedModel cable_none;
    private IBakedModel cable_bottom;

    public CableModel(IBakedModel cable_top, IBakedModel cable_none, IBakedModel cable_bottom) {
        this.cable_top = cable_top;
        this.cable_none = cable_none;
        this.cable_bottom = cable_bottom;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
         if (state instanceof IExtendedBlockState) {
             IExtendedBlockState ext = (IExtendedBlockState) state;

             CableBlock.CableState state_the_cramp = ext.getValue(CableBlock.STATE);
             switch (state_the_cramp) {
                 case TOP:
                     return this.cable_top.getQuads(state, side, rand);
                 case MIDDLE:
                 default:
                     return this.cable_none.getQuads(state, side, rand);
                 case BOTTOM:
                     return this.cable_bottom.getQuads(state, side, rand);
             }
         }
         else {
             return this.cable_none.getQuads(state, side, rand);
         }
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return cable_none.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }
}
