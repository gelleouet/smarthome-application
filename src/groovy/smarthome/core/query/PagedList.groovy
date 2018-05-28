package smarthome.core.query

import java.util.Collection;

class PagedList<E> extends ArrayList<E> {
	
	int totalCount

	public PagedList() {
		super();
	}

	public PagedList(Collection<? extends E> c) {
		super(c);
	}
	
}
