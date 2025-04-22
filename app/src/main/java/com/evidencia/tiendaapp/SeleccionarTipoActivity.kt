package com.evidencia.tiendaapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.evidencia.tiendaapp.Cliente.LoginClienteActivity
import com.evidencia.tiendaapp.Vendedor.LoginVendedorActivity
import com.evidencia.tiendaapp.databinding.ActivityMainClienteBinding
import com.evidencia.tiendaapp.databinding.ActivitySeleccionarTipoBinding

class SeleccionarTipoActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySeleccionarTipoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionarTipoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tipoVendedor.setOnClickListener{
            startActivity(Intent(this@SeleccionarTipoActivity, LoginVendedorActivity::class.java ))
        }

        binding.tipoCliente.setOnClickListener{
            startActivity(Intent(this@SeleccionarTipoActivity, LoginClienteActivity::class.java))
        }

    }
}