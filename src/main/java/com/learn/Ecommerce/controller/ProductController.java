package com.learn.Ecommerce.controller;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StreamUtils;

import com.learn.Ecommerce.entity.Product;
import com.learn.Ecommerce.repository.ProductRepository;
import com.learn.Ecommerce.service.FileService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
public class ProductController {
	
    @Value("${product.image.path}")
    private String imageUploadPath;
    
    
    @Autowired
    private FileService fileService;
    
    
    
    @Autowired
    private ProductRepository productRepository;
    

    //upload user image
    @PostMapping("products/image/{productId}")
    public ResponseEntity<String> uploadUserImage(@RequestParam("userImage") MultipartFile image, @PathVariable int productId) throws IOException, java.io.IOException {
        String imageName = fileService.uploadFile(image, imageUploadPath); 
         Product product = productRepository.findById(productId).get();
         System.out.println("ImageName"+imageName);
         product.setImage(imageName);
         productRepository.save(product);
        return new ResponseEntity<String>("Successs",HttpStatus.ACCEPTED);

    }

    //serve user image

    @GetMapping(value = "products/image/{productId}")
    public void serveUserImage(@PathVariable int productId, HttpServletResponse response) throws IOException, java.io.IOException {
        Product product = productRepository.findById(productId).get();
        InputStream resource = fileService.getResource(imageUploadPath, product.getImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }


}
