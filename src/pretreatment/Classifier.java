package pretreatment;

import pretreatment.analyze.DocumentAnalyzer;
import pretreatment.analyze.featureChoose.FeatureChooseFactory;
import pretreatment.analyze.featureChoose.FrequencyFeatureChooser;
import pretreatment.analyze.featureChoose.IGFeatureChooser;
import pretreatment.data.Data;
import pretreatment.data.DataSet;
import svm.SVMTest;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by xing89qs on 16/3/11.
 */

class FileHandler{
    public FileHandler(File file){

    }
}

public class Classifier {

    private static String charEncoding = "GBK";
    private static String fileEncoding = "UTF-8";

    private static Boolean[] random_shuffle(int n){
        Boolean[] b = new Boolean[n];
        Arrays.fill(b,false);
        Arrays.fill(b,0,n/2,true);
        List<Boolean> temp = Arrays.asList(b);
        Collections.shuffle(temp);
        return (Boolean[]) temp.toArray();
    }

    public void init() throws IOException {
        DataSet trainData = new DataSet();
        DataSet testData = new DataSet();

        File allFile = new File("text classification");
        File[] files = allFile.listFiles();
        for(File trainDir: files){
            String label = trainDir.getName();
            File[] trainFiles = trainDir.listFiles();
            Boolean[] isTrainFile = Classifier.random_shuffle(trainFiles.length);
            int cnt = 0;
            for(File trainFile: trainFiles) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(trainFile),fileEncoding));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if(line.equals("")) continue;
                    builder.append(line+"\n");
                }
                Data data = new Data(trainFile.getName(),builder.toString(),label);
                if(isTrainFile[cnt++]) trainData.addData(data);
                else testData.addData(data);
            }
        }
        //trainData.writeToFile("train_data");
       // testData.writeToFile("test_data");
    }

    private DataSet readData(String folderName) throws IOException {
        File allFile = new File(folderName);
        File[] files = allFile.listFiles();
        DataSet dataSet = new DataSet();
        for(File trainDir: files){
            String label = trainDir.getName();
            File[] trainFiles = trainDir.listFiles();
            for(File trainFile: trainFiles) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(trainFile),fileEncoding));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if(line.equals("")) continue;
                    builder.append(line+"\n");
                }
                Data data = new Data(trainFile.getName(),builder.toString(),label);
                dataSet.addData(data);
            }
        }
        return dataSet;
    }

    public DocumentAnalyzer train() throws IOException, DataSet.NoAnalyzerException {
        DataSet trainData = readData("train_data");
        //FrequencyFeatureChooser frequencyFeatureChooser = FeatureChooseFactory.getFrequencyFeatureChooser(trainData);
        //frequencyFeatureChooser.setChooseFeatureNum(2000);
        IGFeatureChooser igFeatureChooser = FeatureChooseFactory.getIGFeatureChooser(trainData);
        igFeatureChooser.setChooseFeatureNum(3000);
        DocumentAnalyzer analyzer = new DocumentAnalyzer(trainData.getDataIterator(),igFeatureChooser);
        trainData.setAnalyzer(analyzer);
        trainData.toLibSVMFile("data.train");
        return analyzer;
    }

    public void test(DocumentAnalyzer analyzer) throws IOException, DataSet.NoAnalyzerException {
        DataSet testData = readData("test_data");
        testData.setAnalyzer(analyzer);
        testData.toLibSVMFile("data.test");
    }

    public static void main(String[] args) throws IOException, DataSet.NoAnalyzerException {
        Classifier classifier = new Classifier();
        DocumentAnalyzer analyzer = classifier.train();
        classifier.test(analyzer);
        SVMTest.main(null);
    }
}
