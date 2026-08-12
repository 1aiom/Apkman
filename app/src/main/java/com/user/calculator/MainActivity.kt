package com.user.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView

    private var currentInput: String = "0"
    private var storedValue: Double = 0.0
    private var pendingOperator: String? = null
    private var shouldResetInput: Boolean = false

    private val df = DecimalFormat("#,##0.##########")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tvResult)

        // أزرار الأرقام
        val numberIds = mapOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2",
            R.id.btn3 to "3", R.id.btn4 to "4", R.id.btn5 to "5",
            R.id.btn6 to "6", R.id.btn7 to "7", R.id.btn8 to "8", R.id.btn9 to "9"
        )
        numberIds.forEach { (id, value) ->
            findViewById<Button>(id).setOnClickListener { onNumberPressed(value) }
        }

        findViewById<Button>(R.id.btnDot).setOnClickListener { onDotPressed() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { onClearPressed() }
        findViewById<Button>(R.id.btnPlusMinus).setOnClickListener { onPlusMinusPressed() }
        findViewById<Button>(R.id.btnPercent).setOnClickListener { onPercentPressed() }

        findViewById<Button>(R.id.btnPlus).setOnClickListener { onOperatorPressed("+") }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { onOperatorPressed("−") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { onOperatorPressed("×") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { onOperatorPressed("÷") }

        findViewById<Button>(R.id.btnEquals).setOnClickListener { onEqualsPressed() }
    }

    private fun onNumberPressed(digit: String) {
        if (currentInput == "0" || shouldResetInput) {
            currentInput = digit
            shouldResetInput = false
        } else {
            currentInput += digit
        }
        updateDisplay()
    }

    private fun onDotPressed() {
        if (shouldResetInput) {
            currentInput = "0."
            shouldResetInput = false
        } else if (!currentInput.contains(".")) {
            currentInput += "."
        }
        updateDisplay()
    }

    private fun onClearPressed() {
        currentInput = "0"
        storedValue = 0.0
        pendingOperator = null
        shouldResetInput = false
        updateDisplay()
    }

    private fun onPlusMinusPressed() {
        val value = currentInput.toDoubleOrNull() ?: 0.0
        currentInput = formatNumber(value * -1)
        updateDisplay()
    }

    private fun onPercentPressed() {
        val value = currentInput.toDoubleOrNull() ?: 0.0
        currentInput = formatNumber(value / 100.0)
        updateDisplay()
    }

    private fun onOperatorPressed(op: String) {
        val currentValue = currentInput.toDoubleOrNull() ?: 0.0

        if (pendingOperator != null && !shouldResetInput) {
            storedValue = calculate(storedValue, currentValue, pendingOperator!!)
            currentInput = formatNumber(storedValue)
        } else {
            storedValue = currentValue
        }

        pendingOperator = op
        shouldResetInput = true
        updateDisplay()
    }

    private fun onEqualsPressed() {
        val currentValue = currentInput.toDoubleOrNull() ?: 0.0
        if (pendingOperator != null) {
            storedValue = calculate(storedValue, currentValue, pendingOperator!!)
            currentInput = formatNumber(storedValue)
            pendingOperator = null
            shouldResetInput = true
            updateDisplay()
        }
    }

    private fun calculate(a: Double, b: Double, op: String): Double {
        return when (op) {
            "+" -> a + b
            "−" -> a - b
            "×" -> a * b
            "÷" -> if (b != 0.0) a / b else Double.NaN
            else -> b
        }
    }

    private fun formatNumber(value: Double): String {
        if (value.isNaN()) return "خطأ"
        return if (value == value.toLong().toDouble()) {
            value.toLong().toString()
        } else {
            value.toString()
        }
    }

    private fun updateDisplay() {
        tvResult.text = if (currentInput == "خطأ") currentInput
            else try {
                val num = currentInput.toDouble()
                if (currentInput.endsWith(".")) currentInput
                else df.format(num)
            } catch (e: Exception) {
                currentInput
            }
    }
}
