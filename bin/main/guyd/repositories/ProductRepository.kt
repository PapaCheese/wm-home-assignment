package guyd

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Projections.computed
import jakarta.inject.Singleton
import jakarta.validation.Valid
import org.bson.types.ObjectId
import org.bson.conversions.Bson
import org.bson.Document


interface ProductRepository {
    fun getIds(limit: Int, offset: Int): List<IdsResponse>
    fun getFiltered(limit: Int, offset: Int, nameFilter: String?, priceFilter: String?, brandFilter: String?, categoryFilter: String?): List<Product>
    fun getById(id: String): List<Product>
    fun save(@Valid product: Product)
    fun updateById(id: String, newName: String?, newPrice: String?, newBrand: String?, newCategory: Category?)
    fun updateById(id: String, @Valid product: Product)
    fun deleteById(id: String)
}


@Singleton 
open class MongoDbProductRepository(
    private val mongoConf: MongoDbConfiguration, 
    private val mongoClient: MongoClient) : ProductRepository { 

        // returns a list of product IDs
        override fun getIds(limit: Int, offset: Int): List<IdsResponse> {
            return idsCollection.find()
            .projection(fields(
                computed("id", Document("\$toString", "\$_id")) 
            ))
            .skip(offset)
            .limit(limit)
            .into(ArrayList())
        }

        // returns a list of filtered products
        // dynamic filtering, can hold some, all or none of the filters.
        override fun getFiltered(limit: Int, offset: Int, nameFilter: String?, priceFilter: String?, brandFilter: String?, categoryFilter: String?): List<Product> {
            val filters = mutableListOf<Bson>()

            if (!nameFilter.isNullOrEmpty()) {
                filters.add(Filters.eq("name", nameFilter))
            }
            if (!priceFilter.isNullOrEmpty()) {
                filters.add(Filters.eq("price", priceFilter))
            }
            if (!brandFilter.isNullOrEmpty()) {
                filters.add(Filters.eq("brand", brandFilter))
            }
            if (!categoryFilter.isNullOrEmpty()) {
                filters.add(Filters.eq("category", categoryFilter))
            }
        
            val combinedFilter = if (filters.isNotEmpty()) {
                Filters.and(filters)
            } else {
                Filters.empty()
            }
        
            return collection.find(combinedFilter)
                .skip(offset)
                .limit(limit)
                .into(ArrayList())
        }

        // returns a product
        override fun getById(id: String): List<Product> {
            return collection
                .find(Filters.eq("_id", ObjectId(id)))
                .into(ArrayList())
            }

        override fun save(@Valid product: Product) {
            collection.insertOne(product)
        }

        // in case i want the update to be dynamic
        override fun updateById(id: String, newName: String?, newPrice: String?, newBrand: String?, newCategory: Category?) {
            val updates = mutableListOf<Bson>()
        
            if (!newName.isNullOrEmpty()) {
                updates.add(Updates.set("name", newName))
            }
            if (!newPrice.isNullOrEmpty()) {
                updates.add(Updates.set("price", newPrice))
            }
            if (!newBrand.isNullOrEmpty()) {
                updates.add(Updates.set("brand", newBrand))
            }
            if (newCategory != null) {
                updates.add(Updates.set("category", newCategory))
            }
        
            if (updates.isNotEmpty()) {
                val filter = Filters.eq("_id", ObjectId(id))
                val updateResult = collection.updateOne(filter, Updates.combine(updates))
            }
        }

        // in case i know product comes with all the params (type safe) from the frontend 
        override fun updateById(id: String, @Valid product: Product) {
            collection.replaceOne(Filters.eq("_id", ObjectId(id)), product)
        }

        override fun deleteById(id: String) {
            collection.deleteOne(Filters.eq("_id", ObjectId(id)))
        }
        

        private val collection: MongoCollection<Product>
            get() = mongoClient.getDatabase(mongoConf.name)
                    .getCollection("products", Product::class.java)
    
        private val idsCollection: MongoCollection<IdsResponse>
            get() = mongoClient.getDatabase(mongoConf.name)
                    .getCollection("products", IdsResponse::class.java)
        
    }

