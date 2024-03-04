package myproject1.trie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TrieApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrieApplication.class, args);
	}

}
