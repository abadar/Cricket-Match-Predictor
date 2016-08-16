package ai.crawler;

import ai.database.DBHandler;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;

/**
 *
 * @author arsalan
 */
public class CrawlerRunner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        BufferedReader buffer;
        Country country;
        Connection dbConnection;
        InputStreamReader inpStream;
        String htmlCode, stringUrl;

        dbConnection = DBHandler.getDBConnectionInstance();

        stringUrl = "http://www.espncricinfo.com/ci/content/site/cricket_squads_teams/index.html";
        try {
            URL url = new URL(stringUrl);
            URLConnection urlConnection = (URLConnection) url.openConnection();

            inpStream = new InputStreamReader(urlConnection.getInputStream());
            buffer = new BufferedReader(inpStream);
            boolean chk = false, doneReading = false;
            while ((htmlCode = buffer.readLine()) != null && !doneReading) {
                if (htmlCode.contains("<table class=\"teamList\">")) {
                    chk = true;
                }
                if (chk && htmlCode.contains("a href")) {
                    country = getCountry(htmlCode);
                    System.out.println("========>" + country.getName() + " " + country.getId());
                    DBHandler.runInsertQuery("INSERT INTO `country`(`idCountry`, `name`) VALUES (" + country.getId() + ",'" + country.getName() + "')");
                    PlayerManager player = new PlayerManager();
                    player.fetchPlayerInformation(dbConnection, country);
                }
                if (chk && htmlCode.contains("</table>")) {
                    chk = false;
                    doneReading = true;
                }

            }
            inpStream.close();
            buffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Country getCountry(String html) {
        int id;
        String countryName;
        Country country;

        countryName = html.split("html\">")[2].replaceAll("</a></h3>", "");
        html = html.split(" ")[1].replaceAll("\"><img", "");
        html = html.replaceAll("href=\"", "");
        id = Integer.parseInt(html.substring(html.lastIndexOf("/") + 1, html.lastIndexOf(".html")));
        country = new Country(id, countryName);

        return country;
    }

}
