package com.apress.batch.chapter10.batch;

import com.apress.batch.chapter10.domain.AccountResultSetExtractor;
import com.apress.batch.chapter10.domain.Statement;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class AccountItemProcessor implements ItemProcessor<Statement, Statement> {

	@Autowired
	private final JdbcTemplate jdbcTemplate;

	public AccountItemProcessor(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Statement process(Statement item) throws Exception {

//		String memoryBuster = "memoryBuster";
//
//		for (int i = 0; i < 200; i++) {
//			memoryBuster += memoryBuster;
//		}

		item.setAccounts(this.jdbcTemplate.query(
				"select a.account_id," +
				" a.balance," +
				" a.last_statement_date," +
				" t.transaction_id," +
				" t.description," +
				" t.credit," +
				" t.debit," +
				" t.timestamp " +
				"from account a left join transaction t on a.account_id = t.account_account_id " +
				"where a.account_id in " +
				"(select account_account_id from customer_account where customer_customer_id = ?) " +
				"order by a.account_id, t.timestamp",
				new Object[] {item.getCustomer().getId()},
					new AccountResultSetExtractor()));

		return item;
	}
}
