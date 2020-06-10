package Bash;

import org.apache.http.Header;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Response implements Serializable {
    //the headers of the response
    private Header[] responseHeaders = null;
    //can be the path of the file in which the output is
    private String pathOutputFile = "";
    private String output = "";
    private boolean outputContainer = false;
    private int statusCode;
    private Map<String, List<String>> responseHeader = null ;



    public Response(Header[] responseHeaders, String output, boolean outputContainer, String pathOutputFile, int statuscode) {
        this.output = output;
        this.outputContainer = outputContainer;
        this.pathOutputFile=pathOutputFile;
        this.responseHeaders = responseHeaders;
        this.statusCode=statuscode;
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

    public boolean isOutputContainer() {
        return outputContainer;
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

    public void setOutputContainer(boolean outputContainer) {
        this.outputContainer = outputContainer;
    }
}
