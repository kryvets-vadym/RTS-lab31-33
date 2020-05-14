package com.example.realtimesystemslabs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import factors
import isEven
import kotlinx.android.synthetic.main.activity_main.*
import round
import solveEquationGeneticAlgorithm
import testNetwork
import trainIterationsLimit
import twoFactors
import java.math.BigInteger
import java.util.*
import java.util.concurrent.TimeoutException
import android.os.Handler


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun calculateFactors(view: View) {
        val handler = Handler(mainLooper)
        handler.post(Runnable { waitBar.setVisibility(View.VISIBLE) })
        runOnUiThread { waitBar.setVisibility(View.VISIBLE) }
        val maxTime = 3_000 + System.currentTimeMillis()
        val inputNumber: BigInteger
        try {
            try {
                inputNumber = number3_1.text.toString().toBigInteger()
            } catch (e: java.lang.NumberFormatException) {
                Toast.makeText(this, "Number must be positive integer", Toast.LENGTH_SHORT).show()
                return
            }
            val twoFactors: LinkedList<out Number>
            val allPrimeFactors: LinkedList<out Number>
            if (inputNumber.isEven()) {
                Toast.makeText(
                    this,
                    "Can't calculate p, q for even number " + inputNumber,
                    Toast.LENGTH_LONG
                ).show()
                twoFactors = LinkedList()
                twoFactors.add(0)
                twoFactors.add(0)
            } else {
                twoFactors = twoFactors(inputNumber, System.currentTimeMillis(), maxTime)
            }
            allPrimeFactors = factors(inputNumber, System.currentTimeMillis(), maxTime)
            p3_1.setText("p: " + twoFactors[0].toString())
            q3_1.setText("q: " + twoFactors[1].toString())
            prime_factors3_1.setText("Всі прості множники: " + Arrays.toString(allPrimeFactors.toArray()))
        } catch (e: TimeoutException) {
            Toast.makeText(this, "Timeout 3 seconds exceeded!", Toast.LENGTH_LONG).show()
        } finally {
            waitBar.setVisibility(View.INVISIBLE)
        }
    }

    fun calculatePerceptron(view: View) {
        val datasetRaw = dataset3_2.text.toString()
        val dataset = datasetRaw.substring(1, datasetRaw.length - 1).replace(" ", "").split("),(")
        val datasetPoints: List<Pair<Double, Double>> = Array(dataset.size) { i -> Pair(
            dataset[i].split(",")[0].toDouble(),
            dataset[i].split(",")[1].toDouble())
        }.toList()
        val classesRaw = classes3_2.text.toString()
        val classes: List<Int>
        try {
            classes = classesRaw.replace(Regex("[, ]"), " ")
                .split(" ")
                .filter { el -> el.isNotEmpty() }
                .map { el -> el.toInt() }
        } catch (e: java.lang.NumberFormatException) {
            Toast.makeText(this, "Classes must belong to set {0, 1}", Toast.LENGTH_LONG).show()
            return
        }
        val iterationsMax = iterations3_2.text.toString().toInt()
        val threshold = threshold3_2.text.toString().toDouble()
        var resultWeights = trainIterationsLimit(datasetPoints, classes, threshold, iterationsMax)
        resultWeights = Pair(resultWeights.first.round(5), resultWeights.second.round(5))
        weights3_2.setText(resultWeights.toString())
        val testResult = testNetwork(datasetPoints, classes, resultWeights, threshold)
        results3_2.setText(Arrays.toString(testResult))
    }

    fun addPoint3_2(view: View) {
        dataset3_2.setText(dataset3_2.text.toString() + ", ( , )")
        classes3_2.setText(classes3_2.text.toString() + ", ")
    }

    fun calculateGenAlgorithm(view: View) {
        val a1: Int
        val a2: Int
        val a3: Int
        val a4: Int
        val y: Int
        try {
            a1 = a1_3_3.text.toString().toInt()
            a2 = a2_3_3.text.toString().toInt()
            a3 = a3_3_3.text.toString().toInt()
            a4 = a4_3_3.text.toString().toInt()
            y = y3_3.text.toString().toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "ai, y must be integers", Toast.LENGTH_SHORT).show()
            return
        }
        val coefficients = arrayOf(a1, a2, a3, a4)
        val result = solveEquationGeneticAlgorithm(coefficients, y, 0.1)
        val solution = result.first
        val iterations = result.second
        results3_3.setText("Результати: " + Arrays.toString(solution.toArray()))
        iterations3_3.setText("Кількість ітерацій: " + iterations)
    }
}
