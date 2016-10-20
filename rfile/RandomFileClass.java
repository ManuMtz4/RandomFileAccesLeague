package rfile;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

import static rfile.TeamRandomFile.*;


/**
 * RandomFileClass v3.7
 * <p>
 * Copyright 2016 Manuel Martínez <ManuMtz@icloud.com> / <ManuMtz@hotmail.co.uk>
 * <p>
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */

public class RandomFileClass {

    static File fileLeague = new File("league.bin");
    static final int EACH_MATCH = 130;
    static int teams;

    static long leagueSize;

    static boolean fileExist() {
        return fileLeague.exists();
    }

    static void resetLeague() {
        fileLeague.delete();
        writeNTeams();
        newRandomFile();
    }

    /**
     * Creates a league
     *
     * @throws IOException
     * @throws ParseException
     */
    static void newRandomFile() {

        try (RandomAccessFile raf = new RandomAccessFile(fileLeague, "rw");
             FileReader cLang = new FileReader(DEFAULT_DIR + SP + current + D_FILE_EXT_LANG)) {

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(cLang);
            JSONObject jsonObject = (JSONObject) obj;

            raf.seek(0);
            raf.setLength(leagueSize);

            System.out.print(jsonObject.get("sponsor") + " - (30 chars): ");
            String sponsorName = scan.nextLine();

            while (sponsorName.length() > 30 || sponsorName.isEmpty()) {
                System.out.print(jsonObject.get("sponsor") + " - (30 chars): ");
                sponsorName = scan.nextLine();
            }
            raf.seek(0);

            raf.writeUTF(sponsorName); // 4x30 -120 inside of 130 bytes

        } catch (IOException io) {
            io.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    /**
     * Reads the league sponsor
     *
     * @return sponsor
     * @throws IOException
     * @throws ParseException
     */
    static String readLeague() {

        try (RandomAccessFile raf = new RandomAccessFile(fileLeague, "r")) {
            raf.seek(0);

            String league = raf.readUTF();

            return league;
        } catch (IOException io) {
            io.printStackTrace();
        }
        return null;
    }

    /**
     * Writes a match
     *
     * @param teaml
     * @param teamv
     * @param pteam1
     * @param pteam2
     * @param rName
     * @param date
     * @throws IOException
     * @throws ParseException
     */
    static void writeRandom(int teaml, int teamv, short pteam1, short pteam2, String rName, String date) {

        try (RandomAccessFile raf = new RandomAccessFile(fileLeague, "rw")) {

            long eachLocal = leagueSize / teams; // each local space

            long pos = ((teaml - 1) * eachLocal + teamv * EACH_MATCH) - EACH_MATCH;

            raf.seek(pos);

            raf.writeShort(pteam1); // 2
            raf.writeShort(pteam2); // 2
            raf.writeUTF(rName); // 4x30
            raf.writeUTF(date); // 6 - only numbers

            // 130 bytes total

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Reads a match
     *
     * @param teaml
     * @param teamv
     * @throws IOException
     * @throws ParseException
     */
    static void readRandom(int teaml, int teamv) {

        try (FileReader cLang = new FileReader(DEFAULT_DIR + SP + current + D_FILE_EXT_LANG);
             RandomAccessFile raf = new RandomAccessFile(fileLeague, "r")) {

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(cLang);
            JSONObject jsonObject = (JSONObject) obj;


            long eachLocal = leagueSize / teams; // each local space

            long pos = ((teaml - 1) * eachLocal + teamv * EACH_MATCH) - EACH_MATCH;

            raf.seek(pos);

            short p1 = raf.readShort();
            short p2 = raf.readShort();
            String r = raf.readUTF();
            String d = raf.readUTF();

            System.out.println();
            if (p1 >= 0 && p2 >= 0 && !d.isEmpty() && !r.isEmpty()) {
                System.out.println(teaml + " " + jsonObject.get("pteam") + ": " + p1);
                System.out.println(teamv + " " + jsonObject.get("pteam") + ": " + p2);
                System.out.println(jsonObject.get("rname") + ": " + r);
                System.out.println(jsonObject.get("date") + ": " + d);
            } else {
                System.out.println(jsonObject.get("noplayed"));
            }
            System.out.println();

        } catch (IOException io) {
            io.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    /**
     * Reads all matches
     *
     * @param teaml
     * @param teamv
     * @throws IOException
     * @throws ParseException
     */
    static void readAllRandom(int teaml, int teamv) {

        try (FileReader cLang = new FileReader(DEFAULT_DIR + SP + current + D_FILE_EXT_LANG);
             RandomAccessFile raf = new RandomAccessFile(fileLeague, "r")) {

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(cLang);
            JSONObject jsonObject = (JSONObject) obj;

            long eachLocal = leagueSize / teams; // each local space

            long pos = ((teaml - 1) * eachLocal + teamv * EACH_MATCH) - EACH_MATCH;

            raf.seek(pos);

            short p1 = raf.readShort();
            short p2 = raf.readShort();
            String r = raf.readUTF();
            String d = raf.readUTF();

            if (p1 >= 0 && p2 >= 0 && !d.isEmpty() && !r.isEmpty()) {
                System.out.print("[");
                System.out.print(teaml + " " + jsonObject.get("pteam") + ": " + p1 + " | ");
                System.out.print(teamv + " " + jsonObject.get("pteam") + ": " + p2 + " | ");
                System.out.print(jsonObject.get("rname") + ": " + r + " | ");
                System.out.print(jsonObject.get("date") + ": " + d);
                System.out.println("]");
            }

        } catch (IOException io) {
            io.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    static void writeNTeams() {
        try (OutputStream output = new FileOutputStream(defaultFilecfg);
             FileReader cLang = new FileReader(DEFAULT_DIR + SP + current + D_FILE_EXT_LANG)) {

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(cLang);
            JSONObject jsonObject = (JSONObject) obj;

            System.out.print(jsonObject.get("nteams") + " (>1): ");

            int nteams = parseIntN(scan.nextLine());

            while (nteams < 2) {
                System.out.print(jsonObject.get("nteams") + " (>1): ");
                nteams = parseIntN(scan.nextLine());
            }

            // set the properties value
            prop.setProperty("nteams", String.valueOf(nteams));

            teams = nteams;

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    /**
     * Shows teams quantity
     *
     * @throws IOException
     * @throws ParseException
     */
    static void viewNTeams() {
        try (InputStream input = new FileInputStream(defaultFilecfg);
             FileReader cLang = new FileReader(DEFAULT_DIR + SP + current + D_FILE_EXT_LANG)) {

            // load a properties
            prop.load(input);

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(cLang);
            JSONObject jsonObject = (JSONObject) obj;

            System.out.println(jsonObject.get("shownteams") + ": " + prop.getProperty("nteams") + " " + jsonObject.get("teams"));

        } catch (IOException io) {
            io.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }
}

