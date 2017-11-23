package nanorstudios.ie.mathpractice.quiz.presenter

import android.content.Intent
import nanorstudios.ie.mathpractice.Constants
import nanorstudios.ie.mathpractice.OperatorEnum
import nanorstudios.ie.mathpractice.quiz.viewinterfaces.QuizView
import java.util.*

/**
 * Presenter implementation for quizzes.
 */
class QuizPresenterImpl(var mView: QuizView) : QuizPresenter {

    companion object {
        val PLUS = "\u002B"
        val MINUS = "\u2212"
        val MULTIPLY = "\u00D7"
        val DIVIDE = "\u00F7"
    }

    private val mStdQuizNumbers = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ,11, 12)
    private val mSubQuizNumbers = IntArray(13)
    private val mDivQuizNumbers = IntArray(13)
    private val mUsedNumbers = ArrayList<Int>(0)

    private var mCorrectAnswer: Int = -1
    private var mChosenNumber: Int = -1
    private var mRandomNumber: Int = -1
    private lateinit var mChosenOperator: OperatorEnum
    private var mOperaterSymbol = ""

    override fun extractExtras(intent: Intent) {
        if (intent.hasExtra(Constants.CHOSEN_NUMBER)) {
            mChosenNumber = intent.getIntExtra(Constants.CHOSEN_NUMBER, -1)
        }
        if (intent.hasExtra(Constants.CHOSEN_OPERATOR)) {
            mChosenOperator = intent.getSerializableExtra(Constants.CHOSEN_OPERATOR) as OperatorEnum
        }
        verifyValidChosenNumber()
    }

    private fun verifyValidChosenNumber() {
        if (mChosenNumber == -1) {
            mView.displayInvalidChosenNumber()
            mView.callFinish()
        }
    }

    override fun updateChosenNumber(chosenNumber: Int) {
        mChosenNumber = chosenNumber
    }

    override fun updateChosenOperator(chosenOperator: OperatorEnum) {
        mChosenOperator = chosenOperator
    }

    override fun initializeArrays() {
        when (mChosenOperator) {
            OperatorEnum.SUBTRACTION -> initSubtractionArray()
            OperatorEnum.DIVISION -> initDivisionArray()
        }
    }

    private fun initSubtractionArray() {
        for (i in mStdQuizNumbers.indices) {
            mSubQuizNumbers[i] = mStdQuizNumbers[i] + mChosenNumber
        }
    }

    private fun initDivisionArray() {
        for (i in mDivQuizNumbers.indices) {
            mDivQuizNumbers[i] = i * mChosenNumber
        }
    }

    private fun endQuiz() {
        mView.endQuiz(mChosenNumber)
    }

    override fun setupQuestion() {
        when (mChosenOperator) {
            OperatorEnum.ADDITION -> setupAdditionQuestion()
            OperatorEnum.SUBTRACTION -> setupSubtractionQuestion()
            OperatorEnum.MULTIPLICATION -> setupMultiplicationQuestion()
            OperatorEnum.DIVISION -> setupDivisionQuestion()
        }

        if (mRandomNumber < mCorrectAnswer) {
            mView.populateQuestion(mChosenNumber.toString(), mOperaterSymbol, mRandomNumber.toString())
        } else {
            mView.populateQuestion(mRandomNumber.toString(), mOperaterSymbol, mChosenNumber.toString())
        }
    }

    private fun setupAdditionQuestion() {
        generateRandomNumberFromStdArray()

        if (hasEndOfQuizBeenReached()) {
            endQuiz()
            return
        }
        mOperaterSymbol = PLUS
        mCorrectAnswer = mRandomNumber + mChosenNumber
    }

    private fun setupSubtractionQuestion() {
        generateRandomSubtractionNumberFromArray()

        if (hasEndOfQuizBeenReached()) {
            endQuiz()
            return
        }
        mOperaterSymbol = MINUS

        mCorrectAnswer = when {
            mRandomNumber > mChosenNumber -> mRandomNumber - mChosenNumber
            mRandomNumber < mCorrectAnswer -> mChosenNumber - mRandomNumber
            else -> mChosenNumber - mRandomNumber
        }
    }

    private fun setupMultiplicationQuestion() {
        generateRandomNumberFromStdArray()

        if (hasEndOfQuizBeenReached()) {
            endQuiz()
            return
        }

        mOperaterSymbol = MULTIPLY

        mCorrectAnswer = mRandomNumber * mChosenNumber
    }

    private fun setupDivisionQuestion() {
        generateRandomDivisionNumber()

        if (hasEndOfQuizBeenReached()) {
            endQuiz()
            return
        }

        mOperaterSymbol = DIVIDE

        mCorrectAnswer = when {
            mRandomNumber > mChosenNumber || mRandomNumber == 0 -> mRandomNumber / mChosenNumber
            mRandomNumber < mCorrectAnswer -> mChosenNumber / mRandomNumber
            else -> mChosenNumber / mRandomNumber
        }
    }

    private fun hasEndOfQuizBeenReached() : Boolean {
        return mRandomNumber == -1
    }

    private fun generateRandomNumberFromStdArray() {
        if (mUsedNumbers.size == mStdQuizNumbers.size) {
            mRandomNumber = -1
            return
        }

        mRandomNumber = Random().nextInt(mStdQuizNumbers.size)
        if (mUsedNumbers.contains(mRandomNumber)) {
            generateRandomNumberFromStdArray()
            return
        }

        mUsedNumbers.add(mRandomNumber)
    }

    private fun generateRandomSubtractionNumberFromArray() {
        if (mUsedNumbers.size == mSubQuizNumbers.size - 1) {
            mRandomNumber = -1
            return
        }

        mRandomNumber = Random().nextInt(mSubQuizNumbers[mSubQuizNumbers.size - 1] - mChosenNumber) + mChosenNumber
        if (mUsedNumbers.contains(mRandomNumber)) {
            generateRandomSubtractionNumberFromArray()
            return
        }

        mUsedNumbers.add(mRandomNumber)
    }

    private fun generateRandomDivisionNumber() {
        if (mUsedNumbers.size == mDivQuizNumbers.size - 1) {
            mRandomNumber = -1
            return
        }

        mRandomNumber = Random().nextInt(mDivQuizNumbers.size) * mChosenNumber
        if (mUsedNumbers.contains(mRandomNumber)) {
            generateRandomDivisionNumber()
            return
        }

        mUsedNumbers.add(mRandomNumber)
    }

    override fun setupButtonText() {
        val correctAnswerButton = Random().nextInt(3)

        mView.populateCorrectAnswerButton(correctAnswerButton, mCorrectAnswer.toString())

        val wrongAnswerList = ArrayList<Int>()

        for (i in 0..2) {
            if (i != correctAnswerButton) {
                var wrongAnswer = -1
                wrongAnswer = getRandomWrongAnswer(wrongAnswerList, wrongAnswer)
                wrongAnswerList.add(wrongAnswer)
                mView.populateWrongAnswerButton(i, wrongAnswer.toString())
            }
        }
    }

    private fun getRandomWrongAnswer(randomWrongAnswerList: List<Int>, wrongAnswer: Int): Int {
        var randomWrongAnswer = wrongAnswer
        while (randomWrongAnswer == -1) {
            randomWrongAnswer = Random().nextInt(mStdQuizNumbers.size)
            if (randomWrongAnswerList.contains(randomWrongAnswer) || randomWrongAnswer == mCorrectAnswer) {
                randomWrongAnswer = -1
            }
        }
        return randomWrongAnswer
    }
}