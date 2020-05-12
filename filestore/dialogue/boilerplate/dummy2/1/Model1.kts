package boilerplate.dummy2

import com.promethist.core.dialogue.*

class Model1 : BasicDialogue() {

    override val name = "boilerplate/dummy2/1"

    val welcome = Response(
            { """This is dummy dialogue defined by ${name}. Do you want to proceed?""" }
    )

    val intent_yes = Intent("intent1","yes", "okay")

    val intent_no = Intent("intent2", "no", "nope")

    val input1 = UserInput(arrayOf(intent_yes, intent_no)) {
        pass
    }

    val response_yes = Response(
            { """you said ${input.transcript.text}""" }
    )

    val response_no = Response(
            { """you said ${input.transcript.text}""" }
    )

    val function_yes = Function {
        val proceed = Transition(response_yes)
        // function code
        proceed
    }

    // --
    val function_no = Function {
        val proceed = Transition(welcome)
        // function code
        proceed
    }

    init {
        // transit node references
        start.next = welcome
        welcome.next = input1
        intent_yes.next = function_yes
        intent_no.next = function_no
        response_yes.next = stop
        response_no.next = welcome
    }
}