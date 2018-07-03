package dmfmm.catwalks.client;

import dmfmm.catwalks.block.CatwalkBlock;
import dmfmm.catwalks.client.catwalks.CatwalkModel;
import dmfmm.catwalks.client.catwalks.CatwalkState;
import dmfmm.catwalks.item.BlowtorchItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class RedOverlayEvent {

    private static class RedWrapper implements IBakedModel {
        IBakedModel delegate;

        public RedWrapper(IBakedModel model) {
            this.delegate = model;
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
            List<BakedQuad> bbq = delegate.getQuads(state, side, rand);
            bbq = bbq.stream().map(q -> {
                UnpackedBakedQuad.Builder b = new UnpackedBakedQuad.Builder(q.getFormat());
                q.pipe(new VertexTransformer(b) {
                    @Override
                    public void put(int element, float... data) {
                        if (this.getVertexFormat().getElement(element).getUsage() == VertexFormatElement.EnumUsage.COLOR) {
                            if (data.length >= 3) {
                                data[0] = 1;
                                data[1] = 0;
                                data[2] = 0;
                                super.put(element, data);
                            }
                            else {
                                super.put(element, data);
                            }
                        }
                        else {
                            super.put(element, data);
                        }
                    }
                });
                UnpackedBakedQuad ubq = b.build();
                return new BakedQuad(ubq.getVertexData(), ubq.getTintIndex(), ubq.getFace(), ubq.getSprite(), ubq.shouldApplyDiffuseLighting(), ubq.getFormat());
            }).collect(Collectors.toList());
            return bbq;
        }

        @Override
        public boolean isAmbientOcclusion() {
            return delegate.isAmbientOcclusion();
        }

        @Override
        public boolean isGui3d() {
            return delegate.isGui3d();
        }

        @Override
        public boolean isBuiltInRenderer() {
            return delegate.isBuiltInRenderer();
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return delegate.getParticleTexture();
        }

        @Override
        public ItemOverrideList getOverrides() {
            return delegate.getOverrides();
        }
    }

    @SubscribeEvent
    public void renderCatwalkEmptyFaces(RenderWorldLastEvent evt) {
        Minecraft mc = Minecraft.getMinecraft();

        EntityPlayerSP p = mc.player;

        ItemStack heldItem = p.getHeldItem(EnumHand.MAIN_HAND);
        if (!heldItem.isEmpty()) {
            if (heldItem.getItem() instanceof BlowtorchItem && p.isSneaking()) {
                renderFullCatwalkBlocks(evt, mc);
            }
        }
    }

    private static void renderFullCatwalkBlocks(RenderWorldLastEvent evt, Minecraft mc){
        EntityPlayerSP p = mc.player;
        WorldClient world = mc.world;
        double doubleX = p.lastTickPosX + (p.posX - p.lastTickPosX) * evt.getPartialTicks();
        double doubleY = p.lastTickPosY + (p.posY - p.lastTickPosY) * evt.getPartialTicks();
        double doubleZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * evt.getPartialTicks();

        GlStateManager.pushMatrix();
        GlStateManager.translate(-doubleX, -doubleY, -doubleZ);


        GlStateManager.colorMask(false, true, true, true);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        for (int dx = -5 ; dx <= 5 ; dx++) {
            for (int dy = -5 ; dy <= 5 ; dy++) {
                for (int dz = -5 ; dz <= 5 ; dz++) {
                    BlockPos c = p.getPosition().add(dx, dy, dz);
                    IBlockState state = world.getBlockState(c);
                    Block block = state.getBlock();
                    if(block instanceof CatwalkBlock){
                        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                        IBlockState actualState = block.getActualState(state, world, c);
                        IBakedModel model = dispatcher.getModelForState(actualState);//new RedWrapper(dispatcher.getModelForState(actualState));
                        IBlockState state1 = computeMissingSides(world, actualState, c);
                        dispatcher.getBlockModelRenderer().renderModel(world, model, state1, c, buffer, false);
                    }
                }
            }
        }
        tessellator.draw();
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.popMatrix();

    }

    private static IBlockState computeMissingSides(World world, IBlockState state, BlockPos pos){
        IBlockState bS = ((CatwalkBlock) state.getBlock()).getExtendedState(state, world, pos);
        if(bS instanceof IExtendedBlockState){
            IExtendedBlockState extBS = (IExtendedBlockState) bS;
            CatwalkState catwalkState = extBS.getValue(CatwalkBlock.CATWALK_STATE);
            CatwalkState state1 =  new CatwalkState(
                    getOpposite(catwalkState.getRailSections().get(0)),
                    getOpposite(catwalkState.getRailSections().get(1)),
                    getOpposite(catwalkState.getRailSections().get(3)),
                    getOpposite(catwalkState.getRailSections().get(2)),
                    catwalkState.getFloorSections().get(0) == null ? CatwalkModel.FloorSection.OUTER : null,
                    catwalkState.getFloorSections().get(1) == null ? CatwalkModel.FloorSection.OUTER : null,
                    catwalkState.getFloorSections().get(2) == null ? CatwalkModel.FloorSection.OUTER : null,
                    catwalkState.getFloorSections().get(3) == null ? CatwalkModel.FloorSection.OUTER : null
            );
            return extBS.withProperty(CatwalkBlock.CATWALK_STATE, state1);
        }
        return state;
    }

    private static CatwalkModel.RailSection getOpposite(CatwalkModel.RailSection current) {
        switch (current){
            case X_EDGE:
            case X_END:
                return CatwalkModel.RailSection.Z_EDGE;
            case MIDDLE:
                return CatwalkModel.RailSection.OUTER;
            case Z_END:
            case Z_EDGE:
                return CatwalkModel.RailSection.X_EDGE;
            case INNER:
                return CatwalkModel.RailSection.OUTER;
            case OUTER:
            default:
                return CatwalkModel.RailSection.MIDDLE;
        }
    }

}
