package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_question.*

class QuizQuestionActivity : AppCompatActivity(), OnClickListener {     // Implements OnClickListener

    // Global Variables
    var mCurrentPosition: Int = 1
    var mQuestionsList: ArrayList<Question>?= null
    var mSelectedOptionPosition: Int = 0
    var mUserName: String? = null
    var mCorrectAnswers: Int = 0
    var isSubmitClicked: Boolean = false
    var isOptionClicked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        // Setting onClickListeners on each of the TextViews
        // NOTE : We could also set onClick functionality here for each of the TextView.
        //        But this will increase the code in onCreate() method which we should avoid
        tvOptionOne?.setOnClickListener(this)
        tvOptionTwo?.setOnClickListener(this)
        tvOptionThree?.setOnClickListener(this)
        tvOptionFour?.setOnClickListener(this)
        btnSubmit?.setOnClickListener(this)

        // Initializing global variables in onCreate()
        mQuestionsList = Constants.getQuestions()
        Log.d("QuestionList size is:", "${mQuestionsList!!.size}")
        mCurrentPosition = 1
        isSubmitClicked = false
        isOptionClicked = false

        // Function to set the Questions for Quiz
        setQuestion()
    }

    private fun setQuestion() {

        // Setting default properties on all TextViews initially on every new question
        setDefaultOptionsView()
        isSubmitClicked = false
        isOptionClicked = false

        // Getting the Question object from mQuestionsList at (mCurrentPosition-1)
        var questionObj: Question = mQuestionsList!![mCurrentPosition - 1]

        // Setting the views using Question Object
        tvQuestion?.text = questionObj.question
        ivImage?.setImageResource(questionObj.image)
        progressBar?.progress = mCurrentPosition-1
        tvProgress?.text = "$mCurrentPosition / ${progressBar.max + 1}"
        tvOptionOne?.text = questionObj.optionOne
        tvOptionTwo?.text = questionObj.optionTwo
        tvOptionThree?.text = questionObj.optionThree
        tvOptionFour?.text = questionObj.optionFour

        // Setting text for Submit button as "FINISH" on reaching last question otherwise "SUBMIT"
        if(mCurrentPosition == mQuestionsList!!.size) {
            btnSubmit?.text = "FINISH"
        }
        else {
            btnSubmit?.text = "SUBMIT"
        }
    }

    /**
     * A function to set default options view when the new question is loaded or when the answer is reselected.
     */
    private fun setDefaultOptionsView() {
        // Creating an Array List of TextView to store the TextViews for options
        val options = ArrayList<TextView>()

        // Adding each of the option TextViews in the ArrayList at given index
        tvOptionOne?.let {
            options.add(0, it)      // it -> tvOptionOne
        }

        tvOptionTwo?.let {
            options.add(0, it)      // it -> tvOptionTwo
        }

        tvOptionThree?.let {
            options.add(0, it)      // it -> tvOptionThree
        }

        tvOptionFour?.let {
            options.add(0, it)      // it -> tvOptionFour
        }

        // Setting default textColor, Typeface and background for each of TextViews
        for(option in options) {

            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
                )
        }
    }

    /**
     * A function to set the view of selected option view.
     */
    private fun setSelectedOptionView(textView: TextView, selectedOptionNum: Int) {

        // Setting default properties on all TextViews initially on selecting any option
        setDefaultOptionsView()

        mSelectedOptionPosition = selectedOptionNum

        // Setting textColor, Typeface and background for selected TextView
        textView.setTextColor(Color.parseColor("#363A43"))
        textView.setTypeface(textView.typeface, Typeface.BOLD)
        textView.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
        isOptionClicked = true

    }

    override fun onClick(view: View?) {


        when(view?.id) {

            // On clicking the TextViews, calling setSelectedOptionView() on it

            R.id.tvOptionOne -> {
                tvOptionOne?.let {
                    if(!isSubmitClicked)    // Check to not allow selecting options again when user clicks submit
                        setSelectedOptionView(it, 1)
                }
            }

            R.id.tvOptionTwo -> {
                tvOptionTwo?.let {
                    if(!isSubmitClicked)
                        setSelectedOptionView(it, 2)
                }
            }

            R.id.tvOptionThree -> {
                tvOptionThree?.let {
                    if(!isSubmitClicked)
                        setSelectedOptionView(it, 3)
                }
            }

            R.id.tvOptionFour -> {
                tvOptionFour?.let {
                    if(!isSubmitClicked)
                        setSelectedOptionView(it, 4)
                }
            }

            // Submit Button Functionality
            R.id.btnSubmit -> {

                isSubmitClicked = true

                // By default if none of the options are selected then mSelectedOptionPosition = 0
                if(mSelectedOptionPosition == 0) {


                    // If no argument is supplied to 'when' then the branch conditions are simply boolean expressions,
                    // and a branch is executed only when its condition is true:
                    when {
                        mCurrentPosition < mQuestionsList!!.size -> {
                            if(isOptionClicked) {   // Check to not allow clicking submit without selecting option
                                // Increase mCurrentPosition for moving to next question
                                mCurrentPosition++
                                // Keep setting questions until condition is true
                                setQuestion()
                            }
                            else {
                                Toast.makeText(this, "Please select an option!", Toast.LENGTH_SHORT).show()
                                isSubmitClicked = false
                            }
                        }

                        else -> {

//                            Toast.makeText(
//                                this,
//                                "Congrats! You made it to the end!!!",
//                                Toast.LENGTH_SHORT
//                            ).show()

                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList?.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
                else {   // Executes when user have selected any option

                    val question = mQuestionsList!![mCurrentPosition-1]

                    // If incorrect option selected by user, show with red color
                    if(mSelectedOptionPosition != question.correctAnswer) {
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    }
                    else {
                        // Increasing count of correct answers
                        mCorrectAnswers++
                    }

                    // Always show the correct option with green color after clicking submit
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)


                    // Changing the text of Submit button
                    if(mCurrentPosition == mQuestionsList!!.size) {
                        btnSubmit.text = "FINISH"
                    }
                    else {
                        btnSubmit.text = "GO TO NEXT QUESTION"
                    }

                    // Resetting the value of mSelectedOptionPosition
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    /**
     * A function for answer view which is used to highlight the answer is wrong or right.
     */
    private fun answerView(option: Int, drawableView: Int) {
        when(option) {
            1 -> {
                tvOptionOne?.let {
                    it.background = ContextCompat.getDrawable(
                        this,
                        drawableView
                    )
                }
            }

            2 -> {
                tvOptionTwo?.let {
                    it.background = ContextCompat.getDrawable(
                        this,
                        drawableView
                    )
                }
            }

            3 -> {
                tvOptionThree?.let {
                    it.background = ContextCompat.getDrawable(
                        this,
                        drawableView
                    )
                }
            }

            4 -> {
                tvOptionFour?.let {
                    it.background = ContextCompat.getDrawable(
                        this,
                        drawableView
                    )
                }
            }
        }
    }

}