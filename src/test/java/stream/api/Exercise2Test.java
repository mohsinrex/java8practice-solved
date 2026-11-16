package stream.api;

import common.test.tool.annotation.Easy;
import common.test.tool.dataset.ClassicOnlineStore;
import common.test.tool.entity.Customer;
import common.test.tool.entity.Item;
import common.test.tool.util.AssertUtil;

import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

public class Exercise2Test extends ClassicOnlineStore {

    @Easy
    @Test
    public void sortByAge() {
        List<Customer> customerList = this.mall.getCustomerList();

        Stream<Integer> ageAscending = customerList.stream()
                .map(Customer::getAge)
                .sorted();

        List<Integer> result = ageAscending.collect(Collectors.toList());
        assertThat(result, contains(21, 22, 22, 26, 27, 28, 32, 35, 36, 38));
    }

    @Easy
    @Test
    public void descSortByAge() {
        List<Customer> customerList = this.mall.getCustomerList();

        Comparator<Integer> reverseOrder = (x, y) -> y.compareTo(x);
        Stream<Integer> ageDescending = customerList.stream()
                .map(Customer::getAge)
                .sorted(reverseOrder);

        assertTrue(AssertUtil.isLambda(reverseOrder));
        List<Integer> result = ageDescending.collect(Collectors.toList());
        assertThat(result, contains(38, 36, 35, 32, 28, 27, 26, 22, 22, 21));
    }

    @Easy
    @Test
    public void top3RichCustomer() {
        List<Customer> customerList = this.mall.getCustomerList();

        Stream<String> topThree = customerList.stream()
                .sorted((c1, c2) -> c2.getBudget() - c1.getBudget())
                .limit(3)
                .map(Customer::getName);

        List<String> result = topThree.collect(Collectors.toList());
        assertThat(result, contains("Diana", "Andrew", "Chris"));
    }

    @Easy
    @Test
    public void distinctAge() {
        List<Customer> customerList = this.mall.getCustomerList();

        Stream<Integer> uniqueAges = customerList.stream()
                .map(Customer::getAge)
                .distinct();

        List<Integer> result = uniqueAges.collect(Collectors.toList());
        assertThat(result, contains(22, 27, 28, 38, 26, 32, 35, 21, 36));
    }

    @Easy
    @Test
    public void itemsCustomersWantToBuy() {
        List<Customer> customerList = this.mall.getCustomerList();

        Function<Customer, Stream<Item>> itemStreamMapper = c -> c.getWantToBuy().stream();
        Stream<String> allWantedItems = customerList.stream()
                .flatMap(itemStreamMapper)
                .map(Item::getName);

        assertTrue(AssertUtil.isLambda(itemStreamMapper));
        List<String> result = allWantedItems.collect(Collectors.toList());
        assertThat(result,
                contains("small table", "plate", "fork", "ice cream", "screwdriver", "cable", "earphone", "onion",
                        "ice cream", "crisps", "chopsticks", "cable", "speaker", "headphone", "saw", "bond",
                        "plane", "bag", "cold medicine", "chair", "desk", "pants", "coat", "cup", "plate", "fork",
                        "spoon", "ointment", "poultice", "spinach", "ginseng", "onion"));
    }
}