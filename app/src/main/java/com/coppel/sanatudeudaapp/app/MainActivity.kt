package com.coppel.sanatudeudaapp.app

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.coppel.sanatudeudaapp.R
import com.coppel.sanatudeudaapp.fragmentos.AbonosFragmento
import com.coppel.sanatudeudaapp.fragmentos.PerfilFragmento
import com.coppel.sanatudeudaapp.fragmentos.NegociacionFragmento
import com.coppel.sanatudeudaapp.utilerias.Preferencias
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import com.coppel.sanatudeudaapp.fragmentos.ViewPagerAdapter

open class MainActivity : AppCompatActivity() {

    val manager = this.supportFragmentManager
    var prevMenuItem: MenuItem? = null
    private val adapter = ViewPagerAdapter(supportFragmentManager)

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_negociacion -> {
                viewpager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_abonos -> {
                viewpager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_perfil -> {
                viewpager.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val perfilFragmento = PerfilFragmento()
        val abonosFragmento = AbonosFragmento()
        val negociacionFragmento = NegociacionFragmento()
        adapter.addFragment(negociacionFragmento)
        adapter.addFragment(abonosFragmento)
        adapter.addFragment(perfilFragmento)
        viewPager.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var preferencias = Preferencias(applicationContext)
        var check: Boolean = preferencias.sesion

        if(check){
            startActivity(Intent(this, Login::class.java))
            finish()
        }else{
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener )

            viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }
                override fun onPageSelected(position: Int) {
                    if (prevMenuItem != null) {
                        prevMenuItem!!.isChecked = false
                    } else {
                        navigation.menu.getItem(0).isChecked = false
                    }
                    //Log.d("page", "onPageSelected: $position")
                    navigation.menu.getItem(position).isChecked = true
                    prevMenuItem = navigation.menu.getItem(position)

                    when(position){
                        0->{
                            Log.d("SI","Entró a la primera")
                            (adapter.getItem(position) as NegociacionFragmento).cargarInformacion()
                        }
                    }

                }
                override fun onPageScrollStateChanged(state: Int) {
                }
            })
            setupViewPager(viewpager)

        }

    }

    @SuppressLint("ResourceAsColor")
    override fun onBackPressed() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val dialog: AlertDialog = builder.setTitle("¿Desea salir?")
                //.setMessage("¿Desea salir?")
                .setPositiveButton("Aceptar") {dialog, which ->
                    super.onBackPressed()
                    finish()
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
