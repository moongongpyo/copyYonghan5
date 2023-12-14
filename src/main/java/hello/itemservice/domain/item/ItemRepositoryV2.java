package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository// 실제 데이터베이스와 연동이 되는 저장소의 경우에는 데이터베이스 예외를 다듬어 준다던가 스프링과 호환성을 증진시켜주는 역할을 한다.
public class ItemRepositoryV2 {
    private static final Map<Long,Item> store = new HashMap<>(); //실무에서는 HashMap을 사용하면 안됨//동시에 여러 쓰레드가 접근하는 환경에서는 ConcurrentHashMap을 사용해야 한다.
    private static long sequence = 0L; //스프링은 싱글톤을 자동으로 적용해 주긴 하지만 static을 하지 않는다면 자바 문법인 new 등을 사용하여 객체를 생성할 때마다 sequence가 만들어진다//기본적으로 한 개의 과정을 수행할때마다 증가해야하는 sequence는 static을 해준다.//위와 같은 이유에서 실무에서는 AtomicLong을 활용하여 원자성을 보장헤애 한다

    public Item save(Item item){
        item.setId(++sequence);// 아이템 인스턴스가 생성될 때마다 식별자인 아이디 값을 1증가시키는 역할
        store.put(item.getId(), item); //메모리에 저장되어있는 컬렉션 아이디와 객체를 저장한다.
        return item;    //생성한 인스턴스를 반환한다
    }

    public Item findById(Long id){
       return store.get(id);
    }
    public List<Item> findAll(){
        return new ArrayList<>(store.values()); //store를 바로 반환하지 않은 이유는 array로 값싸서 값에 변동이 생겨도 store이 원래 상태를 유지하게 하기 위함
    }


    //여태까지 프로젝트에서 이게 엔티티나 서비스에 있었는데 repository에 있구나 앞으로는 JPA레포지토리를 오버라이드해서 이 부분을 작성해야겠다
    //여기서는 파라미터에 아이템 객체를 그대로 사용했지만 좀 더 정확히 하면 업데이트용 객체가 따로 존재해야 한다.
    public void update(Long itemId,Item updateParam){
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
        findItem.setOpen(updateParam.getOpen());
        findItem.setRegions(updateParam.getRegions());
        findItem.setItemType(updateParam.getItemType());
        findItem.setDeliveryCode(updateParam.getDeliveryCode());
    }
    public void clearStore(){
        store.clear();
    }
}
