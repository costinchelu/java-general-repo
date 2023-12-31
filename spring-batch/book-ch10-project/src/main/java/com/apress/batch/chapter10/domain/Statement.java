package com.apress.batch.chapter10.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Statement {

	private final Customer customer;

	private List<Account> accounts = new ArrayList<>();

	public Statement(Customer customer, List<Account> accounts) {
		this.customer = customer;
		this.accounts.addAll(accounts);
	}

	public Statement(Customer customer) {
		this.customer = customer;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts.addAll(accounts);
	}
}
