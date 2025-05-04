package com.evidencia.tiendaapp.Cliente

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.evidencia.tiendaapp.Constantes
import com.evidencia.tiendaapp.R
import com.evidencia.tiendaapp.Vendedor.RegistroVendedorActivity
import com.evidencia.tiendaapp.databinding.ActivityLoginClienteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginClienteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginClienteBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog : ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnLoginc.setOnClickListener{
            validarInfo()
        }

        binding.txtRegistrarC.setOnClickListener{
            startActivity(Intent(applicationContext, RegistroClienteActivity::class.java))
        }

    }


    private var email = ""
    private var password = ""
    private fun validarInfo() {
        email = binding.edtxtEmail.text.toString().trim()
        password = binding.edtxtPassword.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.edtxtEmail.error = "Email inválido"
            binding.edtxtEmail.requestFocus()
        }
        else if (email.isEmpty()){
            binding.edtxtEmail.error = "Ingrese email"
            binding.edtxtEmail.requestFocus()
        }
        else if (password.isEmpty()){
            binding.edtxtPassword.error = "Ingrese password"
            binding.edtxtPassword.requestFocus()
        }else{
            loginCliente()
        }

    }

    private fun loginCliente() {
        progressDialog.setMessage("Ingresando")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityCliente::class.java))
                finishAffinity()
                Toast.makeText(this, "Bienvenido(a)",
                    Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                Toast.makeText(this, "No se pudo iniciar sesión debido a ${e.message}",
                    Toast.LENGTH_SHORT).show()
            }

    }


    private fun llenarInfoBD() {
        progressDialog.setMessage("Guardando información")

        val uid = firebaseAuth.uid
        val nombreC = firebaseAuth.currentUser?.displayName
        val emailC = firebaseAuth.currentUser?.email
        val tiempoRegistro = Constantes().obtenerTiempoD()

        val datosCliente = HashMap<String, Any>()

        datosCliente["uid"] = "$uid"
        datosCliente["nombres"] = "$nombreC"
        datosCliente["email"] = "$emailC"
        datosCliente["telefono"] = ""
        datosCliente["dni"] = ""
        datosCliente["proveedor"] = "google"
        datosCliente["tRegistro"] = "$tiempoRegistro"
        datosCliente["imagen"] = ""
        datosCliente["tipoUsuario"] = "cliente"

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(uid!!)
            .setValue(datosCliente)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityCliente::class.java))
                finishAffinity()
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()

            }



    }
}