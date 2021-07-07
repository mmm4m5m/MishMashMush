package com.mmm4m5m.mishmashmush

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.mmm4m5m.mishmashmush.databinding.FragmentDiceBinding
//import androidx.fragment.app.Fragment // custom Overrides

class DiceFragment : Fragment() {
    private val KEY_GUEST_NAME       = "GuestName" //??? todo const
    private val KEY_GUEST_EDIT       = "GuestEdit"
    private val KEY_DICE1            = "Dice1"
    private val KEY_DICE2            = "Dice2"
    private val KEY_ROLLING          = "Rolling"
    private val DRAWABLE_DICE_PREFIX = "dice_"
    private val ROLLING_TURNS        = 15
    private val ROLLING_DELAY        = (200..800)

    data class Dices(var dice1: Int = 0, var dice2: Int = 0) {
        fun isClear(): Boolean { return dice1 == 0 }
        fun clear() { dice1 = 0; dice2 = 0 }
        fun dice() { dice1 = (1..6).random(); dice2 = (1..6).random() }
        fun isLucky() = dice1 != 0 && dice1 == dice2
        fun count(def1: Int = 6) {
            if (isClear()) return
            if (dice2 < 6) {
                dice2++
                if (dice1 == 0) dice1 = def1
            } else if (dice1 < 6) dice1++
            else if (def1 == 0) { dice1 = 0; dice2 = 0; }
            else { dice1 = 1; dice2 = 1; }
        }
    }

    private lateinit var mmmLifecycle: MMMLifecycle
    //private lateinit var diceRollButton: Button
    private lateinit var binding: FragmentDiceBinding
    var guestName = ""
    var guestEdit = ""
    val dices = Dices().apply { clear() }
    var diceRolling = 0
    private var toast: Toast? = null

    override fun onCreate(ourState: Bundle?) {
        super.onCreate(ourState)
        mmmLifecycle = MMMLifecycle(this.lifecycle).apply {
            //??? todo move to constructor
            onRun = ::onRolling
            onDelay = { ROLLING_DELAY.random().toLong() }
        }

        if (PRJTST?.TEST_Dice == true) guestName = getString(R.string.app_name)

        if (ourState == null) return
        guestName   = ourState.getString(KEY_GUEST_NAME, guestName)
        guestEdit   = ourState.getString(KEY_GUEST_EDIT, guestEdit)
        dices.dice1 = ourState.getInt   (KEY_DICE1, dices.dice1)
        dices.dice2 = ourState.getInt   (KEY_DICE2, dices.dice2)
        diceRolling = ourState.getInt   (KEY_ROLLING, diceRolling)
        //??? todo restore state - guest name after restart
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        //return inflater.inflate(R.layout.fragment_dice, container, false)
        binding = FragmentDiceBinding.inflate(inflater, container, false)

        binding.data = this
        updateGuest(guestName == "" || guestEdit != "")
        updateDices()

        binding.guestButton    .setOnClickListener{onClickGuestButton()}
        binding.guestText      .setOnClickListener{onClickGuestText()}
        //diceRollButton = findViewById(R.id.dice_roll_button)
        binding.diceRollButton .setOnClickListener{onClickDiceRollButton()}
        binding.diceCountButton.setOnClickListener{onClickDiceCountButton()}
        binding.diceClearButton.setOnClickListener{onClickDiceClearButton()}
        binding.diceWonButton  .setOnClickListener{onClickDiceWonButton()}
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateSoftInput()
        //??? todo fix - show keyboard on first start
    }

    override fun onPause() {
        super.onPause()
        toast?.cancel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_GUEST_NAME, guestName)
        outState.putString(KEY_GUEST_EDIT,
            if (binding.guestEdit.isVisible) binding.guestEdit.text.toString() else "")
        outState.putInt   (KEY_DICE1     , dices.dice1)
        outState.putInt   (KEY_DICE2     , dices.dice2)
        outState.putInt   (KEY_ROLLING   , diceRolling)
    }

    private fun getDiceResId(idx: Int) : Int {
        if (idx in 1..6) return resources.getIdentifier(DRAWABLE_DICE_PREFIX+idx, "drawable", context?.packageName)
        return R.drawable.dice_0 // to get compile-time error when renamed
    }

    private fun updateGuest(edit: Boolean) {
        binding.guestView.isVisible =  edit
        binding.guestText.isVisible = !edit
        if (edit) binding.guestEdit.requestFocus()
        //binding.diceView .isVisible = guestName != ""
        //if (edit) binding.guestEdit.setText(guestName)
        //else binding.guestText.text = guestName
    }

    private fun updateDices() {
        //??? todo use data binding for image
        if (dices.dice1 == 0) {
            //binding.diceText.text = getString(R.string.id_diceText)
            val clearIsRandom = true
            binding.dice1Image.setImageResource(getDiceResId(if (clearIsRandom) (1..6).random() else 0))
            binding.dice2Image.setImageResource(getDiceResId(if (clearIsRandom) (1..6).random() else 0))
        } else {
            //binding.diceText.text = getString(R.string.diceFormat, dice1, dice2)
            binding.dice1Image.setImageResource(getDiceResId(dices.dice1))
            binding.dice2Image.setImageResource(getDiceResId(dices.dice2))
        }
        //binding.diceWonButton.isVisible = dice1 !=0 && dice1 == dice2
    }

    private fun updateSoftInput() {
        (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also {
            if (binding.guestEdit.isFocused) it.showSoftInput(binding.guestEdit, 0)
            else it.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }

    private fun toastReShow(msg: String) {
        toast?.cancel()
        toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT).apply{ show() }
    }

    private fun onClickGuestButton() {
        val name = binding.guestEdit.text.toString() //??? todo use two-way data binding
        if (name != "") {
            guestName = name
            binding.invalidateAll()
        }
        updateGuest(false)
        updateSoftInput()
    }

    private fun onClickGuestText() {
        guestEdit = guestName
        binding.invalidateAll()
        updateGuest(true)
        updateSoftInput()
    }

    private fun onRolling() {
        if (diceRolling == 0) return
        diceRolling--
        if (diceRolling == 0) {
            dices.dice()
            toast?.cancel()
            if (dices.isLucky()) ToneGenerator(AudioManager.STREAM_ALARM, 100).
                startTone(ToneGenerator.TONE_CDMA_ALERT_INCALL_LITE, 500)
            else ToneGenerator(AudioManager.STREAM_ALARM, 100).
                startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 150)
        } else {
            toastReShow(getString(R.string.diceRolling))
        }
        binding.invalidateAll()
        updateDices()
    }

    private fun onClickDiceRollButton() {
        if (diceRolling == 0) dices.clear()
        diceRolling = if (diceRolling == 0) ROLLING_TURNS else 1
        onRolling()
    }

    private fun onClickDiceCountButton() {
        toastReShow(getString(R.string.diceCounting))
        dices.count()
        binding.invalidateAll()
        updateDices()
    }

    private fun onClickDiceClearButton() {
        dices.clear()
        binding.invalidateAll()
        updateDices()
    }

    private fun onClickDiceWonButton() {
        binding.root.findNavController().navigate(R.id.gameQuestionsFragmentNav)
    }
}
