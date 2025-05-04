package com.trungduc.drinkshop.controller.admin;

import com.trungduc.drinkshop.controller.common.BaseController;
import com.trungduc.drinkshop.dto.DrinkSearchDTO;
import com.trungduc.drinkshop.entity.Drink;
import com.trungduc.drinkshop.entity.Category;
import com.trungduc.drinkshop.service.DrinkService;
import com.trungduc.drinkshop.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/admin/drinks_management")
public class AdminDrinkController extends BaseController {

    private final DrinkService drinkService;
    private final CategoryService categoryService;


    @GetMapping
    public String showDrinksPage(Model model,
                                @RequestParam(name = "page", defaultValue = "1") int page,
                                @RequestParam(name = "size", defaultValue = "4") int size,
                                @ModelAttribute("search") DrinkSearchDTO search) {
        Page<Drink> drinkPage = drinkService.searchDrinks(search, PageRequest.of(page - 1, size));
        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("drinkPage", drinkPage);
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", drinkPage.getTotalPages());

        return "admin/drinks";
    }


    @GetMapping("/add")
    public String showAddDrinkForm(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("drink", new Drink());
        return "admin/drinks_add_or_update";
    }

    @PostMapping("/add")
    public String addOrUpdateDrink(@ModelAttribute("drink") @Valid Drink drink,
                                  BindingResult bindingResult,
                                  @RequestParam("cover_image") MultipartFile coverImage,
                                  Model model
            , RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            model.addAttribute("error", "Đã có lỗi xảy ra vui lòng nhập lại");
            return "admin/drinks_add_or_update";
        }


        if (drink.getId() != null) {
            // Check if there is an existing drink with the given ID
            Drink existingDrink = drinkService.getDrinkById(drink.getId());
            if (existingDrink != null) {
                // Update the drink with new data
                if (drink.getPublishedDate() == null) {
                    drink.setPublishedDate(existingDrink.getPublishedDate());
                }
                if (coverImage.isEmpty()) {
                    drink.setCoverImage(existingDrink.getCoverImage());
                }

                drinkService.editDrink(drink, coverImage);
                Drink editedDrink = drinkService.getDrinkById(drink.getId());
                model.addAttribute("drink", editedDrink);
                redirectAttributes.addFlashAttribute("message", "Sửa thông tin đồ uống thành công!");
            }
        } else {
            Drink exist = drinkService.getDrinkByName(drink.getTitle());

            if (exist != null) {
                model.addAttribute("error", "Đã tồn tại đồ uống với tên này");
                return "admin/drinks_add_or_update";
            } else drinkService.addDrink(drink, coverImage);
            redirectAttributes.addFlashAttribute("message", "Thêm đồ uống thành công!");
        }

        return "redirect:/admin/drinks_management/add";
    }


    @GetMapping("/edit/{id}")
    public String showEditDrinkForm(@PathVariable Long id, Model model) {
        Drink drink = drinkService.getDrinkById(id);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("drink", drink);
        model.addAttribute("categories", categories);

        return "admin/drinks_add_or_update";
    }


    @GetMapping("/delete/{id}")
    public String deleteDrink(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean activeFlag, RedirectAttributes redirectAttributes) {
        try{
            drinkService.setActiveFlag(id, activeFlag);
            // Add a success message to the model
            String action = activeFlag ? "kích hoạt lại trạng thái đang bán cho" : "cập nhật trạng thái không bán cho";
            redirectAttributes.addFlashAttribute("message", action + " đồ uống thành công!");

            return "redirect:/admin/drinks_management";
        } catch (Exception ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/admin/drinks_management";
        }


    }


}
