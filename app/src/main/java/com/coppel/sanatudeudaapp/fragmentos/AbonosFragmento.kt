package com.coppel.sanatudeudaapp.fragmentos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.*
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView

import com.coppel.sanatudeudaapp.R
import com.coppel.sanatudeudaapp.actividades.Informacion_Abonos
import com.coppel.sanatudeudaapp.modelos.Abonos
import com.coppel.sanatudeudaapp.utilerias.DatabaseHelper
import com.coppel.sanatudeudaapp.utilerias.GuardarDatos
import kotlinx.android.synthetic.main.abonos_fragmento.*
import java.text.NumberFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AbonosFragmento : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var databaseHelper: DatabaseHelper
    private var listAbonos = ArrayList<Abonos>()
    private var abonado: Int = 0
    private var format = NumberFormat.getCurrencyInstance(Locale.CANADA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        databaseHelper = DatabaseHelper(this@AbonosFragmento.activity!!)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.abonos_fragmento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val negociacionlist = databaseHelper.readNegociacion()
        var id_negociacion: Int?=0

        readAbonos()

        for(negociacion in negociacionlist)
            id_negociacion = negociacion.idu_negociacion!!

        pull_to_refresh_abonos.setOnRefreshListener {
            actualizarAbonos(id_negociacion!!) {
                pull_to_refresh_abonos!!.isRefreshing = false
            }
        }

        /*list_abonos.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this@AbonosFragmento.activity,DetalleAbono::class.java)
            intent.putExtra("Monto",format.format(listAbonos[position].monto))
            startActivity(intent)
        }*/
    }

    @SuppressLint("ResourceType")
    private fun actualizarAbonos(id_negociacion:Int, callback: (Boolean) -> Unit){
        val guardarDatos: GuardarDatos? = GuardarDatos(this@AbonosFragmento.activity!!)

        //Borrar y crear Tabla ABONOS
        databaseHelper.dropAndCreateTableAbonos()

        guardarDatos!!.guardarAbonos(id_negociacion){
            readAbonos()

            callback(true)
        }
    }

    private fun readAbonos(){
        val negociacionlist = databaseHelper.readNegociacion()
        val productoList = databaseHelper.readProducto()
        var porcentaje_descuento: Double? = 0.0

        for(producto in productoList)
            porcentaje_descuento = producto.descuento!!.toDouble() / 100

        listAbonos.clear()
        abonado = 0
        var abonosList = databaseHelper.readAllAbonos()
        for (abono in abonosList) {
            listAbonos.add(Abonos(
                    abono.idu_abono,
                    abono.idu_negociacion,
                    abono.atrasado,
                    abono.fecha_hora,
                    abono.folio,
                    abono.monto,
                    abono.tienda))
            abonado += abono.monto!!

            var abonosAdapter = AbonosAdapter(this@AbonosFragmento, listAbonos)
            list_abonos!!.adapter = abonosAdapter
            val str_abonado = format.format(abonado)
            txt_abonado!!.text = str_abonado
        }

        for(negociacion in negociacionlist) {
            var descuento: Double? = negociacion.saldo_anterior!!.toDouble() * porcentaje_descuento!!
            var nuevo_saldo = negociacion.saldo_anterior!! - descuento!!.toInt()
            var saldo_actual = nuevo_saldo - abonado
            txt_saldo_actual.text = format.format(saldo_actual)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId

        if(itemId == R.id.action_name){
            startActivity(Intent(this@AbonosFragmento.activity,Informacion_Abonos::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    inner class AbonosAdapter : BaseAdapter {

        private var abonosList = ArrayList<Abonos>()
        private var context: AbonosFragmento? = null

        constructor(context: AbonosFragmento, abonosList: ArrayList<Abonos>) : super() {
            this.abonosList = abonosList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

            val view: View?
            val holder: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.row_abono, parent, false)
                holder = ViewHolder(view)
                view.tag = holder
                //Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }

            if(abonosList[position].atrasado!!) {
                holder.color_abono.setBackgroundColor(ContextCompat.getColor(this@AbonosFragmento.activity!!, R.color.red))
            }
            else{
                holder.color_abono.setBackgroundColor(ContextCompat.getColor(this@AbonosFragmento.activity!!, R.color.green))
            }

            holder.txt_numero_abono.text = abonosList[position].idu_abono.toString()
            holder.txt_folio.text = abonosList[position].folio
            holder.txt_tienda.text = abonosList[position].tienda
            holder.txt_fecha_hora.text = abonosList[position].fecha_hora
            val str_monto = format.format(abonosList[position].monto)
            holder.txt_monto.text = str_monto

            return view
        }

        override fun getItem(position: Int): Any {
            return abonosList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return abonosList.size
        }
    }

    class ViewHolder(view: View?) {

        val color_abono: LinearLayout
        val txt_numero_abono: TextView
        val txt_folio: TextView
        val txt_tienda: TextView
        val txt_fecha_hora: TextView
        val txt_monto: TextView

        init {
            this.color_abono = view?.findViewById(R.id.color_abono) as LinearLayout
            this.txt_numero_abono = view?.findViewById(R.id.txt_numero_abono) as TextView
            this.txt_folio = view.findViewById(R.id.txt_folio) as TextView
            this.txt_tienda = view.findViewById(R.id.txt_tienda) as TextView
            this.txt_fecha_hora = view.findViewById(R.id.txt_fecha_hora) as TextView
            this.txt_monto = view.findViewById(R.id.txt_monto) as TextView
        }

        //  if you target API 26, you should change to:
//        init {
//            this.tvTitle = view?.findViewById<TextView>(R.id.tvTitle) as TextView
//            this.tvContent = view?.findViewById<TextView>(R.id.tvContent) as TextView
//        }
    }

}
