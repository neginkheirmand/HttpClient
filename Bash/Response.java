package Bash;

import org.apache.http.Header;

import java.io.Serializable;

public class Response implements Serializable {
    //the headers of the response
    private Header[] responseHeaders = null;
    //can be the path of the file in which the output is
    private String pathOutputFile = "";
    private String output = "";
    private boolean outputContainer = false;
    private int statusCode;



    public Response(Header[] responseHeaders, String output, boolean outputContainer, String pathOutputFile, int status code) {
        this.output = output;
        this.outputContainer = outputContainer;
        this.responseHeaders = responseHeaders;
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
