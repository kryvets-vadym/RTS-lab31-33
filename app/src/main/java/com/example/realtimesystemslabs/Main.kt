import java.lang.Integer.min
import java.util.*

//fun sumator(weight1: Double, weight2: Double, x1: Int, x2: Int) : Double {
//    return weight1 * x1 + weight2 * x2
//}
//
//fun activation(sum: Double, threshold: Double) : Boolean {
//    return sum > threshold
//}
//
//fun newIteration(oldWeight1: Double, oldWeight2: Double, x1: Int, x2: Int, threshold: Int, learningSpeed: Double, shouldActivate: Boolean) : Array<Double> {
//    val y = sumator(oldWeight1, oldWeight2, x1, x2)
//    val activated = y >= threshold
//    if (activated == shouldActivate)
//        return arrayOf(oldWeight1, oldWeight2)
//    val delta = threshold - y
//    val newWeight1 = oldWeight1 + delta*learningSpeed*x1
//    val newWeight2 = oldWeight2 + delta*learningSpeed*x2
//    return arrayOf(newWeight1, newWeight2)
//}

fun main() {
//    // perceptron
//    val dataset = arrayListOf(Pair(0.0, 6.0), Pair(1.0, 5.0), Pair(3.0, 3.0), Pair(2.0, 4.0))
//    val datasetClasses = arrayListOf(0, 0, 1, 1)
//    val iterationsLimit = 700
//    val p = 7.0
//    val weights = trainIterationsLimit(dataset, datasetClasses, p, iterationsLimit)
//    println(weights)
//    for (i in 0 until dataset.size) {
//        val sum = sumator(weights, dataset[i])
//        val choice = choice(sum, p)
//        println(choiceCorrect(choice, classToActivation(datasetClasses[i])))
//    }
//
//    // genetic algorithm
//    val coefficients = arrayOf(9, 1, 30, 5)
//    val y = 4
//    println("Solution ${solveEquationGeneticAlgorithm(coefficients, y, 0.1)}")
//
//    // factorization
//    println(factors(156))
}
