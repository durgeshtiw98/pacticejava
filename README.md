# Java Debugging Practice: Inventory

A mostly completed class with unit tests, set up like a debugging interview round.
`Inventory.java` contains 8 planted bugs. The tests catch some of them. You find the rest by reading the spec.

## Setup

Requirements: Java 17 or newer, and Maven (IntelliJ includes Maven).

**IntelliJ:** File > Open > select this folder (the one containing `pom.xml`). Wait for Maven to sync, then right-click `src/test/java` > Run 'All Tests'.

**Terminal:**
```
mvn test
```

You should see 8 tests run and 5 fail. That is expected.

## Project layout

```
java-debug-practice/
  pom.xml                                   Maven build file (JUnit 5)
  src/main/java/com/practice/
    Inventory.java                          The class to debug (spec is in the top comment)
    Product.java                            Simple data class
  src/test/java/com/practice/
    InventoryTest.java                      Starting tests (incomplete on purpose)
```

## The process

1. Read the spec comment at the top of `Inventory.java`. Turn each rule into a checklist item.
2. Run all tests. Group the failures; several may share one root cause.
3. For each failure: what was expected, what came back, which line produced it.
4. Say a hypothesis out loud before you change anything.
5. Make the smallest fix. Rerun every test.
6. For each spec rule with no test, write one. That is where the hidden bugs are.

Timebox yourself to 45 minutes and talk out loud, as you would in the interview.
