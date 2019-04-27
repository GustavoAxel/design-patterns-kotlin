import org.junit.jupiter.api.Test

interface DialogSpec {
    fun setTitle(text: String)
    fun setTitleColor(color: String)
    fun setMessage(text: String)
    fun setMessageColor(color: String)

    fun show()
}

class Dialog : DialogSpec{
    override fun setTitle(text: String) = println("setting title text $text")
    override fun setTitleColor(color: String) = println("setting title color $color")
    override fun setMessage(text: String) = println("setting message $text")
    override fun setMessageColor(color: String) = println("setting message color $color")

    override fun show() = println("showing dialog $this")
}

class DialogBuilder() {

    constructor(init: DialogBuilder.() -> Unit) : this() {
        init()
    }

    private var titleHolder: TextView? = null
    private var messageHolder: TextView? = null

    fun title(attributes: TextView.() -> Unit) {
        titleHolder = TextView().apply { attributes() }
    }

    fun message(attributes: TextView.() -> Unit) {
        messageHolder = TextView().apply { attributes() }
    }

    fun build(): Dialog {
        println("build")
        val dialog = Dialog()

        titleHolder?.apply {
            dialog.setTitle(text)
            dialog.setTitleColor(color)
        }

        messageHolder?.apply {
            dialog.setMessage(text)
            dialog.setMessageColor(color)
        }

        return dialog
    }

    class TextView {
        var text: String = ""
        var color: String = "#00000"
    }
}

//Function that creates dialog builder and builds Dialog
fun dialog(init: DialogBuilder.() -> Unit): Dialog = DialogBuilder(init).build()

class BuilderTest {

    @Test
    fun `Builder`() {

        println("Build dialog")

        val dialog: Dialog =
                dialog {
                    title {
                        text = "Dialog Title"
                    }
                    message {
                        text = "Dialog Message"
                        color = "#333333"
                    }
                }

        println("Show dialog")
        dialog.show()
    }
}