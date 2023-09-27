import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.navArgs
import com.example.project_three.R

class QuestionsFragment : Fragment() {
    //private val args: QuestionsFragment by navArgs() // Use SafeArgs to retrieve arguments


    // Variables to keep track of the current question and correct answers
    private var currentQuestionIndex = 0
    private var correctAnswers = 0
    private lateinit var questions: List<Question>
    private lateinit var answerEditText: EditText
    private lateinit var correctSoundPlayer: MediaPlayer
    private lateinit var incorrectSoundPlayer: MediaPlayer


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

        // Initialize the MediaPlayer objects
        correctSoundPlayer = MediaPlayer.create(requireContext(), R.raw.positive)
        incorrectSoundPlayer = MediaPlayer.create(requireContext(), R.raw.negative)



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
            // Check the user's answer and move to the next question
            checkAnswer()
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                showQuestion(questions[currentQuestionIndex])
            } else {
                // All questions answered, prepare the result and return to the first activity
                val totalQuestions = questions.size
                val userScore = correctAnswers

                // Check if the user passed (80% or more correct)
                val isPassed = userScore >= (0.8 * totalQuestions)

                // Prepare the result message
                val resultMessage = if (isPassed) {
                    "You got $userScore out of $totalQuestions correct. Good Job!"
                } else {
                    "You got $userScore out of $totalQuestions correct. You need to practice more."
                }

                // Display the result message using Toast
                showToast(resultMessage)

                // Create an intent to pass the results back to the first activity
                val resultIntent = Intent()
                resultIntent.putExtra("isPassed", isPassed)
                resultIntent.putExtra("userScore", userScore)

                // Set the result code to indicate success
                requireActivity().setResult(Activity.RESULT_OK, resultIntent)

                // Finish the current activity (second activity)
                requireActivity().finish()
            }
        }
    }
    //This is the code to implement SafeArgs
    //

    /*
        // Retrieve the number of questions from SafeArgs
        val numQuestions = args.numberOfQuestions

        // Retrieve selected difficulty and operation from SafeArgs
        val difficulty = args.difficulty
        val operation = args.operation

      // Navigate to MainActivity using SafeArgs
                val action = QuestionsFragmentDirections.actionQuestionsFragmentToMainActivity(
                    isPassed = isPassed,
                    userScore = userScore,
                    numberOfQuestions = totalQuestions
                )

                findNavController().navigate(action)
     */

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
        val userAnswerText = answerEditText.text.toString()
        val correctAnswer = questions[currentQuestionIndex].correctAnswer

        // Check if the user's input is a valid integer
        val userAnswer = userAnswerText.toIntOrNull()

        if (userAnswer != null) {
            if (userAnswer == correctAnswer) {
                // Display a Toast for a correct answer and play the correct sound
                correctSoundPlayer.start()
                showToast("Correct. Good work!")
                correctAnswers++
            } else {
                // Display a Toast for a wrong answer and play the incorrect sound
                incorrectSoundPlayer.start()
                showToast("Wrong")
            }
        } else {
            // Display a Toast for an invalid input
            showToast("Invalid input. Please enter a number.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
