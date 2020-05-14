import java.util.*
import kotlin.collections.HashMap

const val populationSize = 16;

class Solution constructor (vararg numbers: Int) {
    private val el1 = numbers[0]
    private val el2 = numbers[1]
    private val el3 = numbers[2]
    private val el4 = numbers[3]

    val size = 4
    fun toArray() : IntArray {
        return intArrayOf(el1, el2, el3, el4)
    }

    override fun toString() : String {
        return intArrayOf(el1, el2, el3, el4).contentToString()
    }
}

fun generateRandomSolutions(y: Int) : List<Solution> {
    val r = Random();
    return Array(populationSize){Solution(*IntArray(4){ r.nextInt(Math.ceil(y.toDouble() / 2).toInt()) }) }.toList()
}

fun getDelta(coefficients: Array<Int>, y: Int, solutionToTest: Solution) : Int {
    return y - coefficients.foldIndexed(0){ i, acc, coeff -> acc + coeff*solutionToTest.toArray()[i]}
}

fun mutation(sol: Solution, mutationProbability: Double) : Solution {
    val r = Random()
    val solArr = sol.toArray()
    for (i in 0 until sol.size) {
        val rand = r.nextDouble()
        if (rand <= mutationProbability)
            solArr[i] += r.nextInt(4) - 2
    }
    return Solution(solArr[0], solArr[1], solArr[2], solArr[3])
}

fun children(parent1: Solution, parent2: Solution, mutationProbability: Double) : Array<Solution> {
    val r = Random()
    val crossingLine = r.nextInt(Math.min(parent1.size, parent2.size) - 2) + 1
    val arrayParent1 = parent1.toArray()
    val arrayParent2 = parent2.toArray()

    val arrayChild1 = arrayParent1.copyOfRange(0, crossingLine).plus(arrayParent2.copyOfRange(crossingLine, arrayParent2.size))
    val child1 = mutation(Solution(*arrayChild1), mutationProbability)
    val arrayChild2 = arrayParent2.copyOfRange(0, crossingLine).plus(arrayParent1.copyOfRange(crossingLine, arrayParent1.size))
    val child2 = mutation(Solution(*arrayChild2), mutationProbability)
    return arrayOf(child1, child2)
}

fun selectOneOfEvents(events: List<Int>, probabilities: List<Double>) : Int {
    val random = Random().nextDouble()
    val sumOfProbabilities = probabilities.toDoubleArray().mapIndexed { i, el -> el + probabilities.toDoubleArray().copyOfRange(0, i).sum() }
    val eventsSumOfProbabilities = HashMap<Double, Int>()
    for (i in 0 until Math.min(events.size, probabilities.size))
        eventsSumOfProbabilities.put(sumOfProbabilities[i], events[i])
    val bestFitting = eventsSumOfProbabilities.keys.filter { key -> key >= random}.sorted()[0]
    return eventsSumOfProbabilities[bestFitting]!!
}

fun produceNewGeneration(oldGeneration: List<Solution>, mutationProbability: Double, coefficients: Array<Int>, y: Int) : List<Solution> {
    val deltas = Array(oldGeneration.size) { i -> getDelta(coefficients, y, oldGeneration[i]) }
    val inverseDeltas = deltas.copyOf().map { el -> 1.0 / el }.toTypedArray()
    val inverseDeltasSum = inverseDeltas.sum()
    val probabilitiesToBecomeParent = inverseDeltas.copyOf().map { el -> el / inverseDeltasSum }
    val parentsToSelect = Array(oldGeneration.size) { i -> i }.toList()
    val newGeneration = ArrayList<Solution>()
    for (i in 0..oldGeneration.size) {
        val parentsToSelectTemp = ArrayList(parentsToSelect)
        val probabilitiesToBecomeParentTemp = ArrayList(probabilitiesToBecomeParent)
        val parent1Index = selectOneOfEvents(parentsToSelectTemp, probabilitiesToBecomeParentTemp)
        //parentsToSelectTemp.removeAt(parent1Index)
        //probabilitiesToBecomeParentTemp.removeAt(parent1Index)
        val parent2Index = selectOneOfEvents(parentsToSelectTemp, probabilitiesToBecomeParentTemp)
        val parent1 = oldGeneration[parent1Index]
        val parent2 = oldGeneration[parent2Index]
        val children = children(parent1, parent2, mutationProbability)
        newGeneration.addAll(children)
        if (newGeneration.size >= oldGeneration.size)
            return newGeneration.subList(0, oldGeneration.size).toList()
    }
    return newGeneration
}

fun solveEquationGeneticAlgorithm(equationCoefficients: Array<Int>, y: Int, mutationProbability: Double) : Pair<Solution, Int> {
    var oldGeneration = generateRandomSolutions(y*4)
    //println(deltasGeneration(oldGeneration, indices, y))
    var generationsCount = 1
    do {
        val newGeneration = produceNewGeneration(oldGeneration, 0.1, equationCoefficients, y)
        //println(deltasGeneration(newGeneration, indices, y))
        oldGeneration = newGeneration
        generationsCount++
    } while (newGeneration.filter { el -> getDelta(equationCoefficients, y, el) == 0}.isEmpty())
    val solution = oldGeneration.filter { el -> getDelta(equationCoefficients, y, el) == 0}[0]
    //println("Solution: $solution")
    println("Used generations: $generationsCount")
    return Pair(solution, generationsCount)
}
