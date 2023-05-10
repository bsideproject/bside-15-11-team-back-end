package beside15th11team.beside15th11team.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/hello")
@Slf4j
public class HelloController {
	private final HelloRepository helloRepository;

	public HelloController(HelloRepository helloRepository) {
		this.helloRepository = helloRepository;
	}

	@GetMapping
	public String hello() {
		log.info("success! -> {}",helloRepository.findById(1l).get().toString());
		return "OK";
	}
}
