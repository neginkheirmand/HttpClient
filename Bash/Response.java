package Bash;

import org.apache.http.Header;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Response implements Serializable {
    //the headers of the response
    private Header[] responseHeaders = null;
    private Map<String, List<String>> responseHeader = null ;
    private String[][] standardFormHeaders = null;
    //can be the path of the file in which the output is
    private String pathOutputFile = "";
    private String output = "";
    private boolean outputContainer = false;
    private int statusCode;
    private Long contentSize = 0L;
    private Long timeTaken = 0L;

    public Response(Header[] responseHeaders, String output, boolean outputContainer, String pathOutputFile, int statuscode) {
        this.output = output;
        this.outputContainer = outputContainer;
        this.pathOutputFile=pathOutputFile;
        this.responseHeaders = responseHeaders;
        this.statusCode=statuscode;
    }

    public Response(){

    }

    public Map<String, List<String>> getResponseHeader() {
        return responseHeader;
    }

    public String getPathOutputFile() {
        return pathOutputFile;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Header[] getResponseHeaders() {
        return responseHeaders;
    }

    public String getOutput() {
        return output;
    }

    public Long getContentSize() {
        return contentSize;
    }

    public Long getTimeTaken() {
        return timeTaken;
    }

    public boolean isOutputContainer() {
        return outputContainer;
    }

    public String[][] getStandardFormHeaders() {
        if(standardFormHeaders==null){
            String[][] str ={{"",""}};
            standardFormHeaders = str;
        }
        return standardFormHeaders;
    }

    public void setStandardFormHeaders(String[][] standardFormHeaders) {
        this.responseHeaders=null;
        this.responseHeader=null;
        this.standardFormHeaders = standardFormHeaders;
    }

    public void setResponseHeader(Map<String, List<String>> responseHeader) {
        this.responseHeader = responseHeader;
    }

    public void setPathOutputFile(String pathOutputFile) {
        this.pathOutputFile = pathOutputFile;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setResponseHeaders(Header[] responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public void setContentSize(Long contentSize) {
        this.contentSize = contentSize;
    }

    public void setTimeTaken(Long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public void setOutputContainer(boolean outputContainer) {
        this.outputContainer = outputContainer;
    }
}
