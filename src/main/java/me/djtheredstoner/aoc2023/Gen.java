package me.djtheredstoner.aoc2023;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Gen {

    public static void main(String[] args) throws Exception {
        for (int day = 1; day <= 25; day++) {
            String file = """
                package me.djtheredstoner.aoc2023.days;
                
                import me.djtheredstoner.aoc2023.DayBase;
                
                import java.util.List;
                
                public class Day%% implements DayBase {
                    
                    public void init(List<String> lines) {
                        
                    }
                    
                    public void part1(List<String> lines) {
                        
                    }
                    
                    public void part2(List<String> lines) {
                        
                    }
                    
                    public static void main(String...args) {
                        new Day%%().test(1);
                        new Day%%().test(2);
                        new Day%%().run();
                    }
                }
                """.replace("%%", day + "");

            Path p = Path.of("src", "main", "java", "me", "djtheredstoner", "aoc2023", "days", "Day" + day + ".java");

            maybeCreate(Path.of("inputs", day + ".test.1.txt"));
            maybeCreate(Path.of("inputs", day + ".test.2.txt"));

            if (!Files.exists(p)) {
                Files.writeString(p, file);
            }
        }
    }

    private static void maybeCreate(Path p) throws Exception {
        if (!Files.exists(p))
            Files.createFile(p);
    }

}
