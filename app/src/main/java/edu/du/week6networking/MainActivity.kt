package edu.du.week6networking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    lateinit var service: BikeService
    lateinit var gson: Gson
    lateinit var requestText: TextView
    lateinit var responseText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://my-json-server.typicode.com/jdepree/du-Week6Networking/")
            .build()

        service = retrofit.create(BikeService::class.java)
        gson = GsonBuilder().setPrettyPrinting().create()

        requestText = findViewById(R.id.request_text)
        responseText = findViewById(R.id.response_text)

        findViewById<Button>(R.id.get_button).setOnClickListener {
            makeCall {
                if (TextUtils.isEmpty(requestText.text)) {
                    service.getBikes()
                } else {
                    service.getBike(requestText.text.toString())
                }
            }
        }

        findViewById<Button>(R.id.post_button).setOnClickListener {
            val jsonObject = JSONObject()
            jsonObject.put("id", "3")
            jsonObject.put("make", "Diamondback")
            jsonObject.put("model", "El Oso Nino 20")
            makeCall {
                service.createBike(jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull()))
            }
        }

        findViewById<Button>(R.id.put_button).setOnClickListener {
            val jsonObject = JSONObject()
            jsonObject.put("id", "1")
            jsonObject.put("model", "Mountain Trek 820")
            makeCall {
                service.updateBike(jsonObject.getString("id"), jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull()))
            }
        }

        findViewById<Button>(R.id.delete_button).setOnClickListener {
            makeCall {
                service.deleteBike(requestText.text.toString())
            }
        }
    }

    fun makeCall(action: suspend () -> Response<ResponseBody>) {
        CoroutineScope(Dispatchers.IO).launch {
            var response: Response<ResponseBody> = action()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    responseText.text = formatJson(response.body()?.string())
                } else {
                    responseText.text = response.code().toString()
                }
            }
        }
    }

    fun formatJson(text: String?): String {
        return gson.toJson(JsonParser.parseString(text))
    }
}