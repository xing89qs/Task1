package pretreatment.data;

import pretreatment.analyze.DocumentAnalyzer;

import java.io.*;
import java.util.*;

/**
 * Created by xing89qs on 16/3/11.
 */

public class DataSet {

    private static final String DEFAULT_ENCODING = "UTF-8";

    public int getLabelNum() {
        return this.labelMap.size();
    }

    public class NoAnalyzerException extends Exception {
        public  NoAnalyzerException(String msg){
            super(msg);
        }
    }
    private HashMap<String,Integer> labelMap = new HashMap<>();
    private ArrayList<Data> dataList;
    private DocumentAnalyzer analyzer;
    private int label_count = 0;

    public DataSet(){
        dataList = new ArrayList<>();
    }

    public void addData(Data data){
        dataList.add(data);
        if(!labelMap.containsKey(data.getLabel())) {
            labelMap.put(data.getLabel(), label_count++);
        }
    }

    public int getIndexByLabel(String label){
        return labelMap.get(label);
    }

    public Iterator<Data> getDataIterator(){
        return this.dataList.iterator();
    }

    public void toLibSVMFile(String fileName) throws NoAnalyzerException, IOException {
        if(this.analyzer==null) throw new NoAnalyzerException("未指定Analyzer!");
        File file = new File(fileName);
        try {
            File directory = new File(file.getParentFile().getAbsolutePath());
            directory.mkdirs();
            if (!file.exists()) file.createNewFile();
        }catch(Exception e){
            e.printStackTrace();
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),DEFAULT_ENCODING));
        for(Data data: this.dataList){
            Vector vector = analyzer.documentToVector(data);
            StringBuilder dataRow = new StringBuilder();
            dataRow.append(labelMap.get(data.getLabel())+" ");
            Map.Entry<Integer,Double>[] indices = new Map.Entry[vector.getVector().size()];
            int tot = 0;
            for(Map.Entry<String,Double> entry: vector.getVector().entrySet()){
                String token = entry.getKey();
                indices[tot++] = new AbstractMap.SimpleEntry<Integer, Double>(analyzer.getIndex(token),entry.getValue());
            }
            Arrays.sort(indices, new Comparator<Map.Entry<Integer,Double> >() {
                @Override
                public int compare(Map.Entry<Integer,Double> a,Map.Entry<Integer,Double> b){
                    return a.getKey().compareTo(b.getKey());
                }
            });
            for(int i = 0;i<tot;i++){
                dataRow.append(" "+indices[i].getKey()+":"+indices[i].getValue()+" ");
            }
            writer.write(dataRow.toString());
            writer.newLine();
        }
        writer.close();
    }

    public void setAnalyzer(DocumentAnalyzer analyzer) {
        this.analyzer = analyzer;
    }
}
