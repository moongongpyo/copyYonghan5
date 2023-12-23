package hello.itemservice.domain.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();
    @AfterEach
    void afterEach(){
        itemRepository.clearStore();
    }
    @Test
    void save() {
        //given
        Item item = new Item("ItemA",10000,10);
        //when
        Item saveditem = itemRepository.save(item);
        //then
        Item findItem = itemRepository.findById(item.getId());
        assertThat(findItem).isSameAs(saveditem); //실제값,예상값 순서
    }

    @Test
    void findAll() {
        //given
        Item item1 = new Item("ItemA",10000,10);
        Item item2 = new Item("ItemB",20000,20);
        //when
        itemRepository.save(item1);
        itemRepository.save(item2);
        List<Item> result = itemRepository.findAll();
        //then
        assertThat(result.size()).isEqualTo(2);//결과값이 총 몇 개인지
        assertThat(result).contains(item1,item2);//해당 두 인스턴스를 포함하는지
        //위 두개를 모두 비교해야 의미있다.
    }

    @Test
    void updateItem() {
        //given
        Item item = new Item("Item1",10000,10);
        Item savedItem = itemRepository.save(item);
        Long itemId = savedItem.getId();
        //when
        Item updateParam = new Item("Item2",20000,30);
        itemRepository.update(itemId, updateParam);
        //then
        Item findItem = itemRepository.findById(itemId);

        assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity());
    }


}