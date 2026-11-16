package stream.api;

import common.test.tool.annotation.Easy;
import common.test.tool.dataset.ClassicOnlineStore;
import common.test.tool.entity.Customer;

import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class Exercise3Test extends ClassicOnlineStore {

    @Easy
    @Test
    public void howManyItemsWanted() {
        List<Customer> customerList = this.mall.getCustomerList();

        long totalItems = customerList.stream()
                .flatMap(c -> c.getWantToBuy().stream())
                .count();

        assertThat(totalItems, is(32L));
    }

    @Easy
    @Test
    public void richestCustomer() {
        List<Customer> customerList = this.mall.getCustomerList();

        Comparator<Integer> naturalInt = Comparator.naturalOrder();
        Optional<Integer> maxBudget = customerList.stream()
                .map(Customer::getBudget)
                .max(naturalInt);

        assertThat(naturalInt.getClass().getSimpleName(), is("NaturalOrderComparator"));
        assertThat(maxBudget.get(), is(12000));
    }

    @Easy
    @Test
    public void youngestCustomer() {
        List<Customer> customerList = this.mall.getCustomerList();

        Comparator<Customer> byAge = Comparator.comparing(Customer::getAge);
        Optional<Customer> youngest = customerList.stream()
                .min(byAge);

        assertThat(youngest.get(), is(customerList.get(8)));
    }
}