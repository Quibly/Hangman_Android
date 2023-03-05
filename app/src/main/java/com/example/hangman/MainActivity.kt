package com.example.hangman

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent.ACTION_DOWN
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var guessesRemainingView: TextView
    private lateinit var phraseView: TextView
    private lateinit var lettersGuessedView: TextView
    private lateinit var messageView: TextView
    private lateinit var guessedLetterInput: EditText

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainlayout)
        guessesRemainingView = findViewById(R.id.guessesRemaining)
        phraseView = findViewById(R.id.phrase)
        lettersGuessedView = findViewById(R.id.lettersGuessed)
        messageView = findViewById(R.id.message)
        guessedLetterInput = findViewById(R.id.guessedLetterInput)
        messageView.text = "Welcome to Hangman\n\nEnjoy your game"

        guessedLetterInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

                // Retrieve the text value from the EditText field
                if (event.action == ACTION_DOWN) {
                    val text = v.text.toString()
                    control(text)
                    // Initialize the EditText field with the retrieved value
                    guessedLetterInput.getText().clear()
                }
                // Return true to indicate that the event has been handled
                true
            } else {
                // Return false to indicate that the event has not been handled
                false
            }
        }
    }

    // Class to hold guesses, phrases, and keep track of game progress
    private class Display {
        var maxGuesses = 7
        var missedLetters = 0
        var incorrectGuesses = 1
        var phrase: String = "The sum of aegis"
        var lettersGuessed = ArrayList<String>()
    }
    private val display1 = Display()
    // Function to start the program and house control function.


    // Function that houses the progress of the program by controlling loops
    // for gameplay and checks for game completion
    @SuppressLint("SetTextI18n")
    private fun control(guess: String) {

        // While the player hasn't missed too many guesses or completed the puzzle.
        // they will be able to keep guessing
        if (display1.incorrectGuesses < display1.maxGuesses) {
            messageView.text = "Please guess a letter:"

            // When is used to determine if the guess is part of the phrase or not.
            // Returns a count of missed letters to Display.missedLetters variable
            when (guess.lowercase()) {
                in display1.phrase.lowercase() -> {
                    display1.missedLetters = correctGuess(display1, guess.lowercase())
                }
                !in display1.phrase.lowercase() -> {
                    display1.missedLetters = incorrectGuess(display1, guess.lowercase())
                }
            }
            // If the player guessed all the letters, they win.
            // Otherwise, if they miss too many guesses they lose.
            if (display1.missedLetters == 0) {
                display1.incorrectGuesses = display1.maxGuesses
                //println("You got all of the letters! You won!")
                messageView.text = "You got all of the letters! You won!"
            } else if (display1.incorrectGuesses == display1.maxGuesses) {
                //println("You hung the man. You lost at hang man. He's dead. Sorry.")
                messageView.text = "You hung the man. You lost at hang man. He's dead. Sorry."
            }
        }
    }

    // Function to create the display of data for gameplay.
    // It shows a display of the hangman graphic, guessed letters,
    // number of missed guesses left, and determines missed guesses.
    @SuppressLint("SetTextI18n")
    private fun createDisplay(display: Display): Int {
        var counter = 0
        val displayArray = arrayOf("  |  ", "  () ", "  -  ", " /|\\ ", " --- ", " || ", " __ ")
        // Loop to display the status of the hangman graphic.
        for (i in 1..display.incorrectGuesses)
            println(displayArray[i - 1])
        //println("")
        //println("")
        // Loop to display the guessed and missed letters of the puzzle.
        var phraseString = ""
        for (i in 1..display.phrase.length) {
            val letter: String = display.phrase[i - 1].toString()
            phraseString += if (letter.lowercase() in display.lettersGuessed) {
                //print(letter)
                //print(" ")
                "$letter "
            } else if (letter == " ") {
                //print(" ")
                //print(" ")
                "  "
            } else {
                ++counter
                //print("_")
                //print(" ")
                "_ "
            }
        }
        phraseView.text = phraseString
        // If the player guesses all the letters then println()
        // Otherwise, update Document.missedLetters count.
        if (counter == 0) {
            //println()
        } else {
            //println()
            display.missedLetters = counter
        }
        // Display guessed letters and number of missed guesses left.
        //print("Letters you've guessed already: ")
        var guessString = ""
        for (i in 1..display.lettersGuessed.count()) {
            //print("${display.lettersGuessed[i - 1]} ")
            guessString += "${display.lettersGuessed[i - 1]} "
        }
        lettersGuessedView.text = guessString

        //println()
        //println("You have ${display.maxGuesses - display.incorrectGuesses} missed guesses left.")
        guessesRemainingView.text = "${display.maxGuesses - display.incorrectGuesses}"
        return counter
    }

    //Function to handle a correct guess from the player.
    //It returns a number of missed letters upon completion.
    @SuppressLint("SetTextI18n")
    private fun correctGuess(display: Display, guess: String): Int {
        if (guess.lowercase() in display.lettersGuessed) {
            //println("That's a correct guess, but you already guessed it. Try a different letter")
            messageView.text = "That's a correct guess, but you already guessed it. Try a different letter"
        } else {
            display.lettersGuessed.add(guess.lowercase())
        }
        return createDisplay(display)
    }

    //Function to handle an incorrect guess from the player.
    //It returns a number of missed letters upon completion.
    @SuppressLint("SetTextI18n")
    private fun incorrectGuess(display: Display, guess: String): Int {
        if (guess in display.lettersGuessed) {
            //println("That's an incorrect guess and you already guessed it. Try a different letter")
            messageView.text = "That's an incorrect guess and you already guessed it. Try a different letter"
        } else {
            display.lettersGuessed.add(guess)
            ++display.incorrectGuesses
        }
        return createDisplay(display)
    }
}