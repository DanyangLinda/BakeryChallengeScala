package bakery

import java.io.{File, PrintWriter}
import java.security.InvalidParameterException

import org.slf4j.{Logger, LoggerFactory}

import scala.io.Source

case class Product(name: String, code: String, packSize: Array[Int], packCost: Array[Double])
case class OrderItem(quantity: Int, code: String)

object OrderMaker {
  val logger: Logger = LoggerFactory.getLogger(OrderMaker.getClass)
  /*the elements in packSize array are pre-sorted and share
  **the same index with the correspond elements in packCost array*/
  val product1 = Product("Vegemite Scroll", "VS5", Array(5, 3), Array(8.99, 6.99))
  val product2 = Product("Blueberry Muffin", "MB11", Array(8, 5, 2), Array(9.95, 16.95, 24.95))
  val product3 = Product("Croissant", "CF", Array(9, 5, 3), Array(5.95, 9.95, 16.99))
  val products = List(product1, product2, product3)

  def main(args: Array[String]): Unit = {
    var writer:PrintWriter = null

    try {
      if(args.isEmpty) {
        throw new InvalidParameterException("Please specify the path of input file")
      }
      val order: Array[OrderItem] = parseOrder(args.head)
      //val order = Array[OrderItem](item1, item2, item3)
      val output = processOrder(order)
      writer = new PrintWriter(new File("output.txt"))
      writer.write(output)
      writer.close()
    } catch {
      case e: InvalidParameterException => logger.error(e.getMessage)
      case e: Exception => e.printStackTrace()
    } finally {
      if(writer!=null) {
        writer.close()
      }
    }
  }


  def parseOrder(inputFile: String): Array[OrderItem] ={
    var order = Array[OrderItem]()
    for (line <- Source.fromFile(inputFile).getLines) {
      val rawItem = line.split(" ")
      if(rawItem.size!=2 || !isInt(rawItem(0))) {
        throw new InvalidParameterException("Invalid input line items. The expected format is <quantity: Int> <product_code: String>")
      } else {
        order = order:+OrderItem(rawItem(0).toInt, rawItem(1))
      }
    }
    order
  }

  def isInt(str: String): Boolean = {
    try{
      str.toInt
      true
    } catch {
      case e: Exception => false
    }
  }

  def processOrder(order: Array[OrderItem]): String = {
    var outputArr = Array[String]()

    for(item <- order) {
      val productOption = products.find(_.code == item.code)
      if(productOption.isDefined) {
        val product = productOption.get
        val packNumbers = getThePackNumbers(item.quantity, product.packSize)
        if(packNumbers.sum == 0) { //Stop processing the order if no pack combination found
          throw new InvalidParameterException(s"The quantity (${item.quantity}) of item ${item.code} is not valid. Stop processing the order.")
        } else {
          val resultStr = createResultStr(packNumbers, product, item.quantity)
          outputArr = outputArr:+resultStr
        }
      } else { //Stop processing the order if the product code is not defined
        throw new InvalidParameterException(s"Item code ${item.code} doesn't exist. Stop processing the order.")
      }
    }
    outputArr.mkString("\n")
  }

  def createResultStr(packNumbers: Array[Int], product: Product, quantity: Int): String = {
    var strArray = Array[String]()
    var totalCost: Double = 0.0
    for(i <- packNumbers.indices) {
      if(packNumbers(i) != 0) {
        totalCost+=packNumbers(i)*product.packCost(i)
        strArray = strArray:+"    %d x %s $%1.2f".format(packNumbers(i), product.packSize(i), product.packCost(i))
      }
    }
    strArray.+:("%d %s $%1.2f".format(quantity, product.code, totalCost)).mkString("\n")
  }

  def hasReminder(x: Int, nums: Array[Int]): Boolean = {

    val n = nums.head

    if(x%n == 0) return false

    if(nums.length == 1) return true

    hasReminder(x%n, nums.tail)
  }

  def getThePackNumbers(target: Int, nums: Array[Int]): Array[Int] = {
    val len = nums.length
    var continue = true
    var i = 0
    var result:Array[Int] = null

    while(i < len && continue) {
      result = new Array[Int](len)
      var reminder = target%nums(i)

      if(reminder == 0) {
        result(i) = target/nums(i)
        continue = false
      } else {
        var j = i+1
        while(j < len && continue) {
          if (!hasReminder(reminder, nums.slice(j, len))) {
            result(i) = target/nums(i)
            for(m <- j until len) {
              result.update(m, reminder/nums(m))
              reminder = reminder%nums(m)
            }
            continue = false
          }
          j+=1
        }
      }

      i+=1

    }

    result
  }
}
