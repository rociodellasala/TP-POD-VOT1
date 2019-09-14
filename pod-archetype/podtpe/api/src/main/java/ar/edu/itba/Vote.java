package ar.edu.itba;

import java.util.List;

public class Vote {
	
	private Integer tableId;
	private Province province;
	List<Party> ranking;
	
	public Vote(Integer tableId, Province province, List<Party> ranking) {
		super();
		this.tableId = tableId;
		this.province = province;
		this.ranking = ranking;
	}
	
	public Integer getTableId() {
		return tableId;
	}
	public Province getProvince() {
		return province;
	}
	public List<Party> getRanking() {
		return ranking;
	}
	
	
}