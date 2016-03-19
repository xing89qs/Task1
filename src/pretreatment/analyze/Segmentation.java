package pretreatment.analyze;

/**
 * Created by xing89qs on 16/3/11.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

class TokenAnalyzer {

    private static HashSet<String> stopWords = new HashSet<>();

    static {
        try {
            File file = new File("stop.dic");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String stopWord;
            while ((stopWord = reader.readLine()) != null) {
                stopWords.add(stopWord);
            }
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static  boolean isNumber(String number){
        try {
            double val = Double.valueOf(number);
            return true;
        }catch(Exception e){
            return false;
        }
    }


    private static ArrayList<String> getTokenListByIK(String content) {
        ArrayList<String> tokenList = new ArrayList<>();
        Configuration config = DefaultConfig.getInstance();
        config.setUseSmart(false);
        IKSegmenter ik = new IKSegmenter(new StringReader(content), config);
        Lexeme lex = null; // 分词
        try {
            while ((lex = ik.next()) != null) {
                String token = lex.getLexemeText();
                if (stopWords.contains(token))
                    continue;
                if(isNumber(token)) continue;
                tokenList.add(token);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tokenList;
    }

    private static ArrayList<String> getTokenListByAnsj(String content) {
        ArrayList<String> tokenList = new ArrayList<>();

        List<Term> tokens = ToAnalysis.parse(content);
        for (Term term : tokens) {
            if (stopWords.contains(term.getName()))
                continue;
            tokenList.add(term.getName());
        }
        return tokenList;
    }

    public static ArrayList<String> getTokenList(String content) {
        return getTokenListByAnsj(content);
    }
}


public class Segmentation {
    public static ArrayList<String> splitWord(String str){
        ArrayList<String> tokenList = TokenAnalyzer.getTokenList(str);
        return tokenList;
    }

    public static void main(String[] args){

    }
}
