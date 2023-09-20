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
import androidx.fragment.app.commit
import com.example.project_three.R

class QuestionsFragment : Fragment() {

    // Variables to keep track of the current question and correct answers
    private var currentQuestionIndex = 0
    private var correctAnswers = 0
    private lateinit var questions: List<Question>
    private lateinit var answerEditText: EditText

    // This function inflates the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_questions, container, false)
    }

    // This function is called after the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the number of questions from the fragment arguments, default to 0 if not provided
        val numQuestions = arguments?.getInt("numberOfQuestions") ?: 0

        // Find the difficulty and operation radio groups in the layout
        val difficultyRadioGroup = view.findViewById<RadioGroup>(R.id.difficultyRadioGroup)
        val operationRadioGroup = view.findViewById<RadioGroup>(R.id.operationRadioGroup)

        // Get the IDs of the selected radio buttons for difficulty and operation
        val selectedDifficultyRadioButtonId = difficultyRadioGroup.checkedRadioButtonId
        val selectedOperationRadioButtonId = operationRadioGroup.checkedRadioButtonId

        // Find the selected difficulty and operation radio buttons
        val difficultyRadioButton = view.findViewById<RadioButton>(selectedDifficultyRadioButtonId)
        val operationRadioButton = view.findViewById<RadioButton>(selectedOperationRadioButtonId)

        // Get the text of the selected difficulty and operation
        val difficulty = difficultyRadioButton.text.toString().toLowerCase()
        val operation = operationRadioButton.text.toString().toLowerCase()

        // Find the answer EditText in the layout
        answerEditText = view.findViewById<EditText>(R.id.answerEditText) ?: EditText(requireContext())

        // Generate a list of questions based on difficulty, operation, and number of questions
        questions = generateQuestions(difficulty, operation, numQuestions)

        // Show the first question
        showQuestion(questions[currentQuestionIndex])

        // Find the "Done" button and set a click listener
        val doneButton = view.findViewById<Button>(R.id.button_done)
        doneButton.setOnClickListener {
            // Check the user's answer, move to the next question, or navigate to the results screen
            checkAnswer()
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                showQuestion(questions[currentQuestionIndex])
            } else {
                // All questions answered, navigate to the result screen
                val scoreText = "Your score: $correctAnswers out of $numQuestions"

                val bundle = Bundle()
                bundle.putString("scoreText", scoreText)

                val resultsFragment = ResultsFragment()
                resultsFragment.arguments = bundle

                // Replace the current fragment with the results fragment
                requireActivity().supportFragmentManager.commit {
                    replace(R.id.questionsFragment, resultsFragment)
                    addToBackStack(null)
                }
            }
        }
    }

    // This function displays the current question on the screen
    private fun showQuestion(question: Question) {
        val questionTextView = view?.findViewById<TextView>(R.id.questionTextView)
        val number1TextView = view?.findViewById<TextView>(R.id.number1TextView)
        val number2TextView = view?.findViewById<TextView>(R.id.number2TextView)
        val operationTextView = view?.findViewById<TextView>(R.id.operationTextView)

        // Set the text for question, operands, and operation
        questionTextView?.text = question.questionText
        number1TextView?.text = question.operand1.toString()
        number2TextView?.text = question.operand2.toString()
        operationTextView?.text = question.operation

        // Clear the answer EditText for the new question
        answerEditText.text.clear()
    }

    // This function checks the user's answer and updates the correctAnswers count
    private fun checkAnswer() {
        val userAnswer = answerEditText.text.toString().toIntOrNull()
        val correctAnswer = questions[currentQuestionIndex].correctAnswer

        if (userAnswer != null && userAnswer == correctAnswer) {
            correctAnswers++
        }
    }

    // This function generates a list of questions based on difficulty, operation, and number of questions
    private fun generateQuestions(
        difficulty: String?,
        operation: String?,
        numQuestions: Int
    ): List<Question> {
        val questions = mutableListOf<Question>()

        // Define the maximum operand based on difficulty
        val maxOperand = when (difficulty) {
            "easy" -> 10
            "medium" -> 25
            "hard" -> 50
            else -> 10
        }

        // Generate questions and correct answers
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

// Data class to represent a question
data class Question(
    val questionText: String,
    val operand1: Int,
    val operand2: Int,
    val operation: String,
    val correctAnswer: Int
)
