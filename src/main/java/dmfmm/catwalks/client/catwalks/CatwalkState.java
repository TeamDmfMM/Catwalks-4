package dmfmm.catwalks.client.catwalks;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CatwalkState {

    TIntSet layers;
    List<CatwalkModel.RailSection> railSections;
    List<CatwalkModel.FloorSection> floorSections;

    private CatwalkState(List<CatwalkModel.RailSection> rail, List<CatwalkModel.FloorSection> floor, TIntSet layers) {
        this.layers = layers;
        this.floorSections = floor;
        this.railSections = rail;
    }

    public CatwalkState(CatwalkModel.RailSection railNW, CatwalkModel.RailSection railNE, CatwalkModel.RailSection railSW, CatwalkModel.RailSection railSE,
                        CatwalkModel.FloorSection floorNW, CatwalkModel.FloorSection floorNE, CatwalkModel.FloorSection floorSW, CatwalkModel.FloorSection floorSE, int... layers) {
        this(new ArrayList<CatwalkModel.RailSection>(Arrays.asList(railNW, railNE, railSE, railSW)), new ArrayList<CatwalkModel.FloorSection>(Arrays.asList(floorNW, floorNE, floorSE, floorSW)), new TIntHashSet(layers));
    }

    public boolean hasLayer(int layer) {
        return this.layers.contains(layer);
    }

    @Override
    public int hashCode(){
        return 31 * (31 * railSections.hashCode() + floorSections.hashCode()) + layers.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(this.hashCode() == o.hashCode()) return true;
        if(!(o instanceof CatwalkState)) return false;

        if(railSections != ((CatwalkState) o).railSections) return false;
        if(floorSections != ((CatwalkState) o).floorSections) return false;
        if(layers != ((CatwalkState) o).layers) return false;
        return true;
    }

    public List<CatwalkModel.RailSection> getRailSections(){
        return railSections;
    }

    public List<CatwalkModel.FloorSection> getFloorSections() {
        return floorSections;
    }
}
