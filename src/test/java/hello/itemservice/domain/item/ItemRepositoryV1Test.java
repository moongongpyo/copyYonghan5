package hello.itemservice.domain.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class ItemRepositoryV1Test {

    ItemRepositoryV1 itemRepositoryV1 = new ItemRepositoryV1();
    @AfterEach
    void afterEach(){
        itemRepositoryV1.clearStore();
    }
    @Test
    void save() {
        //given
        Item item = new Item("ItemA",10000,10);
        //when
        Item saveditem = itemRepositoryV1.save(item);
        //then
        Item findItem = itemRepositoryV1.findById(item.getId());
        assertThat(findItem).isSameAs(saveditem); //실제값,예상값 순서
    }

    @Test
    void findAll() {
        //given
        Item item1 = new Item("ItemA",10000,10);
        Item item2 = new Item("ItemB",20000,20);
        //when
        itemRepositoryV1.save(item1);
        itemRepositoryV1.save(item2);
        List<Item> result = itemRepositoryV1.findAll();
        //then
        assertThat(result.size()).isEqualTo(2);//결과값이 총 몇 개인지
        assertThat(result).contains(item1,item2);//해당 두 인스턴스를 포함하는지
        //위 두개를 모두 비교해야 의미있다.
    }

    @Test
    void updateItem() {
        //given
        Item item = new Item("Item1",10000,10);
        Item savedItem = itemRepositoryV1.save(item);
        Long itemId = savedItem.getId();
        //when
        Item updateParam = new Item("Item2",20000,30);
        itemRepositoryV1.update(itemId, updateParam);
        //then
        Item findItem = itemRepositoryV1.findById(itemId);

        assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity());
    }


}