package com.example.androidtesttask

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.androidtesttask.DataBase.MyAdapter
import com.example.androidtesttask.DataBase.MyDbManager
import com.example.androidtesttask.jsonObj.Information
import com.example.androidtesttask.databinding.ActivityMainBinding
import com.example.androidtesttask.jsonObj.Bank
import com.example.androidtesttask.jsonObj.Country
import com.example.androidtesttask.jsonObj.Number
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var bindingClass: ActivityMainBinding
    private lateinit var bin: String
    private val myDbManager = MyDbManager(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        bindingClass.editBIN.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER &&
                keyEvent.action == KeyEvent.ACTION_UP
            ) {
                this.currentFocus?.let { view ->
                    bin = bindingClass.editBIN.text.toString()
                    if (bin.length == 8) {
                        saveData(bin)
                        connectResource(bin)
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as?
                                InputMethodManager
                        imm?.hideSoftInputFromWindow(view.windowToken, 0)
                    } else {
                        bindingClass.editBIN.error = "Need enter 8 first number"
                    }
                }
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDB()
    }

    private fun saveData(bin: String) {
        myDbManager.insertToDb(bin)
    }


    private fun connectResource(bin: String) {
        val url = "https://lookup.binlist.net/$bin"
        val queue = Volley.newRequestQueue(baseContext)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result ->
                parseJSON(result)
            },
            { error ->
                Log.d("MyLog", "Error: $error")
            }
        )
        queue.add(request)
    }

    private fun parseJSON(result: String) {
        val mainObject = JSONObject(result)

        val numberItems = Number(
            mainObject.getJSONObject("number").optInt("length"),
            mainObject.getJSONObject("number").optBoolean("luhn")
        )
        val countryItems = Country(
            mainObject.getJSONObject("country").optString("numeric", "-"),
            mainObject.getJSONObject("country").optString("alpha2", "-"),
            mainObject.getJSONObject("country").optString("name", "-"),
            mainObject.getJSONObject("country").optString("emoji", "-"),
            mainObject.getJSONObject("country").optString("currency", "-"),
            mainObject.getJSONObject("country").optInt("latitude", 0),
            mainObject.getJSONObject("country").optInt("longitude", 0)
        )
        val bankItems = Bank(
            mainObject.getJSONObject("bank").optString("name", "-"),
            mainObject.getJSONObject("bank").optString("url", "-"),
            mainObject.getJSONObject("bank").optString("phone", "-"),
            mainObject.getJSONObject("bank").optString("city", "-")
        )
        val informationItems = Information(
            numberItems,
            mainObject.optString("scheme", "-"),
            mainObject.optString("type", "-"),
            mainObject.optString("brand", "-"),
            mainObject.optBoolean("prepaid"),
            countryItems,
            bankItems
        )
        parseData(informationItems)
    }

    @SuppressLint("SetTextI18n")
    fun parseData(information: Information) {
        bindingClass.textSchemeNetwork.text = information.scheme.uppercase()
        bindingClass.textType.text = information.type.uppercase()
        bindingClass.textBrand.text = information.brand

        if (information.prepaid) {
            bindingClass.textRepaid.text = "Yes"
        } else {
            bindingClass.textRepaid.text = "No"
        }

        if (information.number.length == 0) {
            bindingClass.textLength.text = "-"
        } else {
            bindingClass.textLength.text = information.number.length.toString()
        }

        if (information.number.luhn) {
            bindingClass.textLuhn.text = "Yes"
        } else {
            bindingClass.textLuhn.text = "No"
        }

        bindingClass.textCountry.text = "${information.country.emoji} ${information.country.name}"

        bindingClass.textCoordinates.text = "geo:${information.country.latitude}, " +
                "${information.country.longitude}"

        bindingClass.textBankName.text = "${information.bank.name}, ${information.bank.city}"
        bindingClass.textBankSite.text = information.bank.url
        bindingClass.textBankPhone.text = information.bank.phone
    }

    fun onClickGeo(view: View) {
        val geoURI = String.format(bindingClass.textCoordinates.text.toString())
        val uri = Uri.parse(geoURI)
        val geoMap = Intent(Intent.ACTION_VIEW, uri)
        startActivity(geoMap)
    }

    fun onClickHistory(view: View) {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }
}
