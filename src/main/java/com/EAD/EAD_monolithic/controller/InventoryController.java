package com.EAD.EAD_monolithic.controller;

import com.EAD.EAD_monolithic.dto.InventoryDTO;
import com.EAD.EAD_monolithic.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
@CrossOrigin
public class InventoryController {

    @Autowired
    private InventoryService productService;

    @GetMapping("/getProducts")
    public List<InventoryDTO> getProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/saveProduct")
    public InventoryDTO saveProduct(@RequestBody InventoryDTO productDTO) {
        return productService.saveProduct(productDTO);
    }

    @PutMapping("updateProduct")
    public InventoryDTO updateProduct(@RequestBody InventoryDTO productDTO){
        return productService.saveProduct(productDTO);
    }

    @DeleteMapping("/deleteProduct")
    public boolean deleteProduct(@RequestBody InventoryDTO productDTO){
        return productService.deleteProduct(productDTO);
    }

    @GetMapping("/getSingleProduct/{id}")
    public InventoryDTO getProductByProductId(@PathVariable int id){
        return productService.getProductByProductID(id);
    }
}
