import java.util.Random
import java.util.function.UnaryOperator
import java.util.stream.Collectors
import java.util.stream.DoubleStream
import java.util.stream.IntStream
import kotlin.math.cos
import kotlin.math.sin

object RandomSignal {
    private const val n = 8
    private const val MAX_OMEGA = 2000
    @JvmOverloads
    fun generateRandomSignal(n: Int = RandomSignal.n): UnaryOperator<Double> {
        val r = Random()
        val amplitudes =
            IntStream.generate { r.nextInt(10) }.limit(n.toLong()).boxed()
                .collect(Collectors.toList())
        val phases =
            DoubleStream.generate { r.nextDouble() * 2 * Math.PI }.limit(n.toLong()).boxed()
                .collect(Collectors.toList())
        return UnaryOperator { t: Double ->
            var result = 0.0
            for (p in 0 until n) {
                result += amplitudes[p] * Math.sin(
                    MAX_OMEGA.toDouble() * (n - p) / n * t + phases[p]
                )
            }
            return@UnaryOperator result
        }
    }
}

fun generateResults(function: UnaryOperator<Double>, n: Int) : Array<Pair<Double,Double>> {
    val xValues = Array(n) { i -> i.toDouble() / 20 }
    val yValues = Array(n) { i -> function.apply(xValues[i]) }
    return Array(n) { i -> Pair(xValues[i], yValues[i]) }
}

fun wCoef(n: Int, p: Int, k: Int): Pair<Double, Double> {
    return Pair(cos(2*Math.PI / n * p * k), -sin(2*Math.PI / n * p * k))
}

fun discreteFourierTransform(funSamples: Array<Pair<Double, Double>>) : Array<Pair<Double, Double>> {
    val n: Int = funSamples.size
    val wCoefficientsStorage: Array<Pair<Double, Double>?> = Array((n-1)*(n-1)+1) { null }
    val resultFun = Array(n) {i -> Pair(i.toDouble(), 0.0) }
    for (p in 0 until n) {
        for (k in 0 until n) {
            var coeff = wCoefficientsStorage[p*k]
            if (coeff == null) {
                coeff = wCoef(n, p, k)
                wCoefficientsStorage[p * k] = coeff
            }
            resultFun[p] = Pair(resultFun[p].first, resultFun[p].second + coeff.second)
        }
    }
    return resultFun
}

fun fastFourierTransform(funSamples: Array<Pair<Double, Double>>) : Array<Pair<Double, Double>> {
    val n = funSamples.size
    val resultFun = Array(n) { Pair(0.0, 0.0) }
    val wCoefficientsStorage: Array<Pair<Double, Double>?> = Array((n-1)*(n-1)+1) { null }
    for (p in 0 until n) {
        for (k in 0..(n/2-1)) {
            var coeff = wCoefficientsStorage[p*k]
            if (coeff == null) {
                coeff = wCoef(n, p, k)
                wCoefficientsStorage[p * k] = coeff
            }
            resultFun[p] = Pair(resultFun[p].first, resultFun[p].second + funSamples[2*k].second * coeff.second)
        }
        for (k in 0..(n/2)-1) {
            var coeff = wCoefficientsStorage[p*(2*k+1)]
            if (coeff == null) {
                coeff = wCoef(n, p, k)
                wCoefficientsStorage[p*(2*k+1)] = coeff
            }
            resultFun[p] = Pair(resultFun[p].first, resultFun[p].second + funSamples[2*k + 1].second * coeff.second)
        }
    }
    return resultFun
}