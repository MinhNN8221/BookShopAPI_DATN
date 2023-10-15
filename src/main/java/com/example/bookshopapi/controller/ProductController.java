package com.example.bookshopapi.controller;

import com.example.bookshopapi.config.jwt.JwtUtil;
import com.example.bookshopapi.dto.objectdto.authordto.AuthorDto;
import com.example.bookshopapi.dto.objectdto.bookdto.*;
import com.example.bookshopapi.dto.objectdto.supplierdto.SupplierDto;
import com.example.bookshopapi.dto.response.Error;
import com.example.bookshopapi.dto.response.book.*;
import com.example.bookshopapi.entity.Book;
import com.example.bookshopapi.service.ProductService;
import com.example.bookshopapi.service.WishListItemService;
import com.example.bookshopapi.util.BookUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/products")
@AllArgsConstructor
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private WishListItemService wishListItemService;

    @GetMapping("/hello")
    public String hello(@RequestParam("name") String name,
                        @RequestParam(required = false, defaultValue = "") String email) {
        if (email != "") {
            return "hellO " + name + " " + email;
        } else {
            return "hello " + name;
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestParam("page") int page,
                                    @RequestParam("limit") int limit,
                                    @RequestParam("description_length") int descriptionLength) {

        List<Book> products = productService.getProducts(page, limit, descriptionLength);
        List<BookDto> bookDtos = new BookUtil().addBook(products);
        BookResponse response = new BookResponse(products.size(), bookDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/new")
    public ResponseEntity<?> getProductsNew() {
        List<Book> products = productService.getProductsNew();
        List<BookHotNewDto> bookHotNewDtos = new BookUtil().addBookNewHot(products);
        BookHotNewResponse response = new BookHotNewResponse(products.size(), bookHotNewDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hot")
    public ResponseEntity<?> getProductsHot() {
        List<Book> products = productService.getProductsHot();
        List<BookHotNewDto> bookHotNewDtos = new BookUtil().addBookNewHot(products);
        BookHotNewResponse response = new BookHotNewResponse(products.size(), bookHotNewDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recommend")
    public ResponseEntity<?> getProductsRecomend() {
        List<Book> products = productService.getProductsBanner();
        List<BookBannerDto> bookBannerDtos = new BookUtil().addBookBanner(products);
        BookBannerResponse response = new BookBannerResponse(bookBannerDtos.size(), bookBannerDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/incategory/{categoryId}")
    public ResponseEntity<?> getProductsByCategory(
            @PathVariable int categoryId,
            @RequestParam("description_length") int descriptionLength,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit) {
        Page<Book> products = productService.findProductsByCategory(
                categoryId, descriptionLength, page, limit);
        List<BookHotNewDto> bookByCategory = new BookUtil().addBookNewHot(products.getContent());
        BookInCategoryResponse response = new BookInCategoryResponse(bookByCategory.size(), bookByCategory);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/supplier")
    public ResponseEntity<?> getProductBySupplier(@RequestParam("supplier_id") int supplierId,
                                                  @RequestParam("limit") int limit,
                                                  @RequestParam("page") int page,
                                                  @RequestParam("description_length") int descriptionLength) {
        Page<Book> books = productService.findProductsBySupplier(supplierId, limit, page, descriptionLength);
        List<BookDto> bookBySupplier = new BookUtil().addBook(books.getContent());
        BookResponse response = new BookResponse(bookBySupplier.size(), bookBySupplier);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/author")
    public ResponseEntity<?> getProductByAuthor(@RequestParam("author_id") int authorId,
                                                @RequestParam("limit") int limit,
                                                @RequestParam("page") int page,
                                                @RequestParam("description_length") int descriptionLength) {
        Page<Book> books = productService.findProductsByAuthor(authorId, limit, page, descriptionLength);
        List<BookDto> bookByAuthor = new BookUtil().addBook(books.getContent());
        BookResponse response = new BookResponse(bookByAuthor.size(), bookByAuthor);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getSearchProduct(@RequestParam("limit") int limit,
                                              @RequestParam("page") int page,
                                              @RequestParam("description_length") int descriptionLength,
                                              @RequestParam("query_string") String queryString,
                                              @RequestParam("filter_type") int filterType,
                                              @RequestParam("price_sort_order") String priceSortOrder) {
        Page<Book> books;
        List<BookDto> bookDtos = new ArrayList<>();

        if (filterType == 1) {
            books = productService.getBooksNew(limit, page, descriptionLength, queryString);

        } else if (filterType == 2) {
            books = productService.getBooksSelling(limit, page, descriptionLength, queryString);
        } else {
            books = productService.getBooksPriceSortOrder(limit, page, descriptionLength, queryString, priceSortOrder);
        }
        bookDtos = new BookUtil().addBook(books.getContent());
        BookResponse response = new BookResponse(bookDtos.size(), bookDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/author/search")
    public ResponseEntity<?> getSearchProductsByAuthor(@RequestParam("author_id") int authorId,
                                                       @RequestParam("limit") int limit,
                                                       @RequestParam("page") int page,
                                                       @RequestParam("description_length") int descriptionLength,
                                                       @RequestParam("query_string") String queryString) {
        Page<Book> books = productService.searchBooksByAuthor(authorId, limit, page, descriptionLength, queryString);
        List<BookDto> bookDtos = new BookUtil().addBook(books.getContent());
        BookResponse response = new BookResponse(bookDtos.size(), bookDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/search")
    public ResponseEntity<?> getSearchProductsByCategory(@RequestParam("category_id") int categoryId,
                                                         @RequestParam("limit") int limit,
                                                         @RequestParam("page") int page,
                                                         @RequestParam("description_length") int descriptionLength,
                                                         @RequestParam("query_string") String queryString) {
        Page<Book> books = productService.searchBooksByCategory(categoryId, limit, page, descriptionLength, queryString);
        List<BookDto> bookDtos = new BookUtil().addBook(books.getContent());
        BookResponse response = new BookResponse(bookDtos.size(), bookDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/supplier/search")
    public ResponseEntity<?> getSearchProductsBySupplier(@RequestParam("supplier_id") int supplierId,
                                                         @RequestParam("limit") int limit,
                                                         @RequestParam("page") int page,
                                                         @RequestParam("description_length") int descriptionLength,
                                                         @RequestParam("query_string") String queryString) {
        Page<Book> books = productService.searchBooksBySupplier(supplierId, limit, page, descriptionLength, queryString);
        List<BookDto> bookDtos = new BookUtil().addBook(books.getContent());
        BookResponse response = new BookResponse(bookDtos.size(), bookDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{product_id}")
    public ResponseEntity<?> getProductDetail(@RequestHeader("user-key") String userKey,
                                              @PathVariable("product_id") int productId) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int customerId = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            List<Book> booksInWishlist = wishListItemService.getAllBooksInWishlist(customerId);
            Book book = productService.findById(productId);
            BookDetailDto bookDetailDto = new BookUtil().addBookDetailDto(book, booksInWishlist);
            SupplierDto supplierDto = new SupplierDto(book.getSupplier().getId(), book.getSupplier().getName());
            AuthorDto authorDto = new AuthorDto(book.getAuthor().getId(), book.getAuthor().getName());
            BookDetailResponse response = new BookDetailResponse(bookDetailDto, supplierDto, authorDto);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }
}