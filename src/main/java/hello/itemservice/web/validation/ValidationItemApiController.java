package hello.itemservice.web.validation;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import hello.itemservice.web.form.ItemSaveForm;
import hello.itemservice.web.form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/validation/api/items")
@RequiredArgsConstructor
public class ValidationItemApiController {
    private final ItemRepository itemRepository;

    @GetMapping
    private List<Item> items(){
        return itemRepository.findAll();
    }

    @GetMapping("/{itemId}")
    public Item item(@PathVariable long itemId){
        return itemRepository.findById(itemId);
    }


    @PostMapping("/add")
        public Object addItem(@Validated @RequestBody ItemSaveForm form, BindingResult bindingResult){
        Item item =new Item(form.getItemName(), form.getPrice(), form.getQuantity());
        ObjectError(item, bindingResult);
        if(bindingResult.hasErrors()){
            log.info("검증오류발생 errors = {}",bindingResult);
            return bindingResult.getAllErrors();
        }
        //성공 로직
        log.info("성공 로직 실행");
       return itemRepository.save(item);

    }

    @PostMapping("/{itemId}/edit")
    public Object editItem( @Validated @RequestBody ItemUpdateForm form, BindingResult bindingResult){
        Item item =new Item(form.getId(),form.getItemName(), form.getPrice(), form.getQuantity());
        ObjectError(item, bindingResult);
        if(bindingResult.hasErrors()){
            log.info("검증오류발생 errors = {}",bindingResult);
            return bindingResult.getAllErrors();
        }
        log.info("성공 로직 실행");
        itemRepository.update(form.getId(),item);
        return item;
    }

    private static void ObjectError(Item item, BindingResult bindingResult) {
        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin",new Object[]{10000,resultPrice},null);
            }
        }
    }



}
