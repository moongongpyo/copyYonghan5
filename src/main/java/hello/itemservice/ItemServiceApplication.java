package hello.itemservice;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@RequiredArgsConstructor
public class ItemServiceApplication {
	private final ItemRepository itemRepository;


	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}
	/*@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new
				ResourceBundleMessageSource();
		messageSource.setBasenames("messages", "errors");
		messageSource.setDefaultEncoding("utf-8");
		return messageSource;
	}*/

	@PostConstruct
	private void init(){
		itemRepository.save(new Item("itemA",10000,10));
		itemRepository.save(new Item("itemB",20000,20));
	}
}
