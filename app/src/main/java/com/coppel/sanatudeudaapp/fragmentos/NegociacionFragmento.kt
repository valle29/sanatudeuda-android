package com.coppel.sanatudeudaapp.fragmentos

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.*

import com.coppel.sanatudeudaapp.R
import com.coppel.sanatudeudaapp.actividades.Informacion_Negociacion
import com.coppel.sanatudeudaapp.modelos.Abonos
import com.coppel.sanatudeudaapp.modelos.Negociacion
import com.coppel.sanatudeudaapp.modelos.Producto
import com.coppel.sanatudeudaapp.utilerias.DatabaseHelper
import kotlinx.android.synthetic.main.negociacion_fragmento.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NegociacionFragmento : Fragment(){

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var databaseHelper: DatabaseHelper
    private var format = NumberFormat.getCurrencyInstance(Locale.CANADA)

    private var negociacionlist = ArrayList<Negociacion>()
    private var abonosList = ArrayList<Abonos>()
    private var productoList = ArrayList<Producto>()
    private var porcentaje_descuento: Double? = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        databaseHelper  = DatabaseHelper(this@NegociacionFragmento.activity!!)
        negociacionlist = databaseHelper.readNegociacion()
        abonosList = databaseHelper.readAllAbonos()
        productoList = databaseHelper.readProducto()

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.negociacion_fragmento, container, false)

    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cargarInformacion()
    }

    fun cargarInformacion(){
        var abonado: Int? = 0
        for(abonos in abonosList)
            abonado= abonado!! + abonos.monto!!

        for(producto in productoList) {
            porcentaje_descuento = producto.descuento!!.toDouble() / 100
            txt_porciento_descuento.text = producto.descuento!!.toString() + "%"
        }

        for (negociacion in negociacionlist) {
            var descuento: Double? = negociacion.saldo_anterior!!.toDouble() * porcentaje_descuento!!
            var nuevo_saldo = negociacion.saldo_anterior!! - descuento!!.toInt()
            var plazo = negociacion.plazo!!
            var nuevo_abono:Double = nuevo_saldo / plazo.toDouble()
            var saldo_actual = nuevo_saldo - abonado!!
            txt_saldo_actual_neg.text = format.format(saldo_actual)
            txt_nuevo_saldo.text = format.format(nuevo_saldo)
            txt_saldo_anterior.text = format.format(negociacion.saldo_anterior!!)
            txt_descuento.text = format.format(descuento.roundToInt())
            txt_moratorio.text = format.format(negociacion.moratorio!!)
            txt_plazo.text = "$plazo meses"
            txt_nuevo_abono.text = format.format(nuevo_abono.roundToInt())
            var fecha= negociacion.dia_formalizacion
            txt_fecha_formalizacion.text = "Fecha de formalización: $fecha"

            var progreso: Double = (1-(saldo_actual.toDouble()/nuevo_saldo.toDouble()))*100
            Bar_progreso.progress= progreso.toInt()

            txt_porcentaje_progreso.text="%.0f".format(progreso)+"%"

            Bar_progreso.progressDrawable.setColorFilter(ContextCompat.getColor(this@NegociacionFragmento.activity!!, R.color.green), PorterDuff.Mode.SRC_IN)
        }
    }

    override fun onStart() {
        super.onStart()
        cargarInformacion()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId

        if(itemId == R.id.action_name){
            startActivity(Intent(this@NegociacionFragmento.activity,Informacion_Negociacion::class.java))
        }

        return super.onOptionsItemSelected(item)
    }
}
