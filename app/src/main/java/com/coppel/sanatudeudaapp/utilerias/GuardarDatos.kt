package com.coppel.sanatudeudaapp.utilerias

import android.content.Context
import android.util.Log
import com.coppel.sanatudeudaapp.modelos.Abonos
import com.coppel.sanatudeudaapp.modelos.Cliente
import com.coppel.sanatudeudaapp.modelos.Negociacion
import com.coppel.sanatudeudaapp.modelos.Producto
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class GuardarDatos(context: Context){

    private lateinit var db : FirebaseFirestore
    private var databaseHelper = DatabaseHelper(context)

    fun guardarCliente(correo: String,callback: (Boolean) -> Unit){
        db = FirebaseFirestore.getInstance()

        db.collection("clientes").whereEqualTo("correo",correo)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            val objCliente = document.toObject(Cliente::class.java)
                            databaseHelper.addCliente(
                                    objCliente.idu_cliente!!,
                                    objCliente.nombre!!,
                                    objCliente.correo!!,
                                    objCliente.contraseña!!,
                                    objCliente.num_cliente!!,
                                    objCliente.avatar!!,
                                    objCliente.celular!!,
                                    objCliente.telefono!!,
                                    objCliente.fecha_alta!!,
                                    objCliente.puntualidad!!,
                                    objCliente.token!!)
                            guardarNegociaciones(objCliente.idu_cliente!!){
                                callback(true)
                            }
                        }
                    } else {
                        callback(false)
                        Log.w("ERROR EN DOC CLIENTES", "Error getting documents.", task.exception)
                    }
                }
    }

    fun guardarNegociaciones(clienteID: Int,callback: (Boolean) -> Unit){
        db = FirebaseFirestore.getInstance()

        db.collection("negociaciones")
                .whereEqualTo("idu_cliente",clienteID)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            val objNegociacion = document.toObject(Negociacion::class.java)

                            databaseHelper.addNegociacion(
                                    objNegociacion.idu_negociacion!!,
                                    objNegociacion.idu_cliente!!,
                                    objNegociacion.idu_producto!!,
                                    objNegociacion.saldo_anterior!!,
                                    objNegociacion.atrasado!!,
                                    objNegociacion.moratorio!!,
                                    objNegociacion.dia_formalizacion!!,
                                    objNegociacion.plazo!!)
                            guardarProducto(objNegociacion.idu_producto!!){
                                guardarAbonos(objNegociacion.idu_negociacion!!){
                                    callback(true)
                                }
                            }
                        }

                    } else {
                        callback(false)
                        Log.w("ERROR EN DOC NEGOCIACIO", "Error getting documents.", task.exception)
                    }
                }
    }

    fun guardarAbonos(negociacionID: Int,callback: (Boolean) -> Unit){
        db = FirebaseFirestore.getInstance()
        db.collection("abonos")
                .whereEqualTo("idu_negociacion", negociacionID)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            val objAbono = document.toObject(Abonos::class.java)
                            databaseHelper.addAbono(
                                    objAbono.idu_abono!!,
                                    objAbono.idu_negociacion!!,
                                    objAbono.atrasado!!,
                                    objAbono.fecha_hora!!,
                                    objAbono.folio!!,
                                    objAbono.monto!!,
                                    objAbono.tienda!!)
                        }
                        callback(true)
                    } else {
                        callback(false)
                        Log.w("ERROR EN DOC ABONOS", "Error getting documents.", task.exception)
                    }
                }
    }

    fun guardarProducto(idu_producto: Int,callback: (Boolean) -> Unit){
        db = FirebaseFirestore.getInstance()
        db.collection("productos")
                .whereEqualTo("idu_producto", idu_producto)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            val objAbono = document.toObject(Producto::class.java)
                            databaseHelper.addProducto(
                                    objAbono.idu_producto!!,
                                    objAbono.descripcion!!,
                                    objAbono.descuento!!)
                        }
                        callback(true)
                    } else {
                        callback(false)
                        Log.w("ERROR EN DOC ABONOS", "Error getting documents.", task.exception)
                    }
                }
    }

}