package ru.veryprosto.nakhodkaT.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping()
public class NakhodkaController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getSum(
            @RequestParam(name = "a", required = false) String a,
            @RequestParam(name = "b", required = false) String b) {
        int sum = 0;
        try {
            sum = Integer.parseInt(a) + Integer.parseInt(b);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request, please enter numbers");
        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("sum", sum));
    }

  /* //вариант без проверки, спринг бут рест контроллер имеет встроенные механизмы конвертиции в json и обратно.
  @PostMapping
    public ResponseEntity<Object> getCountByArray(@RequestBody Map<String, int[]> message) {
        int[] arr = message.get("list");
        Map<String, Integer> map = new HashMap<>();
        map.put("count", arr.length);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }*/

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)//вариант с проверкой, решил проверять не на число, а на то приходит ли массив с нужным именем.
    public ResponseEntity<Object> getCountByArray(@RequestBody String jsonMessage) {
        JSONArray arr = null;
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonMessage);
            arr = (JSONArray) jsonObject.get("list");
            if (arr==null) throw new ParseException(2);
        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request, please enter array with name 'list'");
        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("count", arr.size()));
    }
}


//fetch('/message', {method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify({"list":[1,2,3,4,5,6,7,8,9]})}).then(console.log)
