package com.driver.controllers;

import com.driver.services.impl.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/payment")
public class PaymentController {
	
	@Autowired
    PaymentServiceImpl paymentService;

    @PostMapping("/pay")
    public Payment pay(@RequestParam Integer reservationId, @RequestParam Integer amountSent, @RequestParam String mode) throws Exception{
        //Attempt a payment of amountSent for reservationId using the given mode ("cASh", "card", or "upi")
        //If the amountSent is less than bill, throw "Insufficient Amount" exception, otherwise update payment attributes
        //If the mode contains a string other than "cash", "card", or "upi" (any character in uppercase or lowercase), throw "Payment mode not detected" exception.
        //Note that the reservationId always exists
        // get the bill amount for the reservation
        int billAmount = paymentService.getReservationBill(reservationId);

        // check if the amountSent is sufficient for the bill
        if (amountSent < billAmount) {
            throw new Exception("Insufficient Amount");
        }

        // validate the payment mode
        if (!mode.equalsIgnoreCase("cash") && !mode.equalsIgnoreCase("card") && !mode.equalsIgnoreCase("upi")) {
            throw new Exception("Payment mode not detected");
        }

        // process the payment using the PaymentServiceImpl
        Payment payment = paymentService.processPayment(reservationId, amountSent, mode);

        return payment;
    }
}
