package guyd

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import jakarta.inject.Singleton
import jakarta.validation.Valid


interface ProductRepository {
    fun list(): List<Product>
    fun save(@Valid product: Product)
}


@Singleton 
open class ProductRepository(
    private val mongoConf: MongoDbConfiguration, 
    private val mongoClient: MongoClient) : ProductRepository { 

    override fun list(): List<Product> = collection.find().into(ArrayList())

    // override fun find(filter: Product): List<Product> = collection.find(filter)

    // override fun findOne(id: String): List<Product> = collection.findOne(id)

    override fun save(@Valid product: Product) {
        collection.insertOne(product)
    }

    private val collection: MongoCollection<Product>
        get() = mongoClient.getDatabase(mongoConf.name)
                .getCollection("products", Product::class.java)
}
