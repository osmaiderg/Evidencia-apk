package com.evidencia.tiendaapp.Cliente.Nav_Fragments_Cliente


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evidencia.tiendaapp.Cliente.Botton_Nav_Fragments_Cliente.FragmentMisPedidosCliente
import com.evidencia.tiendaapp.Cliente.Botton_Nav_Fragments_Cliente.FragmentTiendaCliente
import com.evidencia.tiendaapp.R

import com.evidencia.tiendaapp.databinding.FragmentInicioVBinding


class FragmentInicioCliente : Fragment() {

    private lateinit var binding: FragmentInicioVBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInicioVBinding.inflate(inflater, container, false)

        binding.bottomNavigation.setOnItemSelectedListener{
            when(it.itemId){
                R.id.op_tienda_c->{
                   replaceFragment(FragmentTiendaCliente())
                }
                R.id.op_mis_pedidos_c->{
                   replaceFragment(FragmentMisPedidosCliente())
                }
            }
            true
        }

        replaceFragment(FragmentTiendaCliente())
        binding.bottomNavigation.selectedItemId = R.id.op_tienda_c

        return binding.root
    }

    private fun replaceFragment(fragment: Fragment){
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment, fragment)
            .commit()
    }
}