package ai.crawler;

import ai.database.DBHandler;
import java.sql.Connection;

/**
 *
 * @author arsalan
 */
public class PlayerStatsManager {

    String htmlCode = "", stringUrl = "";

    void fetchPlayerStatistics(Connection con, Country country, Player player) {
        stringUrl = ("http://www.espncricinfo.com/" + country.getName().replaceAll(" ", "").toLowerCase() + "/content/player/" + player.getPlayerCode() + ".html");
        System.out.println(stringUrl);
        try {
            Crawler crawler = new Crawler(stringUrl);
            boolean isBattingStats = false, isBowlingStats = false, doneReading = false;
            while ((htmlCode = crawler.readNext()) != null && !doneReading) {
                if (htmlCode.contains("<span class=\"ciPhotoWidgetLink\">Batting and fielding averages</span>")) {
                    isBattingStats = true;
                    isBowlingStats = false;
                } else if (htmlCode.contains("<span class=\"ciPhotoWidgetLink\">Bowling averages</span>")) {
                    isBattingStats = false;
                    isBowlingStats = true;
                }

                if (htmlCode.contains("<b>T20Is</b>")) {
                    if (isBattingStats) {
                        htmlCode = crawler.readNext();
                        htmlCode = htmlCode.substring(htmlCode.indexOf("\">") + 2, htmlCode.lastIndexOf("</td>"));
                        DBHandler.runInsertQuery("INSERT INTO `player`(`idPlayer`,`Country_idCountry`, `Name`,`Matches`) VALUES (" + player.getPlayerCode() + "," + country.getId() + ",'" + player.getPlayerName() + "','" + htmlCode + "')");
                        String str = "";
                        for (int i = 0; i < 13; i++) {
                            htmlCode = crawler.readNext();
                            str = str + "'" + htmlCode.substring(htmlCode.indexOf("\">") + 2, htmlCode.lastIndexOf("</td>")) + "',";
                            System.out.print(htmlCode.substring(htmlCode.indexOf("\">") + 2, htmlCode.lastIndexOf("</td>")) + " ==> ");
                        }
                        str = str.substring(0, str.length() - 1);
                        System.out.println(str);
                        DBHandler.runInsertQuery("INSERT INTO `Bat_stat` (`idBat`, `idPlayer`, `inns`, `NOs`, `Runs`, `HS`, `Ave`, `BF`, `SR`, `Hund`, `Fift`, `Fours`, `Sixes`, `Ct`, `St`) VALUES (null," + player.getPlayerCode() + "," + str + ")");
                        System.out.println("");
                        isBattingStats = false;
                    }

                    if (isBowlingStats) {
                        crawler.readNext();
                        String str = "";
                        for (int i = 0; i < 11; i++) {
                            htmlCode = crawler.readNext();
                            str = str + "'" + htmlCode.substring(htmlCode.indexOf("\">") + 2, htmlCode.lastIndexOf("</td>")) + "',";
                            System.out.print(htmlCode.substring(htmlCode.indexOf("\">") + 2, htmlCode.lastIndexOf("</td>")) + " ==> ");
                        }
                        str = str.substring(0, str.length() - 1);
                        DBHandler.runInsertQuery("INSERT INTO `Bowl_stat` (`idBowl`, `idPlayer`, `inns`, `Balls`, `Runs`, `Wkts`, `BBI`, `BBM`, `Ave`, `Econ`, `SR`, `FourW`, `FiveW`) VALUES (null," + player.getPlayerCode() + "," + str + ")");
                        System.out.println("");
                        isBowlingStats = false;
                        doneReading = true;
                    }
                }
            }
            crawler.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
