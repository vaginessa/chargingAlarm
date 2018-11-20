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
}
