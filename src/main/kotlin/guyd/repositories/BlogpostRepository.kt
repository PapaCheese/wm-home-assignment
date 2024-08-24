package guyd

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import jakarta.inject.Singleton
import jakarta.validation.Valid


interface BlogpostRepository {
    fun list(): List<Blogpost>
    fun save(@Valid blogpost: Blogpost)
}


@Singleton 
open class MongoDbBlogpostRepository(
    private val mongoConf: MongoDbConfiguration, 
    private val mongoClient: MongoClient) : BlogpostRepository { 

    override fun list(): List<Blogpost> = collection.find().into(ArrayList())

    override fun save(@Valid blogpost: Blogpost) {
        collection.insertOne(blogpost)
    }

    private val collection: MongoCollection<Blogpost>
        get() = mongoClient.getDatabase(mongoConf.name)
                .getCollection("blogposts", Blogpost::class.java)
}
