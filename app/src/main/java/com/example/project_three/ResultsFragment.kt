import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.project_three.R

class ResultsFragment : Fragment() {

    // This function inflates the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_results, container, false)
    }

    // This function is called after the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the score text from the fragment arguments or use an empty string if not provided
        val scoreText = arguments?.getString("score") ?: ""

        // Find the TextView in the layout to display the score and set its text
        val textViewResult = view.findViewById<TextView>(R.id.textViewScore)
        textViewResult.text = scoreText

        // Find the "Do It Again" button in the layout and set a click listener
        val doItAgainButton = view.findViewById<Button>(R.id.buttonDoItAgain)
        doItAgainButton.setOnClickListener {
            // Navigate back to the main activity or initial screen when the button is clicked
            findNavController().popBackStack(R.id.mainActivity, false)
        }
    }

    // This companion object is used to create a new instance of the ResultsFragment with a score parameter
    companion object {
        fun newInstance(score: String): ResultsFragment {
            val fragment = ResultsFragment()
            val args = Bundle()

            // Put the score as an argument to pass it to the fragment
            args.putString("score", score)
            fragment.arguments = args
            return fragment
        }
    }
}
