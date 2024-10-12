package com.example.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    var mTotalQuestions: Int = 0
    var mCorrectAnswers: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        tvName.text = intent.getStringExtra(Constants.USER_NAME)

        mTotalQuestions = intent.getIntExtra(Constants.TOTAL_QUESTIONS, 0)
        mCorrectAnswers = intent.getIntExtra(Constants.CORRECT_ANSWERS, 0)
        tvScore.text = "Your score is $mCorrectAnswers out of $mTotalQuestions"

        // Moving back to Main Activity on clicking Finish Button
        btnFinish.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
}