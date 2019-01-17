package com.coppel.sanatudeudaapp.utilerias

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.coppel.sanatudeudaapp.modelos.Abonos
import com.coppel.sanatudeudaapp.modelos.Cliente
import com.coppel.sanatudeudaapp.modelos.Negociacion
import com.coppel.sanatudeudaapp.modelos.Producto

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    lateinit var db_delete: SQLiteDatabase

    companion object {

        var DATABASE_NAME = "sanatudeuda"
        private val DATABASE_VERSION = 2
        private val TABLE_ABONOS = "abonos"
        private val TABLE_CLIENTE = "cliente"
        private val TABLE_NEGOCIACION = "negociaciones"
        private val TABLE_PRODUCTO = "producto"
        private val KEY_ID = "id"

        /*CREATE TABLE students ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone_number TEXT......);*/

        private val CREATE_TABLE_ABONOS = ("CREATE TABLE "
                + TABLE_ABONOS +
                "(id_abono INTEGER ," +
                "id_negociacion INTEGER," +
                "atrasado BOOLEAN," +
                "fecha_hora TEXT," +
                "folio TEXT,"+
                "monto INTEGER,"+
                "tienda TEXT);")

        private val CREATE_TABLE_CLIENTE = ("CREATE TABLE "
                + TABLE_CLIENTE+
               "(id_cliente INTEGER," +
                "nombre TEXT," +
                "correo TEXT," +
                "contraseña TEXT," +
                "num_cliente TEXT," +
                "avatar TEXT," +
                "celular TEXT," +
                "telefono TEXT," +
                "fecha_alta TEXT," +
                "puntualidad TEXT," +
                "token TEXT );")

        private val CREATE_TABLE_NEGOCIACION = ("CREATE TABLE "
                + TABLE_NEGOCIACION +
                "(id_negociacion INTEGER," +
                "id_cliente INTEGER," +
                "id_producto INTEGER," +
                "saldo_anterior INTEGER," +
                "atrasado INTEGER," +
                "moratorio INTEGER," +
                "dia_formalizacion TEXT," +
                "porcentaje_descuento INTEGER," +
                "plazo INTEGER );")

        private val CREATE_TABLE_PRODUCTO = ("CREATE TABLE "
                + TABLE_PRODUCTO +
                "(id_producto INTEGER," +
                "descripcion TEXT," +
                "descuento INTEGER);")
    }

    // looping through all rows
    // and adding to list
    //getting user hobby where id = id from user_hobby table
    //SQLiteDatabase dbhobby = this.getReadableDatabase();
    //getting user city where id = id from user_city table
    //SQLiteDatabase dbCity = this.getReadableDatabase();
    // adding to Students list

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_ABONOS)
        db.execSQL(CREATE_TABLE_CLIENTE)
        db.execSQL(CREATE_TABLE_NEGOCIACION)
        db.execSQL(CREATE_TABLE_PRODUCTO)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS '$TABLE_ABONOS'")
        db.execSQL("DROP TABLE IF EXISTS '$TABLE_CLIENTE'")
        db.execSQL("DROP TABLE IF EXISTS '$TABLE_NEGOCIACION'")
        db.execSQL("DROP TABLE IF EXISTS '$TABLE_PRODUCTO'")
        onCreate(db)
    }

    fun deleteAll(){
        db_delete = this.writableDatabase
        db_delete.delete(TABLE_ABONOS,null,null)
        db_delete.delete(TABLE_CLIENTE,null,null)
        db_delete.delete(TABLE_NEGOCIACION,null,null)
        db_delete.delete(TABLE_PRODUCTO,null,null)
    }

    fun dropAndCreateTableAbonos(){
        db_delete = this.writableDatabase
        db_delete.execSQL("DROP TABLE IF EXISTS '$TABLE_ABONOS'")
        db_delete.execSQL(CREATE_TABLE_ABONOS)
    }

    fun addAbono(id_abono: Int,
                 id_negociacion: Int,
                 atrasado: Boolean,
                 fecha_hora: String,
                 folio: String,
                 monto: Int,
                 tienda: String) {
        val db = this.writableDatabase
        //adding user name in users table
        val values = ContentValues()
        values.put("id_abono", id_abono)
        values.put("id_negociacion", id_negociacion)
        values.put("atrasado", atrasado)
        values.put("fecha_hora", fecha_hora)
        values.put("folio", folio)
        values.put("monto", monto)
        values.put("tienda", tienda)
        db.insert(TABLE_ABONOS, null, values)
    }

    fun addCliente(id_cliente: Int,
                   nombre: String,
                   correo: String,
                   contraseña: String,
                   num_cliente: String,
                   avatar: String,
                   celular: String,
                   telefono: String,
                   fecha_alta: String,
                   puntualidad: String,
                   token: String) {
        val db = this.writableDatabase
        //adding user name in users table
        val values = ContentValues()
        values.put("id_cliente", id_cliente)
        values.put("nombre", nombre)
        values.put("correo", correo)
        values.put("contraseña", contraseña)
        values.put("num_cliente", num_cliente)
        values.put("avatar", avatar)
        values.put("celular", celular)
        values.put("telefono", telefono)
        values.put("fecha_alta", fecha_alta)
        values.put("puntualidad", puntualidad)
        values.put("token", token)
        db.insert(TABLE_CLIENTE, null, values)

    }

    fun addNegociacion(idu_negociacion: Int,
                       idu_cliente: Int?,
                       idu_producto: Int,
                       saldo_anterior: Int,
                       atrasado: Int,
                       moratorio: Int,
                       dia_formalizacion: String,
                       plazo: Int) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put("id_negociacion", idu_negociacion)
        values.put("id_cliente", idu_cliente)
        values.put("id_producto", idu_producto)
        values.put("saldo_anterior", saldo_anterior)
        values.put("atrasado",atrasado)
        values.put("moratorio",moratorio)
        values.put("dia_formalizacion",dia_formalizacion)
        values.put("plazo",plazo)
        db.insert(TABLE_NEGOCIACION, null, values)
    }

    fun addProducto(idu_producto: Int,
                    descripcion: String?,
                    descuento: Int) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put("id_producto", idu_producto)
        values.put("descripcion", descripcion)
        values.put("descuento", descuento)
        db.insert(TABLE_PRODUCTO, null, values)
    }

    fun readAllAbonos(): ArrayList<Abonos> {
        val abonos = ArrayList<Abonos>()
        //val a = readableDatabase
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from $TABLE_ABONOS order by id_abono", null)
        } catch (e: SQLiteException) {
            db.execSQL(CREATE_TABLE_ABONOS)
            return ArrayList()
        }

        var abonoid: Int
        var negociacionid: Int
        var atrasado: Boolean
        var fecha_hora: String
        var folio: String
        var monto: Int
        var tienda: String
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                abonoid = cursor.getInt(cursor.getColumnIndex("id_abono"))
                negociacionid = cursor.getInt(cursor.getColumnIndex(  "id_negociacion"))
                atrasado = cursor.getInt(cursor.getColumnIndex("atrasado")) > 1
                fecha_hora = cursor.getString(cursor.getColumnIndex("fecha_hora"))
                folio = cursor.getString(cursor.getColumnIndex("folio"))
                monto = cursor.getInt(cursor.getColumnIndex("monto"))
                tienda= cursor.getString(cursor.getColumnIndex("tienda"))

                abonos.add(Abonos(
                        abonoid,
                        negociacionid,
                        atrasado,
                        fecha_hora,
                        folio,
                        monto,
                        tienda))

                cursor.moveToNext()
            }
        }
        return abonos
    }

    fun readCliente(): ArrayList<Cliente> {
        val cliente = ArrayList<Cliente>()
        val db = writableDatabase
        var cursor: Cursor?
        try {
            cursor = db.rawQuery("select * from $TABLE_CLIENTE", null)
        } catch (e: SQLiteException) {
            db.execSQL(CREATE_TABLE_CLIENTE)
            return ArrayList()
        }

        var id_cliente: Int
        var nombre: String
        var correo: String
        var contraseña: String
        var num_cliente: String
        var avatar: String
        var celular: String
        var telefono: String
        var fecha_alta: String
        var puntualidad: String
        var token: String
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                id_cliente = cursor.getInt(cursor.getColumnIndex("id_cliente"))
                nombre = cursor.getString(cursor.getColumnIndex(  "nombre"))
                correo = cursor.getString(cursor.getColumnIndex("correo"))
                contraseña = cursor.getString(cursor.getColumnIndex("contraseña"))
                num_cliente = cursor.getString(cursor.getColumnIndex("num_cliente"))
                avatar = cursor.getString(cursor.getColumnIndex("avatar"))
                celular = cursor.getString(cursor.getColumnIndex("celular"))
                telefono = cursor.getString(cursor.getColumnIndex("telefono"))
                fecha_alta = cursor.getString(cursor.getColumnIndex("fecha_alta"))
                puntualidad = cursor.getString(cursor.getColumnIndex("puntualidad"))
                token = cursor.getString(cursor.getColumnIndex("token"))

                cliente.add(Cliente(
                        id_cliente,
                        nombre,
                        correo,
                        contraseña,
                        num_cliente,
                        avatar,
                        celular,
                        telefono,
                        fecha_alta,
                        puntualidad,
                        token))
                cursor.moveToNext()
            }
        }
        return cliente
    }

    fun readNegociacion(): ArrayList<Negociacion> {
        val negociacion = ArrayList<Negociacion>()
        val db = writableDatabase
        var cursor: Cursor?
        try {
            cursor = db.rawQuery("select * from $TABLE_NEGOCIACION", null)
        } catch (e: SQLiteException) {
            db.execSQL(CREATE_TABLE_NEGOCIACION)
            return ArrayList()
        }

        var idu_negociacion: Int
        var idu_cliente:Int
        var idu_producto: Int
        var saldo_anterior: Int
        var atrasado: Int
        var moratorio: Int
        var dia_formalizacion: String
        var plazo: Int
        cursor.moveToFirst()
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                idu_negociacion = cursor.getInt(cursor.getColumnIndex("id_negociacion"))
                idu_cliente = cursor.getInt(cursor.getColumnIndex(  "id_cliente"))
                idu_producto = cursor.getInt(cursor.getColumnIndex("id_producto"))
                saldo_anterior = cursor.getInt(cursor.getColumnIndex("saldo_anterior"))
                atrasado = cursor.getInt(cursor.getColumnIndex("atrasado"))
                moratorio = cursor.getInt(cursor.getColumnIndex("moratorio"))
                dia_formalizacion = cursor.getString(cursor.getColumnIndex("dia_formalizacion"))
                plazo = cursor.getInt(cursor.getColumnIndex("plazo"))

                negociacion.add(Negociacion(
                        idu_negociacion,
                        idu_cliente,
                        idu_producto,
                        saldo_anterior,
                        atrasado,
                        moratorio,
                        dia_formalizacion,
                        plazo))
                cursor.moveToNext()
            }
        }
        return negociacion
    }

    fun readProducto(): ArrayList<Producto> {
        val producto = ArrayList<Producto>()
        val db = writableDatabase
        var cursor: Cursor?
        try {
            cursor = db.rawQuery("select * from $TABLE_PRODUCTO", null)
        } catch (e: SQLiteException) {
            db.execSQL(CREATE_TABLE_NEGOCIACION)
            return ArrayList()
        }

        var id_producto: Int
        var descripcion:String
        var descuento: Int
        cursor.moveToFirst()
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                id_producto = cursor.getInt(cursor.getColumnIndex("id_producto"))
                descripcion = cursor.getString(cursor.getColumnIndex(  "descripcion"))
                descuento = cursor.getInt(cursor.getColumnIndex("descuento"))

                producto.add(Producto(
                        descripcion,
                        descuento,
                        id_producto))
                cursor.moveToNext()
            }
        }
        return producto
    }

    fun updateAvatarCliente(avatar: String) {
        val db = this.writableDatabase

        // updating avatar in cliente table
        val values = ContentValues()
        values.put("avatar", avatar)
        db.update(TABLE_CLIENTE, values, "$KEY_ID = ?", arrayOf(avatar))

    }

    /*fun updateUser(id: Int, name: String, hobby: String, city: String) {
        val db = this.writableDatabase

        // updating name in users table
        val values = ContentValues()
        values.put(KEY_FIRSTNAME, name)
        db.update(TABLE_USER, values, "$KEY_ID = ?", arrayOf(id.toString()))

        // updating hobby in users_hobby table
        val valuesHobby = ContentValues()
        valuesHobby.put(KEY_HOBBY, hobby)
        db.update(TABLE_USER_HOBBY, valuesHobby, "$KEY_ID = ?", arrayOf(id.toString()))

        // updating city in users_city table
        val valuesCity = ContentValues()
        valuesCity.put(KEY_CITY, city)
        db.update(TABLE_USER_CITY, valuesCity, "$KEY_ID = ?", arrayOf(id.toString()))
    }*/

    /*fun deleteUSer(id: Int) {

        // delete row in students table based on id
        val db = this.writableDatabase

        //deleting from users table
        db.delete(TABLE_USER, "$KEY_ID = ?", arrayOf(id.toString()))

        //deleting from users_hobby table
        db.delete(TABLE_USER_HOBBY, "$KEY_ID = ?", arrayOf(id.toString()))

        //deleting from users_city table
        db.delete(TABLE_USER_CITY, "$KEY_ID = ?", arrayOf(id.toString()))
    }*/

}
