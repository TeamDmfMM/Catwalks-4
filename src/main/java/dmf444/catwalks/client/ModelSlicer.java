package dmf444.catwalks.client;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ModelSlicer {

    public List<BakedQuad> slice(List<BakedQuad> quads, AxisAlignedBB bb, Vec3d offset) {
        return this.slice(quads, bb, offset, (BakedQuad q) -> true);
    }

    public List<BakedQuad> slice(List<BakedQuad> quads, AxisAlignedBB bb, Vec3d offset, Function<BakedQuad, Boolean> filter){
        ImmutableList.Builder<BakedQuad> builder = new ImmutableList.Builder<BakedQuad>();
        sliceInto(builder, quads, bb, offset, filter);

        return builder.build();
    }

    private void sliceInto(ImmutableList.Builder<BakedQuad> builder, List<BakedQuad> quads, AxisAlignedBB bb, Vec3d offset, Function<BakedQuad, Boolean> filter) {
        double tiny = 0.000001;
        AxisAlignedBB expandedBB = bb.expand(tiny, tiny, tiny);
        for (BakedQuad it : quads) {
            if (!filter.apply(it)) continue;
            BakedQuad slicedDicedAndServedWithRice = sliceQuad(it, expandedBB, offset);

            if (slicedDicedAndServedWithRice != null) builder.add(slicedDicedAndServedWithRice);
        }
    }

    private BakedQuad sliceQuad(BakedQuad quad, AxisAlignedBB bb, Vec3d offset){
        ArrayList<Vec3d> positions = new ArrayList<Vec3d>(4);
        positions.add(Vec3d.ZERO);
        positions.add(Vec3d.ZERO);
        positions.add(Vec3d.ZERO);
        positions.add(Vec3d.ZERO);

        final int[] i = {0};
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(quad.getFormat());

        quad.pipe(new VertexTransformer(builder){
            @Override
            public void put(int element, float... data)
            {
                VertexFormatElement.EnumUsage usage = parent.getVertexFormat().getElement(element).getUsage();
                if(usage == VertexFormatElement.EnumUsage.POSITION && data.length == 3){
                    positions.set(i[0]++,new Vec3d(data[0], data[1], data[2]));
                }
                super.put(element, data);
            }
        });

        Vec3d normal = ((positions.get(1).subtract(positions.get(0)))).crossProduct(positions.get(2).subtract(positions.get(0))).normalize();

        Vec3d pos = Vec3d.ZERO;
        for (Vec3d it : positions) {
            pos = pos.add(it);
        }

        pos = pos.scale(0.25);
        pos = pos.subtract(normal.scale(0.001));
        if (!bb.contains(pos)) return null;

        builder = new UnpackedBakedQuad.Builder(quad.getFormat());

        quad.pipe(new VertexTransformer(builder) {
            @Override
            public void put(int element, float... data_) {
                float[] data = data_;
                VertexFormatElement.EnumUsage usage = parent.getVertexFormat().getElement(element).getUsage();

                if (usage == VertexFormatElement.EnumUsage.POSITION && data.length >= 3) {
                    data = data.clone();
                    data[0] = (float)(data[0] - (bb.minX - offset.x));
                    data[1] = (float)(data[1] - (bb.minY - offset.y));
                    data[2] = (float)(data[2] - (bb.minZ - offset.z));
                }
                if (usage == VertexFormatElement.EnumUsage.NORMAL && data.length >= 3 && i[0] >= 2) {
                    data = data.clone();
                    data[0] = (float) normal.x;
                    data[1] = (float) normal.y;
                    data[2] = (float) normal.z;
                }
                super.put(element, data);
            }
        });

        UnpackedBakedQuad unpacked = builder.build();

        return new BakedQuad( // pack the quad, for memory reasons
                unpacked.getVertexData(), unpacked.getTintIndex(), unpacked.getFace(), unpacked.getSprite(),
                unpacked.shouldApplyDiffuseLighting(), unpacked.getFormat()
        );
    }
}
