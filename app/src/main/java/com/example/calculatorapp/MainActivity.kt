package com.example.calculatorapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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
            listOf(
                buttonzero, button1, button2, button3, button4,
                button5, button6, button7, button8, button9
            ).forEach { button ->
                button.setOnClickListener { numberAction(it) }
            }

            listOf(buttonAdd, buttonSubtract, buttonMultiply, buttonDivide).forEach { button ->
                button.setOnClickListener { operationAction(it) }
            }

            buttonClear.setOnClickListener { allClearAction() }
            buttonDelete.setOnClickListener { backSpaceAction() }
            buttonequals.setOnClickListener { equalsAction(it) }
        }
    }

    private fun numberAction(view: View) {
        val button = view as Button
        val buttonText = button.text.toString()

        if (buttonText == "." && canAddDecimal) {
            binding.edittext1.append(buttonText)
            canAddDecimal = false
        } else if (buttonText != ".") {
            binding.edittext1.append(buttonText)
        }

        canAddOperation = true
    }

    private fun operationAction(view: View) {
        val button = view as Button
        val operator = button.text.toString()

        if (canAddOperation) {
            binding.edittext1.append(operator)
            canAddOperation = false
            canAddDecimal = true
        }
    }


    fun allClearAction() {
        binding.edittext1.text.clear()
        binding.resulttextview.text = ""
        canAddOperation = true
        canAddDecimal = true
    }

    fun backSpaceAction() {
        val length = binding.edittext1.length()
        if (length > 0) {
            binding.edittext1.text.replace(   0, length,
                binding.edittext1.text.subSequence(0, length - 1)
            )
        }
    }

    fun equalsAction(view: View) {
        binding.resulttextview.text = performCalculation()
    }


    private fun performCalculation(): String {
        val elements = parseDigitOperators()
        if (elements.isEmpty()) return ""

        var result = elements[0] as Float

        try {
            for (i in 1 until elements.size step 2) {
                val operator = elements[i] as Char
                val nextNumber = elements[i + 1] as Float

                result = when (operator) {
                    '+' -> result + nextNumber
                    '-' -> result - nextNumber
                    'x' -> result * nextNumber
                    '/' -> {
                        if (nextNumber == 0f) throw ArithmeticException("Division by zero")
                        result / nextNumber
                    }
                    else -> throw IllegalArgumentException("Invalid Operator: $operator")
                }
            }
        } catch (e: ArithmeticException) {
            return "Error: Division by zero"
        } catch (e: IllegalArgumentException) {
            return "Error: Invalid operator"
        } catch (e: Exception) {
            return "Error: Unknown error occurred"
        }

        return result.toString()
    }



    private fun parseDigitOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""

        val text = binding.edittext1.text.toString()

        for (character in text) {
            if (character.isDigit() || character == '.') {
                currentDigit += character
            } else {
                if (currentDigit.isNotEmpty()) {
                    list.add(currentDigit.toFloat())
                    currentDigit = ""
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