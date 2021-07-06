package com.mmm4m5m.mishmashmush

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.mmm4m5m.mishmashmush.databinding.FragmentGameTitleBinding
//import androidx.fragment.app.Fragment // custom Overrides

class GameTitleFragment : Fragment() {
    private val MIMETYPE_TEXT_PLAIN = "text/plain"

    private var args: GameTitleFragmentArgs? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        //return inflater.inflate(R.layout.fragment_title, container, false)
        setHasOptionsMenu(true)
        val binding = FragmentGameTitleBinding.inflate(inflater, container, false)

        updateLayout(binding)

        binding.gamePlayButton.setOnClickListener(::onClickButton)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        //inflater.inflate(R.menu.menu_main, menu)
        if (menu.size() == 0) return
        menu.findItem(R.id.gameQuestionsMenu).isVisible = false

        menu.findItem(R.id.shareActionMenu)  .isVisible = isWin()
        menu.findItem(R.id.shareMenu)        .isVisible = menu.findItem(R.id.shareActionMenu).isVisible
        //??? todo research getShowAsAction, isActionButton - if share button then show share menu also
        //menu.findItem(R.id.shareMenu).isVisible = menu.findItem(R.id.shareActionMenu).getShowAsAction
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.shareActionMenu, R.id.shareMenu -> { startActionSend(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isWin(): Boolean {
        if (args == null) args = arguments?.let {
            if (it.isEmpty) null else GameTitleFragmentArgs.fromBundle(it)
        }
        return args != null && args?.questionsCount == args?.questionsIndex
    }

    private fun updateLayout(binding: FragmentGameTitleBinding) {
        binding.gameWinImage  .isVisible =  isWin()
        binding.gameStartImage.isVisible = !isWin()
        binding.gamePlayButton.text =
            if (isWin()) getString(R.string.gamePlayAgain)
            else getString(R.string.id_gamePlayButton)
    }

    private fun startActionSend() {
        val action = if (PRJTST?.TEST_Intent == true) Intent.ACTION_PICK else Intent.ACTION_SEND
        val intent = Intent(action)
        intent.setType(MIMETYPE_TEXT_PLAIN).putExtra(Intent.EXTRA_TEXT
            ,getString(R.string.gameWinShare, args?.questionsCount, args?.questionsIndex))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, getString(R.string.errStartActivity, Intent.ACTION_SEND)
                ,Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClickButton(view: View) {
        view.findNavController().navigate(R.id.action_gameTitle_to_gameQuestions)
    }
}
