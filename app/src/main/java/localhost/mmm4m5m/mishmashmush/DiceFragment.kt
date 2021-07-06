package localhost.mmm4m5m.mishmashmush

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import localhost.mmm4m5m.mishmashmush.databinding.FragmentDiceBinding
//import androidx.fragment.app.Fragment // custom Overrides

class DiceFragment : Fragment() {
    data class Dices(var dice1: Int = 0, var dice2: Int = 0) {
        fun isClear(): Boolean { return dice1 == 0 }
        fun clear() { dice1 = 0; dice2 = 0 }
        fun dice() { dice1 = (1..6).random(); dice2 = (1..6).random() }
        fun isLucky() = dice1 != 0 && dice1 == dice2
        fun countUp(def1: Int = 6) {
            if (isClear()) return
            if (dice2 < 6) {
                dice2++
                if (dice1 == 0) dice1 = def1
            } else if (dice1 < 6) dice1++
            else if (def1 == 0) { dice1 = 0; dice2 = 0; }
            else { dice1 = 1; dice2 = 1; }
        }
    }

    private val DRAWABLE_DICE_PREFIX = "dice_"

    //private lateinit var diceRollButton: Button
    private lateinit var binding: FragmentDiceBinding
    var guestName = ""
    val dices = Dices()
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PRJTST?.TEST_Dice == true) guestName = getString(R.string.app_name)
        dices.clear()
        //??? todo restore state - guest name after restart
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        //return inflater.inflate(R.layout.fragment_dice, container, false)
        binding = FragmentDiceBinding.inflate(inflater, container, false)

        updateLayout()

        binding.guestButton    .setOnClickListener(::onClickGuestButton)
        binding.guestText      .setOnClickListener(::onClickGuestText)
        //diceRollButton = findViewById(R.id.dice_roll_button)
        binding.diceRollButton .setOnClickListener(::onClickDiceRollButton)
        binding.diceCountButton.setOnClickListener(::onClickDiceCountUpButton)
        binding.diceClearButton.setOnClickListener(::onClickDiceClearButton)
        binding.diceWonButton  .setOnClickListener(::onClickDiceWonButton)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateSoftInput()
        //??? todo fix - show keyboard on first start
    }

    private fun getDiceResId(idx: Int) : Int {
        if (idx in 1..6) return resources.getIdentifier(DRAWABLE_DICE_PREFIX+idx, "drawable", context?.packageName)
        return R.drawable.dice_0 // to get compile-time error when renamed
    }

    private fun updateLayout() {
        binding.data = this
        updateGuest(guestName == "")
        updateDices()
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
        //??? todo use data binding
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

    private fun onClickGuestButton(view: View) {
        val name = binding.guestEdit.text.toString()
        if (name == "") return
        guestName = name //??? todo use two-way data binding
        binding.invalidateAll()
        updateGuest(false)
        updateSoftInput()
    }

    private fun onClickGuestText(view: View) {
        updateGuest(true)
        updateSoftInput()
    }

    private fun onClickDiceRollButton(view: View) {
        //??? todo rolling - disable buttons, delay, animation, sound and remove toastReShow
        toastReShow(getString(R.string.diceRolling))
        dices.dice()
        binding.invalidateAll()
        updateDices()
    }

    private fun onClickDiceCountUpButton(view: View) {
        toastReShow(getString(R.string.diceCounting))
        dices.countUp()
        binding.invalidateAll()
        updateDices()
    }

    private fun onClickDiceClearButton(view: View) {
        dices.clear()
        binding.invalidateAll()
        updateDices()
    }

    private fun onClickDiceWonButton(view: View) {
        binding.root.findNavController().navigate(R.id.gameQuestionsFragmentNav)
    }
}
