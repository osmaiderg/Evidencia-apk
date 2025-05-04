package com.evidencia.tiendaapp.Vendedor.Productos

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
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
import com.google.firebase.storage.FirebaseStorage

class AgregarProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoBinding
    private var imagenUri: Uri? = null

    private lateinit var imagenSelectArrayList: ArrayList<ModeloImagenSeleccionada>
    private lateinit var adaptadorImagenSel: AdaptadorImagenSeleccionada

    private lateinit var categoriasArrayList: ArrayList<ModeloCategoria>

    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarCategorias()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        /*Estas vistas se inicializan como ocultas*/
        binding.edtxtPrecioConDescuentoP.visibility = View.GONE
        binding.edtxtNotaDescuentoP.visibility = View.GONE

        binding.descuentoSwitch.setOnCheckedChangeListener { buttonView, isCheked ->
            if (isCheked) {
                binding.edtxtPrecioConDescuentoP.visibility = View.VISIBLE
                binding.edtxtNotaDescuentoP.visibility = View.VISIBLE
            }else{
                //switch está deshabilitado
                binding.edtxtPrecioConDescuentoP.visibility = View.GONE
                binding.edtxtNotaDescuentoP.visibility = View.GONE
            }
        }
        imagenSelectArrayList = ArrayList()

        binding.imgAgregarProducto.setOnClickListener {
            seleccionarImg()
        }

        binding.categoria.setOnClickListener {
            selecCategorias()
        }

        binding.btnAgregarProducto.setOnClickListener {
            validarInfo()
        }

        cargarImagenes()

    }

    private var nombreP = ""
    private var descripcionP = ""
    private var categoriaP = ""
    private var precioP = ""
    private var descuentoHab = false
    private var precioDescP = ""
    private var notaDescP = ""
    private fun validarInfo() {
        nombreP = binding.edtxtNombresP.text.toString().trim()
        descripcionP = binding.edtxtDescripcionP.text.toString().trim()
        categoriaP = binding.categoria.text.toString().trim()
        precioP = binding.edtxtPrecioP.text.toString().trim()
        descuentoHab = binding.descuentoSwitch.isChecked

        if (nombreP.isEmpty()) {
            binding.edtxtNombresP.error = "Ingrese nombre"
            binding.edtxtNombresP.requestFocus()
        } else if (descripcionP.isEmpty()) {
            binding.edtxtDescripcionP.error = "Ingrese descripción"
            binding.edtxtDescripcionP.requestFocus()
        } else if (categoriaP.isEmpty()) {
            binding.categoria.error = "Seleccione una categoría"
            binding.categoria.requestFocus()
        } else if (precioP.isEmpty()) {
            binding.edtxtPrecioP.error = "Ingrese precio"
            binding.edtxtPrecioP.requestFocus()
        } else if (imagenUri == null) {
            Toast.makeText(this, "Agregue al menos una imagen", Toast.LENGTH_SHORT).show()
        } else {
            //descuentoHab = true
            if (descuentoHab) {
                precioDescP = binding.edtxtPrecioConDescuentoP.text.toString().trim()
                notaDescP = binding.edtxtNotaDescuentoP.text.toString().trim()
                if (precioDescP.isEmpty()){
                    binding.edtxtPrecioConDescuentoP.error = "Ingrese precio con desc."
                    binding.edtxtPrecioConDescuentoP.requestFocus()
                }else if (notaDescP.isEmpty()){
                    binding.edtxtNotaDescuentoP.text.toString().trim()
                    binding.edtxtNotaDescuentoP.requestFocus()
                }else{
                    agregarProducto()
                }
            }
            //Si el descuento esta habilitado = false
            else{
                precioDescP = "0"
                notaDescP = ""
                agregarProducto()
            }
        }
    }

    private fun agregarProducto() {
        progressDialog.setMessage("Agregando producto")
        progressDialog.show()

        var ref = FirebaseDatabase.getInstance().getReference("Productos")
        val keyId = ref.push().key

        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "${keyId}"
        hashMap["nombre"] = "${nombreP}"
        hashMap["descripcion"] = "${descripcionP}"
        hashMap["categoria"] ="${categoriaP}"
        hashMap["precio"] = "${precioP}"
        hashMap["precioDesc"] = "${precioDescP}"
        hashMap["notaDesc"] = "${notaDescP}"

        ref.child(keyId!!)
            .setValue(hashMap)
            .addOnSuccessListener{
                subirImgsStorage(keyId)
            }
            .addOnFailureListener {e->
                Toast.makeText(this, "${e.message}",Toast.LENGTH_SHORT).show()
            }

    }

    private fun subirImgsStorage(keyId: String) {
        for (i in imagenSelectArrayList.indices) {
            val modeloImagenSel = imagenSelectArrayList[i]
            val nombreImagen = modeloImagenSel.id
            val rutaImagen = "Productos/$nombreImagen"

            val storageRef = FirebaseStorage.getInstance().getReference(rutaImagen)
            storageRef.putFile(modeloImagenSel.imageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    val uriTask = taskSnapshot.storage.downloadUrl
                    while (!uriTask.isSuccessful);
                    val urlImgCargada = uriTask.result

                    if (uriTask.isSuccessful){
                        val hashMap = HashMap<String, Any>()
                        hashMap["id"] = "${modeloImagenSel.id}"
                        hashMap["imagenUrl"] = "${urlImgCargada}"

                        val ref = FirebaseDatabase.getInstance().getReference("Productos")
                        ref.child(keyId).child("Imagenes")
                            .child(nombreImagen)
                            .updateChildren(hashMap)
                        progressDialog.dismiss()
                        Toast.makeText(this, "Se agrego el producto",Toast.LENGTH_SHORT).show()

                        limpiarCampos()

                    }
                }
                .addOnFailureListener {e->
                    progressDialog.dismiss()
                    Toast.makeText(this, "${e.message}",Toast.LENGTH_SHORT).show()
        }   }
    }

    private fun limpiarCampos() {
        imagenSelectArrayList.clear()
        adaptadorImagenSel.notifyDataSetChanged()
        binding.edtxtNombresP.setText("")
        binding.edtxtDescripcionP.setText("")
        binding.edtxtPrecioP.setText("")
        binding.categoria.setText("")
        binding.descuentoSwitch.isChecked = false
        binding.edtxtPrecioConDescuentoP.setText("")
        binding.edtxtNotaDescuentoP.setText("")

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