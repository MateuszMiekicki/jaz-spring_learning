package pl.edu.pjwstk.jaz.average;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class AverageController {

    @GetMapping("average")
    public String getAverage(@RequestParam("numbers") String numbers) {
        List<String> items = Arrays.asList(numbers.split("\\s*,\\s*"));
        int sumOfAllNumbers = 0;
        for (int i = 0; i < items.size(); ++i) {
            sumOfAllNumbers += Integer.parseInt(items.get(i));
        }
        double avg = (double)sumOfAllNumbers / (double)items.size();
        return String.valueOf(avg);
    }
}
