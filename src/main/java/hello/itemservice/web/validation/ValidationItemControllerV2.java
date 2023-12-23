package hello.itemservice.web.validation;

import hello.itemservice.domain.item.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor //final 이 붙은 클래스 필드를 생성자주입하기 위한 생성자 자동 생성
public class ValidationItemControllerV2 {
    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @InitBinder
    public void init(WebDataBinder dataBinder){
        dataBinder.addValidators(itemValidator);
    }

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
        return "/validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "validation/v2/item";
    }
    @GetMapping("/add")
    public String addForm(Model model)
    {
        model.addAttribute("item", new Item());


        return "validation/v2/addForm";
    }


  /* @PostMapping("/add")
        public String addItemV1(Item item, BindingResult bindingResult,  RedirectAttributes redirectAttributes, Model model){

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수 입니다."));
        }
        if (item.getPrice()==null || item.getPrice() < 1000 ||item.getPrice()>1000000){
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));

        }
        if(item.getQuantity() == null || item.getQuantity()>=9999){
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));

        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 =" + resultPrice));
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }*/
  /*  @PostMapping("/add")
        public String addItemV2(Item item, BindingResult bindingResult,  RedirectAttributes redirectAttributes, Model model){

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item", "itemName",item.getItemName(),false,null,null, "상품 이름은 필수 입니다."));
        }
        if (item.getPrice()==null || item.getPrice() < 1000 ||item.getPrice()>1000000){
            bindingResult.addError(new FieldError("item", "price",item.getPrice(),false,null,null, "가격은 1,000 ~ 1,000,000 까지 허용합니다."));

        }
        if(item.getQuantity() == null || item.getQuantity()>=9999){
            bindingResult.addError(new FieldError("item", "quantity",item.getQuantity(),false,null,null, "수량은 최대 9,999 까지 허용합니다."));

        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 =" + resultPrice));
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }*/
    /* @PostMapping("/add")
        public String addItemV3(Item item, BindingResult bindingResult,  RedirectAttributes redirectAttributes, Model model){

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item", "itemName",item.getItemName(),false,new String[]{"required.item.itemName"},null, null));
        }
        if (item.getPrice()==null || item.getPrice() < 1000 ||item.getPrice()>1000000){
            bindingResult.addError(new FieldError("item", "price",item.getPrice(),false,new String[]{"range.item.price"},new Object[]{1000,1000000}, null));

        }
        if(item.getQuantity() == null || item.getQuantity()>=9999){
            bindingResult.addError(new FieldError("item", "quantity",item.getQuantity(),false,new String[]{"max.item.quantity"},new Object[]{9999}, null));

        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"},new Object[]{10000,resultPrice},null));
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }*/
    /*@PostMapping("/add")
        public String addItemV4(Item item, BindingResult bindingResult,  RedirectAttributes redirectAttributes, Model model){

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "validation/v2/addForm";
        }

        //검증 로직
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult,"itemName","required");     //Empty 나 공백같은 단순한 기능 사용시 if문 제공
        if (!StringUtils.hasText(item.getItemName())){
           bindingResult.rejectValue("itemName","required");
        }
        if (item.getPrice()==null || item.getPrice() < 1000 ||item.getPrice()>1000000){
            bindingResult.rejectValue("price","range",new Object[]{1000,1000000},null);
        }
        if(item.getQuantity() == null || item.getQuantity()>=9999){
            bindingResult.rejectValue("quantity","max",new Object[]{9999},null);
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin",new Object[]{10000,resultPrice},null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }*/
/* @PostMapping("/add")
        public String addItemV5( @ModelAttribute Item item, BindingResult bindingResult,  RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){

         return "validation/v2/addForm";
         }
        itemValidator.validate(item, bindingResult);
        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }*/
    @PostMapping("/add")
        public String addItemV6(@Validated @ModelAttribute  Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);

        return "validation/v2/editForm";
    }
    @PostMapping("/{itemId}/edit")
    public String editItem(@PathVariable long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId,item);
        return "redirect:/validation/v2/items/{itemId}"; //리다이렉트// PathVariable값을 사용
    }



}
