package dmfmm.catwalks.events;

import dmfmm.catwalks.item.ItemBlockCatwalk;
import dmfmm.catwalks.registry.BlockRegistry;
import dmfmm.catwalks.utils.CatwalkMaterial;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.server.FMLServerHandler;

import java.util.List;
import java.util.Random;

public class NyanWalkRecipeEvent {

    @SubscribeEvent
    public void makeNyanWalks(LivingDeathEvent event){
        if (event.getEntity() instanceof EntityOcelot) {
            World world = event.getEntity().getEntityWorld();
            BlockPos catPos = event.getEntity().getPosition();
            List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(catPos.getX() - 5, catPos.getY() - 5, catPos.getZ() - 5, catPos.getX() + 5, catPos.getY() + 5, catPos.getZ() + 5));
            for(EntityItem itemStacker : items){
                if(itemStacker.getItem().getItem() instanceof ItemBlockCatwalk){
                    NBTTagCompound tag = itemStacker.getItem().getTagCompound();
                    if(tag != null) {
                        tag.setString("material", CatwalkMaterial.NYANWALK.getName().toLowerCase());
                        ItemStack jones = new ItemStack(Item.getItemFromBlock(BlockRegistry.CATWALK), itemStacker.getItem().getCount());
                        jones.setTagCompound(tag);
                        EntityItem i = new EntityItem(itemStacker.getEntityWorld(), itemStacker.posX, itemStacker.posY + 8, itemStacker.posZ, jones);
                        itemStacker.setDead();

                        if(!world.isRemote)
                            world.spawnEntity(i);
                        else{
                            world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, itemStacker.posX, itemStacker.posY, itemStacker.posZ, 0, 1, 0);

                            Random random = world.rand;
                            for (int i2 = 0; i2 < 200; ++i2)
                            {
                                float f2 = random.nextFloat() * 4.0F;
                                float f3 = random.nextFloat() * ((float)Math.PI * 2F);
                                double d6 = (double)(MathHelper.cos(f3) * f2);
                                double d7 = 0.01D + random.nextDouble() * 0.5D;
                                double d8 = (double)(MathHelper.sin(f3) * f2);
                                world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, false, (double)itemStacker.posX + d6 * 0.1D, (double)itemStacker.posY + 0.3D, (double)itemStacker.posZ + d8 * 0.1D, d6, d7, d8);
                            }
                        }
                    }
                }
            }
        }
    }
}
