package com.example.tdpa_ex3p

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.tdpa_ex3p.databinding.ActivityDetalleBinding

class Detalle : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        binding = ActivityDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val imagen = bundle?.getString("img")
        val nombre = bundle?.getString("name")
        val materia = bundle?.getString("materia")
        val cal = bundle?.getString("calif1")
        val cal2 = bundle?.getString("calif2")
        val id = bundle?.getString("id")

        getData(imagen, nombre, cal, cal2, materia)

        binding.btnListado.setOnClickListener {
            val intento = Intent(this, Lista::class.java)
            startActivity(intento)
        }

        binding.btnEditar.setOnClickListener {
            val intento = Intent(this, Editar::class.java)
            intento.putExtra("id", id)
            startActivity(intento)
        }

        binding.btnEliminar.setOnClickListener {
            borrar(id.toString())
        }
    }

    fun getData(imagen: String?, nombre: String?, cal: String?, cal2: String?, materia: String?) {
        val url: Uri = Uri.parse(imagen)
        Glide.with(applicationContext).load(url).into(binding.imagenDetalle)
        binding.txtName.setText("$nombre")
        binding.calificacion1.setText("$cal")
        binding.calificacion2.setText("$cal2")
        binding.txtMateria.setText("$materia")
        logica(cal, cal2)
    }

    fun borrar(id:String){
        val intento = Intent(this, MainActivity::class.java)
        val admin = AdminSQLiteOpenHelper(this, "administracion", null, 1)
        val bd = admin.writableDatabase
        bd.delete("alumnos", "_id=$id", null)
        startActivity(intento)
    }

    fun logica(cal: String?, cal2: String?) {
        var tercer = cal?.toDouble()?.times(0.2)?.plus((cal2?.toDouble())?.times(0.2)!!)
        var palten = (((9.5 - tercer!!)/6)*10)
        val redondoTen:Double = Math.round(palten * 1000.0) / 1000.0
        val redondoTen2:Double = (Math.round(redondoTen * 10.0) / 10.0)
        var palsix = (((6 - tercer!!)/6)*10)
        val redondoSix:Double = Math.round(palsix * 1000.0) / 1000.0
        val redondoSix2:Double = (Math.round(redondoSix * 10.0) / 10.0)
        binding.diez.setText("${redondoTen2}")
        binding.seis.setText("${redondoSix2}")

        if(redondoTen2 <= 10){
            binding.txtmsj.setText("Aun se puede")

        }

    }



}