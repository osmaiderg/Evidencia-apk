package com.evidencia.tiendaapp.Cliente

import android.os.Bundle
import android.text.method.Touch
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.evidencia.tiendaapp.Cliente.Botton_Nav_Fragments_Cliente.FragmentMisPedidosCliente
import com.evidencia.tiendaapp.Cliente.Botton_Nav_Fragments_Cliente.FragmentTiendaCliente
import com.evidencia.tiendaapp.Cliente.Nav_Fragments_Cliente.FragmentInicioCliente
import com.evidencia.tiendaapp.Cliente.Nav_Fragments_Cliente.FragmentMiPerfilCliente

import com.evidencia.tiendaapp.R
import com.evidencia.tiendaapp.databinding.ActivityMainClienteBinding
import com.google.android.material.navigation.NavigationView


class MainActivityCliente : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainClienteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        binding.navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        replaceFragment(FragmentInicioCliente())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.op_inicio_c->{
                replaceFragment(FragmentInicioCliente())
            }
            R.id.op_mi_perfil_c->{
                replaceFragment(FragmentMiPerfilCliente())
            }
            R.id.op_cerrar_sesion_c->{
                Toast.makeText(applicationContext, "Has cerrado sesion", Toast.LENGTH_SHORT).show()
            }
            R.id.op_tienda_c->{
                replaceFragment(FragmentTiendaCliente())
            }
            R.id.op_mis_pedidos_c->{
                replaceFragment(FragmentMisPedidosCliente())
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}