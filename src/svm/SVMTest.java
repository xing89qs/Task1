package svm;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Created by xing89qs on 16/3/11.
 */
public class SVMTest {
    public static void main(String[] args) throws IOException {
        PrintStream ps = System.out;
        System.setOut(new PrintStream(new File("data_scale.train")));
        String comman1 = "-l 0 -u 1 data.train";
        svm_scale.main(comman1.split(" "));

        System.setOut(new PrintStream(new File("data_scale.test")));
        comman1 = "-l 0 -u 1 data.test";
        svm_scale.main(comman1.split(" "));

        System.setOut(ps);

        String command = "-t 0 -c 0.8 data_scale.train data.train.model";
        String[] trainArgs = command.split(" ");
        svm_train.main(trainArgs);
        String[] testArgs = {"data_scale.test", "data.train.model", "result"};//directory of test file, model file, result file
        svm_predict.main(testArgs);
    }
}
