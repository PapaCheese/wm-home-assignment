package guyd

import io.micronaut.http.HttpStatus.CREATED
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.validation.Valid

@Controller("/blogpost") 
@ExecuteOn(TaskExecutors.BLOCKING) 
open class BlogpostController(private val blogpostService: BlogpostRepository) { 
    @Get 
    fun list(): List<Blogpost> = blogpostService.list()

    // @Get("/{id}")
    // @Produces(MediaType.APPLICATION_JSON) 
    // fun get(@PathVariable id: String): Blogpost = blogpostService.findOne(id)

    @Post 
    @Status(CREATED) 
    open fun save(@Valid blogpost: Blogpost){
        blogpostService.save(blogpost)
    }
}
