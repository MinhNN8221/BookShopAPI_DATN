package com.example.bookshopapi.service;

import com.cloudinary.Cloudinary;
import com.example.bookshopapi.entity.Customer;
import com.example.bookshopapi.repository.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Service
@RequiredArgsConstructor
public class CustomerService implements UserDetailsService {
    private final Cloudinary cloudinary;
    @Autowired
    private CustomerRepo customerRepo;

    public Customer save(Customer customer) {
        return customerRepo.save(customer);
    }

    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();
        for (Customer customer : customerRepo.findAll())
            if (!customer.getRole().equals("admin"))
                customers.add(customer);
        return customers;
    }

    public boolean isEmailExists(String email) {
        return customerRepo.existsByEmail(email);
    }

    public Customer findByEmail(String email) {
        return customerRepo.findByEmail(email);
    }

    public Customer findById(int customerId) {
        return customerRepo.findById(customerId);
    }

    public String uploadFile(MultipartFile multipartFile, String folderName) throws IOException {
//        MultilpartFile: cung cấp các phương thức để có thể truy cập dữ liệu của tệp trực tiếp
//        thông qua InputStream và lấy thông tin về tên tệp và kiểu MIME
//        kiểu MIME: chuỗi ký tự dùng để định danh loại nội dung của một tệp hoặc dữ liệu,
//        giúp máy tính và trình duyệt hiểu cách xử lý dữ liệu cụ thể và cung cấp cho máy chủ
//        và máy khách thông tin về loại dữ liệu đang truyền qua mạng
        Map<String, Object> uploadParams = new HashMap<>();
        uploadParams.put("public_id", UUID.randomUUID().toString());
        uploadParams.put("folder", folderName);

        return cloudinary.uploader()
                .upload(multipartFile.getBytes(), uploadParams)  //chuyển đổi tệp đa phương tiện thành mảng byte sau đó upload
                .get("url")                                     //truy xuất URL của tệp
                .toString();
    }

    public Customer getCustomer(int id) {
        return customerRepo.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Customer customer = customerRepo.findByEmail(s);
        if (customer == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(customer.getName(), customer.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+customer.getRole().toUpperCase())));
    }

//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        Customer customer = customerRepo.findByEmail(s);
//        if (customer == null) {
//            throw new UsernameNotFoundException("User not found");
//        }
//        return new org.springframework.security.core.userdetails.User(customer.getName(), customer.getPassword(), new ArrayList<>());
//    }
}
