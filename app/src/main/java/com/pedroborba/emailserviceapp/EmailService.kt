package com.pedroborba.emailserviceapp

import android.R
import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.pedroborba.model.LinkedList


class EmailService : IntentService("EmailService") {
    val CHANNEL_ID = "ForegroundServiceChannel"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onHandleIntent(intent: Intent?) {

        var bundle = intent?.getParcelableExtra("myBundle") as Bundle
        var lista = bundle.getParcelable<LinkedList>("lista") as LinkedList

        lista.printarLista()
        lista.removeDuplicates()
        publicarResultado(lista)
        stopForeground(true)
        stopSelf()
    }

    override fun onCreate() {
        super.onCreate()
        //android.os.Debug.waitForDebugger();
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.sym_def_app_icon)
            .setContentTitle("Email Service App")
            .setContentText("Serviço iniciado..").build()

        startForeground(1, notification)
    }

    private fun publicarResultado(lista: LinkedList){
        var it: Intent = Intent()
        it.action = "com.pedroborba.emailclientapp.ENVIAR_USUARIO"
        var bundle = Bundle()
        bundle.putParcelable("lista", lista)

        it.putExtra("myBundle", bundle)
        sendBroadcast(it)
        println("Enviou Broadcast")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

}