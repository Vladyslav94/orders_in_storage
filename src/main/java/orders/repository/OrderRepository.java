package orders.repository;

import orders.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Modifying
    @Query("delete from Order b WHERE b.timeStamp < :currentTime")
    void deleteEveryTenMinutes(@Param("currentTime") long currentTime);

    @Query("from Order b WHERE b.item=:item")
    Page<Order> findByItem(@Param("item")String item, Pageable pageable);
}
