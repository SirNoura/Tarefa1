package slim.project.tarefa1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset
import com.jjoe64.graphview.series.LineGraphSeries

import android.view.View

import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint






class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://app.idicare.com/api/boxes/readings/heart-rate/?offset=0&limit=25"
        val requestBody = "imei=000000001" + "&patient=" + 26
        val stringReq : StringRequest =
            object : StringRequest(Method.POST, url,
                Response.Listener { response ->
                    // response
                    val strResp = response.toString()
                    val json_contact:JSONObject = JSONObject(strResp)
                    Log.d("teste",json_contact.toString())

                    val paciente:JSONObject = json_contact.getJSONObject("patient")

                    var user:Pacient = Pacient(paciente.get("id").toString().toInt(), paciente.get("name").toString())

                    var jsonArray:JSONArray = json_contact.getJSONArray("data")
                    var data:ArrayList<Data> = arrayListOf<Data>()
                    Log.d("teste2", jsonArray.length().toString())
                    val data2:ArrayList<String> = arrayListOf<String>()

                    val graph = findViewById<View>(R.id.graph) as GraphView
                    val series = LineGraphSeries(
                        arrayOf<DataPoint>()
                    )
                    graph.addSeries(series)

                    for(i in 0 until (jsonArray.length() - 1)){
                        var newData:Data = Data(jsonArray.getJSONObject(i).get("value").toString().toInt())
                        data.add(newData)
                        Log.d("teste2", data.get(i).value.toString())

                        series.appendData(DataPoint(i.toDouble(),data.get(i).value.toDouble()),false,jsonArray.length())

                        data2.add(data.get(i).value.toString())

                    }
                    val listview = findViewById<ListView>(R.id.listview)
                    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data2)
                    listview.adapter = adapter
                    var objResponse = UserResponse(data, user)

                    val name = findViewById<TextView>(R.id.name)
                    name.text = objResponse.user!!.name

                    Log.d("teste3", objResponse.user!!.name)
                      //Log.d("API", paciente.toString())
                },
                Response.ErrorListener { error ->
                    Log.d("API", "error => $error")
                }
            ){
                override fun getBody(): ByteArray {
                    return requestBody.toByteArray(Charset.defaultCharset())
                }
            }
        queue.add(stringReq)



    }
}