package com.chargingwatts.chargingalarm.api


import androidx.lifecycle.LiveData
import com.chargingwatts.chargingalarm.vo.UserDetail
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * REST API access points
 */
interface AlarmApiService {
    /**
     * @GET declares an HTTP GET request
     * @Path("user") annotation on the userId parameter marks it as a
     * replacement for the {user} placeholder in the @GET path
     */
    @GET("/v1/user/profile")
    fun getUser(@Query("userId") userId: String): LiveData<ApiResponse<UserDetail>>
}
