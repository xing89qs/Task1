package pretreatment.analyze.featureChoose;

import pretreatment.data.DataSet;

import java.util.HashSet;

/**
 * Created by xing89qs on 16/3/13.
 */
public class FeatureChooser {


    protected DataSet dataSet;

    public FeatureChooser(DataSet dataSet){
        this.dataSet = dataSet;
    }

    public HashSet<String> chooseFeature(){
        return null;
    }
}
