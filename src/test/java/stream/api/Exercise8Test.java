package stream.api;

import common.test.tool.annotation.Difficult;
import common.test.tool.dataset.ClassicOnlineStore;
import common.test.tool.entity.Customer;
import common.test.tool.entity.Item;
import common.test.tool.entity.Shop;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

public class Exercise8Test extends ClassicOnlineStore {

    @Difficult
    @Test
    public void itemsNotOnSale() {
        Stream<Customer> customerStream = this.mall.getCustomerList().stream();
        Stream<Shop> shopStream = this.mall.getShopList().stream();

        List<String> availableItems = shopStream
                .flatMap(s -> s.getItemList().stream())
                .map(Item::getName)
                .collect(Collectors.toList());

        Set<String> unavailableItems = customerStream
                .flatMap(c -> c.getWantToBuy().stream())
                .map(Item::getName)
                .filter(name -> !availableItems.contains(name))
                .collect(Collectors.toSet());

        assertThat(unavailableItems, hasSize(3));
        assertThat(unavailableItems, hasItems("bag", "pants", "coat"));
    }

    @Difficult
    @Test
    public void havingEnoughMoney() {
        Stream<Customer> customerStream = this.mall.getCustomerList().stream();
        Stream<Shop> shopStream = this.mall.getShopList().stream();

        Map<String, Integer> lowestPrices = shopStream
                .flatMap(s -> s.getItemList().stream())
                .collect(Collectors.toMap(
                        Item::getName,
                        Item::getPrice,
                        Math::min
                ));

        List<String> affordableBuyers = customerStream
                .filter(c -> {
                    int requiredMoney = c.getWantToBuy().stream()
                            .mapToInt(i -> lowestPrices.getOrDefault(i.getName(), 0))
                            .sum();
                    return c.getBudget() >= requiredMoney;
                })
                .map(Customer::getName)
                .collect(Collectors.toList());

        assertThat(affordableBuyers, hasSize(7));
        assertThat(affordableBuyers, hasItems("Joe", "Patrick", "Chris", "Kathy", "Alice", "Andrew", "Amy"));
    }
}