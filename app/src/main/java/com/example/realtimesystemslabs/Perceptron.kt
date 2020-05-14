import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round

/**
 * Make an [Sequence] returning elements from the iterable and saving a copy of each.
 * When the iterable is exhausted, return elements from the saved copy. Repeats indefinitely.
 *
 */
fun <T : Any> Iterable<T>.cycle(): Sequence<T> = sequence {
    val saved = mutableListOf<T>()
    for (elem in this@cycle) {
        saved.add(elem)
        yield(elem)
    }
    while (true) {
        for (elem in saved) yield(elem)
    }
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

/**
 * @param className 0 - class1, should activate; 1 - class2, shouldn't activate
 */
fun classToActivation(className: Int) : Boolean {
    return className != 0
}

fun sumator(weights: Pair<Double, Double>, x: Pair<Double, Double>) : Double {
    return weights.first*x.first + weights.second*x.second
}

fun choice(sumatorResult: Double, p: Double) : Boolean {
    return sumatorResult/*.round(10)*/ >= p
}

/**
 * @param pointClass false - class1, should activate; true - class2, shouldn't activate
 */
fun choiceCorrect(choice: Boolean, pointClass: Boolean) : Boolean {
    return pointClass.xor(choice)
}

fun trainByPoint(oldWeights: Pair<Double, Double>, point: Pair<Double, Double>, pointClass: Boolean, p: Double, learningSpeed : Double) : Pair<Double, Double> {
    val sum = sumator(oldWeights, point)
    val oldWeightsChoice = choice(sum, p)
    if (choiceCorrect(oldWeightsChoice, pointClass))
        return oldWeights
    else {
        val delta = p - sum
        val w0New = oldWeights.first + delta*learningSpeed*point.first
        val w1New = oldWeights.second + delta*learningSpeed*point.second
        return Pair(w0New, w1New)
    }
}

fun trainIterationsLimit(dataset: List<Pair<Double, Double>>, datasetClasses: List<Int>, p: Double, iterationsLimit: Int) : Pair<Double, Double> {
    val n = dataset.size
    var weights = Pair(0.0, 0.0)
    val datasetClassesBool = Array(n) { i -> classToActivation(datasetClasses[i])}
    val learningSpeed = 0.1
    for (i in 0 until iterationsLimit) {
        val point = dataset[i%n]
        val pointClass = datasetClassesBool[i%n]
        weights = trainByPoint(weights, point, pointClass, p, learningSpeed)
    }
    return weights
}

fun testNetwork(dataset: List<Pair<Double, Double>>, datasetClasses: List<Int>, weights: Pair<Double, Double>, p: Double) : Array<Boolean> {
    val result = Array(dataset.size) {false}
    for (i in 0 until dataset.size) {
        val sum = sumator(weights, dataset[i])
        val choice = choice(sum, p)
        result[i] = choiceCorrect(choice, classToActivation(datasetClasses[i]))
    }
    return result
}
