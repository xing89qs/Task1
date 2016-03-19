package pretreatment.analyze.featureChoose;

import pretreatment.data.DataSet;

/**
 * Created by xing89qs on 16/3/14.
 */
public class FeatureChooseFactory {
    public static ChiFeatureChooser getChiFeatureChooser(DataSet dataSet){
        return new ChiFeatureChooser(dataSet);
    }

    public static FrequencyFeatureChooser getFrequencyFeatureChooser(DataSet dataSet){
        return new FrequencyFeatureChooser(dataSet);
    }

    public static IGFeatureChooser getIGFeatureChooser(DataSet dataSet) {
        return new IGFeatureChooser(dataSet);
    }
}
