package dmf444.catwalks.utils;

import net.minecraft.util.IStringSerializable;

public enum  CatwalkMaterial implements IStringSerializable {
    CLASSIC;

    @Override
    public String getName() {
        return this.name().toLowerCase();
    }
}
