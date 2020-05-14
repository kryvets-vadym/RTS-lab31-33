import java.math.BigInteger
import java.util.*
import java.util.concurrent.TimeoutException

fun isSquare(num: Number) : Boolean {
    val compare: Double = Math.pow(Math.ceil(Math.sqrt(num.toDouble())), 2.0)
    return compare == num.toDouble()
}

fun BigInteger.isEven() : Boolean {
    return this.mod(2.toBigInteger()).toInt() == 0
}

fun BigInteger.isPrime() : Boolean {
    if (this <= 1.toBigInteger())
        return false
    val maxIterations = Math.sqrt(this.toDouble()).toInt() + 1
    for (i in 2 until maxIterations)
        if (this.mod(i.toBigInteger()).toInt() == 0)
            return false
    return true
}

fun twoFactors(num: BigInteger, elapsedTime: Long, maxTime: Long) : LinkedList<BigInteger> {
    var a : BigInteger = Math.ceil(Math.sqrt(num.toDouble())).toBigDecimal().toBigInteger()
    var b2 : BigInteger = a*a - num;
    while (!isSquare(b2)){
        a++
        b2 = a*a - num
        if (System.currentTimeMillis() >= maxTime)
            throw TimeoutException()
    }
    val factors : LinkedList<BigInteger> = LinkedList(listOf(
        a - Math.sqrt(b2.toDouble()).toBigDecimal().toBigInteger(),
        a + Math.sqrt(b2.toDouble()).toBigDecimal().toBigInteger())
    )
    return factors
}

fun factors(num: BigInteger, elapsedTime: Long, maxTime: Long) : LinkedList<out Number> {
    val result = LinkedList<BigInteger>()
    var operatingNum = num
    var index = 0
    while (operatingNum.mod(2.toBigInteger()).toInt() == 0 && !operatingNum.equals(1)) {
        operatingNum = operatingNum.divide(2.toBigInteger())
        result.addAll(index, listOf(2.toBigInteger(), operatingNum))
        result.removeAt(index + 1)
        index++
    }
    if (operatingNum.equals(1))
        return listToInt(result)
    result.add(operatingNum)
    var tempFactors: LinkedList<BigInteger>?
    while (index < result.size) {
        operatingNum = result.get(index)
        if (System.currentTimeMillis() >= maxTime)
            throw TimeoutException()
        if (!operatingNum.isPrime()) {
            tempFactors = twoFactors(operatingNum, elapsedTime + System.currentTimeMillis(), maxTime)
            result.addAll(index+1, tempFactors)
            result.removeAt(index)
        }
        else index++
    }
    if (hasBigDecimals(result))
        return result
    return listToInt(result)
}

fun listToInt(inputList: LinkedList<BigInteger>) : LinkedList<Int> {
    val result: LinkedList<Int> = LinkedList()
    result.addAll(inputList.map { el -> el.toInt() })
    return result
}

fun hasBigDecimals(inputList: LinkedList<BigInteger>) : Boolean {
    return inputList.filter { el -> el.bitLength() > 32 }.any()
}
