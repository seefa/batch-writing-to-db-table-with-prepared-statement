package ir.seefa;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Saman Delfani
 * @version 1.0
 * @since 2022-07-31 00:52:09
 */
@SpringBootApplication
@EnableBatchProcessing
public class BatchWritingToDbTableWithPreparedStatementApplication {
    public static void main(String[] args) {
        SpringApplication.run(BatchWritingToDbTableWithPreparedStatementApplication.class, args);
    }
}
