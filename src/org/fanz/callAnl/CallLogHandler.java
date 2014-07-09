package org.fanz.callAnl;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Fanz
 * Date: 13-6-29
 * Time: 下午10:11
 * To change this template use File | Settings | File Templates.
 */
public class CallLogHandler {
    //    public List<Map.Entry<String, Integer>> getCallLog(ContentResolver cr) {
    public List<Map> getCallLog(ContentResolver cr) {
        String name;
        String number;
        List<Map> callLogList = new ArrayList<Map>();
        int callLogListSize = callLogList.size();
        Map<String, Object> singleLog = new HashMap<String, Object>();
        int type;
        Date date;
        String time;
        Long duration;
        String durStr;
        String[] skipNumber = {};
        String nullName = "陌生人";
//        ContentResolver cr = getContentResolver();
        final Cursor cursor = cr.query(android.provider.CallLog.Calls.CONTENT_URI,
                new String[]{
                        android.provider.CallLog.Calls.NUMBER,
                        android.provider.CallLog.Calls.CACHED_NAME,
                        android.provider.CallLog.Calls.TYPE,
                        android.provider.CallLog.Calls.DATE,
                        android.provider.CallLog.Calls.DURATION
                }, null, null, android.provider.CallLog.Calls.DEFAULT_SORT_ORDER);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            number = cursor.getString(0).replace("-", "").replace("+86", "");
            boolean isOccur = false;
            duration = cursor.getLong(4);
            if (number == null) {
                continue;
            }

            //skip special number
            for (int x = 0; x < skipNumber.length; x++) {
                if (number.equals(skipNumber[x])) {
                    isOccur = true;
                    break;
                }
            }

            callLogListSize = callLogList.size();
            for (int j = 0; j < callLogListSize && !isOccur; j++) {
                if (callLogList.get(j).get("number").equals(number)) {
                    callLogList.get(j).put("faq", (Integer.parseInt(callLogList.get(j).get("faq").toString()) + 1));
                    Long durationOld = (Long) callLogList.get(j).get("duration");
                    callLogList.get(j).put("duration", durationOld + duration);
                    durStr = (durationOld + duration) / 3600 > 0
                            ? String.valueOf((durationOld + duration) / 3600)
                            + "小时" + String.valueOf((durationOld + duration) % 3600 / 60)
                            + "分钟" + String.valueOf((durationOld + duration) % 3600 % 60)
                            + "秒" : String.valueOf((durationOld + duration) / 60) + "分钟"
                            + String.valueOf((durationOld + duration) % 60) + "秒";
                    callLogList.get(j).put("durStr", durStr);
                    isOccur = true;
                    break;
                }

            }
            if (isOccur) {
                continue;
            }

            name = cursor.getString(1);
            type = cursor.getInt(2);
            SimpleDateFormat sfd = new SimpleDateFormat("yyyy年M月d日 HH:mm:ss");
            date = new Date(Long.parseLong(cursor.getString(3)));
            time = sfd.format(date);

            durStr = (duration / 3600 > 0 ? String.valueOf(duration / 3600) + "小时"
                    + String.valueOf(duration % 3600 / 60) + "分钟"
                    + String.valueOf(duration % 3600 % 60) + "秒"
                    : String.valueOf(duration / 60) + "分钟" + String.valueOf(duration % 60) + "秒");

            singleLog = new HashMap<String, Object>();
            singleLog.put("type", type);
            singleLog.put("time", time);
            singleLog.put("faq", 1);
            singleLog.put("name", name == null ? nullName : name);
            singleLog.put("number", number);
            singleLog.put("duration", duration);
            singleLog.put("durStr", durStr);
            callLogList.add(singleLog);

        }

        Collections.sort(callLogList, new CLComoararator());
        for (int i = 0; i < callLogListSize; i++) {
            Log.i("CallLog", "CallLogList " + i + ": " + callLogList.get(i).get("number") + "   " + callLogList.get(i).get("faq"));
        }

        return callLogList;
    }


}
