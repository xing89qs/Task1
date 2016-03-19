package pretreatment.analyze.featureChoose;

import pretreatment.analyze.Segmentation;
import pretreatment.data.Data;
import pretreatment.data.DataSet;

import java.util.*;

/**
 * Created by xing89qs on 16/3/15.
 */
public class IGFeatureChooser extends FeatureChooser{
    private final int DEFAULT_FEATURE_NUM = 30;//缺省选择的特征数
    private int featureNum = DEFAULT_FEATURE_NUM;

    public IGFeatureChooser(DataSet dataSet) {
        super(dataSet);
    }

    private static HashSet<String> getTopFeatures(HashMap<String,Double> frequency,int topN) {
        HashSet<String> featureSet = new HashSet<>();
        Map.Entry<String,Double> frequencyArray[] = new Map.Entry[frequency.size()];
        frequency.entrySet().toArray(frequencyArray);
        Arrays.sort(frequencyArray, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        for(int i = 0;i<topN&&i<frequencyArray.length;i++){
            featureSet.add(frequencyArray[i].getKey());
        }
        return featureSet;
    }

    @Override
    public HashSet<String> chooseFeature(){
        Iterator<Data> iterator = dataSet.getDataIterator();
        HashMap<String,Double> ig = new HashMap<>();
        HashMap<String,Integer> info[] = new HashMap[dataSet.getLabelNum()];
        for(int i = 0;i<dataSet.getLabelNum();i++) {
            info[i] = new HashMap<>();
        }
        while(iterator.hasNext()){
            Data data = iterator.next();
            int label = dataSet.getIndexByLabel(data.getLabel());
            HashSet<String> tokenSet = new HashSet<>(Segmentation.splitWord(data.getContent()));
            for(String token: tokenSet){
                if(info[label].containsKey(token))
                    info[label].put(token,info[label].get(token)+1);
                else
                    info[label].put(token,1);
            }
        }
        for(int i = 0;i<dataSet.getLabelNum();i++){
            for(String token: info[i].keySet()){
                int allNum = 0;
                for(int j = 0;j<dataSet.getLabelNum();j++)
                    if(info[j].containsKey(token))
                        allNum+=info[j].get(token);
                double information_gain = info[i].get(token)/(double)allNum;
                information_gain = -information_gain*Math.log(information_gain)-(1.0-information_gain)*Math.log(1.0-information_gain);
                if(ig.containsKey(token))
                    ig.put(token,ig.get(token)+information_gain);
                else
                    ig.put(token,information_gain);
            }
        }
        HashSet<String> chosenFeature = IGFeatureChooser.getTopFeatures(ig,this.featureNum);

        for(String token: chosenFeature){
            System.out.println(token);
        }

        return chosenFeature;
    }

    public void setChooseFeatureNum(int chooseFeatureNum) {
        this.featureNum = chooseFeatureNum;
    }
}
