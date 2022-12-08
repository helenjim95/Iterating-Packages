package de.tum.in.ase;

//import org.checkerframework.checker.nullness.qual.*;

import java.util.*;

public class Delivery<T extends String, E extends Set<Package>> implements Iterable<E> {

	private final String address;
	private Map<String, Set<Package>> packagesByAddress;

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

	// TODO: implement iterator
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			private int index = 0;
			private int countOfRemoves = 0;
			private int countOfNext = 0;
			private Delivery delivery;
			private List<Set<String>> keyList = List.of(packagesByAddress.keySet());

			//			TODO: need to fix it
//			For any address, it returns all packages destinated to this address, sorted by their weight.
//			The addresses are sorted in lexiographic order.
//			The heaviest package should be returned first.
//			Throw a NoSuchElementException if next() gets called even though there are no packages to return.
			@Override
			public E next() throws NoSuchElementException {
				this.countOfNext++;
				E temp;
//				Object key = packagesByAddress.keySet().toArray()[index];

				if (!hasNext()) {
					throw new NoSuchElementException();
				} else {
//					Sort by address, the set of packages sort by weight (the heaviest first)
//					Map<String, Set<E>> sortedMap = Stream.of(packagesByAddress).collect(Comparator.comparing(Map::getKey)).collect(Comparator.comparing(Package::getWeight));
					temp = (E) packagesByAddress.get(keyList.get(index));
					index++;
					return temp;
				}
			}

			@Override
			public boolean hasNext() {
				return index < keyList.size();
			}

			public void remove() throws NoSuchElementException {
				this.countOfRemoves++;
//				Throw a NoSuchElementException in case next() was not called previously or if remove() gets called twice in a row.
				if (this.countOfNext < 1 || this.countOfRemoves > 1) {
					throw new NoSuchElementException();
				} else {
//					A call to remove() starts the return process for the last package returned by getNext().
					while(this.hasNext()) {
						E package_set = this.next();
						for (Package package_ : package_set) {
							String temp_sender = package_.getSender();
							String temp_address = package_.getAddress();
							//					Swap the values of sender and address to indicate the return.
							package_.setSender(temp_address);
							package_.setAddress(temp_sender);
							packagesByAddress.get(temp_address).remove(package_);
		//					add this package to the suitable stack according to its new destination.
							delivery.add(package_);
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
