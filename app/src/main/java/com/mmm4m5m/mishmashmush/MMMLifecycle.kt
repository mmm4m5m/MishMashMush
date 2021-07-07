package com.mmm4m5m.mishmashmush

//import android.media.AudioManager
//import android.media.ToneGenerator
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MMMLifecycle(lifecycle: Lifecycle) : LifecycleObserver {
    //private val WORK_INTERVAL_SECS: Long = 5

    private var handler = Handler(Looper.getMainLooper())
    var onRun: (() -> Unit)? = null
    lateinit var onDelay: () -> Long
    private var runnable: Runnable? = null

    //private lateinit var runBeep: Runnable
    //private var beepCount = 0

    //var timerSeconds: Long = 0 // seconds since last start
    //private lateinit var runTimer: Runnable

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        if (onRun != null) {
            runnable = Runnable(::onRunnable)
            handler.postDelayed(runnable!!, onDelay())
        }

        //runBeep = Runnable(::onRunBeep)

        //runTimer = Runnable(::onRunTimer)
        //handler.postDelayed(runTimer, WORK_INTERVAL_SECS*1000)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        //handler.removeCallbacks(runTimer)
        //handler.removeCallbacks(runBeep)
        if (runnable != null) handler.removeCallbacks(runnable!!)
    }

    fun onRunnable() {
        onRun?.invoke()
        handler.postDelayed(runnable!!, onDelay())
    }

    //fun beep(count: Int) {
    //    beepCount = count
    //    handler.postDelayed(runBeep, 0)
    //}

    //fun onRunBeep() {
    //    beepCount--
    //    when (beepCount.mod(2)) {
    //        0 -> ToneGenerator(AudioManager.STREAM_ALARM, 100).
    //            startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200)
    //        1 -> ToneGenerator(AudioManager.STREAM_ALARM, 100).
    //            startTone(ToneGenerator.TONE_SUP_INTERCEPT, 200)
    //    }
    //    if (beepCount == 0) return
    //    if (beepCount > 0) handler.postDelayed(runBeep, 200)
    //}

    //fun onRunTimer() {
    //    try {
    //        timerSeconds += WORK_INTERVAL_SECS
    //        //PRJTST?.Log({this}, "Working... $timerSeconds seconds") //??? todo pending
    //    } finally {
    //        handler.postDelayed(runTimer, WORK_INTERVAL_SECS*1000)
    //    }
    //}
}
