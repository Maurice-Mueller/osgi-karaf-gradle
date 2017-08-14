package testingtests

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals

object SimpleSpec : Spek({
  describe("a calculator") {
    val calculator = SampleCalculator()

    on("addition") {
      val sum = calculator.sum(2, 4)
      calculator.state = 10 //for testing if the setup is clean or not

      it("should return the result of adding the first number to the second number") {
        assertEquals(6, sum)
        assertEquals(10, calculator.state)
        calculator.state = 20 //for testing if the setup is clean or not
      }
    }

    on("subtraction") {
      val subtract = calculator.subtract(4, 2)

      it("should return the result of subtracting the second number from the first number") {
        assertEquals(2, subtract)
        assertEquals(20, calculator.state) //for testing if the setup is clean or not
      }
    }
  }
})
