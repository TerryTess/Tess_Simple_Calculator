package com.example.calculatorapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.calculatorapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var canAddOperation = true
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            // digits buttons
            listOf(
                button0, button1, button2, button3, button4,
                button5, button6, button7, button8, button9
            ).forEach { button ->
                button.setOnClickListener { numberAction(it) }
            }
            // operators buttons
            buttonDecimal.setOnClickListener { decimalAction(it) }

            listOf(buttonAdd, buttonSubtract, buttonMultiply, buttonDivide).forEach { button ->
                button.setOnClickListener { operationAction(it) }
            }

            // Clear, delete and equals buttons
            buttonClear.setOnClickListener { allClearAction() }
            buttonDelete.setOnClickListener { backSpaceAction() }
            buttonEquals.setOnClickListener { equalsAction(it) }
        }
    }

    fun numberAction(view: View) {
        val button = view as Button
        val buttonText = button.text.toString()
        binding.workingtextview.append(buttonText)
        canAddOperation = true
        if (buttonText == "." && canAddDecimal) {
            canAddDecimal = false
        }
    }

    fun decimalAction(view: View) {
        if (canAddDecimal) {
            binding.workingtextview.append(".")
            canAddDecimal = false
        }
    }

    fun operationAction(view: View) {
        val button = view as Button
        val buttonText = button.text.toString()
        if (canAddOperation) {
            binding.workingtextview.append(buttonText)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearAction() {
        binding.workingtextview.text = ""
        binding.resulttextview.text = ""
        canAddOperation = true
        canAddDecimal = true
    }

    fun backSpaceAction() {
        val length = binding.workingtextview.length()
        if (length > 0) {
            binding.workingtextview.text = binding.workingtextview.text.subSequence(0, length - 1)
        }
    }

    fun equalsAction(view: View) {
        val button = view as Button
        val results = button.text.toString()
        binding.resulttextview.text = calculateResults()
    }

    fun calculateResults(): String {
        val digitOperators = parseDigitOperators()
        if (digitOperators.isEmpty()) return ""

        var result = digitOperators[0] as Float

        try {
            for (i in 1 until digitOperators.size step 2) {
                val operator = digitOperators[i] as Char
                val nextDigit = digitOperators[i + 1] as Float

                result = when (operator) {
                    '+' -> result + nextDigit
                    '-' -> result - nextDigit
                    '*' -> result * nextDigit
                    '/' ->
                        {
                        if (nextDigit == 0f) throw ArithmeticException("Division by zero")
                        result / nextDigit
                    }
                    else -> throw IllegalArgumentException("Invalid Operator")
                }
            }
        } catch (e: ArithmeticException) {
            return "Error Invalid"
        } catch (e: IllegalArgumentException) {
            return "Error: Invalid 2"
        } catch (e: Exception) {
            return "Error3"
        }

        return result.toString()
    }

    fun parseDigitOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = "0"

        for (character in binding.workingtextview.text) {
            if (character.isDigit() || character == '.') {
                currentDigit += character
            } else {
            if (currentDigit.isNotEmpty()) {
                list.add(currentDigit.toFloat())
                currentDigit = "0"
            }
            list.add(character)
        }
    }

        if (currentDigit.isNotEmpty()) {
            list.add(currentDigit.toFloat())
        }

        return list
    }
    }
