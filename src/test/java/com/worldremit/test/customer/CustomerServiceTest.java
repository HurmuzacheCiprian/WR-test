package com.worldremit.test.customer;

import org.junit.jupiter.api.Test;

import static com.worldremit.test.customer.MembershipType.*;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerServiceTest {

    private final CustomerService underTest = new CustomerService();

    @Test
    public void shouldCreateACustomerIfTheCustomerDoesNotExist() {
        assertNotNull(underTest.createCustomer("new-email"));
    }

    @Test
    public void shouldReturnCustomerIfCustomerIdExists() {
        final String testEmail = "test-email";
        underTest.createCustomer(testEmail);
        assertThrows(DuplicateCustomerEmailException.class, () -> underTest.createCustomer(testEmail));
    }

    @Test
    public void shouldThrowCustomerNotFoundExceptionIfCustomerDoesNotExist() {
        assertThrows(CustomerNotFoundException.class, () -> underTest.getCustomer("fake-id"));
    }

    @Test
    public void shouldActivateBookClubMembershipIfCurrentMembershipIsNone() {
        final String testEmail = "test-email";
        Customer createdCustomer = underTest.createCustomer(testEmail);

        assertEquals(NONE, createdCustomer.getMembershipType());
        underTest.activateMembership(createdCustomer.getId(), BOOK_CLUB);
        assertEquals(BOOK_CLUB, createdCustomer.getMembershipType());
    }

    @Test
    public void shouldActivatePremiumMembershipIfCurrentMembershipIsBookClubAndCustomerSubscribesToVideoClub() {
        final String testEmail = "test-email";
        Customer createdCustomer = underTest.createCustomer(testEmail);

        assertEquals(NONE, createdCustomer.getMembershipType());
        underTest.activateMembership(createdCustomer.getId(), BOOK_CLUB);
        underTest.activateMembership(createdCustomer.getId(), VIDEO_CLUB);
        assertEquals(PREMIUM, createdCustomer.getMembershipType());
    }

    @Test
    public void shouldActivatePremiumMembershipIfCurrentMembershipIsVideoClubAndCustomerSubscribesToBookClub() {
        final String testEmail = "test-email";
        Customer createdCustomer = underTest.createCustomer(testEmail);

        assertEquals(NONE, createdCustomer.getMembershipType());
        underTest.activateMembership(createdCustomer.getId(), VIDEO_CLUB);
        underTest.activateMembership(createdCustomer.getId(), BOOK_CLUB);
        assertEquals(PREMIUM, createdCustomer.getMembershipType());
    }

    @Test
    public void shouldReturnFalseIfCustomerTriesToActivateSameMembership() {
        final String testEmail = "test-email";
        Customer createdCustomer = underTest.createCustomer(testEmail);

        assertEquals(NONE, createdCustomer.getMembershipType());
        boolean activatedMemberShip = underTest.activateMembership(createdCustomer.getId(), VIDEO_CLUB);
        assertTrue(activatedMemberShip);
        boolean activatedDuplicateMembership = underTest.activateMembership(createdCustomer.getId(), VIDEO_CLUB);
        assertFalse(activatedDuplicateMembership);
    }

}