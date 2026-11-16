package stream.api;

import common.test.tool.annotation.Difficult;
import common.test.tool.annotation.Easy;
import common.test.tool.dataset.ClassicOnlineStore;
import common.test.tool.entity.Customer;
import common.test.tool.entity.Item;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class Exercise9Test extends ClassicOnlineStore {

    @Easy
    @Test
    public void simplestStringJoin() {
        List<Customer> customerList = this.mall.getCustomerList();

        Supplier<Object> joinSupplier = () -> new StringJoiner(",");
        Collector<String, StringJoiner, String> joinCollector = Collector.of(
                () -> (StringJoiner) joinSupplier.get(),
                StringJoiner::add,
                StringJoiner::merge,
                StringJoiner::toString
        );

        String output = customerList.stream().map(Customer::getName).collect(joinCollector);
        assertThat(output, is("Joe,Steven,Patrick,Diana,Chris,Kathy,Alice,Andrew,Martin,Amy"));
    }

    @Difficult
    @Test
    public void mapKeyedByItems() {
        List<Customer> customerList = this.mall.getCustomerList();

        Collector<Customer, Map<String, Set<String>>, Map<String, Set<String>>> itemCollector = Collector.of(
                HashMap::new,
                (map, cust) -> {
                    for (Item it : cust.getWantToBuy()) {
                        map.computeIfAbsent(it.getName(), k -> new HashSet<>()).add(cust.getName());
                    }
                },
                (map1, map2) -> {
                    map2.forEach((k, v) -> {
                        map1.merge(k, v, (set1, set2) -> {
                            set1.addAll(set2);
                            return set1;
                        });
                    });
                    return map1;
                }
        );

        Map<String, Set<String>> resultMap = customerList.stream().parallel().collect(itemCollector);

        assertThat(resultMap.get("plane"), containsInAnyOrder("Chris"));
        assertThat(resultMap.get("onion"), containsInAnyOrder("Patrick", "Amy"));
        assertThat(resultMap.get("ice cream"), containsInAnyOrder("Patrick", "Steven"));
        assertThat(resultMap.get("earphone"), containsInAnyOrder("Steven"));
        assertThat(resultMap.get("plate"), containsInAnyOrder("Joe", "Martin"));
        assertThat(resultMap.get("fork"), containsInAnyOrder("Joe", "Martin"));
        assertThat(resultMap.get("cable"), containsInAnyOrder("Diana", "Steven"));
        assertThat(resultMap.get("desk"), containsInAnyOrder("Alice"));
    }
}