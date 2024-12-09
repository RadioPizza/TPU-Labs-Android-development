# Contents

## Lab 1: Text Formatting

### Overview

This lab introduces Kotlin basics and demonstrates how to use regular expressions for text formatting. The goal is to create a program that processes an input string and applies a series of formatting transformations to improve its readability.

### Key Transformations

The program performs the following operations on the input string:

- **Quote Replacement:**  replace double quotes (`"`) with proper opening («) and closing (») quotation marks.  Opening quotes should precede words, and closing quotes should follow words (including any punctuation).

- **Space Normalization:** standardize spacing around punctuation.  Remove spaces before commas, periods, opening parentheses, etc., and add spaces after commas, periods, closing parentheses, etc.

- **Hyphen Replacement:** replace hyphens/dashes with en-dashes surrounded by spaces (unless adjacent to letters).

- **Double Space Removal:** replace multiple spaces with single spaces.

### Example Input and Output

#### Input:

```plaintext
" Лето , как обычно ,пролетело    незаметно..."-грустно сказал Ваня .Он( и его друзья )сидели на берегу речки с поэтичным названием "Стремительная ".
```

#### Output:

```plaintext
«Лето, как обычно, пролетело незаметно...» – грустно сказал Ваня. Он (и его друзья) сидели на берегу речки с поэтичным названием «Стремительная».
```

## Lab 2: Telephone Station Emulator

### Overview

This lab focuses on the fundamentals of working with **Kotlin classes and objects** and demonstrates how to implement **basic object interactions**. The goal is to create a program that simulates the operation of a simple telephone station. The program allows users to manage a list of subscribers and log calls between them.

### Key Features

The program is divided into two main components:

1. **Abonent Class**  
   Represents a subscriber with the following properties:  
   - **Name** and **Phone Number**  
   - A **call log** that stores incoming and outgoing calls.  

2. **Station Class**  
   Represents the telephone station and includes:  
   - A **list of subscribers**.  
   - The ability to **initiate calls** and log them for both the caller and the recipient.  
   - A method to **display call logs** for all subscribers.  

### Key Functionality

- **Adding Subscribers**  
  Subscribers can be added to the station using the `addAbonent()` method.  

- **Making Calls**  
  The `call(from: String, to: String)` method simulates a call from one subscriber to another:  
  - Logs an **outgoing call** in the caller's call log.  
  - Logs an **incoming call** in the recipient's call log.  

- **Displaying Call Logs**  
  The `showStat()` method displays the full call logs of all subscribers in a structured format.

### Example Program Execution

#### Code Example

```kotlin
fun main() {
    val station = Station()

    station.addAbonent(Abonent("Иван", "001"))
    station.addAbonent(Abonent("Ольга", "002"))
    station.addAbonent(Abonent("Сергей", "003"))

    station.call("Иван", "Ольга")
    station.call("Ольга", "Сергей")
    station.call("Сергей", "Иван")
    station.call("Иван", "Сергей")
    station.call("Ольга", "Иван")

    station.showStat()
}
```

#### Example Output

```plaintext
Журнал звонков абонента Иван:
    Исходящий к Ольга
    Входящий от Сергей
    Исходящий к Сергей
    Входящий от Ольга

Журнал звонков абонента Ольга:
    Входящий от Иван
    Исходящий к Сергей
    Исходящий к Иван

Журнал звонков абонента Сергей:
    Входящий от Ольга
    Исходящий к Иван
    Входящий от Иван
```

## Lab 3: Project Creation and Program Launch

- Text Input and Output, Button, and Listeners

## Lab 4: Simple Calculator

- Activity Lifecycle and GridLayout

## Lab 5: Colored Tiles

- ConstraintLayout

## Lab 6: Quadratic Equation Solver

- Resources

## Lab 7: "Edible - Inedible" Game

- State Preservation, ViewModel, LiveData

## Lab 8: Unit Converter

- Checkboxes, RadioButtons, Spinners

## Lab 9: Questionnaire

- Toast and Snackbar Messages, SeekBar

## Lab 10: Snackbar Tester

- Debugging and Unit Testing

## Lab 11: Calculation Testing

- RecyclerView Control Element

## Lab 12: Storefront

- Intents

## Lab 13: City Directory

- Fragments

## Lab 14: City Directory with Fragments

- Dialogs

## Lab 15: Shopping List

- Navigation
