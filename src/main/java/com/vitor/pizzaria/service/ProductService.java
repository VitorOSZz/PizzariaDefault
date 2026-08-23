package com.vitor.pizzaria.service;

import com.vitor.pizzaria.model.dto.PizzaDTO;
import com.vitor.pizzaria.model.dto.Product;
import com.vitor.pizzaria.model.entity.PizzaPriceModel;
import com.vitor.pizzaria.repository.DrinkRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final PizzaService pizzaService;
    private final DrinkService drinkService;

    public ProductService(PizzaService pizzaService, DrinkRepository drinkRepository) {
        this.pizzaService = pizzaService;
        this.drinkService = new DrinkService(drinkRepository);
    }

    public String getProductHeader(String type, String option) {
        System.out.println("GetProductHeader type: " + type);
        System.out.println("GetProductHeader option: " + option);
        switch (type) {
            case "pizzas":
                switch (option) {
                    case "giant":
                        return """
                                <img src="/images/cards/pizza-giant-size.webp" alt="pizza tamanho familia">
                                <div>
                                <h4>Família (40cm)</h4>
                                <p>Pizza com ate 2 sabores e 12 fatias</p>
                                </div>
                                """;
                    case "big":
                        return """
                                <img src="/images/cards/pizza-big-size.webp" alt="pizza grande">
                                <div>
                                <h4>Grande (35cm)</h4>
                                <p>Pizza com ate 2 sabores e 8 fatias</p>
                                </div>
                                """;
                    case "medium":
                        return """
                                <img src="/images/cards/pizza-medium-size.webp" alt="pizza media">
                                <div>
                                <h4>Média (30cm)</h4>
                                <p>Pizza com 6 fatias e 1 sabor</p>
                                </div>
                                """;
                    case "napoletana":
                        return """
                                <img src="/images/cards/napoletana.webp" alt="napoletana">
                                <div>
                                <h4>Napoletana</h4>
                                <p>Pizza com 4 fatias</p>
                                </div>
                                """;
                }
                break;
            case "drinks":
                switch (option) {
                    case "soda":
                        return """
                                <img src="/images/cards/Soda 2L.webp" alt="refrigerantes" class="contain">
                                <div>
                                <h4>Refrigerantes</h4>
                                </div>
                                """;
                    case "water":
                        return """
                                <img src="/images/cards/water-bottle.webp" alt="Águas" class="contain">
                                <div>
                                <h4>Águas</h4>
                                </div>
                                """;
                    case "beer":
                        return """
                                <img src="/images/cards/beer.webp" alt="Águas" class="contain">
                                <div>
                                <h4>Cervejas</h4>
                                </div>
                                """;
                }
        }

        // Default Message
        return """
                <img src="/images/cards/error.webp" alt="Erro">
                <div>
                    <h4>Nenhum Produto Encontrado</h4>
                    <p>Não encontramos produtos disponíveis para esta opção.Se o problema persistir, por favor, reporte o erro.</p>
                </div>
                """;
    }

    public List<Product> findProducts(String type, String option) {
        List<Product> list = new ArrayList<>();
        switch (type) {
            case "pizzas":
                list.addAll(pizzaService.getPizzasBySize(option));
                break;
            case "drinks":
                list.addAll(drinkService.getDrinksByCategory(option));
                break;
            case "calzones":
                break;
        }

        System.out.println(type + " " + option + " " + list);
        return list;
    }

    public List<Product> findProductById(String category, String option, Long id) {
        List<Product> products = new ArrayList<>();
        switch (category) {
            case "pizzas":
                products.add(pizzaService.getPizzaBySizeAndId(option, id));
                break;
            case "drinks":
                products.add(drinkService.getDrinkById(id));
                break;
        }

        System.out.println(category + " " + option + " " + id + " " + products);
        return products;
    }
}
