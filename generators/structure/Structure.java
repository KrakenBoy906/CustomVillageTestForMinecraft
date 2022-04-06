package com.shourya.customvillage.generators.structure;

import com.shourya.customvillage.datatypes.Bound;
import com.shourya.customvillage.datatypes.Vector2;

public class Structure {
    private int widthElasticity;
    private int heightElasticity;
    private Bound baseBound;
    private Bound elasticBound;
    void setBaseBound(Bound baseBound){}
    void setElasticBound(Bound elasticBound){}

    public Structure(Bound baseBound, int widthElasticity, int heightElasticity) {
        this.baseBound = baseBound;
        this.widthElasticity = widthElasticity;
        this.heightElasticity = heightElasticity;
        elasticBound = new Bound(baseBound.center, baseBound.width + widthElasticity, baseBound.height + heightElasticity);
    }
}
