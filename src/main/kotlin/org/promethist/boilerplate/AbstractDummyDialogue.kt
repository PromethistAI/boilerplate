package org.promethist.boilerplate

import org.promethist.core.ExpectedPhrase
import org.promethist.core.type.*
import org.promethist.core.dialogue.*
import org.promethist.core.type.value.Duration

abstract class AbstractDummyDialogue : BasicDialogue() {

    // init code
    data class Movie(val name: String, val director: String)

    var yesCounter by session { 0 }
    var noCounter by session { 0 }
    var aBoolean by user { false }
    var aString by user { "" }
    var aInt by user { 0 }
    var aDateTime by user { now }

    val aBooleanVal by user { Memory(false) }
    val aStringVal by user { Memory("") }
    val aIntVal by user { Memory(0) }
    val clientAttrA by client { 0 }

    val aBooleanSet by user { BooleanMutableSet() }
    val aStringSet by user { StringMutableSet() }
    val aIntSet by user(null) { IntMutableSet() }

    val aMemoryList by user { MemoryMutableList<Int>() }

    val data by loader<Dynamic>("./resources/data")
    val movies by loader<List<Movie>>("./resources/movies")

    val seq1 by userSequence(listOf("first", "second", "third")) { nextInLine(2.minute) }
    val seq2 by userSequence(listOf("blue", "green", "yellow")) { nextRandom(5.minute, 10, 15.minute) }

    data class Example(override val name: String, val result: Int) : NamedEntity
    val examples = mutableListOf<Example>().apply {
        (1..10).forEach { v1 ->
            (1..10).forEach { v2 ->
                add(Example("$v1 plus $v2", v1 + v2))
                if (v2 > v1)
                    add(Example("$v1 minus $v2", v1 - v2))
                if (v1 * v2 < 40)
                    add(Example("$v1 times $v2", v1 * v2))
            }
        }
    }
    val nextExample by sessionSequence(examples) // not that no sequencer function = nextRandom() will be used by default

    // nodes
    val response0 = Response(
            { """Hello, ${nickname}. This is dummy dialogue defined by ${dialogueName}. Do you want to proceed?""" }
    )

    val intent1 = Intent("intent1","yes", "okay")

    val intent2 = Intent("intent2", "no", "nope")

    val input1 = UserInput(arrayOf(intent1, intent2), arrayOf()) {
        pass
    }

    val response1 = Response(
            { """you said ${input.transcript.text}, yes count is #yesCounter, no count is ${noCounter}""" }
    )

    val response2 = Response(
            { """you said ${input.transcript.text}""" }
    )

    val function1 = Function {
        val trans1 = Transition(response1)
        // function code
        val x = input.entity<Duration>()
        yesCounter++
        addResponseItem("Hi, #context.user.name. yesCounter is #yesCounter1.")
        logger.info(describe(data))
        logger.info(movies.toString())
        logger.info(userProfile.attributes.toString())
        logger.info("function1 executed")

        listOf("ireland", "iceland").forEach {
            turn.expectedPhrases.add(ExpectedPhrase(it/*, optional boostValue = 0.0F .. 1.0F */))
        }
        trans1
    }

    val function2 = Function {
        val trans1 = Transition(response0)
        // function code
        noCounter++
        aIntVal.value++
        aIntSet.add(1)
        aMemoryList.add(1)
        aMemoryList.filter { it.value > 1 }
        logger.info(describe(now - 5.minute))
        logger.info("seq1.next = ${seq1.next}")
        logger.info("seq1.last = ${seq1.last}")
        logger.info("seq2.next = ${seq2.next}")
        logger.info("seq2.last = ${seq2.last}")
        //logger.info("seq2 = $seq2")
        logger.info("session.attributes = ${session.attributes}")
        logger.info("userProfile.attributes = ${userProfile.attributes}")
        logger.info("function2 executed")
        trans1
    }

    init {
        // transit node references
        start.next = response0
        response0.next = input1
        intent1.next = function1
        intent2.next = function2
        response1.next = stopSession
        response2.next = response0
    }
}