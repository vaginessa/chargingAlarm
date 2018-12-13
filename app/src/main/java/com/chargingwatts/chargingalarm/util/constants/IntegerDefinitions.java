package com.chargingwatts.chargingalarm.util.constants;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

public class IntegerDefinitions {

    //--------------------------------ALARM TYPES-------------------------------------------------//

    @IntDef({ALARM_TYPE.BATTERY_HIGH_LEVEL_ALARM, ALARM_TYPE.BATTERY_LOW_LEVEL_ALARM, ALARM_TYPE.BATTERY_HIGH_TEMPERATURE_ALARM, ALARM_TYPE.NONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ALARM_TYPE {
        static final int BATTERY_HIGH_LEVEL_ALARM = 1;
        static final int BATTERY_LOW_LEVEL_ALARM = 2;
        static final int BATTERY_HIGH_TEMPERATURE_ALARM = 3;
        static final int NONE = 4;
    }

    //--------------------------------STOP ALARM EVENT TYPES-------------------------------------------------//

    @IntDef({STOP_ALARM_EVENT_TYPE.STOP_BATTERY_HIGH_LEVEL_ALARM, STOP_ALARM_EVENT_TYPE.STOP_BATTERY_LOW_LEVEL_ALARM, STOP_ALARM_EVENT_TYPE.STOP_BATTERY_HIGH_TEMPERATURE_ALARM, STOP_ALARM_EVENT_TYPE.NONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface STOP_ALARM_EVENT_TYPE {
        static final int STOP_BATTERY_HIGH_LEVEL_ALARM = 1;
        static final int STOP_BATTERY_LOW_LEVEL_ALARM = 2;
        static final int STOP_BATTERY_HIGH_TEMPERATURE_ALARM = 3;
        static final int NONE = 4;
    }
}
