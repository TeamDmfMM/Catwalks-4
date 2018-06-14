package dmf444.catwalks.block;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedArbitraryProperty implements IUnlistedProperty {

    private String name;
    private Class type;

    public UnlistedArbitraryProperty(String name, Class type){
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(Object value) {
        return true;
    }

    @Override
    public Class getType() {
        return type;
    }

    @Override
    public String valueToString(Object value) {
        return value.toString();
    }

}
