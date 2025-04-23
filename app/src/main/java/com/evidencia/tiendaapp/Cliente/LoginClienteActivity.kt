package com.evidencia.tiendaapp.Cliente

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.evidencia.tiendaapp.R
import com.evidencia.tiendaapp.databinding.ActivityLoginClienteBinding

class LoginClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginClienteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtRegistrarC.setOnClickListener{
            startActivity(Intent(this@LoginClienteActivity, RegistroClienteActivity::class.java))
        }

    }
}