package com.example.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart.setOnClickListener {
            if(etName.text?.isEmpty() == true)
                Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show()
            else {
                // Moves from this Activity to QuizQuestionActivity
                val intent = Intent(this, QuizQuestionActivity::class.java)
                // We can pass extra information using intents
                intent.putExtra(Constants.USER_NAME, etName.text.toString())
                startActivity(intent)
                finish()
            }
        }
    }
}