package nanorstudios.ie.mathpractice.quiz.viewinterfaces

import nanorstudios.ie.mathpractice.OperatorEnum

/**
 * View interface for quizzes.
 */
interface QuizView {
    fun displayInvalidChosenNumber()
    fun callFinish()
    fun populateQuestion(firstNumber: String, operatorSymbol: String, secondNumber: String)
    fun endQuiz(chosenNumber: Int)
    fun populateCorrectAnswerButton(correctAnsButton: Int, correctAnswer: String)
    fun populateWrongAnswerButton(wrongButtonPos: Int, wrongAnswer: String)
    fun updateCompletedQuizzes(chosenNumber: Int, operator: OperatorEnum)
    fun closeActivity(chosenNumber: Int)
}