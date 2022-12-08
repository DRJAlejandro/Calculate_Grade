package com.example.tdpa_ex3p

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQuery
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.inputmethod.InputMethodManager
import android.widget.CursorAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tdpa_ex3p.databinding.ActivityDetalleBinding
import com.example.tdpa_ex3p.databinding.ActivityListaBinding
import com.example.tdpa_ex3p.databinding.ListItemsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Lista : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityListaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        binding = ActivityListaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buscar.setOnQueryTextListener(this)
        init()
        binding.regresarBtn.setOnClickListener {
            val intento = Intent(this, MainActivity::class.java)
            startActivity(intento)
        }
    }

    private fun hideKeyboard(){
        val imn: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imn.hideSoftInputFromWindow(binding.viewRoot.windowToken,0)
    }

    inner class CursorAdapterListView(context: Context, cursor:Cursor):
        CursorAdapter(context, cursor, FLAG_REGISTER_CONTENT_OBSERVER){
        override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
            val inflater = LayoutInflater.from(context)
            return inflater.inflate(R.layout.list_items, parent, false)
        }

        override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
            val bindingItems = ListItemsBinding.bind(view!!)
            bindingItems.alumnName.text = cursor!!.getString(1)
            val url: Uri = Uri.parse(cursor.getString(5))
            Glide.with(applicationContext).load(url).into(bindingItems.profilePic)
            bindingItems.txtMateriaList.text = cursor.getString(2)
            bindingItems.caluno.text = cursor.getString(3)
            bindingItems.caldos.text = cursor.getString(4)
            bindingItems.txtimg.text = cursor.getString(5)
            bindingItems.id.text = cursor.getString(0)

            view.setOnClickListener{
                val intento = Intent(this@Lista, Detalle::class.java)
                intento.putExtra("name", bindingItems.alumnName.text)
                intento.putExtra("materia", bindingItems.txtMateriaList.text)
                intento.putExtra("calif1", bindingItems.caluno.text)
                intento.putExtra("calif2", bindingItems.caldos.text)
                intento.putExtra("img", bindingItems.txtimg.text)
                intento.putExtra("id", bindingItems.id.text)
                startActivity(intento)
            }
        }

    }

    private fun init(){
        val bdAlumnos = AdminSQLiteOpenHelper(this, "administracion", null, version = 1)
        val bd = bdAlumnos.readableDatabase
        val cursor = bd.rawQuery("SELECT * FROM alumnos", null)
            val adaptador = CursorAdapterListView(this, cursor)
            binding.listado.adapter = adaptador
    }

    private fun searchByName(query: String) {
        try {
            val admin = AdminSQLiteOpenHelper(this, "administracion", null, 1)
            val bd = admin.writableDatabase
            val cursor = bd.rawQuery("SELECT * FROM alumnos WHERE nombre='"+query+"'", null)
            if(cursor.moveToFirst()){
                val adaptador = CursorAdapterListView(this, cursor)
                binding.listado.adapter = adaptador
            } else {
                showError()
            }
            hideKeyboard()
        } catch (e:Exception){
            Log.d("Error en la busqueda", e.toString())
        }
    }

    private fun showError() {
        Toast.makeText(this,"No existe broder", Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query:String?): Boolean {
        searchByName(query.toString())
        return true
    }



    override fun onQueryTextChange(query: String?): Boolean {
        return true
    }



}


