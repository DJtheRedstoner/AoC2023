package me.djtheredstoner.aoc2023;

import java.nio.file.Files;
import java.nio.file.Path;

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
                        new Day%%().run();
                    }
                }
                """.replace("%%", day + "");

            Path p = Path.of("src", "main", "java", "me", "djtheredstoner", "aoc2023", "days", "Day" + day + ".java");

            if (!Files.exists(p)) {
                Files.writeString(p, file);
            }
        }
    }

}
