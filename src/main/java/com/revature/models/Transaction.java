package com.revature.models;

import java.io.Serializable;
import java.sql.Timestamp;

public class Transaction implements Serializable, Comparable<Transaction> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8345642468335452634L;
	private int transaction_id;
	private int user_id;
	private int account_id;
	private int amount;
	private int withdrawal;
	private Timestamp date_of_purchase;
	public Transaction() {}
	public Transaction(int transaction_id, int user_id, int account_id, int amount, int withdrawal,
			Timestamp date_of_purchase) {
		super();
		this.transaction_id = transaction_id;
		this.user_id = user_id;
		this.account_id = account_id;
		this.amount = amount;
		this.withdrawal = withdrawal;
		this.date_of_purchase = date_of_purchase;
	}
	public int getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(int transaction_id) {
		this.transaction_id = transaction_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getAccount_id() {
		return account_id;
	}
	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getWithdrawal() {
		return withdrawal;
	}
	public void setWithdrawal(int withdrawal) {
		this.withdrawal = withdrawal;
	}
	public Timestamp getDate_of_purchase() {
		return date_of_purchase;
	}
	public void setDate_of_purchase(Timestamp date_of_purchase) {
		this.date_of_purchase = date_of_purchase;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + account_id;
		result = prime * result + amount;
		result = prime * result + ((date_of_purchase == null) ? 0 : date_of_purchase.hashCode());
		result = prime * result + transaction_id;
		result = prime * result + user_id;
		result = prime * result + withdrawal;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		if (account_id != other.account_id)
			return false;
		if (amount != other.amount)
			return false;
		if (date_of_purchase == null) {
			if (other.date_of_purchase != null)
				return false;
		} else if (!date_of_purchase.equals(other.date_of_purchase))
			return false;
		if (transaction_id != other.transaction_id)
			return false;
		if (user_id != other.user_id)
			return false;
		if (withdrawal != other.withdrawal)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Transaction [transaction_id=" + transaction_id + ", user_id=" + user_id + ", account_id=" + account_id
				+ ", amount=" + amount + ", withdrawal=" + withdrawal + ", date_of_purchase=" + date_of_purchase + "]";
	}
	@Override
	public int compareTo(Transaction o) {
		if (this.transaction_id>o.getTransaction_id()) {
			return 1;
		} else if (this.transaction_id<o.getTransaction_id()) {
			return -1;
		}
		return 0;
	}
	
}
