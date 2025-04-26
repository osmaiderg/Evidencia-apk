package com.evidencia.tiendaapp.Vendedor.Nav_Fragments_Vendedor

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.evidencia.tiendaapp.R
import com.evidencia.tiendaapp.databinding.FragmentCategoriasVBinding
import com.google.firebase.database.FirebaseDatabase


class FragmentCategoriasV : Fragment() {

    private lateinit var binding: FragmentCategoriasVBinding
    private lateinit var mContext: Context
    private lateinit var progressDialog: ProgressDialog

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoriasVBinding.inflate(inflater, container, false)

        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnAgregarCat.setOnClickListener{
            validarInfo()
        }

        return binding.root
    }
    private var categoria = ""
    private fun validarInfo() {
        categoria = binding.txtCategoria.text.toString().trim()
        if (categoria.isEmpty()){
            Toast.makeText(context, "Ingrese una categoria", Toast.LENGTH_SHORT).show()
        }else{
            agregarCatBD()
        }
    }

    private fun agregarCatBD() {
        progressDialog.setMessage("Agregando categoria")
        progressDialog.show()

        val ref = FirebaseDatabase.getInstance().getReference("Categorias")
        val keyId = ref.push().key

        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "${keyId}"
        hashMap["categoria"] = "${categoria}"

        ref.child(keyId!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(context, "Se agrego la categoria con exito", Toast.LENGTH_SHORT).show()
                binding.txtCategoria.setText("")
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}