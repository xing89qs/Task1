package pretreatment.analyze.featureChoose;

import pretreatment.analyze.Segmentation;
import pretreatment.data.Data;
import pretreatment.data.DataSet;

import java.util.*;

/**
 * Created by xing89qs on 16/3/14.
 */
public class FrequencyFeatureChooser extends FeatureChooser{

    private final int DEFAULT_FEATURE_NUM = 30;//每一种类别缺省选择的特征数
    private int featureNum = DEFAULT_FEATURE_NUM;

    public FrequencyFeatureChooser(DataSet dataSet){
        super(dataSet);
    }

    private static HashSet<String> getTopFeatures(HashMap<String,Integer> frequency,int topN) {
        HashSet<String> featureSet = new HashSet<>();
        Map.Entry<String,Integer> frequencyArray[] = new Map.Entry[frequency.size()];
        frequency.entrySet().toArray(frequencyArray);
        Arrays.sort(frequencyArray, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        for(int i = 0;i<topN&&i<frequencyArray.length;i++){
            featureSet.add(frequencyArray[i].getKey());
        }
        return featureSet;
    }

    @Override
    public HashSet<String> chooseFeature() {
        Iterator<Data> iterator = dataSet.getDataIterator();
        HashMap<String,Integer> frequency[] = new HashMap[dataSet.getLabelNum()];
        for(int i = 0;i<dataSet.getLabelNum();i++)
            frequency[i] = new HashMap<>();
        while(iterator.hasNext()){
            Data data = iterator.next();
            int label = dataSet.getIndexByLabel(data.getLabel());
            HashSet<String> tokenSet = new HashSet<>(Segmentation.splitWord(data.getContent()));
            for(String token: tokenSet){
                if(frequency[label].containsKey(token))
                    frequency[label].put(token,frequency[label].get(token)+1);
                else frequency[label].put(token,1);
            }
        }
        HashSet<String> chosenFeature = new HashSet<>();
        for(int i = 0;i<dataSet.getLabelNum();i++){
            HashSet<String> featureSet = FrequencyFeatureChooser.getTopFeatures(frequency[i],this.featureNum);
            chosenFeature.addAll(featureSet);
        }
        return chosenFeature;
    }


    public void setChooseFeatureNum(int featureNum) {
        this.featureNum = featureNum;
    }
}
