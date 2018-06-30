package dmfmm.catwalks.client;

import dmfmm.catwalks.block.StairBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * File created by mincrmatt12 on 6/20/2018.
 * Originally written for Catwalk.
 * <p>
 * See LICENSE.txt for license information.
 */
public class StairModel implements IBakedModel {
    public Map<StairBlock.State, List<BakedQuad>> cache = new HashMap<>();

    IBakedModel base;
    private Map<Integer, IBakedModel> stairRight;
    private Map<Integer, IBakedModel> stairLeft;

    public StairModel(Function<String, IBakedModel> loader, IBakedModel base) {
        // load all the models here. woopee

        this.base = base;
        stairRight = new HashMap<>();
        stairLeft = new HashMap<>();

        stairRight.put(0, loader.apply("right_bottom.obj"));
        stairRight.put(1, loader.apply("right_mid.obj"));
        stairRight.put(2, loader.apply("right_top.obj"));
        stairRight.put(3, loader.apply("right_full.obj"));
        stairRight.put(4, loader.apply("right_chopped_top.obj"));
        stairRight.put(5, loader.apply("right_chopped_top_side.obj"));

        stairLeft.put(0, loader.apply("left_bottom.obj"));
        stairLeft.put(1, loader.apply("left_mid.obj"));
        stairLeft.put(2, loader.apply("left_top.obj"));
        stairLeft.put(3, loader.apply("left_full.obj"));
        stairLeft.put(4, loader.apply("left_chopped_top.obj"));
        stairLeft.put(5, loader.apply("left_chopped_top_side.obj"));

    }

    private int getCombination(int a, int b) {
        /*
        States ( l/r map ) :

         0 - bottom
         1 - middle, no combination
         2 - top, no catwalk
         3 - full (one block no catwalk above)
         4 - chopped top (2 + catwalk above)
         5 - chopped full (3 + catwalk above)

         States (a & b) :

         a = up
         b = down

         0 - no connection
         1 - connected stair
         2 - connected catwalk ( a only)
         */

        switch (a) {
            case 0:
                return b == 0 ? 3 : 0;
            case 1:
                return b == 0 ? 2 : 1;
            case 2:
                return b == 0 ? 5 : 4;
            default:
                return 5;
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        StairBlock.State statey_the_state = new StairBlock.State();
        if (statey_the_state instanceof IExtendedBlockState) {
            IExtendedBlockState iebs = (IExtendedBlockState) statey_the_state;
            StairBlock.State temp = iebs.getValue(StairBlock.STATE);
            statey_the_state = temp == null ? statey_the_state : temp;
        }

        return cache.computeIfAbsent(statey_the_state, k -> {
            int sidestep = this.getCombination(k.up, k.down);

            List<BakedQuad> bbq = new ArrayList<>();
            bbq.addAll(base.getQuads(state, null, rand));

            IBakedModel bbl = this.stairLeft.get(sidestep);
            IBakedModel bbr = this.stairRight.get(sidestep);

            if (!k.left) {
                bbq.addAll(bbl.getQuads(state, null, rand));
            }
            if (!k.right) {
                bbq.addAll(bbr.getQuads(state, null, rand));
            }

            // todo: if state == 4, add chopping table frame thing dohickey(tm)

            return bbq;
        });

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
        return base.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }
}
