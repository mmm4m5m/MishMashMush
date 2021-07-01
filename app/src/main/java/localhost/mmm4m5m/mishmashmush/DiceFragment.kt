package localhost.mmm4m5m.mishmashmush

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.findNavController
import localhost.mmm4m5m.mishmashmush.databinding.FragmentDiceBinding

//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DiceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiceFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    private lateinit var binding: FragmentDiceBinding
    //private lateinit var diceRollButton: Button
    private lateinit var diceFormat: String
    private var diceToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//??? todo research (and remove) - ALL fragments!
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.fragment_dice, container, false)
        //setHasOptionsMenu(true)
        binding = FragmentDiceBinding.inflate(layoutInflater)

        binding.guestButton.setOnClickListener(::onClickGuestButton)
        binding.guestText  .setOnClickListener(::onClickGuestText)
        binding.guestEdit.requestFocus()
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(binding.guestEdit, 0) //???
        if (PROJECT_TEST_DICE) {
            binding.guestEdit.setText(R.string.app_name)
            onClickGuestButton(binding.guestButton)
        }

        diceFormat = getString(R.string.dice_format)
        //diceFormat = " $diceFormat " // strings.xml trim the space. Use quotes or '\u0020'
        //diceRollButton = findViewById(R.id.dice_roll_button)
        binding.diceRollButton   .setOnClickListener(::onClickDiceRollButton)
        binding.diceCountUpButton.setOnClickListener(::onClickDiceCountUpButton)
        binding.diceClearButton  .setOnClickListener(::onClickDiceClearButton)
        binding.diceWonButton    .setOnClickListener(::onClickDiceWonButton)

        return binding.root
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment DiceFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            DiceFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }

    private fun onClickGuestButton(view: View) {
        if (binding.guestEdit.text.toString() == "") { binding.diceView.visibility = View.GONE; return }
        binding.guestText.visibility = View.VISIBLE
        binding.guestView.visibility = View.GONE
        binding.diceView .visibility = View.VISIBLE
        binding.guestText.text = binding.guestEdit.text.toString()
        //??? todo save guest name
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun onClickGuestText(view: View) {
        view             .visibility = View.GONE
        binding.guestView.visibility = View.VISIBLE
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

    private fun setDices(num: Int, num2: Int) {
        var s = num.toString()
        if ((num < 1) || (num > 6)) s = "?"
        var s2 = num2.toString()
        if ((num2 < 1) || (num2 > 6)) s2 = "?"
        if ((s == "?") && (s2 == "?")) { binding.diceText.text = getString(R.string.dice_roll)
        } else {                         binding.diceText.text = s + diceFormat + s2
        }
        binding.dice1Image.setImageResource(getDiceImage(num))
        binding.dice2Image.setImageResource(getDiceImage(num2))
        binding.diceWonButton.visibility = if (num == num2) View.VISIBLE else View.GONE
    }

    private fun onClickDiceRollButton(view: View) {
        toastShow(getString(R.string.dice_rolling))
        setDices((1..6).random(), (1..6).random())
    }

    private fun onClickDiceCountUpButton(view: View) {
        toastShow(getString(R.string.dice_counting))
        var txt = binding.diceText.text.toString()
        if (txt.length<diceFormat.length+2) txt = "0"+diceFormat+"0"
        val num  = txt[0].toString().toIntOrNull()
        val num2 = txt[diceFormat.length+1].toString().toIntOrNull()
        if ((num==null) || (num2==null)) { setDices(1, 1)
        } else if (num2<6) {               setDices(num, num2+1)
        } else if (num <6) {               setDices(num+1, 1)
        }
    }

    private fun onClickDiceClearButton(view: View) {
        setDices(0, 0)
    }

    private fun onClickDiceWonButton(view: View) {
        view.findNavController().navigate(R.id.gameQuestionsFragment)
    }
}
