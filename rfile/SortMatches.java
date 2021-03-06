package rfile;

import org.joda.time.DateTime;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

import static rfile.JodaDT.parseYYMMDD;
import static rfile.RandomFileClass.*;
import static rfile.TeamRandomFile.*;

/**
 * SortMatches v2
 * <p>
 * Copyright 2016 Manuel Martínez <ManuMtz@icloud.com> / <ManuMtz@hotmail.co.uk>
 * <p>
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */

public class SortMatches {

    static List<Object[]> sortedMatches = new ArrayList<>();

    /**
     * Adds matches to ArrayList  Object[]
     *
     * @param teaml
     * @param teamv
     * @throws IOException
     * @throws ParseException
     */
    static void readAddArray(int teaml, int teamv) {

        try (RandomAccessFile raf = new RandomAccessFile(fileLeague, "r")) {

            long eachLocal = leagueSize / teams; // each local space

            long pos = ((teaml - 1) * eachLocal + teamv * EACH_MATCH) - EACH_MATCH;

            raf.seek(pos);

            short p1 = raf.readShort();
            short p2 = raf.readShort();
            String r = raf.readUTF();
            String d = raf.readUTF();

            Object[] array = new Object[6];

            if (p1 >= 0 && p2 >= 0 && !d.isEmpty() && !r.isEmpty()) {
                array[0] = p1;
                array[1] = p2;
                array[2] = r;
                array[3] = d;
                array[4] = teaml;
                array[5] = teamv;
                sortedMatches.add(array);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Checks teams before  readAddArray()
     *
     * @throws IOException
     * @throws ParseException
     */
    public static void allMatches() {
        if (!sortedMatches.isEmpty()) {
            sortedMatches.clear();
        }
        int teaml = 1;
        while (teaml <= teams) {
            int teamv = 1;
            while (teamv <= teams) {
                if (teaml != teamv) {
                    readAddArray(teaml, teamv);
                }
                teamv++;
            }
            teaml++;
        }
    }

    /**
     * Gets all matches sorted by date
     *
     * @throws IOException
     * @throws ParseException
     */
    public static void allMatchesByDate() {
        allMatches();
        Collections.sort(sortedMatches, new DateComparator());

        System.out.println();

        System.out.println(jsonObject.get("welcomeleague") + ": " + readLeague());

        for (Object[] sortedMatch : sortedMatches) {

            for (int i = 0; i < 1; i++) {
                System.out.print("[");
                System.out.print(sortedMatch[4] + " " + jsonObject.get("pteam") + ": " + sortedMatch[0] + " | ");
                System.out.print(sortedMatch[5] + " " + jsonObject.get("pteam") + ": " + sortedMatch[1] + " | ");
                System.out.print(jsonObject.get("rname") + ": " + sortedMatch[2] + " | ");
                System.out.print(jsonObject.get("date") + ": " + sortedMatch[3]);
                System.out.println("]");
            }
        }
        System.out.println();
    }

    /**
     * Gets all matches sorted by total points
     *
     * @throws IOException
     * @throws ParseException
     */
    public static void allMatchesByTotalPoints() {
        allMatches();
        Collections.sort(sortedMatches, new PointsComparator());

        System.out.println();

        System.out.println(jsonObject.get("welcomeleague") + ": " + readLeague());

        for (Object[] sortedMatch : sortedMatches) {

            for (int i = 0; i < 1; i++) {
                System.out.print("[");
                System.out.print(sortedMatch[4] + " " + jsonObject.get("pteam") + ": " + sortedMatch[0] + " | ");
                System.out.print(sortedMatch[5] + " " + jsonObject.get("pteam") + ": " + sortedMatch[1] + " | ");
                System.out.print(jsonObject.get("rname") + ": " + sortedMatch[2] + " | ");
                System.out.print(jsonObject.get("date") + ": " + sortedMatch[3]);
                System.out.println("]");
            }
        }
        System.out.println();
    }
}

/**
 * Date Comparator Class
 */
class DateComparator implements Comparator<Object[]> {

    @Override
    public int compare(Object[] x, Object[] y) {
        String ax = String.valueOf(x[3]);
        String bx = String.valueOf(y[3]);
        DateTime a = parseYYMMDD(ax);
        DateTime b = parseYYMMDD(bx);
        return a.toLocalDate().compareTo(b.toLocalDate());
    }
}

/**
 * Points Comparator Class
 */
class PointsComparator implements Comparator<Object[]> {

    @Override
    public int compare(Object[] x, Object[] y) {
        int ax = Integer.parseInt(String.valueOf(x[0])) + Integer.parseInt(String.valueOf(x[1]));
        int bx = Integer.parseInt(String.valueOf(y[0])) + Integer.parseInt(String.valueOf(y[1]));
        return bx - ax;
    }
}