package com.shourya.customvillage.generators.area;

import com.shourya.customvillage.datatypes.Vector2;

import java.util.LinkedList;
import java.util.List;

public class AreaContext {
    int tag;
    int culture;
    int wealthDegree;
    int populationDegree;
    int natureDegree;

    Vector2 tradeCenter;

    public List<Vector2> edgeAreaBlockPosList;
    public List<Vector2> expandableEdgeAreaBlockPosList;
    public List<Vector2> nonExpandableEdgeAreaBlockPosList;

    public AreaContext(int tag) {
        this.tag = tag;

        edgeAreaBlockPosList = new LinkedList<Vector2>();
        expandableEdgeAreaBlockPosList = new LinkedList<Vector2>();
        nonExpandableEdgeAreaBlockPosList = new LinkedList<Vector2>();
    }

    public void free() {
        edgeAreaBlockPosList = null;
        expandableEdgeAreaBlockPosList = null;
        nonExpandableEdgeAreaBlockPosList = null;
    }

    public Vector2 getTradeCenter() {
        return  tradeCenter;
    }

    public void setTradeCenter(Vector2 p)
    {
        tradeCenter = p;
    }
    public int getTagInfo()
    {
        return tag;
    }

}
