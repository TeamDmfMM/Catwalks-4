package dmfmm.catwalks.client.catwalks;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class NyanWalkLoader implements ICustomModelLoader {

    private IResourceManager manager;

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourceDomain().equals(Catwalks.MODID) && modelLocation.getResourcePath().endsWith("!!catwalks:catwalk_nyan");
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return new NyanWalkWrapper(modelLocation.getResourceDomain(), modelLocation.getResourcePath().replace("!!catwalks:catwalk_nyan", "").replace("models/", ""), "");
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        manager = resourceManager;
    }


    class NyanWalkWrapper implements IModel {

        ResourceLocation bottom;
        ResourceLocation corner_outer, right_merge, left_merge, left_connect, right_connect;
        IModel modelbottom;
        IModel modelOuterCorner, modelLeftMerge, modelRightMerge, modelLeftConnect, modelRightConnect;
        boolean match;


        public NyanWalkWrapper(String domain, String path, String postfix) {
            bottom = new ResourceLocation(domain, path + "/bottom" + postfix);
            corner_outer = new ResourceLocation(domain, path + "/corner_outer" + postfix);
            right_merge = new ResourceLocation(domain, path + "/right_end" + postfix);
            left_merge = new ResourceLocation(domain, path + "/left_end" + postfix);
            left_connect = new ResourceLocation(domain, path + "/left_everything" + postfix);
            right_connect = new ResourceLocation(domain, path + "/right_everything" + postfix);

        }

        /**
         * Allows the model to process custom data from the variant definition.
         * If unknown data is encountered it should be skipped.
         * @return a new model, with data applied.
         */
        public IModel process(ImmutableMap<String, String> customData) {
            String floorStr = customData.get("matchFloor");
            this.match = Boolean.valueOf(floorStr);
            return this;
        }

        @Override
        public Collection<ResourceLocation> getDependencies() {
            return ImmutableList.of(bottom, corner_outer, right_merge, left_merge, left_connect, right_connect);
        }

        @Override
        public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
            if (modelbottom == null) {
                modelbottom = ModelLoaderRegistry.getModelOrMissing(bottom);
                modelOuterCorner = ModelLoaderRegistry.getModelOrMissing(corner_outer);
                modelLeftMerge = ModelLoaderRegistry.getModelOrMissing(left_merge);
                modelRightMerge = ModelLoaderRegistry.getModelOrMissing(right_merge);
                modelLeftConnect = ModelLoaderRegistry.getModelOrMissing(left_connect);
                modelRightConnect = ModelLoaderRegistry.getModelOrMissing(right_connect);
            }
            Map<String, IModel> models = new HashMap<>();
            models.put("bottom", modelbottom);
            models.put("corner_outer", modelOuterCorner);
            models.put("left_merge", modelLeftMerge);
            models.put("right_merge", modelRightMerge);
            models.put("left_connect", modelLeftConnect);
            models.put("right_connect", modelRightConnect);

            return new CatwalkLegacyModel(
                    match,
                    "nyanwalk",
                    models,
                    format,
                    bakedTextureGetter
            );
        }
    }
}
