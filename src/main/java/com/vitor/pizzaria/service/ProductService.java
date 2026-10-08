package com.vitor.pizzaria.service;

import com.vitor.pizzaria.model.dto.*;
import com.vitor.pizzaria.model.entity.*;
import com.vitor.pizzaria.repository.*;
import com.vitor.pizzaria.service.tenants.TenantContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;
    private final CategoryRepository categoryRepository;
    private final TenantContext tenantContext;
    private final SizeRepository sizeRepository;


    public ProductService(ProductRepository productRepository, ProductPriceRepository productPriceRepository, CategoryRepository categoryRepository, TenantContext tenantContext, SizeRepository sizeRepository) {
        this.productRepository = productRepository;
        this.productPriceRepository = productPriceRepository;
        this.categoryRepository = categoryRepository;
        this.tenantContext = tenantContext;
        this.sizeRepository = sizeRepository;
    }

    public List<CategoryResponse> getCatalog() {
        UUID tenant = tenantContext.getTenantId();
        List<CategoryResponse> catalog = new ArrayList<>();

        // Queries

        Map<UUID, Product> products = productRepository
                .getProductsByRestaurantIdAndActiveTrue(tenant)
                .stream()
                .collect(Collectors.toMap(
                        Product::getId,
                        product -> product
                ));

        Map<UUID, ProductPrice> productPrices = productPriceRepository
                .getProductPricesByRestaurantIdAndActiveTrue(tenant)
                .stream()
                .collect(Collectors.toMap(
                        ProductPrice::getId,
                        productPrice -> productPrice
                ));

        Map<UUID, Size> sizes = sizeRepository
                .getSizesByRestaurantIdAndActiveTrue(tenant)
                .stream()
                .collect(Collectors.toMap(
                        Size::getId,
                        size -> size
                ));

        for (Category category : categoryRepository.getCategoriesByRestaurantIdAndActiveTrue(tenant)) {
            HashMap<UUID, SizeResponse> sizesResponse = new HashMap<>();

            for (Size size : sizes
                    .values()
                    .stream()
                    .filter(size -> ( size.getCategoryId().equals(category.getId()))).toList()
            ) {
                // Filter by productPrice
                List<ProductPrice> productsInThatSize = productPrices
                        .values()
                        .stream()
                        .filter(productPrice -> (
                                size.getId().equals(productPrice.getSizeId())
                                ))
                        .filter(productPrice -> (
                                tenant.equals(productPrice.getRestaurantId())
                                ))
                        .toList();

                List<ProductResponse> productsResponsesInThatSize = new ArrayList<>();
                for (ProductPrice productPrice : productsInThatSize) {

                    productsResponsesInThatSize.add(
                            new ProductResponse(
                                    productPrice.getProductId(),
                                    products.get(productPrice.getProductId()).getName(),
                                    products.get(productPrice.getProductId()).getDescription(),
                                    productPrice.getPriceCents(),
                                    products.get(productPrice.getProductId()).getImageUrl(),
                                    products.get(productPrice.getProductId()).getImageFit(),
                                    products.get(productPrice.getProductId()).getSortOrder()
                            )
                    );
                }

                System.out.println("Products in that size: " + productsResponsesInThatSize);
                sizesResponse.put(
                        size.getId(),
                        new SizeResponse (
                                size.getId(),
                                productsResponsesInThatSize,
                                size.getName(),
                                size.getSortOrder()
                        )
                );
            }

            catalog.add(
                    new CategoryResponse(
                            category.getId(),
                            sizesResponse.values().stream().toList(),
                            category.getName(),
                            category.getDescription(),
                            category.getSortOrder()
                    )
            );
        }

        catalog.sort(Comparator.comparing(CategoryResponse::sort_order).reversed());

        return catalog;
    }
}
