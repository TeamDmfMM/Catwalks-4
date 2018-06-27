package dmfmm.catwalks.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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

public class CatwalkLegacyModelLoader implements ICustomModelLoader {

    private IResourceManager manager;

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourceDomain().equals(Catwalks.MODID) && modelLocation.getResourcePath().endsWith("!!catwalks:catwalk_legacy");
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return new CatwalkLegacyModelWrapper(modelLocation.getResourceDomain(), modelLocation.getResourcePath().replace("!!catwalks:catwalk_legacy", "").replace("models/", ""), "");
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        manager = resourceManager;
    }


    class CatwalkLegacyModelWrapper implements IModel {

        ResourceLocation bottom, bottom_corner, bottom_edge;
        ResourceLocation corner_outer, right_merge, left_merge, corner_inner;
        IModel modelbottom, modelBottomCorner, modelBottomEdge;
        IModel modelOuterCorner, modelLeftMerge, modelRightMerge, modelInner;
        boolean match;


        public CatwalkLegacyModelWrapper(String domain, String path, String postfix) {
            bottom = new ResourceLocation(domain, path + "/bottom" + postfix);
            bottom_corner = new ResourceLocation(domain, path + "/bottom_corner" + postfix);
            bottom_edge = new ResourceLocation(domain, path + "/bottom_edge" + postfix);
            corner_outer = new ResourceLocation(domain, path + "/corner_outer" + postfix);
            right_merge = new ResourceLocation(domain, path + "/right_merge" + postfix);
            left_merge = new ResourceLocation(domain, path + "/left_merge" + postfix);
            corner_inner = new ResourceLocation(domain, path + "/corner_inner" + postfix);

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
            return ImmutableList.of(bottom, bottom_corner, bottom_edge, corner_outer, right_merge, left_merge, corner_inner);
        }

        @Override
        public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
            if (modelbottom == null || modelBottomCorner == null || modelBottomEdge == null) {
                modelbottom = ModelLoaderRegistry.getModelOrMissing(bottom);
                modelBottomCorner = ModelLoaderRegistry.getModelOrMissing(bottom_corner);
                modelBottomEdge = ModelLoaderRegistry.getModelOrMissing(bottom_edge);
                modelOuterCorner = ModelLoaderRegistry.getModelOrMissing(corner_outer);
                modelLeftMerge = ModelLoaderRegistry.getModelOrMissing(left_merge);
                modelRightMerge = ModelLoaderRegistry.getModelOrMissing(right_merge);
                modelInner = ModelLoaderRegistry.getModelOrMissing(corner_inner);
            }
            Map<String, IModel> models = new HashMap<>();
            models.put("bottom", modelbottom);
            models.put("bottom_corner", modelBottomCorner);
            models.put("bottom_edge", modelBottomEdge);
            models.put("corner_outer", modelOuterCorner);
            models.put("left_merge", modelLeftMerge);
            models.put("right_merge", modelRightMerge);
            models.put("corner_inner", modelInner);

            return new CatwalkLegacyModel(
                    match,
                    models,
                    format,
                    bakedTextureGetter
            );
        }
    }
}

