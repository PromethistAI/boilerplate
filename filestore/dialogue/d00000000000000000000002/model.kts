package boilerplate.dummy._2

import ai.flowstorm.core.dialogue.*
import ai.flowstorm.core.model.Voice
import ai.flowstorm.core.runtime.*

class Model : BasicDialogue() {

    override val dialogueId = "d00000000000000000000002"
    override val dialogueName = "boilerplate/dummy/2"
    override val voice = Voice.Gabriela

    val response0 = Response(
            { """${greeting("Vojtěch")}. ${enumerate(sample.applications().list { name })}.""" }
    )

    init {
        // transit node references
        start.next = response0
        response0.next = stopSession
    }
}

Model::class