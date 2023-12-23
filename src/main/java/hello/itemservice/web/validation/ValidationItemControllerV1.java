package hello.itemservice.web.validation;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/validation/v1/items")
@RequiredArgsConstructor //final 이 붙은 클래스 필드를 생성자주입하기 위한 생성자 자동 생성
public class ValidationItemControllerV1 {
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
        return "/validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "validation/v1/item";
    }
    @GetMapping("/add")
    public String addForm(Model model)
    {
        model.addAttribute("item", new Item());


        return "validation/v1/addForm";
    }


   // @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                           @RequestParam int price,
                           @RequestParam Integer quantity,
                       Model model){
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item",item);
        return "validation/v1/items";
    }
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item")Item item, //이름값을 지정하면 해당 이름으로 모델울 생성하여 뷰로 전달한다 따라서 addAttribute를 안해주어도 된다
                            Model model){

        itemRepository.save(item);
        //model.addAttribute("item",item);
         return "validation/v1/items";  //포스트 이후 리다이렉트를 사용하지 않고 새로고침하면 /add url이 다시 요청되기 때문에 반드시 리다이렉트 처리 해주어야 한다.

    }
    //@PostMapping("/add")
    public String addItemV3(Item item){
        itemRepository.save(item);
         return "redirect:/validation/v1/items"+item.getId();
    }
    @PostMapping("/add")
    public String addItemV4(Item item, RedirectAttributes redirectAttributes, Model model){

        //검증 오류 결과를 보관
        Map<String,String> errors = new HashMap<>();
        //검증 로직
        if (!StringUtils.hasText(item.getItemName())){
            errors.put("itemName", "상품 이름은 필수입니다.");
        }
        if (item.getPrice()==null || item.getPrice() < 1000 ||item.getPrice()>1000000){
            errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다.");
        }
        if(item.getQuantity() == null || item.getQuantity()>=9999){
            errors.put("quantity", "수량은 최대 9,999 까지 허용합니다.");
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                errors.put("globalError","가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 =" + resultPrice);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(!errors.isEmpty()){
            model.addAttribute("errors",errors);
            return "validation/v1/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v1/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);

        return "validation/v1/editForm";
    }
    @PostMapping("/{itemId}/edit")
    public String editItem(@PathVariable long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId,item);
        return "redirect:/validation/v1/items/{itemId}"; //리다이렉트// PathVariable값을 사용
    }




}
