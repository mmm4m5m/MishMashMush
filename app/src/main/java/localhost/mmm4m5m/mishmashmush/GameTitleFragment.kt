package localhost.mmm4m5m.mishmashmush

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import localhost.mmm4m5m.mishmashmush.databinding.FragmentGameTitleBinding

class GameTitleFragment : Fragment() {
    private val MIMETYPE_TEXT_PLAIN = "text/plain"

    private var args: GameTitleFragmentArgs? = null

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //return inflater.inflate(R.layout.fragment_title, container, false)
        setHasOptionsMenu(true)
        val binding = FragmentGameTitleBinding.inflate(inflater, container, false)
        binding.gamePlayButton.setOnClickListener(::onClickButton)

        val bundle = arguments
        if (bundle != null && !bundle.isEmpty) args = GameTitleFragmentArgs.fromBundle(bundle)
        binding.gameStartImage.isVisible = args == null || args?.questionsCount != args?.questionsIndex
        binding.gameWinImage  .isVisible = !binding.gameStartImage.isVisible

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu.size() == 0) return // GameActivity do not have menu
        //inflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.gameQuestionsMenu).isVisible = false

        menu.findItem(R.id.shareActionMenu)  .isVisible = args != null && args?.questionsCount == args?.questionsIndex
        menu.findItem(R.id.shareMenu)        .isVisible = menu.findItem(R.id.shareActionMenu).isVisible
        //??? todo if shareActionMenu.isActionButton then shareMenu.show
        //menu.findItem(R.id.shareMenu).isVisible = menu.findItem(R.id.shareActionMenu).getShowAsAction
    }

    private fun startActionSend() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType(MIMETYPE_TEXT_PLAIN).putExtra(Intent.EXTRA_TEXT,
            getString(R.string.gameWinShare, args?.questionsCount, args?.questionsIndex))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, getString(R.string.errStartActivity, Intent.ACTION_SEND, MIMETYPE_TEXT_PLAIN), Toast.LENGTH_SHORT).show()
            //??? todo Logger.error("Can't handle intent $intent")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.shareActionMenu, R.id.shareMenu -> { startActionSend(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onClickButton(view: View) {
        view.findNavController().navigate(R.id.action_gameTitle_to_gameQuestions)
    }
}
