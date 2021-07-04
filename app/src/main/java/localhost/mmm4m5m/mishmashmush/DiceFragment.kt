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
    private lateinit var binding: FragmentDiceBinding
    //private lateinit var diceRollButton: Button
    private var toast: Toast? = null
    private var dice1 = 0
    private var dice2 = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        //return inflater.inflate(R.layout.fragment_dice, container, false)
        binding = FragmentDiceBinding.inflate(inflater, container, false)

        //??? todo restore state - dices after back, guest name after restart
        if (PRJTST?.TEST_Dice == true) {
            binding.guestName = getString(R.string.app_name)
            binding.executePendingBindings()
            onClickGuestButton(binding.guestButton)
        } else {
            binding.guestEdit.requestFocus()
            //??? todo fix - show keyboard at start
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(binding.guestEdit, 0)
        }
        onClickDiceClearButton(binding.diceClearButton)

        binding.guestButton    .setOnClickListener(::onClickGuestButton)
        binding.guestText      .setOnClickListener(::onClickGuestText)
        //diceRollButton = findViewById(R.id.dice_roll_button)
        binding.diceRollButton .setOnClickListener(::onClickDiceRollButton)
        binding.diceCountButton.setOnClickListener(::onClickDiceCountUpButton)
        binding.diceClearButton.setOnClickListener(::onClickDiceClearButton)
        binding.diceWonButton  .setOnClickListener(::onClickDiceWonButton)
        return binding.root
    }

    private fun toastReShow(msg: String) {
        toast?.cancel()
        toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT).apply{ show() }
    }

    private fun getDiceImage(rnd: Int) : Int {
        return when (rnd) {
            1    -> R.drawable.dice_1
            2    -> R.drawable.dice_2
            3    -> R.drawable.dice_3
            4    -> R.drawable.dice_4
            5    -> R.drawable.dice_5
            6    -> R.drawable.dice_6
            else -> R.drawable.dice_0
        }
    }

    private fun setDices(num1: Int, num2: Int) {
        //??? todo move to (data) class with Increment, Check, Set, Clear, Random, IsLucky
        dice1 = num1; dice2 = num2
        if (dice2>6) { dice1++;   dice2 = 1 }
        if (dice1>6) { dice1 = 1; dice2 = 1 }
        if ((dice1>6) || (dice1==0) || (dice2==0)) {
            dice1 = 0; dice2 = 0
            binding.diceText.text = getString(R.string.id_diceText)
        } else {
            binding.diceText.text = getString(R.string.diceFormat, dice1, dice2) //??? todo use data binding
        }
        binding.dice1Image.setImageResource(getDiceImage(dice1))
        binding.dice2Image.setImageResource(getDiceImage(dice2))
        binding.diceWonButton.isVisible = dice1 !=0 && dice1 == dice2
    }

    private fun onClickGuestButton(view: View) {
        binding.guestName = binding.guestEdit.text.toString() //??? todo use two-way data binding
        if (binding.guestName == "") { binding.diceView.isVisible = false; return }
        binding.guestView.isVisible = false
        binding.guestText.isVisible = true
        binding.diceView .isVisible = true
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun onClickGuestText(view: View) {
        binding.guestText.isVisible = false
        binding.guestView.isVisible = true
        binding.guestEdit.requestFocus()
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(binding.guestEdit, 0)
    }

    private fun onClickDiceRollButton(view: View) {
        //??? todo rolling - disable buttons, delay, animation, sound and remove toastReShow
        toastReShow(getString(R.string.diceRolling))
        setDices((1..6).random(), (1..6).random())
    }

    private fun onClickDiceCountUpButton(view: View) {
        toastReShow(getString(R.string.diceCounting))
        setDices(dice1, dice2+1)
    }

    private fun onClickDiceClearButton(view: View) {
        setDices(0, 0)
    }

    private fun onClickDiceWonButton(view: View) {
        binding.root.findNavController().navigate(R.id.gameQuestionsFragmentNav)
    }
}
