package com.evidencia.tiendaapp.Cliente

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.evidencia.tiendaapp.Constantes
import com.evidencia.tiendaapp.R
import com.evidencia.tiendaapp.databinding.ActivityRegistroClienteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.HashMap


class RegistroClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroClienteBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progresssDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progresssDialog = ProgressDialog(this)
        progresssDialog.setTitle("Espere por favor")
        progresssDialog.setCanceledOnTouchOutside(false)

        binding.btnRegistrarC.setOnClickListener{
            validarInformacion()
        }

    }

    private var nombres = ""
    private var email = ""
    private var password = ""
    private var cpassword = ""
    private fun validarInformacion() {
        nombres = binding.edtxtNombresC.text.toString().trim()
        email = binding.edtxtEmail.text.toString().trim()
        password = binding.edtxtPassword.text.toString().trim()
        cpassword = binding.edtxtCPassword.text.toString().trim()

        if (nombres.isEmpty()){
            binding.edtxtNombresC.error = "Ingrese nombres"
            binding.edtxtNombresC.requestFocus()
        }else if (email.isEmpty()){
            binding.edtxtEmail.error = "Ingrese el email"
            binding.edtxtEmail.requestFocus()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.edtxtEmail.error = "Email no valido"
            binding.edtxtEmail.requestFocus()
        }else if (password.isEmpty()){
            binding.edtxtPassword.error = "Ingrese el password"
            binding.edtxtPassword.requestFocus()
        }else if (password.length < 6){
            binding.edtxtPassword.error = "El password requiere mas de 6 caracteres"
            binding.edtxtPassword.requestFocus()
        }else if (cpassword.isEmpty()){
            binding.edtxtCPassword.error = "Confirme el password"
            binding.edtxtCPassword.requestFocus()
        }else if (password!=cpassword){
            binding.edtxtCPassword.error = "No coincide el password"
            binding.edtxtCPassword.requestFocus()
        }else{
            registrarCliente()
        }

    }

    private fun registrarCliente() {
        progresssDialog.setMessage("Creando cuenta")
        progresssDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                insertarInfoBD()
            }
            .addOnFailureListener { e->
                Toast.makeText(this, "Fallo el registro debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun insertarInfoBD(){
        progresssDialog.setMessage("Guardando informacion")

        val uid = firebaseAuth.uid
        val nombresC = nombres
        val emailC = email
        val tiempoRegistro = Constantes().obtenerTiempoD()

        val datosCliente = HashMap<String, Any>()

        datosCliente["uid"] = "$uid"
        datosCliente["nombres"] = "$nombresC"
        datosCliente["email"] = "$emailC"
        datosCliente["dni"] = ""
        datosCliente["telefono"] = ""
        datosCliente["tRegistro"] = "$tiempoRegistro"
        datosCliente["imagen"] = ""
        datosCliente["tipoUsuario"] = "cliente"

        val reference = FirebaseDatabase.getInstance().getReference("usuarios")
        reference.child(uid!!)
            .setValue(datosCliente)
            .addOnSuccessListener {
                progresssDialog.dismiss()
                startActivity(Intent(this@RegistroClienteActivity, MainActivityCliente::class.java))
                finishAffinity()
            }
            .addOnFailureListener {e->
                progresssDialog.dismiss()
                Toast.makeText(this, "Fallo el registro debido a ${e.message}", Toast.LENGTH_SHORT).show()

            }

    }
}