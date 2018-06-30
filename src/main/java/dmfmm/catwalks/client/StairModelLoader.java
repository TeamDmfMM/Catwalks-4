package dmfmm.catwalks.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * File created by mincrmatt12 on 6/29/2018.
 * Originally written for Catwalk.
 * <p>
 * See LICENSE.txt for license information.
 */
public class StairModelLoader implements ICustomModelLoader {
    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourcePath().endsWith("!catwalks:stair");
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return new StairModelNotYetPlacedAndKeptInOvenForEnoughTimeToBeBaked();
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        // woopee i do nothing
    }

    public static class StairModelNotYetPlacedAndKeptInOvenForEnoughTimeToBeBaked implements IModel {

        private final EnumFacing direction;

        public StairModelNotYetPlacedAndKeptInOvenForEnoughTimeToBeBaked(EnumFacing direction) {
            this.direction = direction;
        }

        public StairModelNotYetPlacedAndKeptInOvenForEnoughTimeToBeBaked() {
            this.direction = null;
        }

        private static List<ResourceLocation> deps;
        private static ImmutableList<String> left_right = ImmutableList.of("left", "right");
        private static String p = "block/stair/classic/";

        static {
            deps = new ArrayList<>();
            String prefix = "catwalks:" + p;

            for (EnumFacing f : EnumFacing.HORIZONTALS) {
                deps.add(new ResourceLocation(prefix + "stairs_" + f.getName() + ".obj"));
            }

            for (String s : left_right) {
                deps.add(new ResourceLocation(prefix + s + "_bottom.obj"));
                deps.add(new ResourceLocation(prefix + s + "_mid.obj"));
                deps.add(new ResourceLocation(prefix + s + "_top.obj"));
                deps.add(new ResourceLocation(prefix + s + "_full.obj"));
                deps.add(new ResourceLocation(prefix + s + "_chopped_top.obj"));
                deps.add(new ResourceLocation(prefix + s + "_chopped_side.obj"));
            }
        }

        @Override
        public Collection<ResourceLocation> getDependencies() {
            return deps;
        }

        private Function<String, IModel> base;

        public Function<String, IModel> getBase() {
            if (base == null) {
                base = (String s) -> {
                    return ModelLoaderRegistry.getModelOrMissing(new ResourceLocation("catwalks", p + s));
                };
            }
            return base;
        }

        @Override
        public IModel process(ImmutableMap<String, String> customData) {
            EnumFacing direction = EnumFacing.byName(customData.get("dir").substring(1, customData.get("dir").length()-1));
            return new StairModelNotYetPlacedAndKeptInOvenForEnoughTimeToBeBaked(direction);
        }

        @Override
        public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
            EnumFacing effectiveDirection = direction;
            if (direction == null) {
                effectiveDirection = EnumFacing.NORTH;
            }
            IBakedModel baseModel = getBase().apply("stairs_" + effectiveDirection.getName() + ".obj").bake(ModelRotation.X0_Y0, format, bakedTextureGetter);
            final IModelState s = state;
            Function<IModel, IBakedModel> theOvenThatWeNeverGotPutInButThatWeWillGiveToOurChild = (IModel m) -> m.bake(s, format, bakedTextureGetter);
            Function<String, IBakedModel> theBetterOvenThatHasALotOfAdditionsSoItCanTakeAFancyStringForTheChild = theOvenThatWeNeverGotPutInButThatWeWillGiveToOurChild.compose(getBase());

            return new StairModel(theBetterOvenThatHasALotOfAdditionsSoItCanTakeAFancyStringForTheChild, baseModel);
        }
    }
}
