package localhost.mmm4m5m.mishmashmush

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import localhost.mmm4m5m.mishmashmush.databinding.FragmentGameQuestionsBinding
import kotlin.math.min

//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameQuestionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameQuestionsFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    data class Question(val text: String, val answers: List<String>)
    // The first answer is the correct one. All questions must have four answers.
    private val questions: MutableList<Question> = mutableListOf(
        Question(text = "What is Android Jetpack?",
            answers = listOf("All of these", "Tools", "Documentation", "Libraries")),
        Question(text = "What is the base class for layouts?",
            answers = listOf("ViewGroup", "ViewSet", "ViewCollection", "ViewRoot")),
        Question(text = "What layout do you use for complex screens?",
            answers = listOf("ConstraintLayout", "GridLayout", "LinearLayout", "FrameLayout")),
        Question(text = "What do you use to push structured data into a layout?",
            answers = listOf("Data binding", "Data pushing", "Set text", "An OnClick method")),
        Question(text = "What method do you use to inflate layouts in fragments?",
            answers = listOf("onCreateView()", "onActivityCreated()", "onCreateLayout()", "onInflateLayout()")),
        Question(text = "What's the build system for Android?",
            answers = listOf("Gradle", "Graddle", "Grodle", "Groyle")),
        Question(text = "Which class do you use to create a vector drawable?",
            answers = listOf("VectorDrawable", "AndroidVectorDrawable", "DrawableVector", "AndroidVector")),
        Question(text = "Which one of these is an Android navigation component?",
            answers = listOf("NavController", "NavCentral", "NavMaster", "NavSwitcher")),
        Question(text = "Which XML element lets you register an activity with the launcher activity?",
            answers = listOf("intent-filter", "app-registry", "launcher-registry", "app-launcher")),
        Question(text = "What do you use to mark a layout for data binding?",
            answers = listOf("<layout>", "<binding>", "<data-binding>", "<dbinding>"))
    )

    private lateinit var binding: FragmentGameQuestionsBinding
    private lateinit var answers: MutableList<String>
    private var questionsIndex = 0
    private val questionsCount = min((questions.size + 1) / 2, 3)

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
    ): View {
        //return inflater.inflate(R.layout.fragment_game, container, false)
        setHasOptionsMenu(true)
        binding = FragmentGameQuestionsBinding.inflate(layoutInflater)

        questions.shuffle()
        setQuestion()

        //??? binding.game = this

        binding.submitButton.setOnClickListener(::onClickSubmitButton)
        return binding.root
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment GameFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            GameQuestionsFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        //inflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.gameQuestionsFragment).isVisible = false
    }

    private fun setQuestion() {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_game_questions, questionsIndex + 1, questionsCount)
        binding.questionText.text = questions[questionsIndex].text
        answers = questions[questionsIndex].answers.toMutableList()
        if (!PROJECT_TEST_GAME) {
            answers.shuffle()
            binding.firstAnswerRadioButton .isChecked = false
        }
        binding.firstAnswerRadioButton .text = answers[0]
        binding.secondAnswerRadioButton.text = answers[1]
        binding.thirdAnswerRadioButton .text = answers[2]
        binding.fourthAnswerRadioButton.text = answers[3]
    }

    private fun onClickSubmitButton(view: View) {
        val answerIndex = when (binding.questionRadioGroup.checkedRadioButtonId) {
            R.id.firstAnswerRadioButton  -> 0
            R.id.secondAnswerRadioButton -> 1
            R.id.thirdAnswerRadioButton  -> 2
            R.id.fourthAnswerRadioButton -> 3
            else -> return
        }
        if (answers[answerIndex] != questions[questionsIndex].answers[0]) {
            // The first answer is the correct one.
            //view.findNavController().navigate(R.id.action_gameQuestionsFragment_to_gameTitleFragment_End)
            view.findNavController().navigate(GameQuestionsFragmentDirections.actionGameQuestionsFragmentToGameTitleFragmentEnd(questionsCount, questionsIndex+1))
        } else if (questionsIndex >= questionsCount-1) {
            //view.findNavController().navigate(R.id.action_gameQuestionsFragment_to_gameTitleFragment_Won)
            view.findNavController().navigate(GameQuestionsFragmentDirections.actionGameQuestionsFragmentToGameTitleFragmentWon(questionsCount, questionsIndex+1))
        } else {
            questionsIndex++
            setQuestion()
            //??? binding.invalidateAll()
        }
    }
}
