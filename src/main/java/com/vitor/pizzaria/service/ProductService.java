package com.vitor.pizzaria.service;

import com.vitor.pizzaria.model.dto.PizzaDTO;
import com.vitor.pizzaria.model.dto.Product;
import com.vitor.pizzaria.model.entity.PizzaPriceModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private PizzaService pizzaService;

    public ProductService(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    public String getProductHeader(String type, String option) {
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
            case "calzones":
                break;
            case "drinks":
                break;
        }

        //System.out.println(type + " " + option + " " + list);
        return list;
    }
}
