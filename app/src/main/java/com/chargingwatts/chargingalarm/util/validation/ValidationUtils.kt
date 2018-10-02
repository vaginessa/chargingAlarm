package com.chargingwatts.chargingalarm.util.validation


object ValidationUtils {

    fun validateMobileNo(mobileNo: String): Boolean {
        val mobileNoRegex = Regex("[6789][0-9]{9}$")
        if (mobileNoRegex.matches(mobileNo))
            return true
        return false

    }
}