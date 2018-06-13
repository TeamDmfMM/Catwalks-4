package dmf444.catwalks.client;

import com.google.common.collect.ImmutableList;
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

/**
 * File created by mincrmatt12 on 6/12/2018.
 * Originally written for Catwalk.
 * <p>
 * See LICENSE.txt for license information.
 */
public class CableModelLoader implements ICustomModelLoader {
    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourcePath().endsWith("!catwalks:cable");
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return new CableModelWrapper();
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        // asdfasdfasdf ayayCRAMPOS
    }

    public static class CableModelWrapper implements IModel {
        static ResourceLocation top_RL = new ResourceLocation("catwalks:block/cable_top");
        static ResourceLocation mid_RL = new ResourceLocation("catwalks:block/cable_middle");
        static ResourceLocation bot_RL = new ResourceLocation("catwalks:block/cable_bottom");

        private IModel top = null, bot = null, mid = null;

        @Override
        public Collection<ResourceLocation> getDependencies() {
            if (top == null) top = ModelLoaderRegistry.getModelOrMissing(top_RL);
            if (mid == null) mid = ModelLoaderRegistry.getModelOrMissing(mid_RL);
            if (bot == null) bot = ModelLoaderRegistry.getModelOrMissing(bot_RL);
            return ImmutableList.of(top_RL, mid_RL, bot_RL);
        }

        @Override
        public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
            if (top == null) top = ModelLoaderRegistry.getModelOrMissing(top_RL);
            if (mid == null) mid = ModelLoaderRegistry.getModelOrMissing(mid_RL);
            if (bot == null) bot = ModelLoaderRegistry.getModelOrMissing(bot_RL);
            return new CableModel(
                    top.bake(state, format, bakedTextureGetter),
                    mid.bake(state, format, bakedTextureGetter),
                    bot.bake(state, format, bakedTextureGetter)
            );
        }
    }
}
