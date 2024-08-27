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


interface BlogpostRepository {
    fun getIds(limit: Int, offset: Int): List<IdsResponse>
    fun getFiltered(limit: Int, offset: Int, nameFilter: String?, textFilter: String?, categoryFilter: String?): List<Blogpost>
    fun getById(id: String): List<Blogpost>
    fun save(@Valid blogpost: Blogpost)
    fun updateById(id: String, newName: String?, newText: String?, newCategory: Category?, newProducts: List<Product>?)
    fun updateById(id: String, @Valid blogpost: Blogpost)
    fun deleteById(id: String)
}


@Singleton 
open class MongoDbBlogpostRepository(
    private val mongoConf: MongoDbConfiguration, 
    private val mongoClient: MongoClient) : BlogpostRepository { 

        // returns a list of blogpost IDs
        override fun getIds(limit: Int, offset: Int): List<IdsResponse> {
            return idsCollection.find()
            .projection(fields(
                computed("id", Document("\$toString", "\$_id")) 
            ))
            .skip(offset)
            .limit(limit)
            .into(ArrayList())
        }

        // returns a list of filtered blogposts
        // dynamic filtering, can hold some, all or none of the filters.
        override fun getFiltered(limit: Int, offset: Int, nameFilter: String?, textFilter: String?, categoryFilter: String?): List<Blogpost> {
            val filters = mutableListOf<Bson>()

            if (!nameFilter.isNullOrEmpty()) {
                filters.add(Filters.eq("name", nameFilter))
            }
            if (!textFilter.isNullOrEmpty()) {
                filters.add(Filters.eq("text", textFilter))
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

        // returns a blogpost
        override fun getById(id: String): List<Blogpost> {
            return collection
                .find(Filters.eq("_id", ObjectId(id)))
                .into(ArrayList())
            }

        override fun save(@Valid blogpost: Blogpost) {
            collection.insertOne(blogpost)
        }

        // in case i want the update to be dynamic
        override fun updateById(id: String, newName: String?, newText: String?, newCategory: Category?, newProducts: List<Product>?) {
            val updates = mutableListOf<Bson>()
        
            if (!newName.isNullOrEmpty()) {
                updates.add(Updates.set("name", newName))
            }
            if (!newText.isNullOrEmpty()) {
                updates.add(Updates.set("text", newText))
            }
            if (newCategory != null) {
                updates.add(Updates.set("category", newCategory))
            }
            if (!newProducts.isNullOrEmpty()) {
                updates.add(Updates.set("product", newProducts))
            }
        
            if (updates.isNotEmpty()) {
                val filter = Filters.eq("_id", ObjectId(id))
                val updateResult = collection.updateOne(filter, Updates.combine(updates))
            }
        }

        // in case i know blogpost comes with all the params (type safe) from the frontend 
        override fun updateById(id: String, @Valid blogpost: Blogpost) {
            collection.replaceOne(Filters.eq("_id", ObjectId(id)), blogpost)
        }

        override fun deleteById(id: String) {
            collection.deleteOne(Filters.eq("_id", ObjectId(id)))
        }
        

        private val collection: MongoCollection<Blogpost>
            get() = mongoClient.getDatabase(mongoConf.name)
                    .getCollection("blogposts", Blogpost::class.java)
    
        private val idsCollection: MongoCollection<IdsResponse>
            get() = mongoClient.getDatabase(mongoConf.name)
                    .getCollection("blogposts", IdsResponse::class.java)
        
    }

