package com.Brew_Track.Cafe.Brew_Track.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Brew_Track.Cafe.Brew_Track.JWT.JwtFilter;
import com.Brew_Track.Cafe.Brew_Track.POJO.Category;
import com.Brew_Track.Cafe.Brew_Track.POJO.Product;
import com.Brew_Track.Cafe.Brew_Track.constents.CafeConstants;
import com.Brew_Track.Cafe.Brew_Track.dao.ProductDao;
import com.Brew_Track.Cafe.Brew_Track.service.ProductService;
import com.Brew_Track.Cafe.Brew_Track.utils.CafeUtils;
import com.Brew_Track.Cafe.Brew_Track.wrapper.ProductWrapper;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired 
    private JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> AddNewProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validProductMap(requestMap, false)) {
                    productDao.save(getProductFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity("Product Added Successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validProductMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (validateId) {
                return requestMap.containsKey("id");
            } else {
                return true;
            }
        }
        return false;
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));

        Product product = new Product();

        // Set ID only if updating
        if (requestMap.containsKey("id")) {
            product.setId(Integer.parseInt(requestMap.get("id")));
        }

        product.setName(requestMap.get("name"));
        product.setCategory(category);
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        product.setStatus("true");

        return product;
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()) {
                if (validProductMap(requestMap, true)) {
                    int productId = Integer.parseInt(requestMap.get("id"));
                    Optional<Product> optional = productDao.findById(productId);

                    if (optional.isPresent()) {
                        Product product = getProductFromMap(requestMap, true);
                        product.setStatus(optional.get().getStatus());
                        productDao.save(product);
                        return CafeUtils.getResponseEntity("Product updated successfully", HttpStatus.OK);
                    } else {
                        return CafeUtils.getResponseEntity("Product does not exist.", HttpStatus.OK);
                    }
                } else {
                    return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional optional = productDao.findById(id);
                if (!optional.isEmpty()) {
                    productDao.deleteById(id);
                    return CafeUtils.getResponseEntity("Product is deleted successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Product ID does not exist.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
            Optional optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
            if (!optional.isEmpty()) {
                productDao.updateProductStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                return CafeUtils.getResponseEntity("Product status updated successfully", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity("Product ID is not exist", HttpStatus.OK);
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
           ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductByCategory(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try {
           return new ResponseEntity<>(productDao.getProductById(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    


}
