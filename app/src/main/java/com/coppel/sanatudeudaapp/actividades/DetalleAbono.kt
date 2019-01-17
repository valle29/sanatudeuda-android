package com.coppel.sanatudeudaapp.actividades

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.coppel.sanatudeudaapp.R
import kotlinx.android.synthetic.main.detalle_abono.*

class DetalleAbono : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle_abono)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val monto:String = intent.getStringExtra("Monto")
        txt_monto_detalle.text = monto

        supportActionBar!!.title = "Detalle abono "
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Respond to the action bar's Up/Home button
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}