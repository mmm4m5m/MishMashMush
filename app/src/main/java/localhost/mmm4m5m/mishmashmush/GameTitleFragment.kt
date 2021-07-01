package localhost.mmm4m5m.mishmashmush

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.findNavController
import localhost.mmm4m5m.mishmashmush.databinding.FragmentGameTitleBinding

//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameTitleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameTitleFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.fragment_title, container, false)
        setHasOptionsMenu(true)
        val binding = FragmentGameTitleBinding.inflate(layoutInflater)
        binding.playButton.setOnClickListener(::onClickButton)
        val args = GameTitleFragmentArgs.fromBundle(requireArguments())
        if (args.questionsCount != args.questionsIndex) binding.titleImage.setImageResource(R.drawable.android_category_simple)
        Toast.makeText(context, "${args.questionsCount} / ${args.questionsIndex}", Toast.LENGTH_LONG).show()
        return binding.root
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment TitleFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            GameTitleFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }

    private fun getShareIntent() : Intent? {
        val args = GameTitleFragmentArgs.fromBundle(requireArguments())
        if (args.questionsCount != args.questionsIndex) return null
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT,
            "I won with %d/%d correct answers!".format(args.questionsCount, args.questionsIndex))
        return shareIntent
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        //inflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.gameQuestionsFragment).isVisible = false

        menu.findItem(R.id.share).isVisible = (getShareIntent()?.resolveActivity(requireActivity().packageManager) != null)

        //??? todo if Share.isActionButton then show menu item Share2
        //menu.findItem(R.id.share2).isVisible = menu.findItem(R.id.share).getShowAsAction
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.share -> { startActivity(getShareIntent()); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onClickButton(view: View) {
        //view.findNavController().navigate(R.id.action_gameTitleFragment_to_gameQuestionsFragment)
        view.findNavController().navigate(GameTitleFragmentDirections.actionGameTitleFragmentToGameQuestionsFragment())
    }
}
