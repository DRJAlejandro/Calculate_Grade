package com.example.tdpa_ex3p

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.tdpa_ex3p.databinding.ActivityEditarBinding
import com.example.tdpa_ex3p.databinding.ActivityListaBinding

class Editar : AppCompatActivity() {
    private lateinit var binding: ActivityEditarBinding
    var imagen = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar)

        binding = ActivityEditarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val id = bundle?.getString("id")

        fillData(id.toString())

        binding.btnGuardar.setOnClickListener {
            editar(id.toString())
        }
    }

    fun validar(): Boolean {
        if (TextUtils.isEmpty(binding.txtNameEdit.text)) {
            binding.txtNameEdit.error = "Nombre Vacio"
            return false
        }
        if (TextUtils.isEmpty(binding.txtCalEdit1.text)) {
            binding.txtCalEdit1.error = "Calificacion del Primer Parcial Invalida"
            return false
        }
        if (TextUtils.isEmpty(binding.txtCalEdit2.text)) {
            binding.txtCalEdit2.error = "Calificacion del segundo Parcial Invalida"
            return false
        }
        if (TextUtils.isEmpty(binding.txtMateriaEdit.text)) {
            binding.txtMateriaEdit.error = "Materia Vacia"
            return false
        }
        if (binding.txtCalEdit1.text.toString().toFloat() > 10) {
            binding.txtCalEdit1.error = "Calificacon del Primer Parcial Invalida"
            return false
        }
        if (binding.txtCalEdit2.text.toString().toFloat() > 10) {
            binding.txtCalEdit2.error = "Calificacion del Segundo Parcial Invalida"
            return false
        }
        return true
    }

    fun fillData(id:String) {
        val admin = AdminSQLiteOpenHelper(this, "administracion", null, 1)
        val bd = admin.writableDatabase
        val cursor = bd.rawQuery("SELECT * FROM alumnos WHERE _id=$id", null)
        if(cursor.moveToFirst()){
            imagen = "${cursor.getString(5)}"
            val url: Uri = Uri.parse(cursor.getString(5))
            Glide.with(applicationContext).load(url).into(binding.imagenEdit)
            binding.txtNameEdit.setText("${cursor.getString(1)}")
            binding.txtMateriaEdit.setText("${cursor.getString(2)}")
            binding.txtCalEdit1.setText("${cursor.getString(3)}")
            binding.txtCalEdit2.setText("${cursor.getString(4)}")
            bd.close()
        }

    }

    fun editar(id:String){
        val intento = Intent(this, Detalle::class.java)
        val admin = AdminSQLiteOpenHelper(this, "administracion", null, version = 1)
        val bd = admin.writableDatabase
        val registro = ContentValues()
        if (validar()) {
            try {
                registro.put("nombre", binding.txtNameEdit.text.toString())
                registro.put("materia", binding.txtMateriaEdit.text.toString())
                registro.put("califUno", binding.txtCalEdit1.text.toString())
                registro.put("califDos", binding.txtCalEdit2.text.toString())
                registro.put("imagen", imagen)
                bd.update("alumnos", registro, "_id=$id", null)
                bd.close()
                intento.putExtra("name", binding.txtNameEdit.text.toString())
                intento.putExtra("materia", binding.txtMateriaEdit.text.toString())
                intento.putExtra("calif1", binding.txtCalEdit1.text.toString())
                intento.putExtra("calif2", binding.txtCalEdit2.text.toString())
                intento.putExtra("img", imagen)

                startActivity(intento)
            } catch (e: Exception) {
                Toast.makeText(this, "ERROR MRPIZZA: $e", Toast.LENGTH_SHORT).show()
            }
        }
    }
}