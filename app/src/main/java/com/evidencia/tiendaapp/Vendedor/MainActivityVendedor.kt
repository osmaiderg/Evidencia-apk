package com.evidencia.tiendaapp.Vendedor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.evidencia.tiendaapp.R
import com.evidencia.tiendaapp.Vendedor.Botton_nav_Fragments_Vendedor.FragmentMisProductosV
import com.evidencia.tiendaapp.Vendedor.Botton_nav_Fragments_Vendedor.FragmentPedidosV
import com.evidencia.tiendaapp.Vendedor.Nav_Fragments_Vendedor.FragmentInicioV
import com.evidencia.tiendaapp.Vendedor.Nav_Fragments_Vendedor.FragmentMiTiendaV
import com.evidencia.tiendaapp.Vendedor.Nav_Fragments_Vendedor.FragmentReseniasV
import com.evidencia.tiendaapp.databinding.ActivityMainVendedorBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivityVendedor : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding :ActivityMainVendedorBinding

    private var firebaseAuth :  FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        firebaseAuth = FirebaseAuth.getInstance()
        comprobarSesion()

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

        replaceFragment(FragmentInicioV())
        binding.navigationView.setCheckedItem(R.id.op_inicio_v)
    }

    private fun cerrarSesion(){
        firebaseAuth!!.signOut()
        startActivity(Intent(applicationContext, LoginVendedorActivity::class.java))
        finish()
        Toast.makeText(applicationContext, "has cerrado sesion", Toast.LENGTH_SHORT).show()
    }

    private fun comprobarSesion() {
        /*Si el usuario no ha iniciado sesion */
        if(firebaseAuth!!.currentUser==null){
            startActivity(Intent(applicationContext, LoginVendedorActivity::class.java))
            Toast.makeText(applicationContext, "vendedor no registrado", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(applicationContext, "vendedor en linea", Toast.LENGTH_SHORT).show()
        }
    }

    private fun replaceFragment(fragment : Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()
    }
    //Esta funcion muestra el fragmento seleccionado en la aplicacion
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.op_inicio_v->{
                replaceFragment(FragmentInicioV())
            }
            R.id.op_mitienda_v->{
                replaceFragment(FragmentMiTiendaV())
            }
            R.id.op_resenia_v->{
                replaceFragment(FragmentReseniasV())
            }
            R.id.op_cerrar_sesion_v->{
                cerrarSesion()
            }
            R.id.op_mis_productos_v->{
                replaceFragment(FragmentMisProductosV())
            }
            R.id.op_mis_pedidos_v->{
                replaceFragment(FragmentPedidosV())
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
