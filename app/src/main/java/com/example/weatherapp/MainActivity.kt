package com.example.weatherapp

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val CITY: String = "seropédica,br"
    val API: String = "6e4ca54b59b3892d5409cc23e5a900ee"
    val LANG: String = "pt_br"
    var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        weatherTask().execute()
    }

    fun basicAlert(message: String){

        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle("Deu ruim Maltar")
            setMessage(message)
            show()
        }
    }
    //Classe para o imput de dados da API
    inner class weatherTask() : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        //Requisicao para API
        @RequiresApi(Build.VERSION_CODES.O)
        override fun doInBackground(vararg p0: String?): String? {


            var response:String? = try {
                URL("https://api.openweathermap.org/data/2.5/forecast?q=$CITY&units=metric&appid=$API&lang=$LANG").readText(Charsets.UTF_8)

            } catch (e: Exception) {
                null
            }

            var addHour = findViewById<Button>(R.id.addHour)

            addHour.setOnClickListener {
                val jsonObj = JSONObject(response)
                val sizeSeila = jsonObj.getJSONArray("list").length()

                if (index < sizeSeila - 1 ) {
                index += 1
                println(index)

                onPostExecute(response)
            } else {
                    basicAlert("Não há mais previsão para os proximos horários")

                }            }
            var subHour = findViewById<Button>(R.id.subHour)

            subHour.setOnClickListener {
                if (index > 0) {
                index -= 1
                println(index)

                onPostExecute(response)
            }else {
                    basicAlert("Horário mínimo")

                }  }

            return response

        }

        //Faz a leitura do dados e modifica as infos da tela do APP
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onPostExecute(result: String?) {



            try {
                val jsonObj = JSONObject(result)
                val today = jsonObj.getJSONArray("list").getJSONObject(index)
                val main = today.getJSONObject("main")
                val weather = today.getJSONArray("weather").getJSONObject(0)
                val city = jsonObj.getJSONObject("city")
                val updateAt = today.getString("dt_txt")

                val humidity = main.getString("humidity")
                val pressure = main.getString("pressure")

                val status = weather.getString("description")
                val temp = main.getString("temp").substring(0,2)
                val temp_min = main.getString("temp_min").substring(0,2)
                val temp_max = main.getString("temp_max").substring(0,2)
                val address = city.getString("name")+", "+ city.getString("country")


                //Exibindo os dados na tela
                findViewById<TextView>(R.id.humidity).text = humidity + "%"
                findViewById<TextView>(R.id.pressure).text = pressure + " mb"

                findViewById<TextView>(R.id.humidity).text = humidity + "%"
                findViewById<TextView>(R.id.pressure).text = pressure + " mb"


                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.temp_min).text = "Mín: " + temp_min + "°"
                findViewById<TextView>(R.id.temp_max).text = "Max: " + temp_max + "°"
                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.status).text = status.capitalize()





                val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val date = isoFormat.parse(updateAt.replace(" ", "T"))
                isoFormat.timeZone = TimeZone.getTimeZone("GMT-3")
                isoFormat.applyPattern(" 'Dia' dd, 'às' HH'h'.");
                val formattedDate = isoFormat.format(date)

                findViewById<TextView>(R.id.updated_at).text = formattedDate


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
