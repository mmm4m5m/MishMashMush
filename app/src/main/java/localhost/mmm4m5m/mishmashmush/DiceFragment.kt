package localhost.mmm4m5m.mishmashmush

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import localhost.mmm4m5m.mishmashmush.databinding.FragmentDiceBinding

class DiceFragment : Fragment() {
    private lateinit var binding: FragmentDiceBinding
    //private lateinit var diceRollButton: Button
    private var dice1 = 0
    private var dice2 = 0
    private var diceToast: Toast? = null

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //return inflater.inflate(R.layout.fragment_dice, container, false)
        //setHasOptionsMenu(true)
        binding = FragmentDiceBinding.inflate(inflater, container, false)
        //??? todo state after back

        binding.guestButton.setOnClickListener(::onClickGuestButton)
        binding.guestText  .setOnClickListener(::onClickGuestText)
        binding.guestEdit.requestFocus()
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(binding.guestEdit, 0) //???
        if (PROJECT_TEST_DICE) {
            binding.guestName = getString(R.string.app_name)
            binding.executePendingBindings() //??? todo two-way data binding
            onClickGuestButton(binding.guestButton)
        }

        //diceRollButton = findViewById(R.id.dice_roll_button)
        binding.diceRollButton .setOnClickListener(::onClickDiceRollButton)
        binding.diceCountButton.setOnClickListener(::onClickDiceCountUpButton)
        binding.diceClearButton.setOnClickListener(::onClickDiceClearButton)
        binding.diceWonButton  .setOnClickListener(::onClickDiceWonButton)
        onClickDiceClearButton(binding.diceClearButton)

        return binding.root
    }

    private fun onClickGuestButton(view: View) {
        binding.guestName = binding.guestEdit.text.toString() //??? todo two-way data binding
        if (binding.guestName == "") { binding.diceView.isVisible = false; return }
        binding.guestView.isVisible = false
        binding.guestText.isVisible = true
        binding.diceView .isVisible = true
        //??? todo save guest name
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun onClickGuestText(view: View) {
        view             .isVisible = false
        binding.guestView.isVisible = true
        binding.guestEdit.requestFocus()
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(binding.guestEdit, 0)
    }

    private fun toastShow(msg: String) {
        diceToast?.cancel()
        diceToast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT)
        diceToast?.show()
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
        dice1 = num1; dice2 = num2
        if (dice2>6) { dice1++;   dice2 = 1 }
        if (dice1>6) { dice1 = 1; dice2 = 1 }
        if ((dice1>6) || (dice1==0) || (dice2==0)) {
            dice1 = 0; dice2 = 0
            binding.diceText.text = getString(R.string.id_diceText)
        } else {
            binding.diceText.text = getString(R.string.diceFormat, dice1, dice2)
        }
        binding.dice1Image.setImageResource(getDiceImage(dice1))
        binding.dice2Image.setImageResource(getDiceImage(dice2))
        binding.diceWonButton.isVisible = dice1 !=0 && dice1 == dice2
    }

    private fun onClickDiceRollButton(view: View) {
        toastShow(getString(R.string.diceRolling))
        setDices((1..6).random(), (1..6).random())
    }

    private fun onClickDiceCountUpButton(view: View) {
        toastShow(getString(R.string.diceCounting))
        setDices(dice1, dice2+1)
    }

    private fun onClickDiceClearButton(view: View) {
        setDices(0, 0)
    }

    private fun onClickDiceWonButton(view: View) {
        view.findNavController().navigate(R.id.gameQuestionsFragmentNav)
    }
}
