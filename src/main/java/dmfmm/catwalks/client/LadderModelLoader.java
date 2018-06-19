package dmfmm.catwalks.client;

import com.google.common.collect.ImmutableList;
import dmfmm.catwalks.Catwalks;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;

import java.util.Collection;
import java.util.function.Function;

public class LadderModelLoader implements ICustomModelLoader{

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourcePath().endsWith("!catwalks:ladder");
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return new LadderModelWrapper(modelLocation.getResourcePath().replace("!!catwalks:ladder", "").replace("models/", ""));
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        //Sing me a song, as we travel along, happy river, happy river doot doot, whoo! Whoo!
    }

    public static class LadderModelWrapper implements IModel {
        static ResourceLocation ladder, side_bar, connectionRight, connectionLeft, wrap, base, secondPole, baseConnected;
        static ResourceLocation connectionRightBig, connectionLeftBig, wrapBig, ladderTop;
        static IModel bakedLadder, bakedSideBar, bakedConnectionRight, bakedConnectionLeft, bakedWrap, bakedBase, bakedSecondPole, bakedBaseConnected;
        static IModel bakedRightBig, bakedLeftBig, bakedWrapBig, bakedLadderTop;

        LadderModelWrapper(String path){
            ladder = new ResourceLocation(Catwalks.MODID, path + "/ladder");
            side_bar = new ResourceLocation(Catwalks.MODID, path + "/corner");
            connectionRight = new ResourceLocation(Catwalks.MODID, path + "/right_connect_small");
            wrap = new ResourceLocation(Catwalks.MODID, path + "/round_corner_small");
            connectionLeft = new ResourceLocation(Catwalks.MODID, path + "/left_connect_small");
            base = new ResourceLocation(Catwalks.MODID, path + "/bottom_connect_none");
            baseConnected = new ResourceLocation(Catwalks.MODID,path + "/bottom_connect_front");
            secondPole = new ResourceLocation(Catwalks.MODID,path + "/bottom_side_bar");
            connectionLeftBig = new ResourceLocation(Catwalks.MODID,path + "/left_connect_large");
            connectionRightBig = new ResourceLocation(Catwalks.MODID,path + "/right_connect_large");
            wrapBig = new ResourceLocation(Catwalks.MODID,path + "/round_corner_large");
            ladderTop = new ResourceLocation(Catwalks.MODID,path + "/ladder_top");
        }


        @Override
        public Collection<ResourceLocation> getDependencies() {
            return ImmutableList.of(ladder, side_bar, connectionRight, connectionLeft, wrap, base, secondPole, baseConnected, connectionLeftBig, connectionRightBig, wrapBig, ladderTop);
        }

        @Override
        public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
            if (bakedLadder == null || bakedSideBar == null || bakedConnectionRight == null || bakedConnectionLeft == null || bakedWrap == null || bakedBase == null) {
                bakedLadder = ModelLoaderRegistry.getModelOrMissing(ladder);
                bakedSideBar = ModelLoaderRegistry.getModelOrMissing(side_bar);
                bakedConnectionRight = ModelLoaderRegistry.getModelOrMissing(connectionRight);
                bakedConnectionLeft = ModelLoaderRegistry.getModelOrMissing(connectionLeft);
                bakedWrap = ModelLoaderRegistry.getModelOrMissing(wrap);
                bakedBase = ModelLoaderRegistry.getModelOrMissing(base);
                bakedSecondPole = ModelLoaderRegistry.getModelOrMissing(secondPole);
                bakedBaseConnected = ModelLoaderRegistry.getModelOrMissing(baseConnected);
                bakedLeftBig = ModelLoaderRegistry.getModelOrMissing(connectionLeftBig);
                bakedRightBig = ModelLoaderRegistry.getModelOrMissing(connectionRightBig);
                bakedWrapBig = ModelLoaderRegistry.getModelOrMissing(wrapBig);
                bakedLadderTop = ModelLoaderRegistry.getModelOrMissing(ladderTop);

            }
            return new LadderModel(
                    bakedLadder,
                    bakedSideBar,
                    bakedConnectionRight,
                    bakedConnectionLeft,
                    bakedWrap,
                    bakedBase,
                    bakedSecondPole,
                    bakedBaseConnected,
                    bakedLeftBig,
                    bakedRightBig,
                    bakedWrapBig,
                    bakedLadderTop,
                    format,
                    bakedTextureGetter
            );
        }
    }
}
