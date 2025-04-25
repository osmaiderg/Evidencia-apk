package com.evidencia.tiendaapp.Vendedor.Nav_Fragments_Vendedor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.evidencia.tiendaapp.R
import com.evidencia.tiendaapp.Vendedor.Botton_nav_Fragments_Vendedor.FragmentMisProductosV
import com.evidencia.tiendaapp.Vendedor.Botton_nav_Fragments_Vendedor.FragmentPedidosV
import com.evidencia.tiendaapp.Vendedor.Productos.AgregarProductoActivity
import com.evidencia.tiendaapp.databinding.FragmentInicioVBinding

class FragmentInicioV : Fragment() {

    private lateinit var binding : FragmentInicioVBinding
    private lateinit var mContext : Context

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInicioVBinding.inflate(inflater, container, false)

        binding.bottomNavigation.setOnItemReselectedListener {
            when(it.itemId){
                R.id.op_mis_productos_v->{
                    replaceFragment(FragmentMisProductosV())
                }
                R.id.op_mis_pedidos_v->{
                    replaceFragment(FragmentPedidosV())
                }
            }
            true
        }

        replaceFragment(FragmentMisProductosV())
        binding.bottomNavigation.selectedItemId = R.id.op_mis_productos_v


        binding.addFab.setOnClickListener{
            startActivity(Intent(context, AgregarProductoActivity::class.java ))
        }

        return binding.root
    }

    private fun replaceFragment(fragment : Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment, fragment)
            .commit()
    }


}