package guyd

import io.micronaut.http.MediaType
import io.micronaut.http.HttpStatus.CREATED
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status
import io.micronaut.http.annotation.Produces
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.validation.Valid

@Controller("/product") 
@ExecuteOn(TaskExecutors.BLOCKING) 
open class ProductController(private val productService: ProductRepository) { 

    @Get 
    fun listProductIds(): List<Product> = productService.listProductIds()

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON) 
    fun get(id: String): List<Product> = productService.findById(id)


    @Post 
    @Status(CREATED) 
    open fun save(@Valid product: Product) = 
        productService.save(product)
}
