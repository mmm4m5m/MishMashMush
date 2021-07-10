package com.mmm4m5m.mishmashmush

import android.media.AudioManager
import android.media.ToneGenerator
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DiceViewModel : ViewModel() {
    private val ROLLING_TURNS = 15

    private val _liveGuestName    = MutableLiveData<String>()
    private val _liveGuestEditing = MutableLiveData<Boolean>()
    private val _liveGuestEmpty   = MutableLiveData<Boolean>()
    private val _liveDiceClear    = MutableLiveData<Boolean>()
    private val _liveDiceRolling  = MutableLiveData<Boolean>()
    private val _liveDiceLucky    = MutableLiveData<Boolean>()

    val liveGuestName   : LiveData<String>  get() = _liveGuestName
    val liveGuestEditing: LiveData<Boolean> get() = _liveGuestEditing
    val liveGuestEmpty  : LiveData<Boolean> get() = _liveGuestEmpty
    val liveDiceClear   : LiveData<Boolean> get() = _liveDiceClear
    val liveDiceRolling : LiveData<Boolean> get() = _liveDiceRolling
    val liveDiceLucky   : LiveData<Boolean> get() = _liveDiceLucky

    inner class Dices() {
        private var _dice1 = 0; val dice1; get() = _dice1
        private var _dice2 = 0; val dice2; get() = _dice2
        init { clear() }
        fun clear() { _dice1 = 0; _dice2 = 0; liveData() }
        fun roll() { _dice1 = (1..6).random(); _dice2 = (1..6).random(); liveData() }
        fun isLucky() = _dice1 != 0 && _dice1 == _dice2
        fun count(checkClear: Boolean = true, defDice1: Int = 6, restart: Boolean = true) {
            if (checkClear && _dice1 == 0) return
            if (_dice2 < 6) { _dice2++; if (_dice1 == 0) _dice1 = defDice1 }
            else if (_dice1 < 6) { _dice1++; _dice2 = 1 }
            else if (restart) { _dice1 = 1; _dice2 = 1; }
            else { _dice1 = 0; _dice2 = 0; }
            liveData()
        }
        private fun liveData() {
            _liveDiceClear.value = _dice1 == 0
            _liveDiceLucky.value = isLucky()
        }
    }

    private var _guestName    = ""
    private var __guestEdit   = false
    val dices = Dices()
    private var __diceRolling = 0

    var guestName; get() = _guestName; set(it) { _guestName = it
        _liveGuestName.value = guestName
        _liveGuestEmpty.value = guestName == ""
        _liveGuestEditing.value = _guestEdit || guestName == "" }

    private var _guestEdit; get() = __guestEdit; set(it) { __guestEdit = it
        _liveGuestEditing.value = _guestEdit || guestName == "" }
    val guestEdit get() = _guestEdit

    private var _diceRolling; get() = __diceRolling; set(it) { __diceRolling = it
        _liveDiceRolling.value = _diceRolling != 0 }
    val diceRolling get() = _diceRolling

    private val _eventView = MutableLiveData<View>()
    val eventView: LiveData<View> get() = _eventView

    init {
        PRJTST?.Log({this})
        guestName    = ""
        _guestEdit   = false
        //dices = Dices()
        _diceRolling = 0
    }

    override fun onCleared() {
        PRJTST?.Log({this})
        super.onCleared()
    }

    fun onBackPressed() {
        _guestEdit = false
    }

    fun onRolling() {
        _diceRolling--
        if (_diceRolling > 0) return
        dices.roll()
        if (dices.isLucky()) ToneGenerator(AudioManager.STREAM_ALARM, 100)
            .startTone(ToneGenerator.TONE_CDMA_ALERT_INCALL_LITE, 500)
        else ToneGenerator(AudioManager.STREAM_ALARM, 100)
            .startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 150)
    }

    fun onClickGuestButton(view: View) {
        _guestEdit = false
        _eventView.apply { value = view }.apply { value = null }
    }

    fun onClickGuestText(view: View) {
        _guestEdit = true
        _eventView.apply { value = view }.apply { value = null }
    }

    fun onClickDiceRollButton(view: View) {
        if (_diceRolling == 0) dices.clear()
        _diceRolling = if (_diceRolling == 0) ROLLING_TURNS else 1
        _eventView.apply { value = view }.apply { value = null }
    }

    fun onClickDiceCountButton(view: View) {
        dices.count()
        _eventView.apply { value = view }.apply { value = null }
    }

    fun onClickDiceClearButton(view: View) {
        dices.clear()
        _eventView.apply { value = view }.apply { value = null }
    }

    fun onClickDiceWonButton(view: View) {
        _eventView.apply { value = view }.apply { value = null }
    }
}
