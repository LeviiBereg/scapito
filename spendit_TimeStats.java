package kvi.spendit;

import java.util.Collections;
import java.util.List;

/**
 * Created by Admin on 31.01.2018.
 */

public class TimeStats {
    private List<Long> timeList;
    private int leftOutlierBoundary;
    private int rightOutlierBoundary;

    public TimeStats(List<Long> timeList) {
        this.timeList = timeList;
        Collections.sort(this.timeList);
        outliersDetection();
    }

    public void outliersDetection() {
        leftOutlierBoundary = rightOutlierBoundary = 0;
        long q1 = quartile(25);
        long q3 = quartile(75);
        long iqr = Math.round(1.5 * (q3 - q1));
        int i = 0;
        while (timeList.get(i) < q1 - iqr) {
            leftOutlierBoundary++; i++;
        }
        i = timeList.size() - 1;
        while (timeList.get(i) > q3 + iqr) {
            rightOutlierBoundary++; i--;
        }
    }

    public long mean() {
        long res = 0;
        for(long i : timeList)
            res += i;
        return Math.round((double)res / timeList.size());
    }

    public long sampleVariance() {
        double res = 0;
        long mean = mean();
        for(long i : timeList)
            res += Math.pow(i - mean, 2);
        return Math.round((double)res / (timeList.size() - 1));
    }

    public long median() {
        int midListInd = timeList.size() / 2;
        long res = timeList.get(midListInd);
        if(timeList.size() % 2 == 0)
            res = Math.round((res + timeList.get(midListInd - 1)) / 2);
        return res;
    }

    public long quartile(double quartilePercent) {
        return timeList.get((int)Math.round(timeList.size() * quartilePercent / 100));
    }

}
