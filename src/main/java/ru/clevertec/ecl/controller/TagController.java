package ru.clevertec.ecl.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.ResourceNotFountException;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.util.SortCreator;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Класс реализующий слой контроллера для запросов по Tag
 */

@RestController
@RequestMapping(value = "/tag")
@AllArgsConstructor
public class TagController {
    private static final String ITEM_ON_PAGE = "10";
    private final static Map<String, String> ACCORDANCE = Map.of("name", "name");
    private TagRepository tagRepo;

    @GetMapping("/{id}")
    public ResponseEntity findTagById(@PathVariable long id) {
        return new ResponseEntity<>(tagRepo.findById(id).orElseThrow(() -> new ResourceNotFountException("id = " + id, 40401)),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getAll(@RequestParam Optional<Integer> page,
                                 @RequestParam(defaultValue = ITEM_ON_PAGE) int size,
                                 @RequestParam(name = "sort") Optional<List<String>> sortParams) {
        Sort sort = SortCreator.getSpringSort(sortParams, ACCORDANCE);
        Pageable pageable = page.isPresent() ? PageRequest.of(page.get(), size, sort) : PageRequest.ofSize(Integer.MAX_VALUE).withSort(sort);
        return new ResponseEntity(tagRepo.findAll(pageable).getContent(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity createTag(@RequestBody Tag tag) {
        return new ResponseEntity<>(tagRepo.save(tag), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity updateTag(@RequestBody Tag tag) {
        return new ResponseEntity<>(tagRepo.save(tag), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTag(@PathVariable long id) {
        tagRepo.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/popular/user/{id}")
    public ResponseEntity getMostUsedTagInOrder(@PathVariable long id) {
        return new ResponseEntity<>(tagRepo.findMostUsedTagMostExpensiveOrder(id)
                .orElseThrow(() -> new ResourceNotFountException("for id = " + id, 40401)), HttpStatus.OK);
    }
}
