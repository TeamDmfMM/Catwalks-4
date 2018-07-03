package dmfmm.catwalks.client;

import com.google.common.collect.ImmutableMap;
import dmfmm.catwalks.block.LadderBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LadderModel implements IBakedModel {

    private ImmutableMap<EnumFacing, IBakedModel> rotatedSide;
    private ImmutableMap<EnumFacing, IBakedModel> rotatedLadder;
    private ImmutableMap<EnumFacing, IBakedModel> rotatedWrap;
    private ImmutableMap<EnumFacing, IBakedModel> rotatedRightConnection;
    private ImmutableMap<EnumFacing, IBakedModel> rotatedLeftConnection;
    private ImmutableMap<EnumFacing, IBakedModel> rotatedBase;
    private ImmutableMap<EnumFacing, IBakedModel> rotatedBasePoles;
    private ImmutableMap<EnumFacing, IBakedModel> rotatedBaseConnected;
    private ImmutableMap<EnumFacing, IBakedModel> rotatedBigLeft;
    private ImmutableMap<EnumFacing, IBakedModel> rotatedBigRight;
    private ImmutableMap<EnumFacing, IBakedModel> rotatedBigWrap;
    private ImmutableMap<EnumFacing, IBakedModel> rotatedLadderTop;
    private ImmutableMap<EnumFacing, IBakedModel> rotatedMergeLeft;
    private ImmutableMap<EnumFacing, IBakedModel> rotatedMergeRight;

    public LadderModel(IModel ladder, IModel side, IModel connectionRight, IModel connectionLeft, IModel wrap, IModel base, IModel secondPole, IModel connectedBase,
                       IModel bigLeft, IModel bigRight, IModel bigWrap, IModel ladderTop, IModel mergeLeft, IModel mergeRight, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter){
        ImmutableMap.Builder<EnumFacing, IBakedModel> sideBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<EnumFacing, IBakedModel> ladderBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<EnumFacing, IBakedModel> wrapBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<EnumFacing, IBakedModel> rightConBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<EnumFacing, IBakedModel> leftConBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<EnumFacing, IBakedModel> baseBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<EnumFacing, IBakedModel> basePoleBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<EnumFacing, IBakedModel> baseConnectedBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<EnumFacing, IBakedModel> bigLeftBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<EnumFacing, IBakedModel> bigRightBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<EnumFacing, IBakedModel> bigWrapBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<EnumFacing, IBakedModel> ladderTopBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<EnumFacing, IBakedModel> mergeLeftBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<EnumFacing, IBakedModel> mergeRightBuilder = ImmutableMap.builder();

        for (EnumFacing facing: EnumFacing.HORIZONTALS) {
            TRSRTransformation transformation = TRSRTransformation.from(facing);

            sideBuilder.put(facing, side.bake(transformation, format, bakedTextureGetter));
            ladderBuilder.put(facing, ladder.bake(transformation, format, bakedTextureGetter));
            wrapBuilder.put(facing, wrap.bake(transformation, format, bakedTextureGetter));
            rightConBuilder.put(facing, connectionRight.bake(transformation, format, bakedTextureGetter));
            leftConBuilder.put(facing, connectionLeft.bake(transformation, format, bakedTextureGetter));
            basePoleBuilder.put(facing, secondPole.bake(transformation, format, bakedTextureGetter));
            baseConnectedBuilder.put(facing, connectedBase.bake(transformation, format, bakedTextureGetter));
            bigLeftBuilder.put(facing, bigLeft.bake(transformation, format, bakedTextureGetter));
            bigRightBuilder.put(facing, bigRight.bake(transformation, format, bakedTextureGetter));
            bigWrapBuilder.put(facing, bigWrap.bake(transformation, format, bakedTextureGetter));
            ladderTopBuilder.put(facing, ladderTop.bake(transformation, format, bakedTextureGetter));
            mergeLeftBuilder.put(facing, mergeLeft.bake(transformation, format, bakedTextureGetter));
            mergeRightBuilder.put(facing, mergeRight.bake(transformation, format, bakedTextureGetter));

            //Non-connected base plate raised
            Vector3f t = transformation.getTranslation();
            t.add(new Vector3f(0f, 0.06f, 0f));
            baseBuilder.put(facing, base.bake(new TRSRTransformation(t, transformation.getLeftRot(), transformation.getScale(), transformation.getRightRot()), format, bakedTextureGetter));

        }

        rotatedSide = sideBuilder.build();
        rotatedLadder = ladderBuilder.build();
        rotatedWrap = wrapBuilder.build();
        rotatedRightConnection = rightConBuilder.build();
        rotatedLeftConnection = leftConBuilder.build();
        rotatedBase = baseBuilder.build();
        rotatedBasePoles = basePoleBuilder.build();
        rotatedBaseConnected = baseConnectedBuilder.build();
        rotatedBigLeft = bigLeftBuilder.build();
        rotatedBigRight = bigRightBuilder.build();
        rotatedBigWrap = bigWrapBuilder.build();
        rotatedLadderTop = ladderTopBuilder.build();
        rotatedMergeLeft = mergeLeftBuilder.build();
        rotatedMergeRight = mergeRightBuilder.build();

    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> bq = new ArrayList<>();
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState ext = (IExtendedBlockState) state;
            LadderBlock.LadderState ladderState = ext.getValue(LadderBlock.STATE);

            EnumFacing facing = ext.getValue(LadderBlock.FACING);
            switch(ladderState){
                case MIDDLE:
                    boolean hasCage = ext.getValue(LadderBlock.HAS_CAGE);
                    //Simple Ladder, not caged
                    bq.addAll(rotatedLadder.get(facing).getQuads(state, side, rand));
                    bq.addAll(rotatedSide.get(facing).getQuads(state, side, rand));
                    bq.addAll(rotatedSide.get(facing.rotateY()).getQuads(state, side, rand));
                    if(hasCage) {
                        //Caged Edition
                        bq.addAll(rotatedWrap.get(facing.getOpposite()).getQuads(state, side, rand));
                        bq.addAll(rotatedWrap.get(facing.rotateYCCW()).getQuads(state, side, rand));
                        bq.addAll(rotatedRightConnection.get(facing.rotateYCCW()).getQuads(state, side, rand));
                        bq.addAll(rotatedLeftConnection.get(facing.rotateY()).getQuads(state, side, rand));
                    }
                    break;
                case BOTTOM:
                    boolean isConnected = ext.getValue(LadderBlock.CONNECTED);
                    //Simple Ladder, not caged
                    bq.addAll(rotatedLadder.get(facing).getQuads(state, side, rand));
                    bq.addAll(rotatedSide.get(facing).getQuads(state, side, rand));
                    bq.addAll(rotatedSide.get(facing.rotateY()).getQuads(state, side, rand));
                    //render in floor piece
                    if(isConnected){
                        bq.addAll(rotatedBaseConnected.get(facing.rotateYCCW()).getQuads(state, side, rand));
                        bq.addAll(rotatedRightConnection.get(facing.rotateYCCW()).getQuads(state, side, rand));
                        bq.addAll(rotatedLeftConnection.get(facing.rotateY()).getQuads(state, side, rand));
                        bq.addAll(rotatedRightConnection.get(facing.rotateY()).getQuads(state, side, rand));
                        bq.addAll(rotatedLeftConnection.get(facing.rotateYCCW()).getQuads(state, side, rand));
                        bq.addAll(rotatedSide.get(facing.getOpposite()).getQuads(state, side, rand));
                        bq.addAll(rotatedSide.get(facing.rotateYCCW()).getQuads(state, side, rand));
                    } else {
                        bq.addAll(rotatedRightConnection.get(facing.rotateYCCW()).getQuads(state, side, rand));
                        bq.addAll(rotatedLeftConnection.get(facing.rotateY()).getQuads(state, side, rand));
                        bq.addAll(rotatedBase.get(facing).getQuads(state, side, rand));
                        bq.addAll(rotatedBasePoles.get(facing.rotateYCCW()).getQuads(state, side, rand));
                        bq.addAll(rotatedBasePoles.get(facing.rotateY()).getQuads(state, side, rand));
                    }
                    break;
                case TOP:
                    isConnected = ext.getValue(LadderBlock.CONNECTED);
                    hasCage = ext.getValue(LadderBlock.HAS_CAGE);
                    if(hasCage){
                        //Thick top cage
                        bq.addAll(rotatedBigWrap.get(facing.getOpposite()).getQuads(state, side, rand));
                        bq.addAll(rotatedBigWrap.get(facing.rotateYCCW()).getQuads(state, side, rand));
                    }
                    if(isConnected){
                        bq.addAll(rotatedLadderTop.get(facing).getQuads(state, side, rand));
                        if(hasCage) {
                            bq.addAll(rotatedMergeRight.get(facing.rotateYCCW()).getQuads(state, side, rand));
                            bq.addAll(rotatedMergeLeft.get(facing.rotateY()).getQuads(state, side, rand));
                        }
                    } else {
                        if(hasCage) {
                            bq.addAll(rotatedBigRight.get(facing.rotateYCCW()).getQuads(state, side, rand));
                            bq.addAll(rotatedBigLeft.get(facing.rotateY()).getQuads(state, side, rand));
                        }
                        //Simple Ladder
                        bq.addAll(rotatedLadder.get(facing).getQuads(state, side, rand));
                        bq.addAll(rotatedSide.get(facing).getQuads(state, side, rand));
                        bq.addAll(rotatedSide.get(facing.rotateY()).getQuads(state, side, rand));
                    }
            }

        }
        return bq;
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
        return rotatedBase.get(EnumFacing.NORTH).getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }
}
