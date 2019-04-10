# BakeryChallengeScala

### Environment
1. scala 2.11.12
2. sbt 1.2.8

### Getting started
1. Download the project
```
git clone https://github.com/DanyangLinda/BakeryChallengeScala.git
```
2. Run tests
```
cd BakeryChallengeScala
sbt test
```
3. Create an input file. Take "BakeryChallengeScala/input.txt" as an exmaple, and run the program 
```
sbt "run <path to the input file>"
```
4. The result are written in output.txt file.

### Error handling
If any error occurs, program with exit properly and error messages will be printed with logger.
1. Error message for invalid input format
```
Invalid input line items. The expected format is <quantity: Int> <product_code: String>
```
2. Error message for invalid product code 
```
Item code <item code> doesn't exist. Stop processing the order.
```
3. Error message for invalid product quantity
```
The quantity (<item quantity>) of item <item code> is not valid. Stop processing the order.
```
4. For system errors, stack trace will be printed
