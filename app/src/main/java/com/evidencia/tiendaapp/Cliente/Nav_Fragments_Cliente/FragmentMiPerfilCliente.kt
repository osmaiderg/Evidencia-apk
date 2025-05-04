package com.evidencia.tiendaapp.Cliente.Nav_Fragments_Cliente


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evidencia.tiendaapp.Constantes
import com.evidencia.tiendaapp.databinding.FragmentMiPerfilClienteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FragmentMiPerfilCliente : Fragment() {
    private lateinit var binding: FragmentMiPerfilClienteBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mContext: Context


    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMiPerfilClienteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        leerInformacion()
    }

    private fun leerInformacion() {
        val ref = FirebaseDatabase.getInstance().getReference("usuarios")
        ref.child("${firebaseAuth.uid}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    //Obtener los datos del usuario
                    val nombres = "${snapshot.child("nombres").value}"
                    val email = "${snapshot.child("email").value}"
                    val dni = "${snapshot.child("dni").value}"
                    val telefono = "${snapshot.child("telefono").value}"
                    val fechaRegistro = "${snapshot.child("tRegistro").value}"

                    val fecha = Constantes().obtenerFecha(fechaRegistro.toLong())

                    binding.nombresCPerfil.setText(nombres)
                    binding.emailCPerfil.setText(email)
                    binding.dniCPerfil.setText(dni)
                    binding.telefonoCPerfil.setText(telefono)
                    binding.fechaRegistroCPerfil.text = fecha


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

}