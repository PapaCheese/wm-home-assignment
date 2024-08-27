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
    fun getProductsIds(limit: Int?, offset: Int?): List<IdsResponse> {
        val _limit = if (limit == null) defaultPageSize else limit
        val _offset = if (offset == null) 0 else offset 

        return productService.getIds(_limit, _offset)
    }
    

    @Get("/search")
    @Produces(MediaType.APPLICATION_JSON) 
    fun getProductsFiltered(limit: Int?, offset: Int?, nameFilter: String?, priceFilter: String?, brandFilter: String?, categoryFilter: String?): List<Product>{
        val _limit = if (limit == null) defaultPageSize else limit
        val _offset = if (offset == null) 0 else offset 

        return  productService.getFiltered(_limit, _offset, nameFilter, priceFilter, brandFilter, categoryFilter)
    }
    

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON) 
    fun getProductById(id: String): List<Product> = productService.getById(id)


    @Post 
    @Status(CREATED) 
    open fun saveProduct(@Valid product: Product): String {
        return productService.save(product)
    }


    @Put 
    @Status(CREATED) 
    open fun updateProductById(id: String, newName: String?, newPrice: String?, newBrand: String?, newCategory: Category?): String = productService.updateById(id, newName, newPrice, newBrand, newCategory)


    // // in case i know product comes with all the params (type safe) from the frontend 
    // @Put 
    // @Status(CREATED) 
    // open fun updateProductById(id: String, @Valid product: Product) = productService.updateById(id, product)


    @Delete("/{id}")
    @Status(CREATED) 
    open fun deleteProductById(id: String) = productService.deleteById(id)

}
