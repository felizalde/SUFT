package pagerank;

import java.util.Comparator;

import core.UThread;

public class ComparatorRank implements Comparator{

		private PageRankSUFT ranking;
		private static final int ORDER = -1;

		public ComparatorRank(PageRankSUFT ranking){
				this.ranking = ranking;
			}

		@Override
		public int compare(Object o1, Object o2) {
				UThread u1 = (UThread)o1;
				UThread u2 = (UThread)o2;
				return ORDER * (this.ranking.getScore(u1.getID()).compareTo(this.ranking.getScore(u2.getID())));
			}

}
