package Utils.HttpSender;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpSender {

    private URL url;
    private String method;
    private String parameters=null;
    String error="";
    boolean wasError=false;
    private String contentType = null;

    HttpSender(){}

    HttpSender setMethod2GET(){
        this.method="GET";
        return this;
    }

    HttpSender setMethod2POST(){
        this.method="POST";
        return this;
    }
    HttpSender setUrl(String url){
        try {
            this.url=new URL(url);
        } catch (MalformedURLException e) {
            this.wasError=true;
            this.error="MalformedURL";
        }
        return this;
    }
    HttpSender setJsonParams(String p){
        this.parameters=p;
        this.contentType="application/json";
        return this;
    }

    String send(){
        String ret="";
        if(wasError)
            return "";
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(this.method);
            con.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            if(parameters!=null){
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type",this.contentType);
                OutputStream os = con.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(this.parameters);
                osw.flush();
                osw.close();
                os.close();
            }
            con.connect();

        } catch (IOException e) {
            wasError=true;
            error="cant open http connection";
            return "";
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            ret=content.toString();
        } catch (IOException e) {
            wasError=true;
            error="cant read response";
        }
        con.disconnect();
        return ret;
    }

}
