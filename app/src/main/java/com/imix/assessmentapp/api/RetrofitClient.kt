package com.imix.assessmentapp.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.imix.assessmentapp.activities.MyApp
import com.imix.assessmentapp.R
import com.imix.assessmentapp.api.RetrofitClient.Companion.BASE_URL

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class RetrofitClient() {

    val webServices: APIService
        get() = retrofit!!.create(APIService::class.java)

    companion object {
        const val BASE_URL = "https://blogswizards.com/mobile_app_assignment/api/"
        private var retrofit: Retrofit? = null
        private var mInstance: RetrofitClient? = null
        private lateinit var apiService: APIService

        @get:Synchronized
        val instance: RetrofitClient?
            get() {
                if (mInstance == null) {
                    mInstance = RetrofitClient()
                }
                return mInstance
            }

        //        public static final String BASE_URL_IMG = "http://yuunri.com/public/";
        //        private static final String BASE_URL = "http://nin9teens.com/yuunri/public/api/";
        //        public static final String BASE_URL_IMG = "http://nin9teens.com/yuunri/public/";

        fun getApiService(): APIService {
            if (!Companion::apiService.isInitialized) {
                fun getRetrofitInstance(): Retrofit? {
                    if (retrofit == null) {
                        retrofit = Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                    }
                    return retrofit
                }

                apiService = retrofit?.create(APIService::class.java)!!

            }

            return apiService
        }
    }

    init {
        val client: OkHttpClient = OkHttpClient.Builder()
//            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
//            .addInterceptor(NetworkConnectionInterceptor(context))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(MyApp.context.getString(R.string.BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }


    class NetworkConnectionInterceptor(val context: Context) : Interceptor {

        @Suppress("DEPRECATION")
        private val isConnected: Boolean
            get() {
                var result = false
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    cm?.run {
                        cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                            result = when {
                                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                                else -> false
                            }
                        }
                    }
                } else {
                    cm?.run {
                        cm.activeNetworkInfo?.run {
                            if (type == ConnectivityManager.TYPE_WIFI) {
                                result = true
                            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                                result = true
                            }
                        }
                    }
                }
                return result
            }

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            if (!isConnected) {
                // Throwing your custom exception
                // And handle it on onFailure
            }

            val builder = chain.request().newBuilder()
            return chain.proceed(builder.build())
        }
    }
}