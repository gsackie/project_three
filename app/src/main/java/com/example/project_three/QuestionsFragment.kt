import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.project_three.R
import com.example.project_three.databinding.FragmentQuestionsBinding

class QuestionsFragment : Fragment() {

    private lateinit var binding: FragmentQuestionsBinding
    private var currentQuestionIndex = 0
    private var correctAnswers = 0
    private lateinit var questions: List<Question>
    private lateinit var answerEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: QuestionsFragmentArgs by navArgs()
        val numQuestions = args.numberOfQuestions

        val difficultyRadioGroup = view.findViewById<RadioGroup>(R.id.difficultyRadioGroup)
        val operationRadioGroup = view.findViewById<RadioGroup>(R.id.operationRadioGroup)

        val selectedDifficultyRadioButtonId = difficultyRadioGroup.checkedRadioButtonId
        val selectedOperationRadioButtonId = operationRadioGroup.checkedRadioButtonId

        val difficultyRadioButton = view.findViewById<RadioButton>(selectedDifficultyRadioButtonId)
        val operationRadioButton = view.findViewById<RadioButton>(selectedOperationRadioButtonId)

        val difficulty = difficultyRadioButton.text.toString().toLowerCase()
        val operation = operationRadioButton.text.toString().toLowerCase()

        answerEditText = view.findViewById<EditText>(R.id.answerEditText) ?: EditText(requireContext())

        questions = generateQuestions(difficulty, operation, numQuestions)

        showQuestion(questions[currentQuestionIndex])

        val doneButton = view.findViewById<Button>(R.id.button_done)
        doneButton.setOnClickListener {
            checkAnswer()
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                showQuestion(questions[currentQuestionIndex])
            } else {
                val scoreText = "Your score: $correctAnswers out of $numQuestions"
                val action =
                    QuestionsFragmentDirections.actionQuestionsFragmentToResultsFragment(scoreText)
                findNavController().navigate(action)
            }
        }
    }

    private fun showQuestion(question: Question) {
        binding.questionTextView.text = question.questionText
        binding.number1TextView.text = question.operand1.toString()
        binding.number2TextView.text = question.operand2.toString()
        binding.operationTextView.text = question.operation

        answerEditText.text.clear()
    }

    private fun checkAnswer() {
        val userAnswer = answerEditText.text.toString().toIntOrNull()
        val correctAnswer = questions[currentQuestionIndex].correctAnswer

        if (userAnswer != null && userAnswer == correctAnswer) {
            correctAnswers++
        }
    }

    private fun generateQuestions(
        difficulty: String?,
        operation: String?,
        numQuestions: Int
    ): List<Question> {
        val questions = mutableListOf<Question>()

        val maxOperand = when (difficulty) {
            "easy" -> 10
            "medium" -> 25
            "hard" -> 50
            else -> 10
        }

        for (i in 1..numQuestions) {
            val operand1 = (1..maxOperand).random()
            val operand2 = (1..maxOperand).random()
            val questionText = "What is $operand1 $operation $operand2?"
            val correctAnswer = when (operation) {
                "addition" -> operand1 + operand2
                "subtraction" -> operand1 - operand2
                "multiplication" -> operand1 * operand2
                "division" -> operand1 / operand2
                else -> operand1 + operand2
            }

            questions.add(Question(questionText, operand1, operand2, operation!!, correctAnswer))
        }

        return questions
    }
}

data class Question(
    val questionText: String,
    val operand1: Int,
    val operand2: Int,
    val operation: String,
    val correctAnswer: Int
)
