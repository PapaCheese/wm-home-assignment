package guyd

import io.micronaut.core.annotation.Creator
import io.micronaut.serde.annotation.Serdeable
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import jakarta.validation.constraints.NotBlank

@Serdeable
data class Blogpost @Creator @BsonCreator constructor( 
    @field:BsonProperty("name") @param:BsonProperty("name") @field:NotBlank val name: String,
    @field:BsonProperty("text") @param:BsonProperty("text") @field:NotBlank var text: String,
    @field:BsonProperty("category") @param:BsonProperty("category") var category: Category?,
    @field:BsonProperty("products") @param:BsonProperty("products") var products: List<Product>?
){ 
    constructor(name: String, text: String) : this(name, text, null, null)
}
