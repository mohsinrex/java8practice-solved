package stream.api;

import common.test.tool.annotation.Easy;
import common.test.tool.dataset.ClassicOnlineStore;
import common.test.tool.entity.Customer;
import common.test.tool.entity.Shop;

import org.junit.Test;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class Exercise7Test extends ClassicOnlineStore {

    @Easy
    @Test
    public void averageAge() {
        List<Customer> customerList = this.mall.getCustomerList();

        IntStream ages = customerList.stream().mapToInt(Customer::getAge);
        OptionalDouble avgAge = ages.average();

        assertThat(avgAge.getAsDouble(), is(28.7));
    }

    @Easy
    @Test
    public void howMuchToBuyAllItems() {
        List<Shop> shopList = this.mall.getShopList();

        LongStream prices = shopList.stream()
                .flatMap(s -> s.getItemList().stream())
                .mapToLong(i -> i.getPrice());

        long totalCost = prices.sum();

        assertThat(totalCost, is(60930L));
    }
}