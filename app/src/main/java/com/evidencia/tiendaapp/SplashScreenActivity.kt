package com.evidencia.tiendaapp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.evidencia.tiendaapp.Vendedor.MainActivityVendedor

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        verBienvenida()

    }

    private fun verBienvenida() {
        //Tiempo de ejecucion de la animacion
        object : CountDownTimer(3000, 1000){
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                startActivity(Intent(applicationContext, MainActivityVendedor::class.java))
                finishAffinity()
            }
        }.start()
    }
}