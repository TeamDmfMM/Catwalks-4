package dmfmm.catwalks.utils;

import net.minecraft.util.IStringSerializable;

public enum  CatwalkMaterial implements IStringSerializable {
    CLASSIC,
    MAGNETICRAFT_STEEL,
    GLASS,
    CUSTOM_0,
    TREATED_WOOD;

    @Override
    public String getName() {
        return this.name().toLowerCase();
    }
}
