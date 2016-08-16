package ai.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author arsalan
 */
public class Crawler {

    private final URL url;
    private final URLConnection urlConnection;
    private final InputStreamReader inpStream;
    private final BufferedReader buffer;

    public Crawler(String stringUrl) throws Exception {
        url = new URL(stringUrl);
        urlConnection = (URLConnection) url.openConnection();
        inpStream = new InputStreamReader(urlConnection.getInputStream());
        buffer = new BufferedReader(inpStream);
    }

    /**
     * Function to read next line from DOM
     * @return
     * @throws IOException 
     */
    String readNext() throws IOException {
        return buffer.readLine();
    }

    /**
     * Close connection
     * @throws IOException 
     */
    void close() throws IOException {
        inpStream.close();
        buffer.close();
    }

}
