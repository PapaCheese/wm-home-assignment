package guyd

import com.mongodb.client.model.Filters.eq
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Projections.computed
import jakarta.inject.Singleton
import jakarta.validation.Valid
import org.bson.types.ObjectId;
import org.bson.Document


interface ProductRepository {
    fun getIds(): List<IdsResponse>
    fun getById(id: String): List<Product>
    fun save(@Valid product: Product)
    fun updateById(id: String, @Valid product: Product)
    fun deleteById(id: String)
}


@Singleton 
open class MongoDbProductRepository(
    private val mongoConf: MongoDbConfiguration, 
    private val mongoClient: MongoClient) : ProductRepository { 
        override fun getIds(): List<IdsResponse> {
            return idsCollection.find()
            .projection(fields(
                computed("id", Document("\$toString", "\$_id")) 
            ))
            .into(ArrayList())
        }

        override fun getById(id: String): List<Product> {
            return collection
                .find(eq("_id", ObjectId(id)))
                .into(ArrayList())
            }

        override fun save(@Valid product: Product) {
            collection.insertOne(product)
        }

        override fun updateById(id: String, @Valid product: Product) {
            collection.updateOne(Document("_id", ObjectId(id)), product)
        }

        override fun deleteById(id: String) {
            collection.deleteOne(Document("_id", ObjectId(id)))
        }

        private val collection: MongoCollection<Product>
            get() = mongoClient.getDatabase(mongoConf.name)
                    .getCollection("products", Product::class.java)
    
        private val idsCollection: MongoCollection<IdsResponse>
            get() = mongoClient.getDatabase(mongoConf.name)
                    .getCollection("products", IdsResponse::class.java)
        
    }


    // include("price", "name", "brand"),