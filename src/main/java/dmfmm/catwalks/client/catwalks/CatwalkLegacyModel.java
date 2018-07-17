package dmfmm.catwalks.client.catwalks;

import com.google.common.collect.ImmutableMap;
import dmfmm.catwalks.block.CatwalkBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CatwalkLegacyModel implements IBakedModel{

    static Map<CatwalkState, List<BakedQuad>> cache = new HashMap<>();
    static Map<String, Map<String, ImmutableMap<EnumFacing, IBakedModel>>> modelPieces = new HashMap<>();

    private boolean match;
    private String modelType;

    public CatwalkLegacyModel(boolean matches, String modelMaterial, Map<String, IModel> models, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter){
        this.match = matches;
        this.modelType = modelMaterial;


        ImmutableMap.Builder<EnumFacing, IBakedModel> builder;
        for(String model: models.keySet()){
            builder = ImmutableMap.builder();
            for (EnumFacing facing: EnumFacing.HORIZONTALS) {
                TRSRTransformation transformation = TRSRTransformation.from(facing);
                builder.put(facing, models.get(model).bake(transformation, format, bakedTextureGetter));

            }
            if(modelPieces.containsKey(modelType)){
                Map<String, ImmutableMap<EnumFacing, IBakedModel>> hash = modelPieces.get(modelType);
                hash.put(model, builder.build());
            } else {
                Map<String, ImmutableMap<EnumFacing, IBakedModel>> hash = new HashMap<>();
                hash.put(model, builder.build());
                modelPieces.put(modelType, hash);
            }


        }
    }


    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        CatwalkState cw = null;
        String material = "treated_wood";
        if(state != null){
            material = state.getValue(CatwalkBlock.MATERIAL).getName().toLowerCase();
        }
        if(state instanceof IExtendedBlockState){
            cw = ((IExtendedBlockState) state).getValue(CatwalkBlock.CATWALK_STATE);
        }
        if (cw == null) {
            cw = new CatwalkState(CatwalkModel.RailSection.OUTER, CatwalkModel.RailSection.OUTER, CatwalkModel.RailSection.OUTER, CatwalkModel.RailSection.OUTER,
                    CatwalkModel.FloorSection.OUTER, CatwalkModel.FloorSection.OUTER, CatwalkModel.FloorSection.OUTER, CatwalkModel.FloorSection.OUTER, 0);
            //railNW, railNE, railSE, railSW || floorNW, floorNE, floorSE, floorSW
        }

        List<BakedQuad> bq = new ArrayList<>();
        for(int i=0; i < 4; i++){
            CatwalkModel.FloorSection section = cw.floorSections.get(i);
            if(match){
                if(section != null){
                    if(!material.equals("nyanwalk")){
                        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                            bq.addAll(modelPieces.get(material).get("bottom_corner").get(facing).getQuads(state, side, rand));
                            bq.addAll(modelPieces.get(material).get("bottom_edge").get(facing).getQuads(state, side, rand));
                        }
                    }
                    bq.addAll(modelPieces.get(material).get("bottom").get(EnumFacing.NORTH).getQuads(state, side, rand));
                }
            } else {
                //We custom build, but I'll write that another day
            }
        }

        for(int i=0; i < 4; i++){
            CatwalkModel.RailSection rail = cw.railSections.get(i);
            String siding;
            EnumFacing facing;
            switch (rail){
                case OUTER:
                    if(!material.equals("nyanwalk")) {
                        bq.addAll(modelPieces.get(material).get("corner_outer").get(facingFromInt(i)).getQuads(state, side, rand));
                        bq.addAll(modelPieces.get(material).get("left_merge").get(facingFromInt(i)).getQuads(state, side, rand));
                        bq.addAll(modelPieces.get(material).get("right_merge").get(facingFromInt(i).rotateYCCW()).getQuads(state, side, rand));
                    } else {
                        if(i == 0){
                            bq.addAll(modelPieces.get(material).get("left_merge").get(facingFromInt(i)).getQuads(state, side, rand));
                            bq.addAll(modelPieces.get(material).get("right_connect").get(facingFromInt(i).rotateYCCW()).getQuads(state, side, rand));
                        } else if(i == 1){
                            bq.addAll(modelPieces.get(material).get("left_connect").get(facingFromInt(i)).getQuads(state, side, rand));
                            bq.addAll(modelPieces.get(material).get("right_merge").get(facingFromInt(i).rotateYCCW()).getQuads(state, side, rand));
                        } else if (i == 2){
                            bq.addAll(modelPieces.get(material).get("left_merge").get(facingFromInt(i)).getQuads(state, side, rand));
                            bq.addAll(modelPieces.get(material).get("right_connect").get(facingFromInt(i).rotateYCCW()).getQuads(state, side, rand));
                        } else {
                            bq.addAll(modelPieces.get(material).get("left_connect").get(facingFromInt(i)).getQuads(state, side, rand));
                            bq.addAll(modelPieces.get(material).get("right_merge").get(facingFromInt(i).rotateYCCW()).getQuads(state, side, rand));
                        }

                    }
                    break;

                case INNER:
                    if(!material.equals("nyanwalk")) {
                        bq.addAll(modelPieces.get(material).get("corner_inner").get(facingFromInt(i)).getQuads(state, side, rand));
                    }
                    break;

                case Z_EDGE:
                    siding = i % 2 == 0 ? "right" : "left";
                    facing = i == 1 || i == 2 ? EnumFacing.EAST : EnumFacing.WEST;
                    bq.addAll(modelPieces.get(material).get(siding + "_connect").get(facing).getQuads(state, side, rand));
                    break;

                case X_END:
                    bq.addAll(modelPieces.get(material).get("corner_outer").get(facingFromInt(i)).getQuads(state, side, rand));

                    siding = i % 2 == 0 ? "left" : "right";
                    facing = i <= 1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                    bq.addAll(modelPieces.get(material).get(siding + "_merge").get(facing).getQuads(state, side, rand));
                    break;

                case Z_END:
                    bq.addAll(modelPieces.get(material).get("corner_outer").get(facingFromInt(i)).getQuads(state, side, rand));

                    siding = i % 2 == 0 ? "right" : "left";
                    facing = i == 1 || i == 2 ? EnumFacing.EAST : EnumFacing.WEST;
                    bq.addAll(modelPieces.get(material).get(siding + "_merge").get(facing).getQuads(state, side, rand));
                    break;

                case X_EDGE:
                    siding = i % 2 == 0 ? "left" : "right";
                    facing = i <= 1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                    bq.addAll(modelPieces.get(material).get(siding + "_connect").get(facing).getQuads(state, side, rand));
                    break;

                case MIDDLE:
                default:
                    break;
            }
        }

        return bq;
    }

    private EnumFacing facingFromInt(int number){
        switch (number){
            case 0:
                return EnumFacing.NORTH;
            case 1:
                return EnumFacing.EAST;
            case 2:
                return EnumFacing.SOUTH;
            case 3:
                return EnumFacing.WEST;
            default:
                return EnumFacing.NORTH;
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
        return modelPieces.get(modelType).get("bottom").get(EnumFacing.NORTH).getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }
}
