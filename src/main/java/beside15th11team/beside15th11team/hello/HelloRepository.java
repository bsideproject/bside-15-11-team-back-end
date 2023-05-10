package beside15th11team.beside15th11team.hello;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HelloRepository extends JpaRepository<Hello, Long> {
	Optional<Hello> findById(Long id);
}
