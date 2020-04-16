package com.promethist.boilerplate

import com.promethist.core.*
import com.promethist.core.type.*
import com.promethist.core.dialogue.*

abstract class AbstractDummyDialogue : Dialogue() {

    // init code
    data class Movie(val name: String, val director: String)

    var yesCounter by profileAttribute<Int>()
    var noCounter by profileAttribute<Int>()

    val data by loader<Dynamic>("./resources/data")
    val movies by loader<List<Movie>>("./resources/movies")

    // nodes
    val response0 = Response(
            { """This is dummy dialogue defined by ${name}. Do you want to proceed?""" }
    )

    val intent1 = Intent("intent1","yes", "okay")

    val intent2 = Intent("intent2", "no", "nope")

    val input1 = UserInput(arrayOf(intent1, intent2)) {
        processPipeline()
        null
    }

    val response1 = Response(
            { """you said ${input.transcript.text}, yes count is ${yesCounter}, no count is ${noCounter}""" }
    )

    val response2 = Response(
            { """you said ${input.transcript.text}""" }
    )

    val function1 = Function {
        val trans1 = Transition(response1)
        // function code
        yesCounter++
        logger.info(describe(data))
        logger.info(movies.toString())
        logger.info(profile.attributes.toString())
        logger.info("function1 executed")
        trans1
    }

    val function2 = Function {
        val trans1 = Transition(response0)
        // function code
        noCounter++
        logger.info(profile.attributes.toString())
        logger.info("function2 executed")
        trans1
    }

    init {
        // transit node references
        start.next = response0
        response0.next = input1
        intent1.next = function1
        intent2.next = function2
        response1.next = stop
        response2.next = response0
    }
}