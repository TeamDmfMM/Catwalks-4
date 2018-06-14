package dmf444.catwalks.client;

import com.google.common.collect.ImmutableList;
import dmf444.catwalks.block.CatwalkBlock;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

import static java.util.Arrays.asList;

public class CatwalkModel implements IBakedModel{

    IBakedModel item, rails, floor;
    static Map<CatwalkState, List<BakedQuad>> cache = new HashMap<>();

    public CatwalkModel(IBakedModel item, IBakedModel rails, IBakedModel floor) {
        this.item = item;
        this.floor = floor;
        this.rails = rails;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        CatwalkState cw = null;
        if(state instanceof IExtendedBlockState){
            cw = ((IExtendedBlockState) state).getValue(CatwalkBlock.CATWALK_STATE);
        }
        if (cw == null) {
            cw = new CatwalkState(RailSection.OUTER, RailSection.OUTER, RailSection.OUTER, RailSection.OUTER,
                                  FloorSection.OUTER, FloorSection.OUTER, FloorSection.OUTER, FloorSection.OUTER, 0);
        }
        if(cache.containsKey(cw)){
            return cache.get(cw);
        } else {
            ImmutableList.Builder<BakedQuad> builder = new ImmutableList.Builder<>();
            List<BakedQuad> railQuads = rails.getQuads(state, side, rand);
            List<BakedQuad> floorQuads = floor.getQuads(state, side, rand);

            CatwalkState finalCw = cw;
            Function<BakedQuad, Boolean> filter = (BakedQuad q) -> (!q.hasTintIndex() || finalCw.hasLayer(q.getTintIndex()));

            for(int it=0; it < 4; it++){
                Vec3d offset = new Vec3d(it == 1 || it == 2 ? 0.5 : 0.0, -1,
                                         it > 1 ? 0.5 : 0.0);
                ModelSlicer.sliceInto(builder, railQuads, cw.railSections.get(it).boundingBoxes.get(it), offset, filter);
                if (cw.floorSections.size() < it) continue;
                ModelSlicer.sliceInto(builder, floorQuads, cw.railSections.get(it).boundingBoxes.get(it), offset, filter);
            }

            cache.put(cw, builder.build());
            return cache.get(cw);
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
        return item.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }


    public enum RailSection {
        MIDDLE(0, 0),
        Z_EDGE(1, 0),
        Z_END(2, 0),
        X_EDGE(0, 1),
        X_END(0, 2),
        INNER(1, 1),
        OUTER(2, 2);

        private int x,z;
        private ArrayList<AxisAlignedBB> boundingBoxes;
        RailSection(int x, int z) {
            this.x = x - 1;
            this.z = z - 1;

            AxisAlignedBB model = new AxisAlignedBB(this.x, -1.0, this.z, this.x+0.5, 2.0, this.z+0.5);
            boundingBoxes = new ArrayList<>();
            boundingBoxes.addAll(Arrays.asList(
                    model,
                    model.offset(0.5, 0.0, 0.0),
                    model.offset(0.5, 0.0, 0.5),
                    model.offset(0.0, 0.0, 0.5)
            ));
        }
    }

    public enum FloorSection {
        MIDDLE(0, 0),
        Z_EDGE(1, 0),
        X_EDGE(0, 1),
        INNER(1, 1),
        OUTER(2, 2);

        private int x,z;
        private ArrayList<AxisAlignedBB> boundingBoxes;
        FloorSection(int x, int z) {
            this.x = x - 1;
            this.z = z - 1;

            AxisAlignedBB model = new AxisAlignedBB(this.x, -1.0, this.z, this.x+0.5, 2.0, this.z+0.5);
            boundingBoxes = new ArrayList<>();
            boundingBoxes.addAll(Arrays.asList(
                    model,
                    model.offset(0.5, 0.0, 0.0),
                    model.offset(0.5, 0.0, 0.5),
                    model.offset(0.0, 0.0, 0.5)
            ));
        }
    }
}
