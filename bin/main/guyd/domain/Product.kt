package guyd

import io.micronaut.core.annotation.Creator
import io.micronaut.serde.annotation.Serdeable
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import jakarta.validation.constraints.NotBlank

@Serdeable
data class Product @Creator @BsonCreator constructor( 
    @field:BsonProperty("id") @param:BsonProperty("id")
    var id: String?,
    @field:BsonProperty("brand") @param:BsonProperty("brand") @field:NotBlank
    var brand: String,
    @field:BsonProperty("name") @param:BsonProperty("name") @field:NotBlank
    val name: String,
    @field:BsonProperty("price") @param:BsonProperty("price") @field:NotBlank
    val price: String,
    @field:BsonProperty("category") @param:BsonProperty("category")
    val category: Category?,
)