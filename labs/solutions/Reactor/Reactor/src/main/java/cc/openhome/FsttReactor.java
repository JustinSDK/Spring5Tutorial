package cc.openhome;

import java.util.Arrays;
import java.util.List;

import reactor.core.publisher.Flux;

import static java.lang.System.out;

public class FsttReactor {
	public static void main(String[] args) {
		List<String> skillSource = Arrays.asList("java", "python", "javascript");
		
		Flux<String> skills = Flux.fromIterable(skillSource);
		Flux<String> upperSkills = skills.map(String::toUpperCase);
		Flux<String> chars = upperSkills.flatMap(skill -> Flux.fromArray(skill.split("")));
		
		chars.subscribe(out::print);
		out.println();
		upperSkills.subscribe(out::println);
		skills.subscribe(out::print);
	}
}
