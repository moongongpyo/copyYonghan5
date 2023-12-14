package hello.itemservice.domain.item;

import lombok.Data;

import java.util.List;

@Data //원래 도메인에서는 사용하지 않고 분리해서 사용한다 //컨트롤러에서 @ModelAttribute 어노테이션이 모델을 만들때 자동으로 세터를 사용하기에 넣었다 다음부터는 dto를 사용하자
public class Item {
    private Long id;
    private String itemName;
    private Integer price; //null을 허용한다는 뜻
    private Integer quantity;

    private Boolean open; // 판매여부
    private List<String> regions; // 판매 지역
    private ItemType itemType;// 상품 종류
    private String deliveryCode;//배송 방식

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

}
