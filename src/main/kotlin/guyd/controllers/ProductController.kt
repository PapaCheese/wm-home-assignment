package guyd

import io.micronaut.http.HttpStatus.CREATED
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.validation.Valid

@Controller("/product") 
@ExecuteOn(TaskExecutors.BLOCKING) 
open class ProductController(private val productService: ProductRepository) { 

    @Get 
    fun list(): List<Product> = productService.list()

    // @Get("/{id}")
    // @Produces(MediaType.APPLICATION_JSON) 
    // fun get(@PathVariable id: String): Product = productService.findOne(id)


    @Post 
    @Status(CREATED) 
    open fun save(@Valid product: Product) = 
        productService.save(product)
}
