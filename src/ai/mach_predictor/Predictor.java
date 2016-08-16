package ai.mach_predictor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arsalan
 */
public class Predictor {

    int[][] transitionTable = new int[13][2];

    public Predictor() {
        constructTransitionTable();
    }

    /**
     * Function to create transition table
     */
    private void constructTransitionTable() {
        try {
            Scanner scan = new Scanner(new FileReader(new File("transition_table.properties")));
            int i = 0;
            while (scan.hasNextInt()) {
                transitionTable[i][0] = scan.nextInt();
                transitionTable[i][1] = scan.nextInt();
                i++;
            }
            scan.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Predictor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Predict the match winner using transition table
     *
     * @param batAvg
     * @param bsr
     * @param bowlAvg
     * @param eco
     * @param bowlSR
     * @param exp
     * @return
     */
    public int predictMatch(boolean batAvg, boolean bsr, boolean bowlAvg, boolean eco, boolean bowlSR, boolean exp) {
        int currentState = 0, winner = 0;
        List<Integer> list = new ArrayList<>();
        list.add(batAvg ? 1 : 0);
        list.add(bsr ? 1 : 0);
        list.add(bsr ? 1 : 0);
        list.add(bowlAvg ? 1 : 0);
        list.add(bowlAvg ? 1 : 0);
        list.add(eco ? 1 : 0);
        list.add(eco ? 1 : 0);
        list.add(bowlSR ? 1 : 0);
        list.add(bowlSR ? 1 : 0);
        list.add(exp ? 1 : 0);
        list.add(exp ? 1 : 0);

        while (currentState < 11) {
            currentState = transitionTable[currentState][list.get(currentState)];
        }

        if (currentState == 11) {
            winner = 1;
        } else {
            winner = 2;
        }

        return winner;
    }
}
