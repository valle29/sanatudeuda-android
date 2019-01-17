package com.coppel.sanatudeudaapp.app

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.view.View

import com.coppel.sanatudeudaapp.R
import com.coppel.sanatudeudaapp.utilerias.GuardarDatos
import com.coppel.sanatudeudaapp.utilerias.Preferencias
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login.*

class Login : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth
    private var guardarDatos = GuardarDatos(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        supportActionBar?.hide()

        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.background)
        }

        auth = FirebaseAuth.getInstance()

        sign_in_button.setOnClickListener {
            login()
        }
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }

    private fun login(){

        // Reset errors.
        txt_email.error = null
        txt_password.error = null

        val email:String = txt_email.text.toString()
        val password:String = txt_password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            txt_email.error = getString(R.string.error_field_required)
            focusView = txt_email
            cancel = true
        } else if (!isEmailValid(email)) {
            txt_email.error = getString(R.string.error_invalid_email)
            focusView = txt_email
            cancel = true
        }

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            login_progress.visibility = View.VISIBLE
            login_form.visibility = View.GONE

            window.decorView.apply {
                systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
            }

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this){
                        task->

                        if(task.isSuccessful){
                            var preferencias = Preferencias(applicationContext)
                            preferencias.sesion = false
                            guardarDatos.guardarCliente(email){
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                        }
                        else{
                            login_progress.visibility = View.GONE
                            login_form.visibility = View.VISIBLE

                            Log.e("TAG", "signIn: Fail!", task.exception)

                            /*val builder = AlertDialog.Builder(this@Login)
                            builder.setTitle("Atención")
                            builder.setMessage("Error en la autenticación")
                            builder.setPositiveButton("Aceptar"){dialog,which->}
                            val dialog: AlertDialog = builder.create()
                            dialog.show()*/
                        }
                    }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}