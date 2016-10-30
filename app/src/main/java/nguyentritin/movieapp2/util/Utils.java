package nguyentritin.movieapp2.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by giacatho on 10/30/16.
 */

public class Utils {
    public static String getShortOverviewStr(String overview) {
        if (overview != null && overview.length() > 200) {
            String shortStr = overview.substring(0, 200);

            return shortStr.substring(0, shortStr.lastIndexOf(" ")) + "...";
        }

        return overview;
    }

    public static String getDateFromUnixTS(long ts) {
        Date date = new Date(ts*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8")); // give a timezone reference for formating (see comment at the bottom

        return sdf.format(date);
    }
}
