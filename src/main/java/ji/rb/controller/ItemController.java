package ji.rb.controller;

import ji.rb.model.Item;
import ji.rb.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/search")
    public List<Item> searchItems(@RequestParam("q") String query) {
         return itemService.searchItems(query);
    }
}
