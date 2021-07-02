package localhost.mmm4m5m.mishmashmush

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import localhost.mmm4m5m.mishmashmush.databinding.FragmentGameQuestionsBinding
import kotlin.math.min

class GameQuestionsFragment : Fragment() {
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
    lateinit var question: Question
    lateinit var answers: MutableList<String>
    private var questionsIndex = -1
    private val questionsCount = min((questions.size + 1) / 2, 3)

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //return inflater.inflate(R.layout.fragment_game, container, false)
        setHasOptionsMenu(true)
        binding = FragmentGameQuestionsBinding.inflate(inflater, container, false)
        //??? todo state after back

        questions.shuffle()
        setQuestion()
        binding.game = this

        binding.gameSubmitButton.setOnClickListener(::onClickSubmitButton)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu.size() == 0) return // GameActivity do not have menu
        //inflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.gameQuestionsMenu).isVisible = false
    }

    private fun setQuestion() {
        questionsIndex++
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.gameQuestionTitle, questionsIndex + 1, questionsCount)
        question = questions[questionsIndex]
        answers = questions[questionsIndex].answers.toMutableList()
        if (PROJECT_TEST_GAME) {
            binding.answer1RadioButtonGame.isChecked = true
        } else {
            answers.shuffle()
        }
    }

    private fun onClickSubmitButton(view: View) {
        val answerIndex = when (binding.answersRadioGroup.checkedRadioButtonId) { //??? todo two-way data binding
            R.id.answer1RadioButtonGame -> 0
            R.id.answer2RadioButtonGame -> 1
            R.id.answer3RadioButtonGame -> 2
            R.id.answer4RadioButtonGame -> 3
            else -> return
        }
        if (answers[answerIndex] != questions[questionsIndex].answers[0]) {
            // The first answer is the correct one.
            //view.findNavController().navigate(R.id.actionEnd_gameQuestions_to_gameTitle)
            view.findNavController().navigate(GameQuestionsFragmentDirections.actionEndGameQuestionsToGameTitle(questionsCount, questionsIndex))
        } else if (questionsIndex >= questionsCount-1) {
            //view.findNavController().navigate(R.id.actionWin_gameQuestions_to_gameTitle)
            view.findNavController().navigate(GameQuestionsFragmentDirections.actionWinGameQuestionsToGameTitle(questionsCount, questionsIndex+1))
        } else {
            setQuestion()
            binding.invalidateAll()
        }
    }
}
