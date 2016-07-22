package indexer;

import core.UThread;

public interface SUFTReader {

	UThread read();
	boolean hasMore();
    int getCount();


}
