package rfile;

import java.io.*;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static rfile.JodaDT.parseYYMMDD;
import static rfile.RandomFileClass.*;
import static rfile.SortMatches.*;

/**
 * TeamRandomFile v4
 * <p>
 * Copyright 2016 Manuel Martínez <ManuMtz@icloud.com> / <ManuMtz@hotmail.co.uk>
 * <p>
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */

public class TeamRandomFile {

    static final String DEFAULT_DIR_TEAM = "data";
    static final String D_FILE_NAME = "default";
    static final String D_FILE_EXT = ".cfg";
    static final String DEFAULT_DIR = "lang";
    static final String DEFAULT_FILE_NAME_LANG = "en_uk";
    static final String D_FILE_EXT_LANG = ".json";
    static final String DEFAULT_FILE = D_FILE_NAME + D_FILE_EXT;
    static final String DEFAULT_LANG = DEFAULT_FILE_NAME_LANG + D_FILE_EXT_LANG;

    static final String SP = File.separator;

    static Properties prop = new Properties();

    static File defaultFilecfg = new File(DEFAULT_DIR_TEAM + SP + DEFAULT_FILE);

    static File defaultFileLang = new File(DEFAULT_DIR + SP + DEFAULT_LANG);

    static String current;

    static HashSet<String> langExist = new HashSet<>();

    static Scanner scan = new Scanner(System.in);

    private static final int MAX_POINTS = 200;

    private static final int REFEREE_LENGTH = 30;

    static JSONObject jsonObject;

    /**
     * Makes a default cfg file
     *
     * @throws IOException
     */
    public static void DefaultCfgFile() {

        File defaultCfgDir = new File(DEFAULT_DIR_TEAM);

        if (!defaultCfgDir.exists()) {
            defaultCfgDir.mkdirs();
        }

        File defaultLangDir = new File(DEFAULT_DIR);

        if (!defaultLangDir.exists()) {
            defaultLangDir.mkdirs();
        }

        if (!defaultFilecfg.exists()) {

            try (OutputStream output = new FileOutputStream(defaultFilecfg)) {

                // set the properties value
                prop.setProperty("lang", "en_uk");

                // save properties to project root folder
                prop.store(output, null);

            } catch (IOException io) {
                io.printStackTrace();
            }
        }
        jsonLang();
    }

