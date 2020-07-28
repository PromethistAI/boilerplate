package boilerplate.dummy._2

import com.promethist.core.dialogue.*
import com.promethist.core.model.Voice
import com.promethist.core.runtime.*

class Model : BasicDialogue() {

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