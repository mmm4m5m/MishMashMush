package com.mmm4m5m.mishmashmush

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.mmm4m5m.mishmashmush.databinding.FragmentDiceBinding
//import androidx.fragment.app.Fragment // custom Overrides

class DiceFragment : Fragment() {
    private val DRAWABLE_DICE_PREFIX = "dice_"
    private val ROLLING_DELAY        = (200..800)

    private lateinit var mmmLifecycle: MMMLifecycle
    private lateinit var binding: FragmentDiceBinding
    lateinit var viewModel: DiceViewModel
    private lateinit var backPressed: OnBackPressedCallback
    private var toast: Toast? = null

    override fun onCreate(ourState: Bundle?) {
        super.onCreate(ourState)
        mmmLifecycle = MMMLifecycle(this.lifecycle).apply {
            countDownListener = ::onRolling
            countDownDelay = { ROLLING_DELAY.random().toLong() }
        }

        //if (ourState == null) return // we use viewModel
        //guestName    = ourState.getString (KEY_GUEST_NAME, guestName)
        //??? todo restore state - guest name after restart
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDiceBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(DiceViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //backPressed = object : OnBackPressedCallback(false) {
        //    override fun handleOnBackPressed() { viewModel.onBackPressed() } }
        //requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressed)
        backPressed = requireActivity().onBackPressedDispatcher
            .addCallback(this, false, { viewModel.onBackPressed() })

        //viewModel.liveGuestEditing.observe(viewLifecycleOwner, object : Observer<Boolean> {
        //    override fun onChanged(t: Boolean?) { onLiveGuestEditing() } })
        viewModel.liveGuestEditing.observe(viewLifecycleOwner, { onLiveGuestEditing() })

        if (PRJTST?.TEST_Dice == true) viewModel.guestName = getString(R.string.app_name)

        updateSoftInput() //??? todo fix - show keyboard on first start
        updateDices()

        viewModel.eventView.observe(viewLifecycleOwner, { onEventView(it) })
        if (viewModel.diceRolling != 0) mmmLifecycle.countDown()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateSoftInput() //??? todo fix - show keyboard on first start
    }

    override fun onPause() {
        super.onPause()
        toast?.cancel()
    }

    //override fun onSaveInstanceState(outState: Bundle) { // we use viewModel
    //    super.onSaveInstanceState(outState)
    //    outState.putString (KEY_GUEST_NAME, guestName)
    //}

    private fun onRolling() {
        viewModel.onRolling()
        if (viewModel.diceRolling == 0) {
            mmmLifecycle.countDownStop()
            toast?.cancel()
        } else toastReShow(getString(R.string.diceRolling))
        updateDices()
    }

    //private fun onBackPressed() {
    //    if (viewModel.guestEdit) { viewModel.guestEdit = false; return }
    //    backPressed.isEnabled = false
    //    requireActivity().onBackPressedDispatcher.onBackPressed()
    //}

    private fun onLiveGuestEditing() {
        backPressed.isEnabled = viewModel.guestEdit
    }

    private fun getDiceResId(idx: Int) : Int {
        if (idx in 1..6) return resources.getIdentifier(
            DRAWABLE_DICE_PREFIX+idx, "drawable", context?.packageName)
        return R.drawable.dice_0 // to get compile-time error if renamed
    }

    private fun updateDices() {
        //??? todo use data binding for image
        if (viewModel.dices.dice1 == 0) {
            binding.dice1Image.setImageResource(getDiceResId((1..6).random()))
            binding.dice2Image.setImageResource(getDiceResId((1..6).random()))
        } else {
            binding.dice1Image.setImageResource(getDiceResId(viewModel.dices.dice1))
            binding.dice2Image.setImageResource(getDiceResId(viewModel.dices.dice2))
        }
    }

    private fun updateSoftInput() {
        (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also {
            if (viewModel.liveGuestEditing.value == true) {
                binding.guestEdit.requestFocus()
                binding.guestEdit.selectAll()
                it.showSoftInput(binding.guestEdit, 0)
            } else it.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }

    private fun toastReShow(msg: String) {
        toast?.cancel()
        toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT).apply{ show() }
    }

    private fun onEventView(eventView: View?) {
        when (eventView) {
            binding.guestButton     -> {
                binding.guestEdit.text.toString().also { if (it != "") viewModel.guestName = it }
                updateSoftInput()
            }
            binding.guestText       -> {
                binding.guestEdit.setText(viewModel.guestName) // without data binding (EditText save its state)
                updateSoftInput()
            }
            binding.diceRollButton  -> {
                mmmLifecycle.countDown()
                onRolling()
            }
            binding.diceCountButton -> {
                toastReShow(getString(R.string.diceCounting))
                updateDices()
            }
            binding.diceClearButton -> {
                updateDices()
            }
            binding.diceWonButton   -> {
                binding.root.findNavController().navigate(R.id.gameQuestionsFragmentNav)
            }
        }
    }
}
