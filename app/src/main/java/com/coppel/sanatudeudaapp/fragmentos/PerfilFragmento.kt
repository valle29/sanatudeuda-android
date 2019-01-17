package com.coppel.sanatudeudaapp.fragmentos

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment

import com.coppel.sanatudeudaapp.R
import android.support.v7.app.AlertDialog
import android.view.*
import com.coppel.sanatudeudaapp.app.Login
import com.coppel.sanatudeudaapp.utilerias.DatabaseHelper
import com.coppel.sanatudeudaapp.utilerias.Preferencias
import kotlinx.android.synthetic.main.perfil_fragmento.*
import java.util.*
import android.graphics.BitmapFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PerfilFragmento : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        databaseHelper  = DatabaseHelper(this@PerfilFragmento.activity!!)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.perfil_fragmento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        for (cliente in databaseHelper.readCliente()) {

            val decodedString = android.util.Base64.decode(cliente.avatar, android.util.Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

            img_avatar.setImageBitmap(decodedByte)
            txt_nombre_cliente.text = cliente.nombre
            txt_correo_cliente.text = cliente.correo

        }

        nav_cerrar_sesion.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
            val dialog: AlertDialog = builder.setTitle("Cerrar Sesión")
                    .setMessage("¿Desea cerrar sesión?")
                    .setPositiveButton("Aceptar") {dialog, which ->
                        var preferencias = Preferencias(view.context)
                        preferencias.sesion = true
                        databaseHelper.deleteAll()
                        startActivity(Intent(view.context, Login::class.java))
                        this@PerfilFragmento.activity!!.finish()
                    }
                    .setNegativeButton("Cancelar") { dialog, which ->
                        dialog.dismiss()
                    }
                    .create()
            dialog.show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE)

        }

    }

    /*override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId

        if(itemId == R.id.action_name){
            startActivity(Intent(this@PerfilFragmento.activity,Informacion_Perfil::class.java))
        }

        return super.onOptionsItemSelected(item)
    }*/

}
