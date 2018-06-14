package dmf444.catwalks.client;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

public class CatwalkModel implements IBakedModel{

    IBakedModel item, rails, floor;

    public CatwalkModel(IBakedModel item, IBakedModel rails, IBakedModel floor) {
        this.item = item;
        this.floor = floor;
        this.rails = rails;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return null;
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
            boundingBoxes.addAll(Arrays.asList(
                    model,
                    model.offset(0.5, 0.0, 0.0),
                    model.offset(0.5, 0.0, 0.5),
                    model.offset(0.0, 0.0, 0.5)
            ));
        }
    }
}
