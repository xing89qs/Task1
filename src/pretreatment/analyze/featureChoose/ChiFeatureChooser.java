package pretreatment.analyze.featureChoose;

import pretreatment.data.DataSet;

import java.util.HashSet;

/**
 * Created by xing89qs on 16/3/13.
 */
public class ChiFeatureChooser extends FeatureChooser{

    public ChiFeatureChooser(DataSet dataSet){
        super(dataSet);
    }

    @Override
    public HashSet<String> chooseFeature(){
        return null;
    }
}
