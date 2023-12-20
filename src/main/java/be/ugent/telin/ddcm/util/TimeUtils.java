package be.ugent.telin.ddcm.util;

import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static String getTimeString(long time) {
        long hours = TimeUnit.MILLISECONDS.toHours(time);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(hours);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);
        long millis = TimeUnit.MILLISECONDS.toMillis(time) - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(seconds);

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
    }
}
