package com.evidencia.tiendaapp.Vendedor

import android.app.ProgressDialog
import android.content.Intent
import android.media.tv.TvContract.Programs
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.core.text.trimmedLength
import com.evidencia.tiendaapp.Constantes
import com.evidencia.tiendaapp.R
import com.evidencia.tiendaapp.databinding.ActivityMainVendedorBinding
import com.evidencia.tiendaapp.databinding.ActivityRegistroVendedorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class RegistroVendedorActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegistroVendedorBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside((false))

        binding.btnRegistrarV.setOnClickListener{
            validarInformacion()
        }

    }

    private var nombres = ""
    private var email = ""
    private var password = ""
    private var cpassword = ""
    private fun validarInformacion() {
        nombres = binding.edtxtNombresV.text.toString().trim()
        email = binding.edtxtEmailV.text.toString().trim()
        password = binding.edtxtPasswordV.text.toString().trim()
        cpassword = binding.edtxtCPasswordV.text.toString().trim()

        if(nombres.isEmpty()){
            binding.edtxtNombresV.error = "Ingrese sus nombres"
            binding.edtxtNombresV.requestFocus()
        }else if(email.isEmpty()){
            binding.edtxtEmailV.error = "ingrese su email"
            binding.edtxtEmailV.requestFocus()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.edtxtEmailV.error = "Email no valido"
            binding.edtxtEmailV.requestFocus()
        }else if(password.isEmpty()){
            binding.edtxtPasswordV.error = "Ingrese Password"
            binding.edtxtPasswordV.requestFocus()
        }else if(password.length < 6){
            binding.edtxtPasswordV.error = "Necesita 6 o mas caracteres"
            binding.edtxtPasswordV.requestFocus()
        }else if (cpassword.isEmpty()){
            binding.edtxtCPasswordV.error = "confirmar Password"
            binding.edtxtCPasswordV.requestFocus()
        }else if(password!=cpassword){
            binding.edtxtCPasswordV.error = "No coinciden"
            binding.edtxtCPasswordV.requestFocus()
        }else{
            registrarVendedor()
        }

    }

    private fun registrarVendedor() {
        progressDialog.setMessage("Creando cuenta")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                insertarInforBD()
            }
            .addOnFailureListener{e->
                Toast.makeText(this, "Fallo el registro debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun insertarInforBD(){
        progressDialog.setMessage("Guardando informacion... ")

        val uidBD = firebaseAuth.uid
        val nombresBD = nombres
        val emailBD = email
       // val tipoUsuario = "vendedor"
        val tiempoBD = Constantes().obtenerTiempoD()

        val datosVendedor = HashMap<String, Any>()

        datosVendedor["uid"] = "$uidBD"
        datosVendedor["nombres"] = "$nombresBD"
        datosVendedor["email"] = "$emailBD"
        datosVendedor["tipoUsuario"] = "vendedor"
        datosVendedor["tiempo_registro"] = "$tiempoBD"

        val references = FirebaseDatabase.getInstance().getReference("usuarios")
        references.child(uidBD!!)
            .setValue(datosVendedor)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityVendedor:: class.java))
                finish()
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this, "Fallo el registro en la base de datos debido a ${e.message}", Toast.LENGTH_SHORT).show()

            }

    }
}