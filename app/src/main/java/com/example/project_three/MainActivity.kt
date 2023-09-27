import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.project_three.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Handle the Start button click to navigate to the questions screen
        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener {
            // Get selected difficulty, operation, and number of questions
            val difficultyRadioGroup = findViewById<RadioGroup>(R.id.difficultyRadioGroup)
            val selectedDifficulty = when (difficultyRadioGroup.checkedRadioButtonId) {
                R.id.easyRadioButton -> "Easy"
                R.id.mediumRadioButton -> "Medium"
                R.id.hardRadioButton -> "Hard"
                else -> "Easy" // Default to Easy if nothing is selected
            }

            val operationRadioGroup = findViewById<RadioGroup>(R.id.operationRadioGroup)
            val selectedOperation = when (operationRadioGroup.checkedRadioButtonId) {
                R.id.additionRadioButton -> "Addition"
                R.id.subtractionRadioButton -> "Subtraction"
                R.id.multiplicationRadioButton -> "Multiplication"
                R.id.divisionRadioButton -> "Division"
                else -> "Addition" // Default to Addition if nothing is selected
            }

            val numberOfQuestions = findViewById<TextView>(R.id.numberOfQuestions).text.toString().toInt()

            // Create a bundle to pass data to the QuestionsFragment
            val bundle = Bundle()
            bundle.putString("difficulty", selectedDifficulty)
            bundle.putString("operation", selectedOperation)
            bundle.putInt("numberOfQuestions", numberOfQuestions)


            //This is the safeargs implementation
            /*
            // Use SafeArgs to navigate to the QuestionsFragment
            val action = MainActivityDirections.actionMainActivityToQuestionsFragment(
                numberOfQuestions = numberOfQuestions,
                difficulty = selectedDifficulty,
                operation = selectedOperation
            )

            findNavController(R.id.nav_host_fragment).navigate(action)
        }
             */

            // Navigate to the QuestionsFragment with the selected options
            val navController = findNavController(R.id.nav_host_fragment)
            navController.navigate(R.id.action_mainActivity_to_questionsFragment, bundle)
        }

        //This is the safeargs implementation

        /*
        // Check if there are results passed from QuestionsFragment using SafeArgs
        val isPassed = args.isPassed
        val userScore = args.userScore
        val numberOfQuestions = args.numberOfQuestions
         */


        // Check if there are results passed from QuestionsFragment
        val isPassed = intent.getBooleanExtra("isPassed", false)
        val userScore = intent.getIntExtra("userScore", 0)


        // Display the results in a TextView
        val resultsTextView = findViewById<TextView>(R.id.resultsTextView)
        val numberOfQuestions = intent.getIntExtra("numberOfQuestions", 0)

        if (isPassed) {
            resultsTextView.text = "Congratulations! You passed with a score of $userScore out of $numberOfQuestions."
            resultsTextView.setTextColor(resources.getColor(android.R.color.black)) // Set text color to black for success
        } else {
            resultsTextView.text = "You need more practice. Your score is $userScore out of $numberOfQuestions."
            resultsTextView.setTextColor(resources.getColor(android.R.color.holo_red_dark)) // Set text color to red for failure
        }
    }
}
