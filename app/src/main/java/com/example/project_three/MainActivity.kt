import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
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

            // Navigate to the QuestionsFragment with the selected options
            val navController = findNavController(R.id.nav_host_fragment)
            navController.navigate(R.id.action_mainActivity_to_questionsFragment)

        }
    }
}
