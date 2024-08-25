package guyd

import io.micronaut.http.MediaType
import io.micronaut.http.HttpStatus.CREATED
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Status
import io.micronaut.http.annotation.Produces
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.validation.Valid

@Controller("/blogpost") 
@ExecuteOn(TaskExecutors.BLOCKING)
open class BlogpostController(private val blogpostService: BlogpostRepository) { 

    @Get 
    @Produces(MediaType.APPLICATION_JSON) 
    fun getBlogpostsIds(limit: Int?, offset: Int?): List<IdsResponse> {
        val _limit = if (limit == null) defaultPageSize else limit
        val _offset = if (offset == null) 0 else offset 

        return blogpostService.getIds(_limit, _offset)
    }
    

    @Get("/search")
    @Produces(MediaType.APPLICATION_JSON) 
    fun getBlogpostsFiltered(limit: Int?, offset: Int?, nameFilter: String?, textFilter: String?, categoryFilter: String?, productFilter: String?): List<Blogpost>{
        val _limit = if (limit == null) defaultPageSize else limit
        val _offset = if (offset == null) 0 else offset 

        return  blogpostService.getFiltered(_limit, _offset, nameFilter, textFilter, categoryFilter, productFilter)
    }
    

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON) 
    fun getBlogpostById(id: String): List<Blogpost> = blogpostService.getById(id)


    @Post 
    @Status(CREATED) 
    open fun saveBlogpost(@Valid blogpost: Blogpost) = blogpostService.save(blogpost)


    @Put 
    @Status(CREATED) 
    open fun updateBlogpostById(id: String, newName: String?, newText: String?, newCategory: Category?, newProducts: List<Product>?) = blogpostService.updateById(id, newName, newText, newCategory, newProducts)


    // // in case i know blogpost comes with all the params (type safe) from the frontend 
    // @Put 
    // @Status(CREATED) 
    // open fun updateBlogpostById(id: String, @Valid blogpost: Blogpost) = blogpostService.updateById(id, blogpost)


    @Delete 
    @Status(CREATED) 
    open fun deleteBlogpostById(id: String) = blogpostService.deleteById(id)

}
