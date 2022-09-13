package com.example.weatherapp

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    val CITY: String = "são paulo,br"
    val API: String = "6e4ca54b59b3892d5409cc23e5a900ee"
    val LANG: String = "pt_br"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        weatherTask().execute()
    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        //Requisicao para API
        override fun doInBackground(vararg p0: String?): String? {

            var response:String? = try {
                URL("https://api.openweathermap.org/data/2.5/forecast?q=$CITY&units=metric&appid=$API&lang=$LANG").readText(Charsets.UTF_8)

            } catch (e: Exception) {
                null
            }
            return response
        }

        //Faz a leitura do dados e modifica os textos da HomeScreen
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onPostExecute(result: String?) {
            try {
                //Extraindo dados do JSON da API
                val jsonObj = JSONObject(result)
                val today = jsonObj.getJSONArray("list").getJSONObject(0)
                val main = today.getJSONObject("main")
                val wind = today.getJSONObject("wind")
                val weather = today.getJSONArray("weather").getJSONObject(0)
                val city = jsonObj.getJSONObject("city")
                val updateAt = today.getString("dt_txt")

                //DetailsContainer
                val windSpeed = wind.getString("speed")
                val humidity = main.getString("humidity")
                val pressure = main.getString("pressure")

                //OverviewContainer
                val status = weather.getString("description")
                //val status = "Thunderstorm"
                val temp = main.getString("temp").substring(0,2)
                val temp_min = main.getString("temp_min").substring(0,2)
                val temp_max = main.getString("temp_max").substring(0,2)
                val address = city.getString("name")+", "+ city.getString("country")

                //ForecastContainer
                val day1 = jsonObj.getJSONArray("list").getJSONObject(1)
                val main1 = day1.getJSONObject("main")
                val weather1 = day1.getJSONArray("weather").getJSONObject(0)
                val status1 = weather1.getString("main")
                val temp1 = main1.getString("temp").substring(0,2)

                val day2 = jsonObj.getJSONArray("list").getJSONObject(2)
                val main2 = day2.getJSONObject("main")
                val weather2 = day2.getJSONArray("weather").getJSONObject(0)
                val status2 = weather2.getString("main")
                val temp2 = main2.getString("temp").substring(0,2)

                val day3 = jsonObj.getJSONArray("list").getJSONObject(3)
                val main3 = day3.getJSONObject("main")
                val weather3 = day3.getJSONArray("weather").getJSONObject(0)
                val status3 = weather3.getString("main")
                val temp3 = main3.getString("temp").substring(0,2)

                val day4 = jsonObj.getJSONArray("list").getJSONObject(4)
                val main4 = day4.getJSONObject("main")
                val weather4 = day4.getJSONArray("weather").getJSONObject(0)
                val status4 = weather4.getString("main")
                val temp4 = main4.getString("temp").substring(0,2)



                //Exibindo os dados em tela
                //findViewById<TextView>(R.id.wind).text = windSpeed + " km/h"
                findViewById<TextView>(R.id.humidity).text = humidity + "%"
                findViewById<TextView>(R.id.pressure).text = pressure + " mb"

                findViewById<TextView>(R.id.humidity).text = humidity + "%"
                findViewById<TextView>(R.id.pressure).text = pressure + " mb"


                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.temp_min).text = temp_min + "°"
                findViewById<TextView>(R.id.temp_max).text = temp_max + "°"
                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.status).text = status.capitalize()
                findViewById<TextView>(R.id.updated_at).text = updateAt





                // -------------------------------------------------- //
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE


            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }

        }
    }
}
