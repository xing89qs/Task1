package pretreatment.data;

/**
 * Created by xing89qs on 16/3/11.
 */
public class Data {
    private String content;
    private String label;
    private String fileName;
    public Data(String fileName,String content,String label){
        this.fileName = fileName;
        this.content = content;
        this.label = label;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
