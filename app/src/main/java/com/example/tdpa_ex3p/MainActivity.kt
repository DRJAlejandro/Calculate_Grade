package com.example.tdpa_ex3p

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.tdpa_ex3p.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import APIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log
import kotlin.math.log2

class MainActivity : AppCompatActivity() {
    private var imagenes = mutableListOf<String>()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getImage()


        binding.btnCalcular.setOnClickListener {
            calcular()
        }

        binding.btnList.setOnClickListener {
            val intento = Intent(this, Lista::class.java)
            startActivity(intento)
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getImage() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val call = getRetrofit().create(APIService::class.java).getImagenes()
                val data = call.body()
                runOnUiThread {
                    if (call.isSuccessful) {
                        try {
                            val imagenUrl = data?.datos?.first()?.picture?.imagenUrl
                            imagenes.clear()
                            imagenes.add(imagenUrl.toString())
                            val url: Uri = Uri.parse(imagenes.first())
                            Glide.with(applicationContext).load(url).into(binding.userImage)
                        } catch (e: Exception) {
                            print(e)
                        }
                    } else {
                        showError()
                    }
                }
            }
        } catch (e: Exception) {
            print(e)
        }

    }



    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    fun validar(): Boolean {
        if (TextUtils.isEmpty(binding.name.text)) {
            binding.name.error = "Nombre vacio"
            return false
        }
        if (TextUtils.isEmpty(binding.calif.text)) {
            binding.calif.error = "Calificacion de Primer Parcial Vacia"
            return false
        }
        if (TextUtils.isEmpty(binding.calif2.text)) {
            binding.calif2.error = "Calificacion de Segundo Parcial Vacia"
            return false
        }
        if (TextUtils.isEmpty(binding.nameMateria.text)) {
            binding.nameMateria.error = "Materia Vacia"
            return false
        }
        if (binding.calif.text.toString().toFloat() > 10 ) {
            binding.calif.error = "Calificacion del Primer Parcial invalida"
            return false
        }
        if (binding.calif2.text.toString().toFloat() > 10) {
            binding.calif2.error = "Cailificacion del Segundo Parcial invalida"
            return false
        }
        return true
    }

    fun calcular() {
        val intento = Intent(this, Detalle::class.java)
        val admin = AdminSQLiteOpenHelper(this, "administracion", null, version = 1)
        val bd = admin.writableDatabase
        val registro = ContentValues()
        if (validar()) {
            try {
                registro.put("nombre", binding.name.text.toString())
                registro.put("materia", binding.nameMateria.text.toString())
                registro.put("califUno", binding.calif.text.toString())
                registro.put("califDos", binding.calif2.text.toString())
                registro.put("imagen", imagenes.first().toString())
                bd.insert("alumnos", null, registro)
                val cursor = bd.rawQuery("SELECT * FROM alumnos", null)
                cursor.moveToLast()
                bd.close()
                intento.putExtra("name", binding.name.text.toString())
                intento.putExtra("materia", binding.nameMateria.text.toString())
                intento.putExtra("calif1", binding.calif.text.toString())
                intento.putExtra("calif2", binding.calif2.text.toString())
                intento.putExtra("img", imagenes.first().toString())
                intento.putExtra("id", cursor!!.getString(0))

                startActivity(intento)

            } catch (e: Exception) {
                Toast.makeText(this, "ERROR MR PIZZA: $e", Toast.LENGTH_SHORT).show()
            }
        }
    }


}