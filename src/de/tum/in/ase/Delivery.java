package de.tum.in.ase;

import java.util.*;
import java.util.stream.Collectors;

public class Delivery implements Iterable<Package> {

	private final String address;
	private final Map<String, Set<Package>> packagesByAddress;

	public Delivery(String address, Map<String, Set<Package>> packages) {
		this.address = address;
		this.packagesByAddress = packages;
	}

	public Delivery(String address) {
		this.address = address;
		this.packagesByAddress = new HashMap<>();
	}

	public String getAddress() {
		return address;
	}

	public Map<String, Set<Package>> getPackagesByAddress() {
		return packagesByAddress;
	}

	public void add(Package aPackage) {
		if (this.packagesByAddress.isEmpty()) {
			this.packagesByAddress.put(aPackage.getAddress(), Set.of(aPackage));
		} else {
			if (this.packagesByAddress.containsKey(aPackage.getAddress())) {
				Set<Package> newSet = new HashSet<>();
				newSet.addAll(this.packagesByAddress.get(aPackage.getAddress()));
				newSet.add(aPackage);
				this.packagesByAddress.put(aPackage.getAddress(), newSet);
			} else {
				this.packagesByAddress.put(aPackage.getAddress(), Set.of(aPackage));
			}
		}
//		System.out.println(this.packagesByAddress);
	}

	@Override
	public String toString() {
		return "Delivery:\n  Address: " + address;
	}

	@Override
	public Iterator<Package> iterator() {
		return new Iterator<>() {
			private int index = 0;
			private int countOfRemoves = 0;
//			For any address, it returns all packages destinated to this address, sorted by their weight.
//			The addresses are sorted in lexiographic order.
//			The heaviest package should be returned first.
//			Throw a NoSuchElementException if next() gets called even though there are no packages to return.
			@Override
			public Package next() throws NoSuchElementException {
				if (!hasNext()) {
					throw new NoSuchElementException();
				} else {
					this.index++;
//					Sort by address, the set of packages sort by weight (the heaviest first)
					Map<String, Set<Package>> sortedMap = getPackagesByAddress().entrySet()
							.stream()
							.sorted(Map.Entry.comparingByKey())
							.collect(Collectors.toMap(Map.Entry::getKey, 
									e -> e.getValue().stream()
									.collect(Collectors.toSet()), (e1, e2) -> e1, LinkedHashMap::new));
							sortedMap.entrySet()
							.stream()
							.forEach(entry -> {
								List<Package> sortedList = entry.getValue().stream().collect(Collectors.toList());
								sortedList.sort(Package::compareTo);
								Set<Package> sortedSet = new HashSet<>(sortedList);
								entry.setValue(sortedSet);
							}); 
					int count = 1;
					for (Map.Entry<String, Set<Package>> entry :sortedMap.entrySet()) {
						if (count == this.index) {
							Set<Package> packageSet = entry.getValue();
							return packageSet.toArray(new Package[0])[0];
						}
						count++;
					}
				}
				return null;
			}

			@Override
			public boolean hasNext() {
				return this.index < getPackagesByAddress().size();
			}

			@Override
			public void remove() throws NoSuchElementException {
				this.countOfRemoves++;
//				Throw a NoSuchElementException in case next() was not called previously or if remove() gets called twice in a row.
				if (this.index < 0) {
					throw new NoSuchElementException();
				} else {
//					A call to remove() starts the return process for the last package returned by getNext().
					this.index--;

					while (this.hasNext()) {
						Package package_ = this.next();
						String temp_sender = package_.getSender();
						String temp_address = package_.getAddress();
						//					Swap the values of sender and address to indicate the return.
						package_.setSender(temp_address);
						package_.setAddress(temp_sender);
//						getPackagesByAddress().get(temp_address).remove(package_);
	//					add this package to the suitable stack according to its new destination.
						if (getPackagesByAddress().isEmpty()) {
							getPackagesByAddress().put(package_.getAddress(), Set.of(package_));
						} else {
							if (getPackagesByAddress().containsKey(package_.getAddress())) {
								Set<Package> newSet = new HashSet<>();
								newSet.addAll(getPackagesByAddress().get(package_.getAddress()));
								newSet.add(package_);
								getPackagesByAddress().put(package_.getAddress(), newSet);
							}
							if (getPackagesByAddress().containsKey(package_.getSender())) {
								Set<Package> newSet = new HashSet<>();
								newSet.addAll(getPackagesByAddress().get(package_.getSender()));
								newSet.add(package_);
								getPackagesByAddress().put(package_.getSender(), newSet);
							}
						}
					}
				}
			}
		};
	}

	public static void main(String[] args) {
		// TODO test your code:
		Delivery delivery = new Delivery("Deliveryplace 1");
		delivery.add(new Package("Iceavenue 5", "Penguinway 4", 100));
		delivery.add(new Package("Iceavenue 5", "Penguinway 1", 85));
		delivery.add(new Package("Iceavenue 5", "Penguinway 1", 73));
		delivery.add(new Package("Snowlane 3", "Antarcticplace 3", 107));
		delivery.add(new Package("Winterhighway 89", "Antarcticplace 27", 20));
		delivery.add(new Package("Penguinway 6", "Tierpark Hellabrunn, Tierparkstr. 30", 1));
		delivery.add(new Package("Tierpark Hellabrunn, Tierparkstr. 30", "Penguinway 6", 0.3));
		delivery.add(new Package("Antarcticplace 123", "Penguroad 1", 6));
		Iterator iterator = delivery.iterator();
//		System.out.println(iterator.hasNext());
//		System.out.println(iterator.next());
		iterator.remove();
		System.out.println(delivery.packagesByAddress);
	}
}
