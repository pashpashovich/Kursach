package com.example.bank.controller;

import com.example.bank.config.OurUserInfoDetails;
import com.example.bank.dao.UserRepository;
import com.example.bank.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/ShowUserInfo")
    public String showUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OurUserInfoDetails user = (OurUserInfoDetails) authentication.getPrincipal();
        System.out.println( user);
        return "home";
    }

//    @GetMapping
//    public String getCustomers(Model model) {
//        List<User> customerList = userRepository.findAll();
//        model.addAttribute("customers",customerList);
//        return "customer/customers";
//    }

//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public String getACustomer(@PathVariable String id, Model model) {
//        Customer customer = customerRepository.findById(Long.parseLong(id)).get();
//        List<Customer> customerList = customerRepository.findAll();
//        Transfer transfer = new Transfer();
//        model.addAttribute("transfer",transfer);
//        model.addAttribute("customer",customer);
//        model.addAttribute("customers",customerList);
//        return "customer/aCustomer";
//    }
//
//    @GetMapping("/new-customer")
//    public String displayEmployeeForm(Model model) {
//        Customer customer = new Customer();
//        model.addAttribute("customer",customer);
//        return "customer/new-customer";
//    }
//
//    @PostMapping("/save")
//    public String createEmployee(Customer customer, Model model) {
//        customerRepository.save(customer);
//        return "redirect:";
//    }
//
//    @RequestMapping(value = "/transfer/{fromId}", method = RequestMethod.POST)
//    public String transferAmount(@PathVariable String fromId, Transfer transfer, Model model) {
//
//        Customer toCustomer = customerRepository.findById(transfer.getToId()).get();
//        Customer fromCustomer = customerRepository.findById(Long.parseLong(fromId)).get();
//
//        if (fromCustomer.getBalance() >= transfer.getBalance()) {
//            customerRepository.updateCustomer(fromCustomer.getBalance() - transfer.getBalance(),
//                    fromCustomer.getCustomerId());
//            customerRepository.updateCustomer(toCustomer.getBalance() + transfer.getBalance(),
//                    toCustomer.getCustomerId());
//        }
//
//        model.addAttribute("customer",fromCustomer);
//
//        Transaction transaction = new Transaction(fromCustomer.getName(),
//                toCustomer.getName(),fromCustomer.getCustomerId(),
//                toCustomer.getCustomerId(),transfer.getBalance());
//
//        transactionRepository.save(transaction);
//
//        return "redirect:/customers";
//    }

}
