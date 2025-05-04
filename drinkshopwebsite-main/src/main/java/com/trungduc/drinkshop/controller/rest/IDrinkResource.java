package com.trungduc.drinkshop.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
@Tag(name = "Get revenue controller", description = "Operations related to drink's management")
public interface IDrinkResource {

    @Operation(summary = "Get drink by id", description = "Get drink's information based on the given id in path variable")
    @GetMapping("/drinks/get/{drinkId}")
    ResponseEntity<?> getDrinkById(@PathVariable("drinkId") Long drinkId);

    @Operation(summary = "Get paginated drink list by  category id and sorting key", description = "Get paginated drink list's information by  category id and sorting key")
    @GetMapping("/drinks/get")
    ResponseEntity<?> getDrinkListPaginatedAndSorted(@RequestParam("sortBy") String sortBy,
                                                    @RequestParam("categoryId") Long categoryId,
                                                    @RequestParam(name = "page", defaultValue = "1") int page,
                                                    @RequestParam("size") int size);

}
