package pretreatment.data;

import java.util.HashMap;

/**
 * Created by xing89qs on 16/3/11.
 */
public class Vector {
    private HashMap<String,Double> vec;
    private String label;

    public Vector(){
        this.vec = new HashMap<>();
    }

    public HashMap<String, Double> getVector() {
        return vec;
    }

    public void addFeature(String feature,double value) {
        this.vec.put(feature,value);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
