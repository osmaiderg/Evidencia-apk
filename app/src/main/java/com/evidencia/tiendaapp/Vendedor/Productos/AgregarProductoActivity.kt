package com.evidencia.tiendaapp.Vendedor.Productos

import android.app.Activity
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.evidencia.tiendaapp.Adaptadores.AdaptadorImagenSeleccionada
import com.evidencia.tiendaapp.Constantes
import com.evidencia.tiendaapp.Modelos.ModeloCategoria
import com.evidencia.tiendaapp.Modelos.ModeloImagenSeleccionada
import com.evidencia.tiendaapp.databinding.ActivityAgregarProductoBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AgregarProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoBinding
    private var imagenUri : Uri?=null

    private lateinit var imagenSelectArrayList: ArrayList<ModeloImagenSeleccionada>
    private lateinit var adaptadorImagenSel : AdaptadorImagenSeleccionada

    private lateinit var  categoriasArrayList : ArrayList<ModeloCategoria>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarCategorias()

        imagenSelectArrayList = ArrayList()

        binding.imgAgregarProducto.setOnClickListener{
            seleccionarImg()
        }

        binding.categoria.setOnClickListener{
            selecCategorias()
        }

        cargarImagenes()

    }

    private fun cargarCategorias() {
        categoriasArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Categorias").orderByChild("categoria")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriasArrayList.clear()
                for (ds in snapshot.children){
                    val modelo = ds.getValue(ModeloCategoria::class.java)
                    categoriasArrayList.add(modelo!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private var idCat = ""
    private var tituloCat = ""
    private fun selecCategorias(){
        val categoriasArray = arrayOfNulls<String>(categoriasArrayList.size)
        for (i in categoriasArray.indices){
            categoriasArray[i] = categoriasArrayList[i].categoria
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccione una categoria")
            .setItems(categoriasArray){dialog, witch->
                idCat = categoriasArrayList[witch].id
                tituloCat = categoriasArrayList[witch].categoria


                binding.categoria.text = tituloCat//validar
            }
            .show()
    }

    private fun cargarImagenes() {
        adaptadorImagenSel = AdaptadorImagenSeleccionada(this, imagenSelectArrayList)
        binding.RVImagenesProduto.adapter = adaptadorImagenSel
    }


    private fun seleccionarImg(){
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                resultadoImg.launch(intent)
            }
    }

    private val resultadoImg =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){resultado->
            if (resultado.resultCode == Activity.RESULT_OK){
                val data = resultado.data
                imagenUri = data!!.data
                val tiempo ="${Constantes().obtenerTiempoD()}"

                val modeloImgSel = ModeloImagenSeleccionada(tiempo, imagenUri, null, false)
                imagenSelectArrayList.add(modeloImgSel)
                cargarImagenes()
            }else{
                Toast.makeText(this, "Accion cancelada", Toast.LENGTH_SHORT).show()
            }
        }
}