    /**
     * Makes a default json english lang
     *
     * @throws IOException
     */
    public static void jsonLang() {
        if (!defaultFileLang.exists()) {
            JSONObject defaultMenu = new JSONObject();

            defaultMenu.put("changelang", "Change language");
            defaultMenu.put("onelang", "Only there is one language");
            defaultMenu.put("newleague", "New League");
            defaultMenu.put("sponsor", "League Sponsor");
            defaultMenu.put("viewmatch", "View a match");
            defaultMenu.put("viewallmatches", "View all matches");
            defaultMenu.put("viewsorteddate", "View all matches sorted by date");
            defaultMenu.put("viewsortedpoints", "View all matches sorted total points");
            defaultMenu.put("viewnteams", "View how many teams I have");
            defaultMenu.put("shownteams", "I have");
            defaultMenu.put("teams", "teams");
            defaultMenu.put("welcomeleague", "Welcome to the league");
            defaultMenu.put("noplayed", "No played yet");
            defaultMenu.put("queuedmatch", "Next match queued");
            defaultMenu.put("reset", "Reset league");
            defaultMenu.put("slang", "Select language");
            defaultMenu.put("clang", "Current language");
            defaultMenu.put("select", "Select");
            defaultMenu.put("selectlteam", "Select Local Team");
            defaultMenu.put("selectvteam", "Select Visitor Team");
            defaultMenu.put("cantbeequals", "Both teams can't be the same, choose another one");
            defaultMenu.put("pteam", "Team points");
            defaultMenu.put("nteams", "How many teams?");
            defaultMenu.put("rname", "Referee name");
            defaultMenu.put("date", "Date");
            defaultMenu.put("menu", "See again main menu");
            defaultMenu.put("exit", "Exit");
            defaultMenu.put("bye", "Bye!");

            try (FileWriter fileJSON = new FileWriter(defaultFileLang)) {

                fileJSON.write(defaultMenu.toString());
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

    /**
     * Finds all .json langs
     */
    public static void langFiles() {
        File langFiles = new File(DEFAULT_DIR);
        File[] rutaDir = langFiles.listFiles();

        String fileName = D_FILE_EXT_LANG;

        ArrayList<File> langF = new ArrayList<>(Arrays.asList(rutaDir));

        for (File file : langF) {
            if (file.isFile() && file.getName().contains(fileName)) {
                langExist.add(file.getName());
            }
        }
    }

    /**
     * Runs cfg file
     *
     * @throws IOException
     */
    public static void loadCfg() {
        DefaultCfgFile();

        try (InputStream input = new FileInputStream(defaultFilecfg)) {

            // load a properties file
            prop.load(input);

            // get the property value
            current = prop.getProperty("lang");

        } catch (IOException io) {
            io.printStackTrace();
        }
        resetlang();
        loadJson();
    }

    /**
     * Default configuration for number of teams
     *
     * @throws IOException
     * @throws ParseException
     */
    static void DefaultTeams() {

        try (InputStream input = new FileInputStream(defaultFilecfg)) {

            // load a properties
            prop.load(input);

            try {
                // get the property value
                teams = Integer.parseInt(prop.getProperty("nteams"));
            } catch (NumberFormatException nfx) {
                writeNTeams();
            }

            leagueSize = teams * teams * EACH_MATCH;
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Resets to default language
     */
    public static void resetlang() {
        //If current language doesn't exist, current language will be set to default

        File cLang = new File(DEFAULT_DIR + SP + current + D_FILE_EXT_LANG);

        if (!cLang.exists()) {
            try (OutputStream output = new FileOutputStream(defaultFilecfg)) {

                // set the properties value
                prop.setProperty("lang", DEFAULT_FILE_NAME_LANG);
                current = DEFAULT_FILE_NAME_LANG;

                // save properties to project root folder
                prop.store(output, null);

            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

    /**
     * Sets another language
     *
     * @throws IOException
     * @throws ParseException
     */
    public static void changeLang() {
        langFiles();

        System.out.println(jsonObject.get("clang") + ": " + current);

        if (langExist.size() > 1) {
            int i = 0;
            System.out.print("[");
            for (String c : langExist) {
                System.out.print(c.substring(0, c.length() - 5));
                if (i < langExist.size() - 1) {
                    System.out.print(", ");
                }
                i++;
            }
            System.out.print("]");
            System.out.println();
        } else if (langExist.size() < 2) {
            System.out.println(jsonObject.get("onelang"));
            return;
        }
        System.out.print(jsonObject.get("slang") + ": ");
        String lang = scan.nextLine().toLowerCase();

        if (langExist.contains(lang + D_FILE_EXT_LANG)) {

            try (OutputStream output = new FileOutputStream(defaultFilecfg)) {

                // set the properties value
                prop.setProperty("lang", lang);

                // save properties to project root folder
                prop.store(output, null);

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        loadCfg();
    }

    /**
     * App Menu
     *
     * @throws IOException
     * @throws ParseException
     */
    public static void menu() {
        System.out.println("\n****************************************");
        System.out.println("[1] " + jsonObject.get("changelang"));
        if (fileExist()) {
            System.out.println("[2] " + jsonObject.get("queuedmatch"));
            System.out.println("[3] " + jsonObject.get("viewmatch"));
            System.out.println("[4] " + jsonObject.get("viewallmatches"));
            System.out.println("[5] " + jsonObject.get("viewsorteddate"));
            System.out.println("[6] " + jsonObject.get("viewsortedpoints"));
            System.out.println("[7] " + jsonObject.get("reset"));
        } else {
            System.out.println("[2] " + jsonObject.get("newleague"));
        }
        System.out.println("[t] " + jsonObject.get("viewnteams"));
        System.out.println("[q] " + jsonObject.get("exit"));
        System.out.println("****************************************\n");
        System.out.print(jsonObject.get("select") + ": ");
    }

    public static void showMenu() {
        System.out.println("[m] " + jsonObject.get("menu"));
        System.out.print(jsonObject.get("select") + ": ");

    }

    public static void showBye() {

        System.out.println("\n" + jsonObject.get("bye"));
        scan.close();

    }

    /**
     * Adds a new match
     *
     * @throws IOException
     * @throws ParseException
     */
    public static void qMatch() {

        String teaml_tmp;
        int teaml = 0;
        while (teaml > teams || teaml < 1) {
            System.out.print(jsonObject.get("selectlteam") + ": ");
            teaml_tmp = scan.nextLine();
            teaml = parseIntN(teaml_tmp);
            if (teaml_tmp.equals("q")) {
                return;
            }
        }

        String teamv_tmp;
        int teamv = 0;
        while (teamv > teams || teamv < 1 || teamv == teaml) {
            System.out.print(jsonObject.get("selectvteam") + ": ");
            teamv_tmp = scan.nextLine();
            teamv = parseIntN(teamv_tmp);
            if (teamv == teaml) {
                System.out.println(jsonObject.get("cantbeequals"));
            }
            if (teamv_tmp.equals("q")) {
                return;
            }
        }

        String pteam1_tmp;
        short pTeam1 = -1;
        while (!(pTeam1 <= MAX_POINTS) || !(pTeam1 >= 0)) {
            System.out.print(jsonObject.get("pteam") + " " + teaml + " (" + MAX_POINTS + "): ");
            pteam1_tmp = scan.nextLine();
            pTeam1 = parseShortN(pteam1_tmp);
            if (pteam1_tmp.equals("q")) {
                return;
            }
        }

        String pteam2_tmp;
        short pTeam2 = -1;
        while (!(pTeam2 <= MAX_POINTS) || !(pTeam2 >= 0)) {
            System.out.print(jsonObject.get("pteam") + " " + teamv + " (" + MAX_POINTS + "): ");
            pteam2_tmp = scan.nextLine();
            pTeam2 = parseShortN(pteam2_tmp);
            if (pteam2_tmp.equals("q")) {
                return;
            }
        }

        String refereeName = "";
        while (refereeName.length() > REFEREE_LENGTH || refereeName.isEmpty() || refereeName.length() < 2) {
            System.out.print(jsonObject.get("rname") + " - (" + REFEREE_LENGTH + " chars): ");
            refereeName = scan.nextLine();
            if (refereeName.equals("q")) {
                return;
            }
        }

        String date = "";
        while (String.valueOf(date).length() != 6 || !parseDate(String.valueOf(date))) {
            System.out.print(jsonObject.get("date") + " - (YYMMDD): ");
            date = scan.nextLine();
            if (date.equals("q")) {
                return;
            }
        }

        writeRandom(teaml, teamv, pTeam1, pTeam2, refereeName, date);
    }

    /**
     * Shows a match
     *
     * @throws IOException
     * @throws ParseException
     */
    public static void viewMatch() {

        String teaml_tmp;
        int teaml = 0;
        while (teaml > teams || teaml < 1) {
            System.out.print(jsonObject.get("selectlteam") + ": ");
            teaml_tmp = scan.nextLine();
            teaml = parseIntN(teaml_tmp);
            if (teaml_tmp.equals("q")) {
                return;
            }
        }

        String teamv_tmp;
        int teamv = 0;
        while (teamv > teams || teamv < 1 || teamv == teaml) {
            System.out.print(jsonObject.get("selectvteam") + ": ");
            teamv_tmp = scan.nextLine();
            teamv = parseIntN(teamv_tmp);
            if (teamv == teaml) {
                System.out.println(jsonObject.get("cantbeequals"));
            }
            if (teamv_tmp.equals("q")) {
                return;
            }
        }

        readRandom(teaml, teamv);
    }

    /**
     * Shows al non empty matches
     *
     * @throws IOException
     * @throws ParseException
     */
    public static void viewAllMatches() {

        System.out.println();

        System.out.println(jsonObject.get("welcomeleague") + ": " + readLeague());

        int teaml = 1;
        while (teaml <= teams) {
            int teamv = 1;
            while (teamv <= teams) {
                if (teaml != teamv) {
                    readAllRandom(teaml, teamv);
                }
                teamv++;
            }
            teaml++;
        }
        System.out.println();

    }

    /**
     * Is numeric?
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Int parser
     * @param str
     * @return
     */
    public static int parseIntN(String str) {
        if (isNumeric(str)) {
            return Integer.parseInt(str);
        } else {
            return -1;
        }
    }

    /**
     * Short parser
     * @param str
     * @return
     */
    public static short parseShortN(String str) {
        if (isNumeric(str)) {
            return Short.parseShort(str);
        } else {
            return -1;
        }
    }

    /**
     * Date parser checker
     * @param str
     * @return
     */
    public static boolean parseDate(String str) {
        try {
            parseYYMMDD(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Loads JSON language
     */
    static void loadJson() {
        try (FileReader cLang = new FileReader(DEFAULT_DIR + SP + current + D_FILE_EXT_LANG)) {

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(cLang);
            jsonObject = (JSONObject) obj;

        } catch (IOException io) {
            io.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        loadCfg();
        DefaultTeams();
        menu();
        String menuItem;
        do {
            menuItem = scan.nextLine();
            switch (menuItem) {
                case "1":
                    changeLang();
                    showMenu();
                    break;
                case "2":
                    if (fileExist()) {
                        qMatch();
                    } else {
                        newRandomFile();
                    }
                    showMenu();
                    break;
                case "3":
                    if (fileExist()) {
                        viewMatch();
                    }
                    showMenu();
                    break;
                case "4":
                    if (fileExist()) {
                        viewAllMatches();
                    }
                    showMenu();
                    break;
                case "5":
                    if (fileExist()) {
                        allMatchesByDate();
                    }
                    showMenu();
                    break;
                case "6":
                    if (fileExist()) {
                        allMatchesByTotalPoints();
                    }
                    showMenu();
                    break;
                case "7":
                    if (fileExist()) {
                        resetLeague();
                    }
                    showMenu();
                    break;
                case "t":
                    viewNTeams();
                    showMenu();
                    break;
                case "m":
                    menu();
                    break;
                case "q":
                    showBye();
                    break;
            }
        } while (!menuItem.equals("q"));
    }
}

