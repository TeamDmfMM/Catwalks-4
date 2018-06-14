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
        ArrayList<Vec3d> positions = new ArrayList<Vec3d>();
        positions.add(Vec3d.ZERO);
        positions.add(Vec3d.ZERO);
        positions.add(Vec3d.ZERO);
        positions.add(Vec3d.ZERO);

        int i = 0;
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(quad.getFormat());

        quad.pipe(new VertexTransformer(builder){
            @Override
            public void put(int element, float... data)
            {
                VertexFormatElement.EnumUsage usage = parent.getVertexFormat().getElement(element).getUsage();
                if(usage == VertexFormatElement.EnumUsage.POSITION && data.length == 3){
                    positions.add(i++, new Vec3d(data[0], data[1], data[2]));
                }
                super.put(element, data);
            }
        });
    }
}
