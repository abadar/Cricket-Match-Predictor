package ai.crawler;

import java.sql.Connection;

/**
 *
 * @author arsalan
 */
public class PlayerManager {

    String htmlCode = "", stringUrl = "";
    int playerCode;
    String playerName;
    PlayerStatsManager stats;
    Player player;

    void fetchPlayerInformation(Connection connection, Country country) {
        stringUrl = ("http://www.espncricinfo.com/" + country.getName().replaceAll(" ", "").toLowerCase() + "/content/player/country.html?country=" + country.getId());
        try {
            Crawler crawler = new Crawler(stringUrl);
            boolean chk = false, doneReading = false;
            while ((htmlCode = crawler.readNext()) != null && !doneReading) {
                if (htmlCode.contains("<div id=\"rectPlyr_Playerlistt20\" style=\"display: none; visibility: hidden;\">")) {
                    chk = true;
                }

                if (chk && htmlCode.contains("a href")) {
                    playerName = htmlCode.substring(htmlCode.lastIndexOf("html\">") + 6, htmlCode.lastIndexOf("</a"));
                    playerCode = Integer.parseInt(htmlCode.substring(htmlCode.lastIndexOf("player/") + 7, htmlCode.lastIndexOf(".html")));
                    System.out.println(playerName + " " + playerCode);
                    stats = new PlayerStatsManager();
                    player = new Player(playerCode, playerName);
                    stats.fetchPlayerStatistics(connection, country, player);
                }
                if (chk && htmlCode.contains("</table>")) {
                    chk = false;
                    doneReading = true;
                }
            }
            crawler.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
