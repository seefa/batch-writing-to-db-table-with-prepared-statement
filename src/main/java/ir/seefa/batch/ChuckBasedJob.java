package ir.seefa.batch;

import ir.seefa.mapper.CustomerItemPreparedStatementSetter;
import ir.seefa.mapper.CustomerRowMapper;
import ir.seefa.model.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Saman Delfani
 * @version 1.0
 * @since 2022-07-31 02:32:59
 */
@Configuration
public class ChuckBasedJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    public static String INSERT_CUSTOMER_SQL = "INSERT INTO " +
            "`spring-batch`.CUSTOMERS_BACKUP(customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, addressLine2, city, state, postalCode, country, creditLimit)"
            + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

    public ChuckBasedJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
    }


    @Bean
    public PagingQueryProvider queryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
        factory.setSelectClause("SELECT customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, addressLine2, city, state, postalCode, country, salesRepEmployeeNumber, creditLimit");
        factory.setFromClause("FROM `spring-batch`.customers");
        factory.setSortKey("customerNumber");
        factory.setDataSource(dataSource);
        return factory.getObject();
    }

    @Bean
    public ItemReader<Customer> itemReader() throws Exception {
        return new JdbcPagingItemReaderBuilder<Customer>()
                .dataSource(dataSource)
                .name("jdbcPagingItemReader")
                .queryProvider(queryProvider())
                .pageSize(10)                           // MUST be equal to chunk size
                .rowMapper(new CustomerRowMapper())
                .build();
    }

    @Bean
    public ItemWriter<Customer> itemWriter() {
        return new JdbcBatchItemWriterBuilder<Customer>()
                .dataSource(dataSource)
                .sql(INSERT_CUSTOMER_SQL)
                .itemPreparedStatementSetter(new CustomerItemPreparedStatementSetter())
                .build();
    }

    @Bean
    public Step chunkBasedWritingToDbTableWithPreparedStatementStep() throws Exception {
        return this.stepBuilderFactory.get("chunkBasedWritingToDbTableWithPreparedStatementStep")
                .<Customer, Customer>chunk(10)                  // Must be equal to queryProvider page size
                .reader(itemReader())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public Job chuckOrientedJob() throws Exception {
        return this.jobBuilderFactory.get("chunkOrientedWritingToDbTableWithPreparedStatementJob")
                .start(chunkBasedWritingToDbTableWithPreparedStatementStep())
                .build();

    }
}
