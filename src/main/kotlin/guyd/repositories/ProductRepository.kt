package guyd

import com.mongodb.client.model.Filters.eq
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import jakarta.inject.Singleton
import jakarta.validation.Valid
import org.bson.types.ObjectId;

interface ProductRepository {
    fun listProductIds(): List<Product>
    fun findById(id: String): List<Product>
    fun save(@Valid product: Product)
}


@Singleton 
open class MongoDbProductRepository(
    private val mongoConf: MongoDbConfiguration, 
    private val mongoClient: MongoClient) : ProductRepository { 

    override fun listProductIds(): List<Product> {
        print(collection.find()
            .projection(fields(include("_id", "price", "name", "brand")))
            .into(ArrayList())
        )
        return collection.find()
        .projection(fields(include("_id", "price", "name", "brand")))
        .into(ArrayList())
    }

    override fun findById(id: String): List<Product> {
        return collection
            .find(eq("_id", ObjectId(id)))
            .projection(fields(include("_id")))
            .into(ArrayList())
        }

    override fun save(@Valid product: Product) {
        collection.insertOne(product)
    }

    private val collection: MongoCollection<Product>
        get() = mongoClient.getDatabase(mongoConf.name)
                .getCollection("products", Product::class.java)
}
