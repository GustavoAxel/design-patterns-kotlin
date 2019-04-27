# Design Patterns

## Contents
* [Creational Patterns](#creational)
    * [Factory Method](#factory-method)
    * [Abstract Factory](#abstract-factory)
    * [Singleton](#singleton)
	* [Builder / Assembler](#builder--assembler)
* [Structural Patterns](#structural)
	* [Adapter](#adapter)
	* [Decorator](#decorator)
	* [Facade](#facade)
	* [Protection Proxy](#protection-proxy)
	* [Composite](#composite)

Creational
==========

[Factory Method](/patterns/src/test/kotlin/FactoryMethod.kt)
-----------------

The factory pattern is used to replace class constructors, abstracting the process of object generation so that the type of the object instantiated can be determined at run-time.

#### Example

```kotlin
sealed class Country {
    object Mexico : Country()
}

object UnitedStatesOfAmerica : Country()
class Germany(val someProperty: String = "") : Country()
data class Canada(val someProperty: String) : Country()
//object France : Country()

class Beer(
        val type: String
)

class BeerFactory {

    fun beerForCountry(country: Country): Beer =
            when (country) {
                is Country.Mexico -> Beer("Corona")
                is UnitedStatesOfAmerica -> Beer("Budlight")
                is Germany -> Beer("Paulaner")
                is Canada -> Beer("Molson")
            }
}
```

#### Usage

```kotlin
val mexicanBeer = BeerFactory().beerForCountry(Country.Mexico).type
println("Mexican beer: $mexicanBeer")

val germanBeer = BeerFactory().beerForCountry(Germany()).type
println("German beer: $germanBeer")
        
```

#### Output

```
Mexican beer: Corona
German beer: Paulaner
```

[Abstract Factory](/patterns/src/test/kotlin/AbstractFactory.kt)
-------------------

The abstract factory pattern is used to provide a client with a set of related or dependant objects.
The "family" of objects created by the factory are determined at run-time.

#### Example

```kotlin
interface Shape

class Circle : Shape
class Square : Shape

class RedCircle : Shape
class BlueCircle : Shape
class RedSquare : Shape
class BlueSquare : Shape

enum class Colors {
    RED, BLUE
}

abstract class ShapeFactory {

    abstract fun makeShape(color: Colors): Shape

    companion object {
        inline fun <reified T : Shape> createFactory(): ShapeFactory =
                when (T::class) {
                    Circle::class -> CircleFactory()
                    Square::class -> SquareFactory()
                    else -> throw IllegalArgumentException()
                }
    }
}

class CircleFactory : ShapeFactory() {
    override fun makeShape(color: Colors): Shape = when (color) {
        Colors.RED -> RedCircle()
        Colors.BLUE -> BlueCircle()
    }
}

class SquareFactory : ShapeFactory() {
    override fun makeShape(color: Colors): Shape = when (color) {
        Colors.RED -> RedSquare()
        Colors.BLUE -> BlueSquare()
    }
}
```

#### Usage

```kotlin
val shapeFactory = ShapeFactory.createFactory<Circle>()
val shape = shapeFactory.makeShape(Colors.RED)
println("Created shape: $shape")
```

#### Output

```kotlin
Created shape: RedCircle@79be0360
```

[Singleton](/patterns/src/test/kotlin/Singleton.kt)
------------

The singleton pattern ensures that only one object of a particular class is ever created.
All further references to objects of the singleton class refer to the same underlying instance.
There are very few applications, do not overuse this pattern!

#### Example:

```kotlin
object KotlinObject {
    init {
        println("Initializing with object: $this")
    }

    fun print() = println("Printing with object: $this")
}
```

#### Usage

```kotlin
println("Start")
KotlinObject.print()
KotlinObject.print()
```

#### Output

```
Start
Initializing with object: KotlinObject@6ff3c5b5
Printing with object: KotlinObject@6ff3c5b5
Printing with object: KotlinObject@6ff3c5b5
```

[Builder / Assembler](/patterns/src/test/kotlin/Builder.kt)
----------

The builder pattern is used to create complex objects with constituent parts that must be created in the same order or using a specific algorithm.
An external class controls the construction algorithm.

#### Example

```kotlin
// Let's assume that Dialog class is provided by external library.
// We have only access to Dialog public interface which cannot be changed.

class Dialog() {

    fun showTitle() = println("showing title")

    fun setTitle(text: String) = println("setting title text $text")

    fun setTitleColor(color: String) = println("setting title color $color")

    fun showMessage() = println("showing message")

    fun setMessage(text: String) = println("setting message $text")

    fun setMessageColor(color: String) = println("setting message color $color")

    fun showImage(bitmapBytes: ByteArray) = println("showing image with size ${bitmapBytes.size}")

    fun show() = println("showing dialog $this")
}

//Builder:
class DialogBuilder() {
    constructor(init: DialogBuilder.() -> Unit) : this() {
        init()
    }

    private var titleHolder: TextView? = null
    private var messageHolder: TextView? = null
    private var imageHolder: File? = null

    fun title(init: TextView.() -> Unit) {
        titleHolder = TextView().apply { init() }
    }

    fun message(init: TextView.() -> Unit) {
        messageHolder = TextView().apply { init() }
    }

    fun image(init: () -> File) {
        imageHolder = init()
    }

    fun build(): Dialog {
        val dialog = Dialog()

        titleHolder?.apply {
            dialog.setTitle(text)
            dialog.setTitleColor(color)
            dialog.showTitle()
        }

        messageHolder?.apply {
            dialog.setMessage(text)
            dialog.setMessageColor(color)
            dialog.showMessage()
        }

        imageHolder?.apply {
            dialog.showImage(readBytes())
        }

        return dialog
    }

    class TextView {
        var text: String = ""
        var color: String = "#00000"
    }
}
```

#### Usage

```kotlin
//Function that creates dialog builder and builds Dialog
fun dialog(init: DialogBuilder.() -> Unit): Dialog {
    return DialogBuilder(init).build()
}

val dialog: Dialog = dialog {
	title {
    	text = "Dialog Title"
    }
    message {
        text = "Dialog Message"
        color = "#333333"
    }
    image {
        File.createTempFile("image", "jpg")
    }
}

dialog.show()
```

#### Output

```
setting title text Dialog Title
setting title color #00000
showing title
setting message Dialog Message
setting message color #333333
showing message
showing image with size 0
showing dialog Dialog@5f184fc6
```

Structural
==========

[Adapter](/patterns/src/test/kotlin/Adapter.kt)
----------

The adapter pattern is used to provide a link between two otherwise incompatible types by wrapping the "adaptee" with a class that supports the interface required by the client.

#### Example

```kotlin
interface Temperature {
    var temperature: Double
}

class CelsiusTemperature(override var temperature: Double) : Temperature

class FahrenheitTemperature(var celsiusTemperature: CelsiusTemperature) : Temperature {

    override var temperature: Double
        get() = convertCelsiusToFahrenheit(celsiusTemperature.temperature)
        set(temperatureInF) {
            celsiusTemperature.temperature = convertFahrenheitToCelsius(temperatureInF)
        }

    private fun convertFahrenheitToCelsius(f: Double): Double = (f - 32) * 5 / 9

    private fun convertCelsiusToFahrenheit(c: Double): Double = (c * 9 / 5) + 32
}

```

#### Usage

```kotlin
val celsiusTemperature = CelsiusTemperature(0.0)
val fahrenheitTemperature = FahrenheitTemperature(celsiusTemperature)

celsiusTemperature.temperature = 36.6
println("${celsiusTemperature.temperature} C -> ${fahrenheitTemperature.temperature} F")

fahrenheitTemperature.temperature = 100.0
println("${fahrenheitTemperature.temperature} F -> ${celsiusTemperature.temperature} C")
```

#### Output

```
36.6 C -> 97.88000000000001 F
100.0 F -> 37.77777777777778 C
```

[Decorator](/patterns/src/test/kotlin/Decorator.kt)
------------

The decorator pattern is used to extend or alter the functionality of objects at run-time by wrapping them in an object of a decorator class.
This provides a flexible alternative to using inheritance to modify behaviour.

#### Example

```kotlin
interface CoffeeMachine {
    fun makeSmallCoffee()
    fun makeLargeCoffee()
}

class NormalCoffeeMachine : CoffeeMachine {
    override fun makeSmallCoffee() = println("Normal: Making small coffee")

    override fun makeLargeCoffee() = println("Normal: Making large coffee")
}

//Decorator:
class EnhancedCoffeeMachine(val coffeeMachine: CoffeeMachine) : CoffeeMachine by coffeeMachine {

    // overriding behaviour
    override fun makeLargeCoffee() {
        println("Enhanced: Making large coffee")
        coffeeMachine.makeLargeCoffee()
    }

    // extended behaviour
    fun makeCoffeeWithMilk() {
        println("Enhanced: Making coffee with milk")
        coffeeMachine.makeSmallCoffee()
        println("Enhanced: Adding milk")
    }
}
```

#### Usage

```kotlin
    val normalMachine = NormalCoffeeMachine()
    val enhancedMachine = EnhancedCoffeeMachine(normalMachine)

    // non-overridden behaviour
    enhancedMachine.makeSmallCoffee()
    // overriding behaviour
    enhancedMachine.makeLargeCoffee()
    // extended behaviour
    enhancedMachine.makeCoffeeWithMilk()
```

#### Output

```
Normal: Making small coffee

Enhanced: Making large coffee
Normal: Making large coffee

Enhanced: Making coffee with milk
Normal: Making small coffee
Enhanced: Adding milk
```

[Facade](/patterns/src/test/kotlin/Facade.kt)
---------

The facade pattern is used to define a simplified interface to a more complex subsystem.

#### Example

```kotlin
class ComplexSystemStore(val filePath: String) {

    init {
        println("Reading data from file: $filePath")
    }

    val store = HashMap<String, String>()

    fun store(key: String, payload: String) {
        store.put(key, payload)
    }

    fun read(key: String): String = store[key] ?: ""

    fun commit() = println("Storing cached data: $store to file: $filePath")
}

data class User(val login: String)

//Facade:
class UserRepository {
    val systemPreferences = ComplexSystemStore("/data/default.prefs")

    fun save(user: User) {
        systemPreferences.store("USER_KEY", user.login)
        systemPreferences.commit()
    }

    fun findFirst(): User = User(systemPreferences.read("USER_KEY"))
}
```

#### Usage

```kotlin
val userRepository = UserRepository()
val user = User("dbacinski")
userRepository.save(user)
val resultUser = userRepository.findFirst()
println("Found stored user: $resultUser")
```

#### Ouput

```
Reading data from file: /data/default.prefs
Storing cached data: {USER_KEY=dbacinski} to file: /data/default.prefs
Found stored user: User(login=dbacinski)
```

[Protection Proxy](/patterns/src/test/kotlin/ProtectionProxy.kt)
------------------

The proxy pattern is used to provide a surrogate or placeholder object, which references an underlying object.
Protection proxy is restricting access.

#### Example

```kotlin
interface File {
    fun read(name: String)
}

class NormalFile : File {
    override fun read(name: String) = println("Reading file: $name")
}

//Proxy:
class SecuredFile : File {
    val normalFile = NormalFile()
    var password: String = ""

    override fun read(name: String) {
        if (password == "secret") {
            println("Password is correct: $password")
            normalFile.read(name)
        } else {
            println("Incorrect password. Access denied!")
        }
    }
}
```

#### Usage

```kotlin
val securedFile = SecuredFile()
securedFile.read("readme.md")

securedFile.password = "secret"
securedFile.read("readme.md")
```

#### Ouput

```
Incorrect password. Access denied!
Password is correct: secret
Reading file: readme.md
```

[Composite](/patterns/src/test/kotlin/Composite.kt)
------------------

The composite pattern is used to composes zero-or-more similar 
objects so that they can be manipulated as one object.

#### Example

```kotlin

open class Equipment(private var price: Int, private var name: String) {
    open fun getPrice(): Int = price
}


/*
[composite]
*/

open class Composite(name: String) : Equipment(0, name) {
    val equipments = ArrayList<Equipment>()

    fun add(equipment: Equipment) {
        this.equipments.add(equipment);
    }

    override fun getPrice(): Int {
        return equipments.map { it -> it.getPrice() }.sum()
    }
}


/*
 leafs
*/

class Cabbinet : Composite("cabbinet")
class FloppyDisk : Equipment(70, "Floppy Disk")
class HardDrive : Equipment(250, "Hard Drive")
class Memory : Equipment(280, "Memory")


```

#### Usage

```kotlin
var cabbinet = Cabbinet()
cabbinet.add(FloppyDisk())
cabbinet.add(HardDrive())
cabbinet.add(Memory())
println(cabbinet.getPrice())
```

#### Ouput

```
600
```