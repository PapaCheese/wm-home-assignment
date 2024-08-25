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

@Controller("/product") 
@ExecuteOn(TaskExecutors.BLOCKING)
open class ProductController(private val productService: ProductRepository) { 

    @Get 
    @Produces(MediaType.APPLICATION_JSON) 
    fun getProductsIds(): List<IdsResponse> = productService.getIds()
    

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON) 
    fun getProductById(id: String): List<Product> = productService.getById(id)


    @Post 
    @Status(CREATED) 
    open fun saveProduct(@Valid product: Product) = productService.save(product)


    @Put 
    @Status(CREATED) 
    open fun updateProductById(id: String, newName: String?, newPrice: String?, newBrand: String?, newCategory: Category?) = productService.updateById(id, newName, newPrice, newBrand, newCategory)


    @Delete 
    @Status(CREATED) 
    open fun deleteProductById(id: String) = productService.deleteById(id)

}
