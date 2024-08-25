package guyd

import io.micronaut.core.annotation.Creator
import io.micronaut.serde.annotation.Serdeable
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import jakarta.validation.constraints.NotBlank

@Serdeable
data class IdsResponse @Creator @BsonCreator constructor( 
    @field:BsonProperty("id") @param:BsonProperty("id")
    var id: String
)