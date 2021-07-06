package com.mmm4m5m.mishmashmush

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.mmm4m5m.mishmashmush.databinding.FragmentGameQuestionsBinding
import kotlin.math.min
//import androidx.fragment.app.Fragment // custom Overrides

class GameQuestionsFragment : Fragment() {
    data class Question(val text: String, val answers: List<String>)

    private val STRINGS_QUESTION_PREFIX = "gameQuestion_"

    private lateinit var binding: FragmentGameQuestionsBinding
    private val questions: MutableList<Question> = mutableListOf()
    private var questionsCount = 0
    private var questionsIndex = 0
    lateinit var question: Question
    lateinit var answers: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadQuestions()
        setQuestion()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        //return inflater.inflate(R.layout.fragment_game, container, false)
        setHasOptionsMenu(true)
        binding = FragmentGameQuestionsBinding.inflate(inflater, container, false)

        binding.data = this
        updateLayout()

        binding.gameSubmitButton.setOnClickListener(::onClickSubmitButton)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        //inflater.inflate(R.menu.menu_main, menu)
        if (menu.size() == 0) return
        menu.findItem(R.id.gameQuestionsMenu).isVisible = false
    }

    private fun loadQuestions() {
        var idx = 0
        while (true) {
            idx++
            val strId = resources.getIdentifier(STRINGS_QUESTION_PREFIX+idx, "string", context?.packageName)
            if (strId == 0) break
            getString(strId).split('|').let {
                questions.add(Question(it[0], it[1].split('/')))
            }
        }
        questionsCount = min((questions.size+1) / 2, 3)
        questions.shuffle()
    }

    private fun setQuestion() {
        question = questions[questionsIndex]
        answers  = questions[questionsIndex].answers.toMutableList()
        if (PRJTST?.TEST_Game != true) answers.shuffle()
    }

    private fun updateLayout() {
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.gameQuestionTitle, questionsIndex + 1, questionsCount)
        binding.answersRadioGroup.clearCheck() // android:checkedButton="@{-1}"
        if (PRJTST?.TEST_Game == true) binding.answer1RadioButtonGame.isChecked = true
    }

    private fun onClickSubmitButton(view: View) {
        val answerIndex = when (binding.answersRadioGroup.checkedRadioButtonId) {
            //??? todo use two-way data binding
            R.id.answer1RadioButtonGame -> 0
            R.id.answer2RadioButtonGame -> 1
            R.id.answer3RadioButtonGame -> 2
            R.id.answer4RadioButtonGame -> 3
            else -> return
        }
        //val bundle = bundleOf(Pair("QUESTIONS_COUNT", QUESTIONS_COUNT),
        //                      Pair("questionsIndex" , questionsIndex))
        if (answers[answerIndex] != questions[questionsIndex].answers[0]) {
            // The first answer is the correct one.
            //view.findNavController().navigate(R.id.actionEnd_gameQuestions_to_gameTitle, bundle)
            binding.root.findNavController().navigate(GameQuestionsFragmentDirections.
                actionEndGameQuestionsToGameTitle(questionsCount, questionsIndex))
        } else if (questionsIndex >= questionsCount-1) {
            //view.findNavController().navigate(R.id.actionWin_gameQuestions_to_gameTitle, bundle)
            binding.root.findNavController().navigate(GameQuestionsFragmentDirections.
                actionWinGameQuestionsToGameTitle(questionsCount, questionsIndex + 1))
        } else {
            questionsIndex++
            setQuestion()
            updateLayout()
            binding.invalidateAll()
        }
    }
}
