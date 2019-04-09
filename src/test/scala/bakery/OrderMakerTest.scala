package bakery

import java.security.InvalidParameterException

import org.scalatest.FunSuite

class OrderMakerTest extends FunSuite {
  test("'getThePackNumbers' should work for a array of two elements.") {
    val testArray = Array[Int](5,3)
    val target = 10
    import OrderMaker.getThePackNumbers
    val result = getThePackNumbers(target, testArray)
    val expected = Array[Int](2,0)
    assert(result sameElements expected)
  }

  test("'getThePackNumbers' should work for the case where the number in the middle " +
    "cannot contribute to the target.") {
    val testArray = Array[Int](8,5,2)
    val target = 14
    import OrderMaker.getThePackNumbers
    val result = getThePackNumbers(target, testArray)
    val expected = Array[Int](1,0,3)
    assert(result sameElements expected)
  }

  test("'getThePackNumbers' should work for the case where largest number in the array " +
    "cannot contribute to the target.") {
    val testArray = Array[Int](9,5,3)
    val target = 13
    import OrderMaker.getThePackNumbers
    val result = getThePackNumbers(target, testArray)
    val expected = Array[Int](0,2,1)
    assert(result sameElements expected)
  }

  test("'getThePackNumbers' should work for the case where only the smallest value can " +
    "contribute to the target.") {
    val testArray = Array[Int](9,8,7)
    val target = 21
    import OrderMaker.getThePackNumbers
    val result = getThePackNumbers(target, testArray)
    val expected = Array[Int](0,0,3)
    assert(result sameElements expected)
  }

  test("'getThePackNumbers' should work for the edge case where none of the element in the array " +
    "can contribute to the target.") {
    val testArray = Array[Int](8,5,3)
    val target = 17
    import OrderMaker.getThePackNumbers
    val result = getThePackNumbers(target, testArray)
    val expected = Array[Int](0,0,0)
    assert(result sameElements expected)
  }

  test("'getThePackNumbers' should work for a array of four elements.") {
    val testArray = Array[Int](10,8,7,3)
    val target = 9
    import OrderMaker.getThePackNumbers
    val result = getThePackNumbers(target, testArray)
    val expected = Array[Int](0,0,0,3)
    assert(result sameElements expected)
  }

  test("'processOrder' should work for the example input.") {
    val item1 = OrderItem(10, "VS5")
    val item2 = OrderItem(14, "MB11")
    val item3 = OrderItem(13, "CF")
    val order = Array[OrderItem](item1,item2,item3)
    import OrderMaker.processOrder
    val outputStr = processOrder(order)
    val expectedOutput = "10 VS5 $17.98\n" +
      "    2 x 5 $8.99\n" +
      "14 MB11 $84.80\n" +
      "    1 x 8 $9.95\n" +
      "    3 x 2 $24.95\n" +
      "13 CF $36.89\n" +
      "    2 x 5 $9.95\n" +
      "    1 x 3 $16.99"
    assert(outputStr.equals(expectedOutput))
  }

  test("'processOrder' should handle invalid product code.") {
    val order = Array[OrderItem](OrderItem(10, "Dummy"))
    import OrderMaker.processOrder
    val thrown = intercept[InvalidParameterException] {
      processOrder(order)
    }

    assert(thrown.getMessage.equals("Item code Dummy doesn't exist. Stop processing the order."))
  }

  test("'processOrder' should handle invalid product quantity.") {
    val order = Array[OrderItem](OrderItem(11, "VS5"))
    import OrderMaker.processOrder
    val thrown = intercept[InvalidParameterException] {
      processOrder(order)
    }

    assert(thrown.getMessage.equals("The quantity (11) of item VS5 is not valid. Stop processing the order."))
  }
}
