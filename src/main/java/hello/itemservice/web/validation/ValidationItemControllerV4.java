package hello.itemservice.web.validation;

import hello.itemservice.domain.item.*;
import hello.itemservice.web.form.ItemForm;
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

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor //final 이 붙은 클래스 필드를 생성자주입하기 위한 생성자 자동 생성
public class ValidationItemControllerV4 {
    private final ItemRepository itemRepository;

    @ModelAttribute("regions") // 이 컨트롤러에서 호출시에 항상 이 모델이 생성됨며 전달됨// 컨트롤러 실행시에 항상 호출되기에 성능상으로는 static 영역에 따로 데이터 모델을 만들어 두는 것이 이득임
    public Map<String,String> regions(){
        Map<String, String> regions = new LinkedHashMap<>();// 일반 해시맵은 순서가 보장되지 않음
        regions.put("SEOUL","서울");
        regions.put("BUSAN","부산");
        regions.put("JEJU","제주");
        return regions;
    }
    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes(){
        /*ItemType[] values = ItemType.values();
        return values; */
        return ItemType.values();
    }
    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes(){
       List<DeliveryCode> deliveryCodes = new ArrayList<>();
       deliveryCodes.add(new DeliveryCode("FAST","빠른 배송"));
       deliveryCodes.add(new DeliveryCode("NORMAL","일반 배송"));
       deliveryCodes.add(new DeliveryCode("SLOW","느린 배송"));
       return deliveryCodes;
    }

    @GetMapping
    private String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items",items);
        return "/validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "validation/v4/item";
    }
    @GetMapping("/add")
    public String addForm(Model model)
    {
        model.addAttribute("item", new Item());


        return "validation/v4/addForm";
    }


    @PostMapping("/add")
        public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        Item item =new Item(form.getItemName(), form.getPrice(), form.getQuantity());

        ObjectError(item, bindingResult);
        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            return "validation/v4/addForm";
        }
        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
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

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);

        return "validation/v4/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String editItem(@PathVariable long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult){
        Item item =new Item(itemId,form.getItemName(), form.getPrice(), form.getQuantity());
        itemRepository.update(itemId,item);
        ObjectError(item, bindingResult);
        if(bindingResult.hasErrors()){
            return "validation/v4/addForm";
        }
        //검증에 실패하면 다시 입력 폼으로
        return "redirect:/validation/v4/items/{itemId}"; //리다이렉트// PathVariable값을 사용
    }




}
