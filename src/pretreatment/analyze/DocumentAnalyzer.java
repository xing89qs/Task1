package pretreatment.analyze;

import pretreatment.analyze.featureChoose.FeatureChooseFactory;
import pretreatment.analyze.featureChoose.FeatureChooser;
import pretreatment.data.Data;
import pretreatment.data.Vector;
import java.util.Map.Entry;

import java.util.*;

/**
 * Created by xing89qs on 16/3/13.
 */

public class DocumentAnalyzer {
    private HashSet<String> featureSet;
    private Iterator<Data> trainingData;
    private HashMap<String, Integer> documentFrequency;

    private HashMap<String, Integer> tokenIndex;
    private int documentNumber;
    private FeatureChooser featureChooser;

    public DocumentAnalyzer(Iterator<Data> trainingData, FeatureChooser featureChooser) {
        this.featureSet = featureChooser.chooseFeature();
        this.documentFrequency = new HashMap<>();
        this.trainingData = trainingData;
        this.tokenIndex = new HashMap<>();
        this.featureChooser = featureChooser;
        this.analyze();
    }

    private void analyze() {
        this.documentNumber = 0;
        while (trainingData.hasNext()) {
            Data data = trainingData.next();
            ++this.documentNumber;
            ArrayList<String> documentTokenList = Segmentation.splitWord(data.getContent());
            HashSet<String> documentTokenSet = new HashSet<>();
            documentTokenSet.addAll(documentTokenList);
            for (String token : documentTokenSet) {
                if (documentFrequency.containsKey(token))
                    documentFrequency.put(token, documentFrequency.get(token) + 1);
                else
                    documentFrequency.put(token, 1);
            }
        }
        this.generateTokenIndex();

    }

    private void generateTokenIndex() {
        int index = 0;
        for (String s : documentFrequency.keySet()) {
            tokenIndex.put(s, ++index);
        }
    }

    public Vector documentToVector(Data data){
        Vector vector = new Vector();
        vector.setLabel(data.getLabel());
        ArrayList<String> documentTokenList = Segmentation.splitWord(data.getContent());
        HashMap<String,Double> termFrequency = new HashMap<>();
        for (String token : documentTokenList) {
            if(!featureSet.contains(token)) continue;
            if(!documentFrequency.containsKey(token)) continue;
            if (termFrequency.containsKey(token))
                termFrequency.put(token, termFrequency.get(token) + 1);
            else
                termFrequency.put(token, 1.0);
        }
        int tokenNum = documentTokenList.size();
        for (Entry<String,Double> entry: termFrequency.entrySet()){
            double tf = entry.getValue();
            double tf_idf = entry.getValue()/((double) tokenNum)
                    * Math.log(Double.valueOf(this.documentNumber) / (documentFrequency.get(entry.getKey())));
            double df = documentFrequency.get(entry.getKey());
            vector.addFeature(entry.getKey(),tf_idf);
        }
        return vector;
    }

    public Integer getIndex(String token) {
        return this.tokenIndex.get(token);
    }
}
