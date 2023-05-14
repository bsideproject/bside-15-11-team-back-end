package beside15th11team.beside15th11team.hello;

import org.springframework.stereotype.Service;

@Service
public class HelloService {
	private final HelloRepository helloRepository;

	public HelloService(HelloRepository helloRepository) {
		this.helloRepository = helloRepository;
	}

	public Hello hello() {
		return helloRepository.findById(1l).get();
	}
}
