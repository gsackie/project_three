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

            val numberOfQuestions =
                findViewById<TextView>(R.id.numberOfQuestions).text.toString().toInt()

            // Use SafeArgs to create the action and pass data to QuestionsFragment
            val action =
                MainFragmentDirections.actionMainActivityToQuestionsFragment(
                    selectedDifficulty,
                    selectedOperation,
                    numberOfQuestions
                )
            findNavController(R.id.nav_host_fragment).navigate(action)
        }
    }
}
