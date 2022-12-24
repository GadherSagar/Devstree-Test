package com.test.devstree_test.network

import com.test.devstree_test.BuildConfig
import com.test.devstree_test.network.Constant.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private lateinit var retrofit: Retrofit

    fun getInstance(): Retrofit {
        if (!::retrofit.isInitialized) {
            val builder = OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS)
            builder.addInterceptor {
                val requestBuilder = it.request().newBuilder()
                requestBuilder.addHeader("Accept", "application/json")
                it.proceed(requestBuilder.build())
            }

            if (BuildConfig.DEBUG) {
                val interceptor =
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                builder.addInterceptor(interceptor)
            }
            retrofit =Retrofit.Builder().baseUrl(BASE_URL).client(builder.build())
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
        return retrofit
    }
}