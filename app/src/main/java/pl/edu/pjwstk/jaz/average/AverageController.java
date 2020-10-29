package pl.edu.pjwstk.jaz.average;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class AverageController {

    @GetMapping("average")
    public String getAverage(@RequestParam(value = "numbers", required = false) String numbers) {
        if (numbers.isEmpty()) {
            return "Please put parameters.";
        }
        List<String> items = Arrays.asList(numbers.split("\\s*,\\s*"));
        int sumOfAllNumbers = 0;
        for (String item : items) {
            sumOfAllNumbers += Integer.parseInt(item);
        }
        double avg = (double) sumOfAllNumbers / (double) items.size();
        avg = (int)(avg * 100.0) / 100.0;
        if (avg == (int) avg) {
            return "Average equals: " + (int) avg;
        }
        else {
            return "Average equals: " + avg;
        }
    }
}
