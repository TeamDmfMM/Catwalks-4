package dmf444.catwalks.client;

import com.google.common.collect.ImmutableList;
import dmf444.catwalks.Catwalks;
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

public class CatwalkModelLoader implements ICustomModelLoader {

    private IResourceManager manager;

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourceDomain().equals(Catwalks.MODID) && modelLocation.getResourcePath().contains("!!catwalks:catwalk");
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return new CatwalkModelWrapper(modelLocation.getResourceDomain(), modelLocation.getResourcePath(), "");
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        manager = resourceManager;
    }


    class CatwalkModelWrapper implements IModel {

        ResourceLocation item_r1, rails_r1, floor_r1;
        IModel item, rails, floor;


        public CatwalkModelWrapper(String domain, String path, String postfix) {
            item_r1 = new ResourceLocation(domain, path + "/item" + postfix);
            rails_r1 = new ResourceLocation(domain, path + "/rails" + postfix);
            floor_r1 = new ResourceLocation(domain, path + "/floor" + postfix);

            item = ModelLoaderRegistry.getModelOrMissing(item_r1);
            rails = ModelLoaderRegistry.getModelOrMissing(rails_r1);
            floor = ModelLoaderRegistry.getModelOrMissing(floor_r1);
        }

        @Override
        public Collection<ResourceLocation> getDependencies() {
            return ImmutableList.of(item_r1, floor_r1, rails_r1);
        }

        @Override
        public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
            return new CatwalkModel(item.bake(state, format, bakedTextureGetter),
                    rails.bake(state, format, bakedTextureGetter),
                    floor.bake(state, format, bakedTextureGetter)
            );
        }
    }
}
