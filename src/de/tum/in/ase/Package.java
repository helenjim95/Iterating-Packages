package de.tum.in.ase;

import java.util.Objects;

public class Package implements Comparable<Package> {
	private String sender;
	private String address;
	private double weight;

	public Package(String sender, String address, double weight) {
		this.sender = sender;
		this.address = address;
		this.weight = weight;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String toString() {
		return "Package from \"" + sender + "\" to \"" + address + "\" with weight " + weight;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || getClass() != other.getClass()) {
			return false;
		}
		Package otherPackage = (Package) other;
		return Double.compare(weight, otherPackage.weight) == 0
		       && Objects.equals(address, otherPackage.address)
		       && Objects.equals(sender, otherPackage.sender);
	}

//	heavier packages should be placed first in the result
	@Override
	public int compareTo(Package other) {
		return Double.compare(other.weight, weight);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sender, address, weight);
	}

}
