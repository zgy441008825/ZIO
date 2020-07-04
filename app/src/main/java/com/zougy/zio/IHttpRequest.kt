package com.zougy.zio

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface IHttpRequest {

    @GET("/photos")
    fun getImages(): Call<ResponseBody>

    object RequestHead {

        const val AccessKey = "78b286a93ced44392d83fb952463f5924463e1201cd15686a5be96ac76a31997"

        const val SecretKey = "c90e0fb9a703e42cdf2cae8c1f5d42ecba77d83cc580f62e5d3d120d01558370"

        const val httpRootUrl = "https://api.unsplash.com/"

    }
}