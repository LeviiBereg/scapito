package kvi.spendit;

import java.util.Collections;
import java.util.List;

/**
 * Created by Admin on 31.01.2018.
 */

public class TimeStats {
    private List<Long> timeList;

    public TimeStats(List<Long> timeList) {
        this.timeList = timeList;
        Collections.sort(this.timeList);
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

}
