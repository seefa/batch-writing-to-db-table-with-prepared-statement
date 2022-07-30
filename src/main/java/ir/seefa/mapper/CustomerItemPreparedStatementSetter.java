package ir.seefa.mapper;

import ir.seefa.model.Customer;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Saman Delfani
 * @version 1.0
 * @since 7/31/22 T 02:26
 */
public class CustomerItemPreparedStatementSetter implements ItemPreparedStatementSetter<Customer> {

    @Override
    public void setValues(Customer item, PreparedStatement ps) throws SQLException {
        ps.setInt(1, item.getCustomerNumber());
        ps.setString(2, item.getCustomerName());
        ps.setString(3, item.getContactLastName());
        ps.setString(4, item.getContactFirstName());
        ps.setString(5, item.getPhone());
        ps.setString(6, item.getAddressLine1());
        ps.setString(7, item.getAddressLine2());
        ps.setString(8, item.getCity());
        ps.setString(9, item.getState());
        ps.setString(10, item.getPostalCode());
        ps.setString(11, item.getCountry());
//        ps.setInt(12, item.getSalesRepEmployeeNumber());
        ps.setDouble(12, item.getCreditLimit());
    }
}
