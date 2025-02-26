package ji.rb.controller;

import ji.rb.model.Item;
import ji.rb.service.ItemService;
import ji.rb.service.CSVParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.util.List;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CSVParserService csvParserService;

    // Initialize itemService with items from CSV upon startup
    @PostConstruct
    public void init() {
        try {
            csvParserService.loadItemsIntoItemService(itemService, "cagemap.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/search")
    public List<Item> searchItems(@RequestParam("q") String query) {
         return itemService.searchItems(query);
    }
}
