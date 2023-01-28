package com.paparazziteam.sslpinningimpl

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.datatheorem.android.trustkit.TrustKit
import com.datatheorem.android.trustkit.pinning.OkHttp3Helper
import com.datatheorem.android.trustkit.reporting.BackgroundReporter
import com.datatheorem.android.trustkit.reporting.PinningFailureReport
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button = findViewById<View>(R.id.button)
        button.setOnClickListener {
            sslPinningClick()
        }
    }

    fun setupSSL():ApiOpenWeather {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val certificatePinner = CertificatePinner.Builder()
            .add("openweathermap.org", "sha256/axmGTWYycVN5oCjh3GJrxWVndLSZjypDO6evrHMwbXg=")
            .build()

        val client = OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://openweathermap.org/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiOpenWeather::class.java)
    }

    private fun callGuideWeatherMapRemote(weatherApi: ApiOpenWeather) {
        //Call the API
        val call = weatherApi.getData()
        call.enqueue(object : retrofit2.Callback<String> {
            override fun onResponse(call: retrofit2.Call<String>, response: retrofit2.Response<String>) {
                //Do something with the response
                val data = response.body()
                println("data: $data")
                Toast.makeText(this@MainActivity, data, Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                //Handle failure
                println("error: ${t.message}")
                Toast.makeText(this@MainActivity, "Network Error!", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun sslPinningClick() {
        println("click sslPinning")
        val weatherApi = setupSSL()
        callGuideWeatherMapRemote(weatherApi)
    }
}


