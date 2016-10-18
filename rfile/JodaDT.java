package rfile;

/**
 * JodaDT v1
 * <p>
 * Copyright 2016 Manuel Martínez <ManuMtz@icloud.com> / <ManuMtz@hotmail.co.uk>
 * <p>
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

public class JodaDT {

    /**
     * Constructor. Private to forbid instances.
     */
    private JodaDT() {
    }

    /**
     * Creates a DateTime object from a String with this format YYMMDD
     *
     * @param date with format YYMMDD
     * @return a DateTime object or null
     */
    public static DateTime parseYYMMDD(String data) {
        if (data != null) {
            DateTimeFormatterBuilder dtfb = new DateTimeFormatterBuilder();
            dtfb.appendYear(2, 2);
            dtfb.appendDayOfMonth(2);
            dtfb.appendMonthOfYear(2);

            DateTimeFormatter dtf = dtfb.toFormatter();
            DateTime dt = dtf.parseDateTime(data);
            return dt;
        } else {
            return null;
        }
    }
}

