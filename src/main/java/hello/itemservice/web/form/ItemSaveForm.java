package hello.itemservice.web.form;

import hello.itemservice.domain.item.ItemType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ItemSaveForm implements ItemForm {
    private Long id;
    @NotBlank
    private String itemName;
    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    @Max(value = 9999)
    private Integer quantity;

    private Boolean open; // 판매여부
    private List<String> regions; // 판매 지역
    private ItemType itemType;// 상품 종류
    private String deliveryCode;//배송 방식



}
