package nanorstudios.ie.mathpractice.quiz.presenter

import android.content.Intent
import nanorstudios.ie.mathpractice.OperatorEnum

/**
 * Presenter interface for quizzes.
 */
interface QuizPresenter {
    fun extractExtras(intent: Intent)
    fun updateChosenNumber(chosenNumber: Int)
    fun updateChosenOperator(chosenOperator: OperatorEnum)
    fun initializeArrays()
    fun setupQuestion()
    fun setupButtonText()
}