package ru.clevertec.ecl.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.exception.NotValidRequestParametersException;
import ru.clevertec.ecl.exception.RequestBodyIncorrectException;
import ru.clevertec.ecl.exception.ResourceNotFountException;
import ru.clevertec.ecl.repository.CertificateRepository;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.CertificateService;
import ru.clevertec.ecl.util.SortCreator;
import ru.clevertec.ecl.entity.GiftCertificate;

import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Класс реализующий слой контроллера для запросов по GiftCertificate
 */

@AllArgsConstructor
@RestController
@RequestMapping(value = "/cert")
public class CertificateController {
    private static final String ITEM_ON_PAGE = "5";
    private final static Map<String, String> ACCORDANCE = Map.of("name", "name", "date", "createDate");
    private final static Map<String, String> ACCORDANCE_NATIVE = Map.of("name", "name", "date", "create_date");

    @Autowired
    private CertificateRepository certRepo;
    @Autowired
    private TagRepository tagRepo;
    @Autowired
    private CertificateService certService;

    @GetMapping("/{id}")
    public ResponseEntity findCertificate(@PathVariable long id) {
        return new ResponseEntity<>(certRepo.findById(id).orElseThrow(() -> new ResourceNotFountException("id = " + id, 40402)),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity findAllCertificate(@RequestParam Optional<Integer> page,
                                             @RequestParam(defaultValue = ITEM_ON_PAGE) int size,
                                             @RequestParam(name = "sort") Optional<List<String>> sortParams) {
        Sort sort = SortCreator.getSpringSort(sortParams, ACCORDANCE);
        Pageable pageable = page.isPresent() ? PageRequest.of(page.get(), size, sort) : PageRequest.ofSize(Integer.MAX_VALUE).withSort(sort);
        Page<GiftCertificate> certs = certRepo.findAll(pageable);
        return new ResponseEntity<>(certs.getContent(), HttpStatus.OK);
    }


    @GetMapping("/has")
    public ResponseEntity findAllCertificateWithSearch(@RequestParam(defaultValue = "") String field,
                                                       @RequestParam(defaultValue = "") String text,
                                                       @RequestParam(name = "sort") Optional<List<String>> sortParam) {
        Sort sort = SortCreator.getSpringSort(sortParam, ACCORDANCE);
        GiftCertificate cert = new GiftCertificate();
        ExampleMatcher CertExampleMatcher = ExampleMatcher.matchingAny().withIgnoreNullValues();
        switch (field) {
            case "name": {
                cert.setName(text);
                CertExampleMatcher = CertExampleMatcher.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.ignoreCase());
                break;
            }
            case "description": {
                cert.setDescription(text);
                CertExampleMatcher = CertExampleMatcher.withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
                break;
            }
            default: {
                throw new NotValidRequestParametersException(" Field for search is incorrect", 40002);
            }
        }
        Example<GiftCertificate> certExample = Example.of(cert, CertExampleMatcher);
        List<GiftCertificate> certs = certRepo.findAll(certExample, sort);
        return new ResponseEntity(certs, HttpStatus.OK);
    }

    @GetMapping("/tag/{name}")
    public ResponseEntity findCertificateWithTag(@PathVariable String name, @RequestParam(name = "sort") Optional<List<String>> sortParams) {
        Sort sort = SortCreator.getSpringSort(sortParams, ACCORDANCE_NATIVE);
        Pageable pageable = PageRequest.ofSize(Integer.MAX_VALUE).withSort(sort);
        List<GiftCertificate> certs = certRepo.findByTagName(name, pageable);
        return new ResponseEntity<>(certs, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity saveCertificate(@RequestBody Optional<GiftCertificate> certInRequest) {
        if (certInRequest.isEmpty()) {
            throw new RequestBodyIncorrectException("Data for save is absent", 40002);
        }
        GiftCertificate cert = certService.createCertificate(certInRequest.get());
        return new ResponseEntity<>(cert, HttpStatus.CREATED);

    }

    @PatchMapping
    public ResponseEntity updateCertFields(@RequestBody Optional<GiftCertificate> certInRequest) {
        if (certInRequest.isEmpty()) {
            throw new RequestBodyIncorrectException("Data for update is absent", 40002);
        }
        GiftCertificate cert = certService.updateCertAllFields(certInRequest.orElse(null));
        return new ResponseEntity<>(cert, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCertificate(@PathVariable long id) {
        certRepo.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}