package com.apress.batch.chapter10.batch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import com.apress.batch.chapter10.configuration.ImportJobConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@JdbcTest
@SpringBatchTest
@ContextConfiguration(classes = {ImportJobConfiguration.class, CustomerItemValidator.class, AccountItemProcessor.class, BatchAutoConfiguration.class})
@TestPropertySource(properties = "debug=true")
@Disabled
class ImportCustomerUpdatesTests {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private DataSource dataSource;

	private JdbcOperations jdbcTemplate;

	@BeforeEach
	public void setUp() {
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
	}

	@Test
	void importCustomerTest() {
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("customerUpdateFile", "classpath:customerFile.csv")
				.toJobParameters();

		JobExecution jobExecution = this.jobLauncherTestUtils.launchStep("importCustomerUpdates", jobParameters);

		assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

		List<Map<String, String>> results = this.jdbcTemplate.query("select * from customer where id = 5", (rs, rowNum) -> {
			Map<String, String> item = new HashMap<>();

			item.put("customer_id", rs.getString("customer_id"));
			item.put("first_name", rs.getString("first_name"));
			item.put("middle_name", rs.getString("middle_name"));
			item.put("last_name", rs.getString("last_name"));
			item.put("address1", rs.getString("address1"));
			item.put("address2", rs.getString("address2"));
			item.put("city", rs.getString("city"));
			item.put("state", rs.getString("state"));
			item.put("postal_code", rs.getString("postal_code"));
			item.put("ssn", rs.getString("ssn"));
			item.put("email_address", rs.getString("email_address"));
			item.put("home_phone", rs.getString("home_phone"));
			item.put("cell_phone", rs.getString("cell_phone"));
			item.put("work_phone", rs.getString("work_phone"));
			item.put("notification_pref", rs.getString("notification_pref"));

			return item;
		});

		Map<String, String> result = results.get(0);

		assertEquals("5", result.get("customer_id"));
		assertEquals("Rozelle", result.get("first_name"));
		assertEquals("Heda", result.get("middle_name"));
		assertEquals("Farnill", result.get("last_name"));
		assertEquals("36 Ronald Regan Terrace", result.get("address1"));
		assertEquals("P.O. Box 33", result.get("address2"));
		assertEquals("Montgomery", result.get("city"));
		assertEquals("Alabama", result.get("state"));
		assertEquals("36134", result.get("postal_code"));
		assertEquals("832-86-3661", result.get("ssn"));
		assertEquals("tlangelay4@mac.com", result.get("email_address"));
		assertEquals("240-906-7652", result.get("home_phone"));
		assertEquals("907-709-2649", result.get("cell_phone"));
		assertEquals("316-510-9138", result.get("work_phone"));
		assertEquals("2", result.get("notification_pref"));
	}
}
